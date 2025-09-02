from fastapi import HTTPException, status
from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession # ✅ Importe a sessão assíncrona

from api.domain.dtos.auth_dto import LoginDto, SiginDto
from api.domain.entity.tipo_usuario import TipoUsuario
from api.domain.entity.usuario import Usuario
from api.infra.security.password_crypt_service import hash_password, verify_password

class AuthService:
    def __init__(self, db_session: AsyncSession):
        self.db = db_session

    async def register_user(self, user_data: SiginDto) -> Usuario:
        """
        Verifica se um usuário já existe e cria um novo, de forma assíncrona.
        """
        query = select(Usuario).where(Usuario.email == user_data.email)
        result = await self.db.execute(query)
        existing_user = result.scalar_one_or_none()
        
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="E-mail já registrado."
            )

        hashed_pw = hash_password(user_data.senha)
        
        new_user = Usuario(
            nome=user_data.nome,
            email=user_data.email,
            senha=hashed_pw,
            telefone=user_data.telefone,
            documento=user_data.documento,
            tipo=TipoUsuario.USUARIO.value
        )

        self.db.add(new_user)
        await self.db.commit()
        await self.db.refresh(new_user)
        return new_user

    async def login_user(self, login_data: LoginDto) -> Usuario:
        """
        Busca um usuário e verifica sua senha, de forma assíncrona.
        """
        query = select(Usuario).where(Usuario.email == login_data.email)
        result = await self.db.execute(query)
        user = result.scalar_one_or_none()

        if not user or not verify_password(login_data.senha, user.senha):
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="E-mail ou senha incorretos"
            )
        
        return user