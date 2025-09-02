from pydantic import EmailStr
from api.domain.dtos.usuario_dto import UsuarioBaseDto
from api.domain.entity.tipo_usuario import TipoUsuario
from api.infra.config.pydantic_config import CustomModel


class EmailTokenPayloadDto(CustomModel):
    email: EmailStr
    tipo: TipoUsuario
    exp: float