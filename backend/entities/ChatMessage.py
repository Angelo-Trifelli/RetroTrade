from config.database_connection import db

class ChatMessage(db.Model):
    __tablename__ = "chat_message"

    id = db.Column(db.Integer, primary_key=True)

    created_at = db.Column(db.DateTime, nullable=False)
    text = db.Column(db.String(255), nullable=True)

    trade_id = db.Column(db.Integer, db.ForeignKey("trade.id"), nullable=False)
    sender_id = db.Column(db.String(255), db.ForeignKey("user.id"), nullable=False)

    trade = db.relationship(
        "Trade",
        foreign_keys=[trade_id]
    )

    sender = db.relationship(
        "User",
        foreign_keys=[sender_id]
    )