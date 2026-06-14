import logging

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
