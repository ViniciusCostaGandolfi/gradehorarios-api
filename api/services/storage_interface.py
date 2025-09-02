from abc import ABC, abstractmethod

from pandas import DataFrame


class IStorageService(ABC):

    @abstractmethod
    def upload_object(self, df: DataFrame):
        pass