FROM python:3.15.0a8-alpine3.23

WORKDIR /app

# Install system dependencies needed to compile Pydantic/Rust tools
RUN apk add --no-cache gcc musl-dev python3-dev libffi-dev

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
