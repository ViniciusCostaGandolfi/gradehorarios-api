import email
from typing import Callable
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from api.domain.entity.usuario import Usuario
from api.infra.security.decode_token_service import verify_token
from api.services.database_service import get_db

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

async def get_current_user(
    token: str = Depends(oauth2_scheme),
    db: AsyncSession = Depends(get_db)
) -> Usuario:
    """
    Esta função implementa a validação no banco de dados de forma assíncrona.
    """
    token_payload = verify_token(token)
    
    query = select(Usuario).where(Usuario.id == token_payload.id)
    
    result = await db.execute(query)
    
    usuario = result.scalars().first()
    
    if usuario is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, 
            detail="Usuário não encontrado."
        )
    return usuario