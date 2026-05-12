from datetime import datetime
from config.database_connection import db


class ItemView(db.Model):
    __tablename__ = "item_view"

    id = db.Column(db.BigInteger, primary_key=True, autoincrement=True)

    viewed_at = db.Column(
        db.DateTime,
        nullable=False,
        default=datetime.utcnow,
        onupdate=datetime.utcnow
    )

    user_id = db.Column(
        db.String(255),
        db.ForeignKey("user.id", ondelete="CASCADE"),
        nullable=False
    )

    item_id = db.Column(
        db.Integer,
        db.ForeignKey("item.id", ondelete="CASCADE"),
        nullable=False
    )

    item = db.relationship(
        "Item",
        foreign_keys=[item_id]
    )

    user = db.relationship(
        "User",
        foreign_keys=[user_id]
    )

    __table_args__ = (
        db.UniqueConstraint(
            "user_id",
            "item_id",
            name="UK_USER_ITEM"
        ),
    )