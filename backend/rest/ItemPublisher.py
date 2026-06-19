import logging
import base64

from rest import ResponseBuilder
from rest.decorator.auth_decorator import firebase_required
from config.database_connection import db
from entities import Item


from flask import Blueprint, request, g


bp = Blueprint('ItemPublisher', __name__)
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')



@bp.route('/items', methods=['GET'])
@firebase_required
def get_items():
    user_id = request.args.get('userId', type=str)
    
    if user_id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)
    
    items = (
        db.session.query(Item)
        .filter(Item.seller_id == user_id)
        .all()
    )

    return ResponseBuilder.create_response([item.to_dict() for item in items], 200, False)


@bp.route('/items', methods=['POST'])
@firebase_required
def create_item():
    firebase_uid = g.firebase_user['uid']
    data = request.get_json()
    
    if not data:
        return ResponseBuilder.create_response("Missing data", 400, True)
    
    new_item = Item(
        name = data.get('name'),
        category = data.get('category'),
        estimated_value = data.get('estimatedValue'),
        icon_char = base64.b64encode(data.get('iconChar').encode('utf-8')).decode('ascii'),
        latitude = data.get('latitude'),
        longitude = data.get('longitude'),
        photo = base64.b64decode(data.get("photo")),
        status = 'ACTIVE',
        seller_id = firebase_uid      
    )

    db.session.add(new_item)
    db.session.commit()

    return ResponseBuilder.create_response(" ", 201, False)


@bp.route('/items/<id>', methods=['GET'])
@firebase_required
def get_item(id):
    if id is None:
        return ResponseBuilder.create_response("Bad Request", 400, True)
    
    existing_item = Item.query.filter_by(id=id).first()

    if not existing_item:
        return ResponseBuilder.create_response("Item not found", 400, True)

    return ResponseBuilder.create_response(existing_item.to_dict(), 200, False)

    