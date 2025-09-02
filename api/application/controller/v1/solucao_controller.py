from io import BytesIO
from typing import List
import uuid
from fastapi import APIRouter, Depends, File, HTTPException, UploadFile, status
from sqlalchemy.ext.asyncio import AsyncSession
from api.application.services.instituicao_service import InstituicaoService
from api.application.services.solucao_service import SolucaoService
from api.domain.dtos.solucao import SolucaoDto
from starlette.concurrency import run_in_threadpool
from api.domain.entity.usuario import Usuario
from api.infra.security.auth_service import get_current_user
from api.services.database_service import get_db
from api.services.storage_service import MinioService, get_minio_service

solucao_controller = APIRouter(prefix='/instituicoes/{instituicao_id}/solucoes', tags=['Solução'])


@solucao_controller.post("/", response_model=SolucaoDto, status_code=status.HTTP_201_CREATED)
async def create_solucao_with_upload(
    instituicao_id: int,
    file: UploadFile = File(...),
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
    minio_service: MinioService = Depends(get_minio_service)
):
    """
    Cria uma nova Solução fazendo o upload de um arquivo de entrada (Excel).
    """
    solucao_service = SolucaoService(db)
    resp = await solucao_service.create_solution(instituicao_id, file)
    
    return {
        "OK"
    }


@solucao_controller.get("/", response_model=List[SolucaoDto])
async def list(
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Lista todas as instituições do usuário autenticado."""



@solucao_controller.get("/", response_model=SolucaoDto)
async def get_by_id(
    instituicao_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Busca uma Solução específica pelo ID."""




@solucao_controller.delete("/", status_code=status.HTTP_204_NO_CONTENT)
async def delete_by_id(
    instituicao_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Deleta uma Solução do usuário autenticado."""
