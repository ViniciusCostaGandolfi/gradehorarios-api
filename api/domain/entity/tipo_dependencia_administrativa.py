from enum import Enum


class TipoDependenciaAdministrativa(Enum):
    """
    Corresponde ao 'DependencyAdministrationType' do Java.
    """
    MUNICIPAL = "MUNICIPAL"
    ESTADUAL = "ESTADUAL"
    FEDERAL = "FEDERAL"
    PRIVADA = "PRIVADA"
