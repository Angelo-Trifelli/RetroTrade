import logging

from rest import ResponseBuilder
from rest.decorator.auth_decorator import firebase_required
from config.database_connection import db

from flask import Blueprint, request, jsonify, g
from entities import User

bp = Blueprint('AuthPublisher', __name__, url_prefix="/auth")
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

@bp.route('/usernameAvailable', methods=['GET'])
def username_available():
    username = request.args.get('username', type=str)

    if username is None:
        return ResponseBuilder.create_response("Missing username!", 400, True)

    existing_user = User.query.filter_by(username=username).first()

    if not existing_user:
        return ResponseBuilder.create_response({"available": True}, 200, False)
    
    return ResponseBuilder.create_response({"available": False}, 200, False)


@bp.route('/createUser', methods=['POST'])
@firebase_required
def create_user():
    firebase_uid = g.firebase_user['uid']
    email = g.firebase_user['email']

    data = request.get_json()

    if not data:
        return ResponseBuilder.create_response("Missing data", 400, True)
    
    new_user = User(
        id = firebase_uid,
        fullName = data.get('fullName'),
        username = data.get('username'),
        email = email
    )

    db.session.add(new_user)
    db.session.commit()

    return ResponseBuilder.create_response(" ", 201, False)


@bp.route('/loadLoggedUserData', methods=['GET'])
@firebase_required
def load_logged_user_data():
    firebase_uid = g.firebase_user['uid']

    if firebase_uid is None:
        return ResponseBuilder.create_response("Missing firebase UID!", 400, True)

    logged_user = User.query.filter_by(id=firebase_uid).first()

    if not logged_user:
        return ResponseBuilder.create_response("User not found", 400, True)

    response = {
        "fullName": logged_user.fullName,
        "username": logged_user.username,
        "email": logged_user.email
    }

    return ResponseBuilder.create_response(response, 200, False)