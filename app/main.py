from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

# Define a schema for the POST request body
class Item(BaseModel):
    name: str
    price: float
    is_offer: bool = None

# 1. Simple GET request
@app.get("/")
def read_root():
    return {"message": "Hello, World!"}

# 2. Simple POST request
@app.post("/items/")
def create_item(item: Item):
    # Here you would typically save 'item' to a database
    return {"status": "item created", "data": item}
