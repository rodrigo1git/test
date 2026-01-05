
# Backend basico de red social

Este proyecto es el backend de una red social desarrollado con **Java 21** y **Spring Boot 3**. Integra **Ollama** y **PostgreSQL**.
La finalidad es hacer pruebas con los embeddings que brinda el modelo nombic-embed-text de ollama y ver como se integra la ia para el manejo de datos a baja escala

## Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.9
* **Base de Datos:** PostgreSQL 16+ con extensión `pgvector`, MinIO para almacenar imagenes.
* **ORM:** Hibernate 6 / Spring Data JPA
* **IA:** Spring AI con Ollama (corriendo localmente)
* **Modelo de Embeddings:** `nomic-embed-text` (768 dimensiones)
* **Seguridad:** Spring Security y JWT

## Funcionalidades Principales

### 1. Categorización Automática

El sistema asigna una **categoría** a cada publicación sin intervención del usuario. El proceso es el siguiente:

1. Se convierte el texto de la publicación en un **vector numérico (embedding)**.
2. Se buscan en la base de datos las publicaciones más **similares** a la nueva (K-Nearest Neighbors).
3. Se aplica un **algoritmo de votación ponderada**: se suman los puntajes de similitud de los vecinos encontrados agrupados por categoría. La categoría con mayor puntaje acumulado es la asignada. Si no hay suficiente coincidencia, se asigna la categoría "General".

### 2. Sistema de Recomendaciones (Content-Based Filtering)

El sistema recomienda contenido basándose en el historial de "me gusta" del usuario.

* Cada usuario tiene un **vector en la base de datos** que representa sus intereses promedio.
* Cuando el usuario da "me gusta" a una publicación, su vector personal se actualiza dinámicamente utilizando una **Media Móvil Acumulativa**. Esto promedia su historial previo con el vector de la nueva publicación.
* Para recomendar, el sistema realiza una consulta de **distancia coseno** para encontrar las publicaciones matemáticamente más cercanas al vector del usuario.

### 3. Funciones de red social.

El sistema permite crear un usuario, crear posts, comentar, likear, seguir.

## Estructura de la Base de Datos

El proyecto utiliza una estructura híbrida que combina tablas relacionales tradicionales con columnas de vectores.

### Tablas Principales

* **`profile`**: Almacena la información de los usuarios.
* `id`: Identificador único.
* `user_embedding`: Columna de tipo `vector(768)`. Representa el **promedio de los gustos** del usuario y evoluciona con cada like.
* `like_count`: Contador utilizado para calcular matemáticamente el promedio móvil de los vectores.


* **`post`**: Almacena las publicaciones.
* `post_id`: Identificador único.
* `embedding`: Columna de tipo `vector(768)`. Es la representación numérica del título y cuerpo del post generada por la IA.
* `category_id`: Clave foránea determinada automáticamente por el algoritmo de clasificación.


* **`post_category`**: Catálogo de categorías (Ej: Tecnología, Deportes).
* `category_id`: Identificador único.
* `embedding`: Columna de tipo `vector(768)`. Representa el "centro conceptual" de la categoría, útil para validaciones de similitud.


* **`liked_post`**: Tabla intermedia transaccional.
* `pk`: Clave compuesta (`post_id`, `profile_id`) para evitar duplicados (un usuario no puede dar like dos veces al mismo post).
* `liked_date`: Fecha de la interacción.



## Instalación y Configuración

### Requisitos

* **Java 21** instalado.
* **PostgreSQL** instalado y configurado.
* **Ollama** instalado y ejecutándose.

### Pasos

1. **Configurar Ollama:**
Descarga el modelo de embeddings necesario ejecutando:
```bash
ollama pull nomic-embed-text

```


2. **Configurar Base de Datos:**
Habilita la extensión vectorial en PostgreSQL:
```sql
CREATE EXTENSION IF NOT EXISTS vector;

```


3. **Ejecutar la aplicación:**
Usa Maven para iniciar el proyecto:
```bash
mvn spring-boot:run

```


