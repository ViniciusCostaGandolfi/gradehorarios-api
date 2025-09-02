import datetime
from typing import TYPE_CHECKING

from sqlalchemy import (
    Boolean,
    DateTime,
    ForeignKey,
    Integer,
    String,
    func
)
from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy.dialects.postgresql import JSONB

from api.services.database_service import Base

if TYPE_CHECKING:
    from .instituicao import Instituicao


class Solucao(Base):
    __tablename__ = "solucoes"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)

    

    criado_em: Mapped[datetime.datetime] = mapped_column(
        DateTime(timezone=True),
        server_default=func.now()
    )

    finalizado_em: Mapped[datetime.datetime] = mapped_column(
        DateTime(timezone=True),
        nullable=True
    )
    
    ativo: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    
    input_path: Mapped[str] = mapped_column(String(1024), nullable=False)

    output_path: Mapped[str] = mapped_column(String(1024), nullable=True)
        
    solver_status: Mapped[str] = mapped_column(String)
    
    time_to_solve: Mapped[datetime.timedelta] = mapped_column()
    
    solver_type: Mapped[str] = mapped_column(String)
    
    solver_runing: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    
    errors: Mapped[dict] = mapped_column(JSONB, nullable=True)

    instituicao_id: Mapped[int] = mapped_column(ForeignKey("instituicoes.id"))

    instituicao: Mapped["Instituicao"] = relationship()
    
    usuario_id: Mapped[int] = mapped_column(ForeignKey("usuarios.id"))
