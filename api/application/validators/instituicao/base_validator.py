from abc import ABC, abstractmethod

from api.domain.dtos.instituicao_dto import InstituicaoBaseDto
from api.domain.entity.usuario import Usuario

class IInstituicaoValidator(ABC):

    @abstractmethod
    def validate(self, instituicao_dto: InstituicaoBaseDto, usuario: Usuario):
        pass
    
    
    
