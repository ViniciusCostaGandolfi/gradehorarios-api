

class TableError(Exception):
    """Exceção base para erros de validação nos dados de entrada."""
    def __init__(self, message: str):
        self.message = message
        super().__init__(self.message)