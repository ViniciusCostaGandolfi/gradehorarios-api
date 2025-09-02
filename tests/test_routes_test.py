from fastapi.testclient import TestClient
from api.main import app 


client = TestClient(app)

    
    
def test_ms_routes():
    response = client.post("/routes/test/auto_generate/30/")
    print(response)
    assert response.status_code == 200

