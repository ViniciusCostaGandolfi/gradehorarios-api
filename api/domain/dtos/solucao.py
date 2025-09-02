from datetime import datetime, timedelta
from typing import Optional
from api.domain.entity.solucao import Solucao
from api.domain.entity.solver_status import SolverStatus
from api.domain.entity.solver_type import SolverType
from api.infra.config.pydantic_config import CustomModel




class SolucaoBaseDto(CustomModel):
    criado_em: Optional[datetime] = None
    finalizado_em: Optional[datetime] = None
    input_path: str
    output_path: Optional[str] = None
    solver_status: Optional[SolverStatus] = None
    time_to_solve: Optional[timedelta] = None
    solver_type: Optional[SolverType] = None
    instituicao_id: int
    usuario_id: int
    
class SolucaoCreateDto(SolucaoBaseDto):
    pass

class SolucaoDto(SolucaoBaseDto):
    id: int
    
    @classmethod
    def from_entity(cls, entity: Solucao):
        """
        Construtor alternativo ("named constructor") que cria um DTO
        a partir de uma entidade SQLAlchemy.
        """
        return cls(
            id=entity.id,
            input_path=entity.input_path,
            instituicao_id=entity.instituicao_id,
            usuario_id=entity.usuario_id,
            criado_em=entity.criado_em,
            finalizado_em=entity.finalizado_em,
            output_path=entity.output_path,
            solver_status=SolverStatus(entity.solver_status) if entity.solver_status else None,
            time_to_solve=entity.time_to_solve if entity.time_to_solve else None,
            solver_type=SolverType(entity.solver_type) if entity.solver_type else None,
        )
