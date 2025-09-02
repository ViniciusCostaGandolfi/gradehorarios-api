from typing import TYPE_CHECKING, List
from sqlalchemy import (
    Boolean,
    ForeignKey,
    Integer,
    String
)
from sqlalchemy.orm import Mapped, mapped_column, relationship

from api.services.database_service import Base


if TYPE_CHECKING:
    from .solucao import Solucao
    from .usuario import Usuario



class Instituicao(Base):
    __tablename__ = "instituicoes"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    codigo: Mapped[int] = mapped_column(Integer, unique=True, nullable=False)
    nome: Mapped[str] = mapped_column(String(255), nullable=False)
    
    usuario_id: Mapped[int] = mapped_column(ForeignKey("usuarios.id"))
    usuario: Mapped["Usuario"] = relationship(back_populates="instituicoes")

    solucoes: Mapped[List["Solucao"]] = relationship(
        back_populates="instituicao",
        cascade="all, delete-orphan"
    )
        
    ativo: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)