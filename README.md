Backend de Red Social con IA y Búsqueda Vectorial
Este proyecto es el backend de una red social desarrollado con Java 21 y Spring Boot 3. Integra Inteligencia Artificial local (Ollama) y una base de datos PostgreSQL con soporte vectorial para automatizar la categorización de publicaciones y generar recomendaciones personalizadas.

Tecnologías Utilizadas
Lenguaje: Java 21

Framework: Spring Boot 3.2.9

Base de Datos: PostgreSQL 16+ con extensión pgvector

ORM: Hibernate 6 / Spring Data JPA

IA: Spring AI con Ollama (corriendo localmente)

Modelo: nomic-embed-text (768 dimensiones)

Seguridad: Spring Security y JWT

Funcionalidades Principales
1. Categorización Automática
El sistema asigna una categoría a cada publicación sin intervención del usuario. El proceso es el siguiente:

Se convierte el texto de la publicación en un vector numérico (embedding).

Se buscan en la base de datos las publicaciones más similares a la nueva.

Se aplica un algoritmo de votación: se suman los puntajes de similitud de los vecinos encontrados. La categoría con mayor puntaje acumulado es la asignada. Si no hay suficiente coincidencia, se asigna la categoría "General".

2. Sistema de Recomendaciones
El sistema recomienda contenido basándose en el historial de "me gusta" del usuario.

Cada usuario tiene un vector en la base de datos que representa sus intereses promedio.

Cuando el usuario da "me gusta" a una publicación, su vector personal se actualiza promediando su vector actual con el vector de la publicación.

Para recomendar, el sistema busca las publicaciones matemáticamente más cercanas al vector del usuario.

3. Búsqueda Semántica
Permite buscar publicaciones por su significado o contexto, en lugar de buscar solo por palabras exactas.

Estructura de la Base de Datos
El proyecto utiliza una estructura híbrida que combina tablas relacionales con columnas de vectores.

profile: Guarda los datos del usuario. Incluye una columna user_embedding que almacena el promedio de sus gustos.

post: Guarda el contenido y la columna embedding generada por la IA.

post_category: Guarda las categorías disponibles.

liked_post: Tabla intermedia para registrar las interacciones entre usuarios y publicaciones.

Instalación y Configuración
Requisitos
Java 21 instalado.

PostgreSQL instalado y configurado.

Ollama instalado y ejecutándose.
