# Copia de basica de Twitter Backend

Este proyecto es el backend de una red social que emula a Twitter construida con **Java 21** y **Spring Boot 3**. 
Esta aplicación integra capacidades de **Inteligencia Artificial Generativa** (Spring AI + Ollama) 
y **Búsqueda Vectorial** (PostgreSQL + pgvector) para automatizar la categorización de contenido y personalizar la experiencia del usuario en tiempo real sin depender de modelos externos costosos.

## Tech Stack

### Core
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.9
* **Build Tool:** Maven

### Datos & IA
* **Base de Datos:** PostgreSQL 16+
* **Extensión Vectorial:** `pgvector`
* **ORM:** Hibernate 6 / Spring Data JPA
* **IA Integration:** Spring AI (0.8.x / 1.0.0-Mx)
* **Modelo de Embeddings:** Ollama local (`nomic-embed-text`)

### Seguridad
* **Autenticación:** Spring Security + JWT (Stateless)

---

##  Características Principales & Lógica de Negocio

### 1. Categorización Automática Inteligente (RAG + K-NN Hybrid)
Al crear un post:
1.  **Vectorización:** Se genera un embedding (vector de 768 dimensiones) del contenido del post usando Ollama.
2.  **Eleccion de categorias cantidatas:** Se eligen n categorias candidatas(se comparan los vectores de cada categoria con el vector del post y se eligen las mas cercanas) 
3.  **Búsqueda de Vecinos (K-NN):** Se buscan los posts más similares semánticamente en la base de datos, antes se filtran los posts segun las categorias candidatas.
4.  **Algoritmo de Votación Ponderada (Weighted Voting):**
    * En lugar de una votación simple, sumamos los *scores de similitud* de los vecinos por categoría.
    * Si la categoría ganadora supera el `CATEGORY_CONFIDENCE_THRESHOLD`, se asigna automáticamente.
    * Si hay un "empate técnico" o baja confianza, se asigna a "General".

### 2. Perfilado de Usuario Dinámico (Content-Based Filtering)
El sistema aprende de los gustos del usuario en tiempo real sin necesidad de re-entrenar modelos complejos de Deep Learning.
* **User Embedding:** Cada perfil tiene un vector (`user_embedding`) que representa el "centro de gravedad" de sus intereses.
* **Algoritmo de Media Móvil (Cumulative Moving Average):**
    * Cuando un usuario da "Like", su vector personal se actualiza matemáticamente:
    * $$V_{nuevo} = \frac{(V_{actual} \times N) + V_{post}}{N + 1}$$
    * Esto permite que la recomendación evolucione instantáneamente con cada interacción.

### 3. Recomendaciones Personalizadas
El feed de "Para ti" se genera mediante una consulta vectorial híbrida:
* Se buscan los posts cuya distancia coseno sea menor respecto al `user_embedding` actual.
* Se excluyen posts ya interactuados (vistos/likeados).

### 4. Optimización de Rendimiento JPA
* Uso extensivo de `getReferenceById` en lugar de `findById` para operaciones de escritura (como Likes), evitando consultas `SELECT` innecesarias y trabajando con Proxies de Hibernate.

---

## Arquitectura de Base de Datos

El esquema se basa en tablas relacionales con columnas vectoriales especializadas.

### Tablas Clave

* **`post`**
    * `id` (PK)
    * `embedding`: `vector(768)` (Generado por `nomic-embed-text`)
    * `like_count`: `bigint DEFAULT 0`
* **`profile`**
    * `id` (PK)
    * `user_embedding`: `vector(768)` (Promedio de gustos)
* **`post_category`**
    * `category_id` (PK)
    * `embedding`: `vector(768)` (Centroide de la categoría)
* **`liked_post`**
    * `post_id`, `profile_id` (Composite PK - Evita duplicados a nivel de DB)
    * `liked_date`: `timestamp`

---

##  Configuración del Entorno

### 1. Requisitos Previos
* Tener **Ollama** instalado y corriendo.
* Descargar el modelo de embeddings:
    ```bash
    ollama pull nomic-embed-text
    ```
* Tener PostgreSQL instalado.

### 2. Base de Datos
Es obligatorio activar la extensión `vector` en la base de datos:

