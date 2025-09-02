from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from api.application.services.auth_service import AuthService
from api.domain.dtos.auth_dto import LoginDto, SiginDto, TokenDto
from api.domain.dtos.usuario_dto import UsuarioDto
from api.infra.security.auth_service import get_current_user
from api.infra.security.decode_token_service import create_access_token
from api.services.database_service import get_db


auth_controller = APIRouter(prefix='/auth', tags=['Auth'])
    
@auth_controller.post("/sigin", response_model=TokenDto, status_code=status.HTTP_201_CREATED)
async def create_user_and_token(
    usuario_create_dto: SiginDto,
    db: AsyncSession = Depends(get_db)
):
    """
    Registra um novo usuário e retorna um token de acesso.
    """
    auth_service = AuthService(db)
    
    novo_usuario = auth_service.register_user(usuario_create_dto)
    
    token = create_access_token(UsuarioDto.model_validate(novo_usuario))
    
    return TokenDto(accessToken=token)


@auth_controller.post("/login", response_model=TokenDto)
async def login_and_create_token(
    login_dto: LoginDto,
    db: AsyncSession = Depends(get_db)
):
    """
    Autentica um usuário e retorna um token de acesso.
    """
    auth_service = AuthService(db)
    
    usuario_autenticado = await auth_service.login_user(login_dto)
    
    token = create_access_token(UsuarioDto.model_validate(usuario_autenticado))
    
    return TokenDto(accessToken=token)
    
@auth_controller.post("/refresh_token", response_model=TokenDto)
async def refresh_access_token(
    current_user: UsuarioDto = Depends(get_current_user)
):
    """
    Gera um novo token para o usuário já autenticado.
    """
    token = create_access_token(current_user)
    return TokenDto(accessToken=token)
