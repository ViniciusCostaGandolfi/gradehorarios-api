from pydantic import EmailStr
from api.domain.dtos.usuario_dto import UsuarioBaseDto
from api.infra.config.pydantic_config import CustomModel


class TokenDto(CustomModel):
    accessToken: str
    

class LoginDto(CustomModel):
    email: EmailStr
    senha: str
    
    
class SiginDto(UsuarioBaseDto):
    senha: str