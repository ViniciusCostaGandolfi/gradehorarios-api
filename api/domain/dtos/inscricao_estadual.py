from typing import Optional

from pydantic import EmailStr
from api.infra.config.pydantic_config import CustomModel


class EnderecoBaseDto(CustomModel):
    pais: str
    estado: str
    cidade: str
    bairro: str
    codigo_postal: str
    logradouro: str
    numero: str
    endereco_formatado: str
    complemento: Optional[str] = None
    latitude: Optional[float] = None
    longitude: Optional[float] = None


class InscricaoEstadualBaseDto(CustomModel):
    codigo: int
    nome: str
    email: EmailStr
    telefone: Optional[str] = None
    localizacao: str
    dependencia_administrativa: str
    

class InscricaoEstadualCreateDto(InscricaoEstadualBaseDto):
    pass

class InscricaoEstadualDto(InscricaoEstadualCreateDto):
    id: int
    usuario_id: int