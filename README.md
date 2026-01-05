

---

# Backend de Red Social con IA y Búsqueda Vectorial

Este proyecto es el backend de una red social desarrollado con **Java 21** y **Spring Boot 3**. Integra **Ollama** y una base de datos **PostgreSQL** con soporte vectorial para automatizar la categorización de publicaciones y generar recomendaciones personalizadas. Además, utiliza **MinIO** para el almacenamiento eficiente de archivos multimedia y está contenerizado con **Docker** para facilitar el despliegue.

## Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2.9
* **Base de Datos:** PostgreSQL 16+ con extensión `pgvector`
* **Almacenamiento de Objetos:** MinIO (Compatible con S3)
* **Infraestructura:** Docker y Docker Compose
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

### 3. Funcionalidad basica de red social.

El sistema permite la creacion de un perfil que puede publicar posts, ver posts, dar likes, seguir otros usuarios, ver perfiles y comentar.

## Estructura de la Base de Datos

El proyecto utiliza una estructura híbrida que combina tablas relacionales, columnas vectoriales y referencias a almacenamiento de objetos.

### Tablas Principales

* **`profile`**: Almacena la información de los usuarios.
    * `id`: Identificador único.
    * `user_embedding`: Columna de tipo `vector(768)`. Representa el **promedio de los gustos** del usuario.
    * `photo`: URL pública o interna que apunta a la imagen de perfil almacenada en **MinIO**.
    * `like_count`: Contador utilizado para calcular el promedio móvil.


* **`post`**: Almacena las publicaciones.
    * `post_id`: Identificador único.
    * `embedding`: Columna de tipo `vector(768)`. Representación numérica del contenido.
    * `image_url`: Referencia a la imagen adjunta almacenada en **MinIO**.
    * `category_id`: Clave foránea determinada automáticamente.


* **`post_category`**: Catálogo de categorías.
    * `category_id`: Identificador único.
    * `embedding`: Vector del "centro conceptual" de la categoría.


* **`liked_post`**: Tabla intermedia transaccional.
    * `pk`: Clave compuesta (`post_id`, `profile_id`).



## Instalación y Configuración

### Requisitos

* **Docker y Docker Compose** (Recomendado para la infraestructura).
* **Java 21** instalado.
* **Ollama** instalado en el host local.

### Pasos de Despliegue

1. **Configurar Ollama (Local):**
Descarga el modelo de embeddings necesario en tu máquina host:
```bash
ollama pull nomic-embed-text

```


2. **Infraestructura con Docker:**
El proyecto incluye un archivo `docker-compose.yml` que levanta los servicios necesarios (PostgreSQL con pgvector y MinIO).
```bash
docker-compose up -d

```


3. **Configuración de la Base de Datos:**
Al iniciar el contenedor, asegúrate de que la extensión vectorial esté habilitada:
```sql
CREATE EXTENSION IF NOT EXISTS vector;

```


4. **Ejecutar la aplicación:**
Con la infraestructura corriendo en Docker, iniciar desde IntelliJ o inicia la aplicación Spring Boot:
```bash
mvn spring-boot:run

```


