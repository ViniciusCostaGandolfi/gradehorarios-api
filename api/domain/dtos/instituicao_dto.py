from typing import List, Optional

from pydantic import EmailStr
from api.domain.dtos import solucao
from api.domain.dtos.solucao import SolucaoDto
from api.domain.entity.instituicao import Instituicao
from api.infra.config.pydantic_config import CustomModel

    
class InstituicaoBaseDto(CustomModel):
    codigo: int
    nome: str
    
class InstituicaoCreateDto(InstituicaoBaseDto):
    pass

class InstituicaoUpdateDto(InstituicaoBaseDto):
    pass

class InstituicaoDto(InstituicaoBaseDto):
    id: int
    usuario_id: int
    
    @classmethod
    def from_entity(cls, entity: Instituicao):
        """
        Construtor alternativo ("named constructor") que cria um DTO
        a partir de uma entidade SQLAlchemy.
        """
        return cls(
            id=entity.id,
            usuario_id=entity.usuario_id,
            codigo=entity.codigo,
            nome=entity.nome,
        )
        
class InstituicaoFullDto(InstituicaoBaseDto):
    id: int
    usuario_id: int
    solucoes: List[SolucaoDto]
    
    @classmethod
    def from_entity(cls, entity: Instituicao):
        """
        Construtor alternativo ("named constructor") que cria um DTO
        a partir de uma entidade SQLAlchemy.
        """
        return cls(
            id=entity.id,
            usuario_id=entity.usuario_id,
            codigo=entity.codigo,
            nome=entity.nome,
            solucoes=[SolucaoDto.from_entity(solucao) for solucao in entity.solucoes]
        )