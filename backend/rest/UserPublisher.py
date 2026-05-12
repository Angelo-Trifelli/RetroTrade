import logging

from rest import ResponseBuilder
from rest.decorator.auth_decorator import firebase_required
from config.database_connection import db
from entities import Item, ItemView, Trade

from flask import Blueprint, request, g
from sqlalchemy import func

bp = Blueprint('UserPublisher', __name__, url_prefix="/users")
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


@bp.route('/<id>/stats', methods=['GET'])
@firebase_required
def get_user_stats(id):
    firebase_uid = g.firebase_user['uid']
    
    if firebase_uid != id:
        return ResponseBuilder.create_response("Unauthorized", 403, True)
    
    trade_count = (
        db.session.query(func.count(Trade.id))
        .filter(
            Trade.receiver_id == firebase_uid,
            Trade.status.in_(["PENDING", "ACCEPTED"])
        )
        .scalar()
    )

    item_count = (
        db.session.query(func.count(Item.id))
        .filter(
            Item.seller_id == firebase_uid
        )
        .scalar()
    )

    return ResponseBuilder.create_response({"totalItems": item_count, "pendingTrades": trade_count}, 200, False)


@bp.route('/<id>/history/recentItems', methods=['GET'])
@firebase_required
def get_recent_items(id):
    firebase_uid = g.firebase_user['uid']
    
    if firebase_uid != id:
        return ResponseBuilder.create_response("Unauthorized", 403, True)

    def to_recent_item_dto(item):
        return {
            "name": item.name,
            "category": item.category,
            "estimatedValue": item.estimated_value,
            "iconChar": item.icon_char
        }

    items = (
        db.session.query(Item)
        .join(ItemView, Item.id == ItemView.item_id)
        .filter(ItemView.user_id == firebase_uid)
        .order_by(ItemView.viewed_at.desc())
        .all()
    )

    return ResponseBuilder.create_response([item.to_dict() for item in items], 200, False)
