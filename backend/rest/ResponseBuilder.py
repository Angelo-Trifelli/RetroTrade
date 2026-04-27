from flask import jsonify

def create_response(data: dict | str, statusCode: int = 200, is_error: bool = False):
    key = "error" if is_error else "message"
    payload = data if isinstance(data, dict) else {key: data}
    return jsonify(payload), statusCode
