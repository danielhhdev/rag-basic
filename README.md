# 🤖 RAG System - Retrieval-Augmented Generation

## 📋 Descripción del Proyecto
Este proyecto implementa un sistema RAG (Retrieval-Augmented Generation) utilizando Spring Boot, diseñado para demostrar la integración de técnicas de recuperación de información con modelos de lenguaje generativo. El sistema permite realizar consultas sobre documentos indexados, combinando la precisión de la búsqueda semántica con la capacidad generativa de LLMs.

### ¿Qué es un RAG?
Un sistema RAG combina dos componentes principales:

- Retrieval: Recuperación de información relevante de una base de conocimiento
- Augmented Generation: Generación de respuestas contextualizadas usando la información recuperada


## 🎯 Objetivos de Aprendizaje
- Implementar un pipeline completo de RAG
- Integrar embeddings y búsqueda vectorial
- Trabajar con modelos de lenguaje (LLMs)
- Aplicar patrones de arquitectura para sistemas de IA
- Demostrar conocimientos en procesamiento de lenguaje natural


## 🏗️ Arquitectura del Sistema

[Documentos] → [Procesamiento] → [Embeddings] → [Vector Store]
↓
[Usuario] → [Query] → [Retrieval] → [LLM] → [Respuesta Generada]

- Extracción y chunking de documentos
- Embeddings con Spring AI
- Almacenamiento y búsqueda en Qdrant

## 🛠️ Tecnologías Utilizadas
Backend
- Spring Boot 3.5
- Ollama (Modelo de LLM)
- qDrant (BD vectorial)
- Docker
- Maven
- OpenAPI


## 🚀 Requisitos
- Java 21
- Maven 3.9+
- Qdrant (local, docker: `docker run -p 6333:6333 qdrant/qdrant`)

##🔧 Implementación Técnica
Pipeline de Procesamiento

Ingesta de Documentos

Extracción de texto (PDF, DOCX, TXT)
Chunking inteligente
Limpieza y normalización


Generación de Embeddings

Transformación a vectores semánticos
Almacenamiento en base vectorial
Indexación para búsqueda eficiente


Proceso de Consulta

Embedding de la query
Búsqueda por similitud coseno
Ranking de resultados relevantes


Generación de Respuesta

Construcción del contexto
Prompt engineering
Llamada al LLM y post-procesamiento

## 📖 ENDPOINT
- POST /documents/upload    # Sube y procesa documento
- POST /rag/query           # Realiza consulta RAG con prompt

## 📈 Métricas y Evaluación
Métricas Implementadas

- Precision@K: Relevancia de los primeros K resultados
- Recall: Cobertura de información relevante
- Response Time: Tiempo de respuesta del sistema
- Semantic Similarity: Similitud semántica de respuestas

Endpoints de Monitoreo
- bashGET /api/metrics/performance
- GET /api/metrics/usage
- GET /api/health


## 📚 Documentación API

## 👨‍💻 Autor