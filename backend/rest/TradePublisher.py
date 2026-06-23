import logging
import base64
from datetime import datetime
from zoneinfo import ZoneInfo

from rest import ResponseBuilder
from rest.decorator.auth_decorator import firebase_required
from config.database_connection import db
from entities import Item, User, Trade, ChatMessage
from sqlalchemy import or_
from sqlalchemy.orm import aliased

from flask import Blueprint, request, g


bp = Blueprint('TradePublisher', __name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


@bp.route('/trades', methods=['GET'])
@firebase_required
def get_trades():
    user_id = request.args.get('userId', type=str)
    
    if user_id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)
    
    Buyer  = aliased(User)
    Seller = aliased(User)
    
    trades = (
        db.session.query(Trade, Buyer, Seller)
        .join(Item,   Item.id   == Trade.item_id)
        .join(Buyer,  Buyer.id  == Trade.buyer_id)
        .join(Seller, Seller.id == Trade.seller_id)
        .filter(
            or_(
                Trade.seller_id == user_id,
                Trade.buyer_id  == user_id
            )
        )
        .order_by(Trade.updated_at.desc())
        .all()
    )

    def time_ago(dt: datetime) -> str:
        """Return a human-readable relative time string."""
        now   = datetime.now(tz=ZoneInfo("Europe/Rome"))
        delta = now - dt.replace(tzinfo=ZoneInfo("Europe/Rome")) if dt.tzinfo is None else now - dt

        seconds = int(delta.total_seconds())
        if seconds < 60:
            return "Just now"
        minutes = seconds // 60
        if minutes < 60:
            return f"{minutes}m ago"
        hours = minutes // 60
        if hours < 24:
            return f"{hours}h ago"
        days = hours // 24
        if days < 7:
            return f"{days}d ago"
        weeks = days // 7
        if weeks < 4:
            return f"{weeks}w ago"
        months = days // 30
        if months < 12:
            return f"{months}mo ago"
        return f"{days // 365}y ago"

    def transform_response(trade: Trade, buyer: User, seller: User) -> dict:
        is_buying = trade.buyer_id == user_id
        return {
            "id":             str(trade.id),
            "sellerName":     seller.username,
            "buyerName":      buyer.username,
            "itemName":       trade.item.name,     
            "itemIcon":       base64.b64decode(trade.item.icon_char).decode("utf-8"),
            "isBuying":       is_buying,
            "requestedPrice": str(trade.item.estimated_value),
            "offeredPrice":   str(trade.offered_price),
            "status":         trade.status,
            "lastMessage":    trade.last_message or "",
            "timeAgo":        time_ago(trade.updated_at),
        }

    return ResponseBuilder.create_response(
        [transform_response(trade, buyer, seller) for trade, buyer, seller in trades],
        200,
        False
    )


@bp.route('/trades', methods=['POST'])
@firebase_required
def create_trade():
    firebase_uid = g.firebase_user['uid']
    data = request.get_json()

    if not data:
        return ResponseBuilder.create_response("Missing data", 400, True)

    item_id = data.get('itemId')
    message = data.get('message')
    
    existing_trade = Trade.query.filter(
        Trade.buyer_id == firebase_uid,
        Trade.item_id == item_id,
        Trade.status != "REJECTED"
    ).all()

    if existing_trade:
        return ResponseBuilder.create_response("There is already an active trade for this item!", 400, True)
    
    item = Item.query.filter_by(id=item_id).first()
    
    new_trade = Trade(
        created_at = datetime.now(tz=ZoneInfo("Europe/Rome")),
        updated_at = datetime.now(tz=ZoneInfo("Europe/Rome")),
        status = 'PENDING',
        offered_price = data.get('amount'),
        last_message = data.get('message'),
        item_id = data.get('itemId'),
        seller_id = item.seller_id,
        buyer_id = firebase_uid
    )

    if item.status == "ACTIVE":
        item.status = "PENDING"


    db.session.add(new_trade)
    db.session.flush()

    if message and message.strip():
        new_message = ChatMessage(
            created_at = datetime.now(tz=ZoneInfo("Europe/Rome")),
            text = message,
            trade_id = new_trade.id,
            sender_id = firebase_uid
        )
        db.session.add(new_message)

    db.session.commit()

    return ResponseBuilder.create_response(" ", 201, False)



@bp.route('/trades/<id>', methods=['GET'])
@firebase_required
def get_trade(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)
    
    Buyer  = aliased(User)
    Seller = aliased(User)
    
    existing_trade = (
        db.session.query(Trade, Buyer, Seller)
        .join(Item,   Item.id   == Trade.item_id)
        .join(Buyer,  Buyer.id  == Trade.buyer_id)
        .join(Seller, Seller.id == Trade.seller_id)
        .filter(Trade.id == id)
        .first()
    )

    if existing_trade is None:
        return ResponseBuilder.create_response("Not Found", 404, True)

    trade, buyer, seller = existing_trade

    def time_ago(dt: datetime) -> str:
        """Return a human-readable relative time string."""
        now   = datetime.now(tz=ZoneInfo("Europe/Rome"))
        delta = now - dt.replace(tzinfo=ZoneInfo("Europe/Rome")) if dt.tzinfo is None else now - dt

        seconds = int(delta.total_seconds())
        if seconds < 60:
            return "Just now"
        minutes = seconds // 60
        if minutes < 60:
            return f"{minutes}m ago"
        hours = minutes // 60
        if hours < 24:
            return f"{hours}h ago"
        days = hours // 24
        if days < 7:
            return f"{days}d ago"
        weeks = days // 7
        if weeks < 4:
            return f"{weeks}w ago"
        months = days // 30
        if months < 12:
            return f"{months}mo ago"
        return f"{days // 365}y ago"

    def transform_response(trade: Trade, buyer: User, seller: User) -> dict:
        is_buying = trade.buyer_id == firebase_uid
        return {
            "id":             str(trade.id),
            "sellerName":     seller.username,
            "buyerName":      buyer.username,
            "itemName":       trade.item.name,     
            "itemIcon":       base64.b64decode(trade.item.icon_char).decode("utf-8"),
            "isBuying":       is_buying,
            "requestedPrice": str(trade.item.estimated_value),
            "offeredPrice":   str(trade.offered_price),
            "status":         trade.status,
            "lastMessage":    trade.last_message or "",
            "timeAgo":        time_ago(trade.updated_at),
        }

    return ResponseBuilder.create_response(
        transform_response(trade, buyer, seller),
        200,
        False
    )


@bp.route('/trades/<id>/messages', methods=['GET'])
@firebase_required
def get_trade_messages(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)
    
    Sender = aliased(User)
    
    existing_messages = (
        db.session.query(ChatMessage, Sender)
        .join(Sender,   Sender.id   == ChatMessage.sender_id)
        .filter(ChatMessage.trade_id == id)
        .order_by(ChatMessage.created_at.asc())
        .all()
    )

    def transform_response(message: ChatMessage, sender: User) -> dict:
        is_from_me = message.sender_id == firebase_uid
        return {
            "id":             str(message.id),
            "senderId":       message.sender_id,
            "senderName":     sender.username,
            "text":           message.text,     
            "timestamp":      message.created_at,
            "isFromMe":       is_from_me
        }

    return ResponseBuilder.create_response(
        [transform_response(message, sender) for message, sender in existing_messages],
        200,
        False
    )


@bp.route('/trades/<id>/messages', methods=['POST'])
@firebase_required
def create_trade_message(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)

    data = request.get_json()

    if not data:
        return ResponseBuilder.create_response("Missing data", 400, True)

    existing_trade = db.session.query(Trade).filter(Trade.id == id).first()

    if existing_trade is None:
        return ResponseBuilder.create_response("Trade Not Found", 404, True)

    # Only buyer/seller on the trade can send messages
    if firebase_uid not in (existing_trade.buyer_id, existing_trade.seller_id):
        return ResponseBuilder.create_response("Forbidden", 403, True)

    new_message = ChatMessage(
        trade_id=id,
        sender_id=firebase_uid,
        text=data.get('text'),
        created_at=datetime.now(tz=ZoneInfo("Europe/Rome")),
    )

    db.session.add(new_message)

    # Keep the trade's preview/timestamp in sync
    existing_trade.last_message = data.get('text')
    existing_trade.updated_at = new_message.created_at

    db.session.commit()

    sender = db.session.query(User).filter(User.id == firebase_uid).first()

    def transform_response(message: ChatMessage, sender: User) -> dict:
        return {
            "id":         str(message.id),
            "senderId":   message.sender_id,
            "senderName": sender.username,
            "text":       message.text,
            "timestamp":  message.created_at,
            "isFromMe":   True,
        }

    return ResponseBuilder.create_response(
        transform_response(new_message, sender),
        201,
        False
    )


@bp.route('/trades/<id>/reject', methods=['POST'])
@firebase_required
def reject_trade(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)

    trade = db.session.query(Trade).filter(Trade.id == id).first()

    if trade is None:
        return ResponseBuilder.create_response("Not Found", 404, True)

    if firebase_uid != trade.seller_id:
        return ResponseBuilder.create_response("Forbidden", 403, True)

    if trade.status not in ("PENDING", "ACCEPTED"):
        return ResponseBuilder.create_response(
            f"Cannot reject a trade with status {trade.status}", 400, True
        )

    trade.status = "REJECTED"
    trade.updated_at = datetime.now(tz=ZoneInfo("Europe/Rome"))

    other_active_trades = db.session.query(Trade).filter(
        Trade.item_id == trade.item_id,
        Trade.id != trade.id,
        Trade.status.in_(("PENDING", "ACCEPTED"))
    ).first()

    if other_active_trades is None:
        item = db.session.query(Item).filter(Item.id == trade.item_id).first()
        if item is not None:
            item.status = "ACTIVE"


    db.session.commit()

    return ResponseBuilder.create_response(" ", 200, False)


@bp.route('/trades/<id>/accept', methods=['POST'])
@firebase_required
def accept_trade(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)

    trade = db.session.query(Trade).filter(Trade.id == id).first()
    if trade is None:
        return ResponseBuilder.create_response("Not Found", 404, True)

    if firebase_uid != trade.seller_id:
        return ResponseBuilder.create_response("Forbidden", 403, True)

    if trade.status not in ("PENDING",):
        return ResponseBuilder.create_response(
            f"Cannot accept a trade with status {trade.status}", 400, True
        )

    trade.status     = "ACCEPTED"
    trade.updated_at = datetime.now(tz=ZoneInfo("Europe/Rome"))
    db.session.commit()

    return ResponseBuilder.create_response(" ", 200, False)


@bp.route('/trades/<id>/complete', methods=['POST'])
@firebase_required
def complete_trade(id):
    firebase_uid = g.firebase_user['uid']

    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)

    trade = db.session.query(Trade).filter(Trade.id == id).first()
    item = db.session.query(Item).filter(Item.id == trade.item_id).first()

    if trade is None or item is None:
        return ResponseBuilder.create_response("Not Found", 404, True)

    if firebase_uid != trade.seller_id:
        return ResponseBuilder.create_response("Forbidden", 403, True)

    if trade.status not in ("ACCEPTED",):
        return ResponseBuilder.create_response(
            f"Cannot complete a trade with status {trade.status}", 400, True
        )

    trade.status     = "COMPLETED"
    trade.updated_at = datetime.now(tz=ZoneInfo("Europe/Rome"))
    item.status = "SOLD"

    competing_trades = db.session.query(Trade).filter(
        Trade.item_id == item.id,
        Trade.id != trade.id,
        Trade.status.in_(("PENDING", "ACCEPTED"))
    ).all()

    for competing_trade in competing_trades:
        competing_trade.status = "REJECTED"
        competing_trade.updated_at = datetime.now(tz=ZoneInfo("Europe/Rome"))

    db.session.commit()

    return ResponseBuilder.create_response(" ", 200, False)