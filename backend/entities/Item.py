from config.database_connection import db

class Item(db.Model):
    __tablename__ = "item"

    id = db.Column(db.Integer, primary_key=True)

    name = db.Column(db.String(255), nullable=False)
    category = db.Column(db.String(255), nullable=False)
    estimated_value = db.Column(db.Numeric(10, 2), nullable=False)
    icon_char = db.Column(db.String(50), nullable=True)

    status = db.Column(db.String(50), nullable=False, default="ACTIVE")
    
    seller_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id"),
        nullable=False
    )
    seller = db.relationship("User")