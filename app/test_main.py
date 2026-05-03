from fastapi.testclient import TestClient
from main import app

client = TestClient(app)


def test_read_root():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"message": "Hello, World!"}


def test_create_item_valid():
    payload = {"name": "Widget", "price": 9.99, "is_offer": True}
    response = client.post("/items/", json=payload)
    assert response.status_code == 200
    assert response.json()["status"] == "item created"
    assert response.json()["data"]["name"] == "Widget"


def test_create_item_missing_field():
    payload = {"price": 9.99}          # missing required 'name'
    response = client.post("/items/", json=payload)
    assert response.status_code == 422  # FastAPI validation error


def test_create_item_default_offer():
    payload = {"name": "Gadget", "price": 4.99}   # is_offer omitted
    response = client.post("/items/", json=payload)
    assert response.status_code == 200
    assert response.json()["data"]["is_offer"] is None
