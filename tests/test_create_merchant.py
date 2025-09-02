from fastapi.testclient import TestClient
from fastapi import status
from api.main import app 
import random

client = TestClient(app)

def test_create_merchant():

    test_merchant_data = {
    "merchant": {
        "name": "string",
        "documentType": "CNPJ",
        "document": "string",
        "address": {
        "streetName": "string",
        "formattedAddress": "string",
        "streetNumber": "string",
        "city": "string",
        "postalCode": "string",
        "neighborhood": "string",
        "state": "string",
        "complement": "string",
        "latitude": 0,
        "longitude": 0
        }
    },
    "user": {
        "name": "string",
        "email": "user@example.com",
        "phone": "string",
        "password": "string"
    }
    }

    # Faça uma chamada para o endpoint de criação de merchant
    response = client.post("/auth/merchants/create/", json=test_merchant_data)
    
    print(response.json())

    # Verifique se a resposta é bem-sucedida (código 200)
    assert response.status_code == 200

    # Verifique se a resposta tem a estrutura esperada
    assert "accessToken" in response.json()
    assert "merchant" in response.json()
    
    headers = {"Authorization": f"Bearer {response.json()['accessToken']}"}

    
    response = client.delete("/merchants/final/delete", headers=headers)
    
    assert response.status_code == status.HTTP_200_OK

    
    