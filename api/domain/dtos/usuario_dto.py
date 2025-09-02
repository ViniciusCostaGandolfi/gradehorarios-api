from pydantic import EmailStr
from api.domain.entity.tipo_usuario import TipoUsuario
from api.infra.config.pydantic_config import CustomModel


class UsuarioBaseDto(CustomModel):
    nome: str
    email: EmailStr
    telefone: str
    documento: str
    tipo: TipoUsuario

class UsuarioDto(UsuarioBaseDto):
    id: int