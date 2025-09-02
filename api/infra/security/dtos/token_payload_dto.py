from api.domain.dtos.usuario_dto import UsuarioDto
from api.infra.config.pydantic_config import CustomModel


class TokenPayloadDto(CustomModel):
    usuario: UsuarioDto
    exp: float