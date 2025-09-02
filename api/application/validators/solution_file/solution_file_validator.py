import pandas as pd

from api.application.validators.solution_file.base_validator import ISolutionFileBaseValidator
from api.application.validators.solution_file.solution_file_validators import *



class SolutionFileValidator:
    """
    Orquestrador que executa um conjunto de validadores para o arquivo de entrada da solução.
    """
    def __init__(self):
        self._validators: list[ISolutionFileBaseValidator] = [
            SolutionFileSheetsNamesValidator(),
            ArquivoSolucaoValidaColunasAbaCadastramento()
        ]

    def run(self, df: dict[str, pd.DataFrame]):
        """Executa todos os validadores em sequência."""
        for validator in self._validators:
            validator.validate(df)