from datetime import UTC, datetime, timedelta
from typing import Optional
from fastapi import HTTPException, status
from jose import JWTError, jwt
from api.domain.dtos.usuario_dto import UsuarioDto
from api.domain.entity.tipo_usuario import TipoUsuario
from api.infra.config.env_config import get_settings
from api.infra.security.dtos.email_token_payload_dto import EmailTokenPayloadDto
from api.infra.security.dtos.token_payload_dto import TokenPayloadDto


SECRET_KEY = get_settings().TOKEN_SECRET_KEY
ALGORITHM = "HS256"

def create_access_token(usuario: UsuarioDto, expires_delta: Optional[timedelta] = None):
    
    expire = datetime.now(UTC) + timedelta(days=3)
    if expires_delta:
        expire = datetime.now(UTC) + expires_delta
    
    return jwt.encode(
        TokenPayloadDto(
            usuario=usuario,
            exp=expire.timestamp()
            ).model_dump(mode='json'), SECRET_KEY, algorithm=ALGORITHM)

def verify_token(token: str) -> UsuarioDto:
    try:
        payload_dict = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return TokenPayloadDto(**payload_dict).usuario
    except JWTError:
        raise HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Não foi possível validar as credenciais",
        headers={"WWW-Authenticate": "Bearer"},
    )


def create_usuario_email_token(
    email:  str,
    tipo: TipoUsuario,
    expires_delta: Optional[timedelta] = None) -> str:

    expire = datetime.now(UTC) + timedelta(days=7)
    if expires_delta:
        expire = datetime.now(UTC) + expires_delta
    
    return jwt.encode(EmailTokenPayloadDto(
            email=email,
            tipo=tipo,
            exp=expire.timestamp()
        ).model_dump(), SECRET_KEY, algorithm=ALGORITHM)

def verify_usuario_email_token(token: str) -> EmailTokenPayloadDto:
    try:
        payload_dict = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return EmailTokenPayloadDto(**payload_dict)
    
    except JWTError:
        raise  HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Não foi possível validar as credenciais",
        headers={"WWW-Authenticate": "Bearer"},
    )