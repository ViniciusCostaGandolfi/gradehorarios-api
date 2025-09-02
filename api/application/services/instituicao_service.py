


from typing import List
from fastapi import HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy import select
from api.domain.dtos.instituicao_dto import InstituicaoBaseDto, InstituicaoCreateDto
from api.domain.entity.instituicao import Instituicao
from api.domain.entity.usuario import Usuario


class InstituicaoService:
    def __init__(self, db_session: AsyncSession):
        self.db = db_session

    async def create(self, instituicao_data: InstituicaoCreateDto, current_user: Usuario) -> Instituicao:
        nova_instituicao = Instituicao(
            **instituicao_data.model_dump(),
            usuario_id=current_user.id
        )
        self.db.add(nova_instituicao)
        await self.db.commit()
        await self.db.refresh(nova_instituicao)
        return nova_instituicao
    
    async def list(self, current_user: Usuario) -> List[Instituicao]:
        if current_user.tipo == 'CONSULTOR':
            return await self.list_by_admin()
        return await self.list_by_user(current_user)

    async def list_by_user(self, current_user: Usuario) -> List[Instituicao]:
        query = select(Instituicao).where(
            Instituicao.usuario_id == current_user.id,
            Instituicao.ativo == True
        )
        result = await self.db.execute(query)
        return [result for result in result.scalars().all()]
    
    async def list_by_admin(self) -> List[Instituicao]:
        query = select(Instituicao)
        result = await self.db.execute(query)
        return [result for result in result.scalars().all()]

    async def get_by_id(self, instituicao_id: int, current_user: Usuario) -> Instituicao:
        query = select(Instituicao).where(Instituicao.id == instituicao_id)
        result = await self.db.execute(query)
        db_instituicao = result.scalar_one_or_none()
        
        if not db_instituicao:
            raise HTTPException(status_code=404, detail="Instituição não encontrada.")
            
        if db_instituicao.usuario_id != current_user.id and current_user.tipo != 'CONSULTOR':
            raise HTTPException(status_code=403, detail="Acesso negado.")
            
        return db_instituicao

    async def update_by_id(self, instituicao_id: int, instituicao_data: InstituicaoBaseDto, current_user: Usuario) -> Instituicao:
        db_instituicao = await self.get_by_id(instituicao_id, current_user)
        
        update_data = instituicao_data.model_dump(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_instituicao, key, value)
            
        await self.db.commit()
        await self.db.refresh(db_instituicao)
        return db_instituicao

    async def delete_by_id(self, instituicao_id: int, current_user: Usuario) -> Instituicao:
        db_instituicao = await self.get_by_id(instituicao_id, current_user)
        
        db_instituicao.ativo = False
        
        await self.db.commit()
        await self.db.refresh(db_instituicao)
        return db_instituicao