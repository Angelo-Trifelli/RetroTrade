import logging

from config import database_connection, firebase_config
from rest import AuthPublisher

from flask import Flask, Blueprint, request
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy


app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ENGINE_OPTIONS"] = {
    "creator": database_connection.get_connection
}

database_connection.db.init_app(app)


#Register publisher endpoints
app.register_blueprint(AuthPublisher.bp)


@app.route("/")
def fetch():
    return "The server is running!"


if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
    logging.info(f"Using db environment: {database_connection.ENV}")
    logging.info("Starting server...")

    try:
        app.run(host='0.0.0.0', port=8080, debug=True)
    except Exception as e:
        logging.error(f"Error: {e}")
        logging.info("Server stopped.")