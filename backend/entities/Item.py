import base64

from config.database_connection import db

class Item(db.Model):
    __tablename__ = "item"

    id = db.Column(db.Integer, primary_key=True)

    name = db.Column(db.String(255), nullable=False)
    category = db.Column(db.String(255), nullable=False)
    estimated_value = db.Column(db.Numeric(10, 2), nullable=False)
    icon_char = db.Column(db.String(50), nullable=True)
    photo = db.Column(db.LargeBinary, nullable=False)

    status = db.Column(db.String(50), nullable=False, default="ACTIVE")
    
    seller_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id"),
        nullable=False
    )
    seller = db.relationship("User")

    def to_dict(self):
        return {
            "id": self.id,
            "name": self.name,
            "category": self.category,
            "estimatedValue": self.estimated_value,
            "iconChar": (
                base64.b64decode(self.icon_char).decode("utf-8")
                if self.icon_char else None
            ),
            "photo": base64.b64encode(self.photo).decode("utf-8"),
            "status": self.status
        }