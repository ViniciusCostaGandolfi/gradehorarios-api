from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker

from sqlalchemy.orm import DeclarativeBase



from api.infra.config.env_config import get_settings





async_engine = create_async_engine(get_settings().DATABASE_URL)





AsyncSessionLocal = async_sessionmaker(

    bind=async_engine,

    autocommit=False,

    autoflush=False,

    expire_on_commit=False,

)



class Base(DeclarativeBase):
    pass



async def get_db():
    async with AsyncSessionLocal() as session:
        try:
            yield session
        finally:
            await session.close()