from typing import TYPE_CHECKING
from sqlalchemy import (
    ForeignKey,
    Integer,
    String
)
from sqlalchemy.orm import Mapped, mapped_column, relationship

from api.services.database_service import Base

if TYPE_CHECKING:
    from .endereco import Endereco


class InscricaoEstadual(Base):
    __tablename__ = "inscricoes_estaduais"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    codigo: Mapped[int] = mapped_column(Integer, unique=True, nullable=False)
    nome: Mapped[str] = mapped_column(String(255), nullable=False)
    email: Mapped[str] = mapped_column(String(255), unique=True)
    telefone: Mapped[str] = mapped_column(String(20))
    
    localizacao: Mapped[str] = mapped_column(String(32))
    dependencia_administrativa:  Mapped[str] = mapped_column(String(32))

    endereco_id: Mapped[int] = mapped_column(ForeignKey("enderecos.id"))
    endereco: Mapped["Endereco"] = relationship(uselist=False)