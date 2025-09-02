import uuid
from fastapi.concurrency import run_in_threadpool
import pandas as pd
from fastapi import UploadFile, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from io import BytesIO

from api.application.services.instituicao_service import InstituicaoService
from api.application.validators.solution_file.solution_file_validator import SolutionFileValidator
from api.services.storage_service import get_minio_service


class SolucaoService:
    def __init__(self, db_session: AsyncSession):
        self.db = db_session
        self.file_validator = SolutionFileValidator()
        self.minio_service = get_minio_service()
        self.instituicao_service = InstituicaoService(db_session)

    async def validate_and_parse_excel(self, file: UploadFile) -> dict[str, pd.DataFrame]:
        """
        Lê um arquivo Excel em memória, valida sua estrutura e retorna os dados parseados.
        Levanta HTTPException em caso de erro de validação.
        """
        file_content = await file.read()
        
        allowed_content_types = [
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ]
        
        if file.content_type not in allowed_content_types:
            raise HTTPException(
                status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
                detail=f"Tipo de arquivo não suportado. Use um dos seguintes: {', '.join(allowed_content_types)}"
            )

        try:
            excel_data_dict = pd.read_excel(BytesIO(file_content), sheet_name=None)
            self.file_validator.run(excel_data_dict)

            return excel_data_dict

        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
                detail=f"{e}"
            )
            
            
    async def create_solution(self, instituicao_id: int, file: UploadFile):
        dict_df = await self.validate_and_parse_excel(file)
                
        unique_filename = f"{uuid.uuid4()}_{file.filename}"
        object_name = f"instituicao_{instituicao_id}/entradas/{unique_filename}"
        file_data = await file.read()
        file_size = len(file_data)
        
        result = await run_in_threadpool(
            self.minio_service.upload_object,
            object_name=object_name,
            data=file.file,
            length=file_size,
            content_type="application/vnd.ms-excel"
        )
