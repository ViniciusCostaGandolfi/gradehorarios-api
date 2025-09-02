import pandas as pd

from api.application.validators.solution_file.base_validator import ISolutionFileBaseValidator



class SolutionFileSheetsNamesValidator(ISolutionFileBaseValidator):
    def validate(self, df_dict: dict[str, pd.DataFrame]):
        expected_sheets = ["Cursos", "Disciplinas", "Professores", "Restrições", "Oferta"]
        for sheet in expected_sheets:
            if sheet not in df_dict.keys():
                raise ValueError(f"Erro, a aba {sheet} não esta presente no arquivo excel, baixe o modelo e revise.")

        
        
        
class ArquivoSolucaoValidaColunasAbaCadastramento(ISolutionFileBaseValidator):
    def validate(self, df_dict: dict[str, pd.DataFrame]):
        df = df_dict["1_Cadastramento"]
        colunas_esparados = ["dksfnfsdnldslknf"]
        for coluna in colunas_esparados:
            if coluna not in df.columns:
                raise ValueError(f"Erro, a coluna {coluna} não esta presente no arquivo excel, baixe o modelo e revise.")
