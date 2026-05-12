from config.database_connection import db

class Trade(db.Model):
    __tablename__ = "trade"

    id = db.Column(db.Integer, primary_key=True)

    created_at = db.Column(db.DateTime)

    status = db.Column(
        db.String(50),
        default="PENDING"
    )

    item_id = db.Column(
        db.Integer,
        db.ForeignKey("item.id"),
        nullable=False
    )

    requester_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id"),
        nullable=False
    )

    receiver_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id"),
        nullable=False
    )

    item = db.relationship(
        "Item",
        foreign_keys=[item_id]
    )

    requester = db.relationship(
        "User",
        foreign_keys=[requester_id]
    )

    receiver = db.relationship(
        "User",
        foreign_keys=[receiver_id]
    )