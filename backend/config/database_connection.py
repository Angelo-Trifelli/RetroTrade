import os
import logging
import pymysql
from dotenv import load_dotenv
from google.cloud.sql.connector import Connector
from flask_sqlalchemy import SQLAlchemy


logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

load_dotenv()

connector = Connector()
db = SQLAlchemy()

ENV = os.environ.get("ENV", "local")

def get_db_user():
    return os.environ[f"DB_USER_{ENV.upper()}"]

def get_db_password():
    return os.environ[f"DB_PASSWORD_{ENV.upper()}"]

def get_db_name():
    return os.environ[f"DB_NAME_{ENV.upper()}"]

def get_connection():
    if ENV == "remote":
        return connector.connect(
            os.environ["REMOTE_INSTANCE_CONNECTION_NAME"],
            "pymysql",
            user=get_db_user(),
            enable_iam_auth=True,
            db=get_db_name()
        )
    else:   #Local env
        return pymysql.connect(
            host=os.environ["DB_HOST_LOCAL"],
            port=int(os.environ.get("DB_PORT_LOCAL", 3306)),
            user=get_db_user(),
            password=get_db_password(),
            database=get_db_name()
        )