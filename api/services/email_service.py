from fastapi_mail import FastMail, ConnectionConfig
from pydantic import SecretStr
from api.infra.config.env_config import get_settings


conf = ConnectionConfig(
    MAIL_USERNAME = get_settings().EMAIL_USERNAME,
    MAIL_PASSWORD = SecretStr(get_settings().EMAIL_PASSWORD),
    MAIL_FROM = get_settings().EMAIL_USERNAME,
    MAIL_PORT = int(get_settings().EMAIL_PORT),
    MAIL_SERVER = get_settings().EMAIL_SERVER,
    MAIL_FROM_NAME="Email Grade Horários",
    MAIL_STARTTLS = True,
    MAIL_SSL_TLS = False,
    USE_CREDENTIALS = True,
    VALIDATE_CERTS = True
)

email_sandler = FastMail(conf)