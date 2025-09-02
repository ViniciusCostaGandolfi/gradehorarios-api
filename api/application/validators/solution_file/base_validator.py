from abc import ABC, abstractmethod
import pandas as pd


class ISolutionFileBaseValidator(ABC):

    @abstractmethod
    def validate(self, df_dict: dict[str, pd.DataFrame]):
        pass