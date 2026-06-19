from config.database_connection import db

class Trade(db.Model):
    __tablename__ = "trade"

    id = db.Column(db.Integer, primary_key=True)

    created_at = db.Column(db.DateTime, nullable=False)
    updated_at = db.Column(db.DateTime, nullable=False)
    status = db.Column(db.String(50), default="PENDING")
    offered_price = db.Column(db.Numeric(10, 2), nullable=False)
    last_message = db.Column(db.String(255), nullable=True)

    item_id = db.Column(
        db.Integer,
        db.ForeignKey("item.id"),
        nullable=False
    )

    buyer_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id"),
        nullable=False
    )

    seller_id = db.Column(
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
        foreign_keys=[buyer_id]
    )

    receiver = db.relationship(
        "User",
        foreign_keys=[seller_id]
    )