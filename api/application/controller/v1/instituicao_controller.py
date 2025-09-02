from typing import List
from fastapi import APIRouter, Depends, Response, status
from sqlalchemy.ext.asyncio import AsyncSession
from api.application.services.instituicao_service import InstituicaoService
from api.domain.dtos.instituicao_dto import InstituicaoBaseDto, InstituicaoCreateDto, InstituicaoDto, InstituicaoFullDto
from api.domain.entity.usuario import Usuario
from api.infra.security.auth_service import get_current_user
from api.services.database_service import get_db

instituicao_controller = APIRouter(prefix='/instituicoes', tags=['Instituição'])


@instituicao_controller.post("/", response_model=InstituicaoDto, status_code=status.HTTP_201_CREATED)
async def create(
    instituicao_data: InstituicaoCreateDto,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Cria uma nova instituição associada ao usuário autenticado."""
    
    nova_instituicao = await InstituicaoService(db).create(instituicao_data, current_user)
    return InstituicaoDto.from_entity(nova_instituicao)




@instituicao_controller.get("/", response_model=List[InstituicaoDto])
async def list_by_user(
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Lista todas as instituições do usuário autenticado."""
    instituicoes_db = await InstituicaoService(db).list(current_user)
    return [InstituicaoDto.from_entity(instituicao) for instituicao in instituicoes_db]


@instituicao_controller.get("/{instituicao_id}", response_model=InstituicaoDto)
async def get_by_id(
    instituicao_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Busca uma instituição específica pelo ID."""
    instituicao_db = await InstituicaoService(db).get_by_id(instituicao_id, current_user)
    return InstituicaoDto.from_entity(instituicao_db)

@instituicao_controller.get("/{instituicao_id}/full", response_model=InstituicaoDto)
async def get_by_id_full(
    instituicao_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Busca uma instituição específica pelo ID."""
    instituicao_db = await InstituicaoService(db).get_by_id(instituicao_id, current_user)
    return InstituicaoFullDto.from_entity(instituicao_db)


@instituicao_controller.put("/{instituicao_id}", response_model=InstituicaoDto)
async def update_by_id(
    instituicao_id: int,
    instituicao_dto: InstituicaoBaseDto,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Atualiza uma instituição do usuário autenticado."""
    instituicao_db = await InstituicaoService(db).update_by_id(instituicao_id, instituicao_dto, current_user)
    return InstituicaoDto.from_entity(instituicao_db)




@instituicao_controller.delete("/{instituicao_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_by_id(
    instituicao_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: Usuario = Depends(get_current_user)
):
    """Deleta uma instituição do usuário autenticado."""
    await InstituicaoService(db).delete_by_id(instituicao_id, current_user)
    
    return Response(status_code=status.HTTP_204_NO_CONTENT)