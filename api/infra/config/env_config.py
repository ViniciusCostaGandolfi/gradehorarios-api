from pydantic_settings import BaseSettings, SettingsConfigDict
from dotenv import load_dotenv

load_dotenv()

class Settings(BaseSettings):
    TOKEN_SECRET_KEY: str = ''
    
    GRADEHORARIOS_SOLVER_URL: str = ''
    
    DATABASE_URL: str = ''
    
    EMAIL_PASSWORD: str = ''
    EMAIL_USERNAME: str = ''

    EMAIL_PORT: str = ''
    EMAIL_SERVER: str = ''

    MINIO_ENDPOINT: str = ''
    MINIO_ACCESS_KEY: str = ''
    MINIO_SECRET_KEY: str = ''
    MINIO_BUCKET_NAME: str = "gradehorarios"
    MINIO_SECURE: bool = True
    
    ABAS_INPUT_EXCEL = ['fsdlkmfdskmlfds', 'dsklfkjlsdkl']


    model_config = SettingsConfigDict(env_file='.env', extra='allow')
    
settings_singleton = Settings()

def get_settings() -> Settings:
    return settings_singleton