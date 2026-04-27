from rest import ResponseBuilder

from functools import wraps
from flask import request, g
from firebase_admin import auth

import logging

def firebase_required(f):

    @wraps(f)
    def decorated_function(*args, **kwargs):
        auth_header = request.headers.get('Authorization')

        if not auth_header:
            return ResponseBuilder.create_response("Missing Authorization header", 401, True)

        if not auth_header.startswith("Bearer "):
            return ResponseBuilder.create_response("Invalid Authorization header", 401, True)

        id_token = auth_header.split(" ")[1]

        try:
            decoded_token = auth.verify_id_token(id_token)

            g.firebase_user = decoded_token
        except Exception as e:
            logging.exception("Token verification failed")
            return ResponseBuilder.create_response("Invalid token", 401, True)
        
        return f(*args, **kwargs)
    
    return decorated_function