from config.database_connection import db

class User(db.Model):
    __tablename__ = "user"
    
    id = db.Column(db.String(255), primary_key=True)
    registered_at = db.Column(db.DateTime, nullable=False)
    fullName = db.Column(db.String(255), unique=False, nullable=False)
    username = db.Column(db.String(255))
    email = db.Column(db.String(255))