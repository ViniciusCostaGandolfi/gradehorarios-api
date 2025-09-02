from abc import ABC, abstractmethod

from api.domain.dtos.usuario_dto import UsuarioBaseDto

class IUsuarioValidator(ABC):

    @abstractmethod
    async def validate(self, usuario_dto: UsuarioBaseDto):
        pass