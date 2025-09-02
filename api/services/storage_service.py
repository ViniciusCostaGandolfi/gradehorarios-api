import io
from typing import BinaryIO

from fastapi import HTTPException
from minio import Minio
from minio.error import S3Error

from api.infra.config.env_config import get_settings



class MinioService:
    """
    Serviço para gerenciar a comunicação com o MinIO (upload, download, delete).
    """
    def __init__(self):
        try:
            self.client = Minio(
                endpoint=get_settings().MINIO_ENDPOINT,
                access_key=get_settings().MINIO_ACCESS_KEY,
                secret_key=get_settings().MINIO_SECRET_KEY,
                secure=get_settings().MINIO_SECURE
            )
            print("✅ Conexão com o MinIO estabelecida com sucesso.")
            self._ensure_bucket_exists(get_settings().MINIO_BUCKET_NAME)
        except Exception as e:
            print(f"❌ Erro ao conectar com o MinIO: {e}")
            self.client = None

    def _ensure_bucket_exists(self, bucket_name: str):
        """Verifica se um bucket existe e o cria caso não exista."""
        if self.client:
            found = self.client.bucket_exists(bucket_name)
            if not found:
                self.client.make_bucket(bucket_name)
                print(f"Bucket '{bucket_name}' criado com sucesso.")
            else:
                print(f"Bucket '{bucket_name}' já existe.")

    def upload_object(
        self,
        object_name: str,
        data: BinaryIO,
        length: int,
        content_type: str = 'application/octet-stream',
        bucket_name: str = get_settings().MINIO_BUCKET_NAME
    ) -> str:
        """
        Faz o upload (Create/Update) de um objeto para o MinIO.
        Se o objeto já existir, ele será sobrescrito.

        Args:
            object_name: O nome/caminho do arquivo no bucket (ex: 'documentos/fatura-07-2025.pdf').
            data: O conteúdo do arquivo como um stream de bytes (ex: file.file de um UploadFile).
            length: O tamanho total do stream em bytes.
            content_type: O MIME type do arquivo.
            bucket_name: O nome do bucket.
        """
        if not self.client:
            raise HTTPException(status_code=503, detail="Serviço de armazenamento indisponível.")
        
        try:
            result = self.client.put_object(
                bucket_name=bucket_name,
                object_name=object_name,
                data=data,
                length=length,
                content_type=content_type,
            )
            return result.bucket_name + result.object_name
        except S3Error as exc:
            raise HTTPException(status_code=500, detail=f"Erro no MinIO ao fazer upload: {exc}")

    def get_object(self, object_name: str, bucket_name: str = get_settings().MINIO_BUCKET_NAME) -> io.BytesIO:
        """
        Busca (Get) um objeto do MinIO e retorna seu conteúdo como um stream de bytes.
        """
        if not self.client:
            raise HTTPException(status_code=503, detail="Serviço de armazenamento indisponível.")

        try:
            response = self.client.get_object(bucket_name, object_name)
            file_content = io.BytesIO(response.read())
            response.close()
            response.release_conn()
            return file_content
        except S3Error as exc:
            if exc.code == "NoSuchKey":
                raise HTTPException(status_code=404, detail=f"Objeto '{object_name}' não encontrado.")
            raise HTTPException(status_code=500, detail=f"Erro no MinIO ao buscar objeto: {exc}")

    def delete_object(self, object_name: str, bucket_name: str = get_settings().MINIO_BUCKET_NAME) -> None:
        """
        Deleta um objeto do MinIO.
        """
        if not self.client:
            raise HTTPException(status_code=503, detail="Serviço de armazenamento indisponível.")

        try:
            self.client.remove_object(bucket_name, object_name)
        except S3Error as exc:
            raise HTTPException(status_code=500, detail=f"Erro no MinIO ao deletar objeto: {exc}")


minio_service_singleton = MinioService()


def get_minio_service() -> MinioService:
    """
    Dependência do FastAPI que retorna uma instância Singleton do MinioService.
    """
    if minio_service_singleton.client is None:
        raise HTTPException(status_code=503, detail="Serviço de armazenamento indisponível. Verifique a conexão com o MinIO.")
    return minio_service_singleton
