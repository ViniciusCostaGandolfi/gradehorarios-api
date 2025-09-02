from typing import TYPE_CHECKING, List
from sqlalchemy import Boolean, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from api.services.database_service import Base

if TYPE_CHECKING:
    from .instituicao import Instituicao


class Usuario(Base):

    __tablename__ = "usuarios"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    nome: Mapped[str] = mapped_column(String(255), nullable=False)
    email: Mapped[str] = mapped_column(String(255), unique=True, nullable=False, index=True)
    senha: Mapped[str] = mapped_column(String(255), nullable=False)
    telefone: Mapped[str] = mapped_column(String(32), nullable=True)
    documento: Mapped[str] = mapped_column(String(32), nullable=False)
    tipo : Mapped[str] = mapped_column(String(32), nullable=False)

    instituicoes: Mapped[List["Instituicao"]] = relationship(
        back_populates="usuario",
        cascade="all, delete-orphan"
    )
    
    ativo: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)