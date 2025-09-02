from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from api.application.controller.v1.router import v1_router


app = FastAPI(
    
    title='Grade Horarios API',
    version='v1',
    swagger_ui_parameters={"syntaxHighlight.theme": "obsidian"}
)

app.add_middleware(
    CORSMiddleware,
    allow_credentials=True,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
    
)


app.include_router(v1_router)