from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from api.application.validators.usuario.base_validator import IUsuarioValidator
from api.domain.dtos.usuario_dto import UsuarioBaseDto
from api.domain.entity.usuario import Usuario

class AdminTypeNotAllowedValidator(IUsuarioValidator):
    """Implementação concreta que valida se o tipo de usuário não é ADMIN."""
    
    async def validate(self, usuario_dto: UsuarioBaseDto):
        if usuario_dto.tipo and usuario_dto.tipo.value == 'CONSULTOR':
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="A criação de usuários com o tipo 'CONSULTOR' não é permitida."
            )

class EmailAlreadyExistsValidator(IUsuarioValidator):
    """Implementação concreta que valida se o e-mail já existe no banco de dados."""

    def __init__(self, db_session: AsyncSession):
        self.db = db_session

    async def validate(self, usuario_dto: UsuarioBaseDto):
        query = select(Usuario).where(Usuario.email == usuario_dto.email)
        result = await self.db.execute(query)
        if result:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="E-mail já registrado."
            )
            
            
class UserCreationValidator:
    """
    Orquestrador que executa um conjunto de validadores para a criação de um usuário.
    """
    def __init__(self, db_session: AsyncSession):
        self._validators: list[IUsuarioValidator] = [
            AdminTypeNotAllowedValidator(),
            EmailAlreadyExistsValidator(db_session),
        ]

    async def run(self, usuario_dto: UsuarioBaseDto):
        """Executa todos os validadores em sequência."""
        for validator in self._validators:
            await validator.validate(usuario_dto)