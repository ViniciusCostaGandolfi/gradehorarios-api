import typing
from sqlalchemy import String, Float
from sqlalchemy.orm import Mapped, mapped_column

from api.services.database_service import Base


class Endereco(Base):
    __tablename__ = "enderecos"
    
    id: Mapped[int] = mapped_column(primary_key=True)

    pais: Mapped[str] = mapped_column(String(2))
    estado: Mapped[str] = mapped_column(String(50))
    cidade: Mapped[str] = mapped_column(String(100))
    bairro: Mapped[str] = mapped_column(String(100))
    codigo_postal: Mapped[str] = mapped_column(String(10))
    logradouro: Mapped[str] = mapped_column(String(255))
    numero: Mapped[str] = mapped_column(String(20))
    endereco_formatado: Mapped[str] = mapped_column(String(500))

    complemento: Mapped[typing.Optional[str]] = mapped_column(String(100), nullable=True)
    latitude: Mapped[typing.Optional[float]] = mapped_column(Float, nullable=True)
    longitude: Mapped[typing.Optional[float]] = mapped_column(Float, nullable=True)