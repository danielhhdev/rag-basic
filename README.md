# rag-basic

Pipeline básico RAG (Retrieval-Augmented Generation) con Spring Boot 3, Java 21 y Qdrant.

## Estructura
- Extracción y chunking de documentos
- Embeddings con Spring AI
- Almacenamiento y búsqueda en Qdrant

## Requisitos
- Java 21
- Maven 3.9+
- Qdrant (local, docker: `docker run -p 6333:6333 qdrant/qdrant`)


## ENDPOINT
- POST /documents/upload    # Sube y procesa documento
- POST /rag/query           # Realiza consulta RAG con prompt

