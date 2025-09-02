from fastapi import APIRouter
from api.application.controller.v1.auth_controller import auth_controller
from api.application.controller.v1.instituicao_controller import instituicao_controller
from api.application.controller.v1.solucao_controller import solucao_controller





v1_router = APIRouter(prefix="/v1")

v1_router.include_router(auth_controller)
v1_router.include_router(instituicao_controller)
v1_router.include_router(solucao_controller)