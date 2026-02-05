Descripción del Proyecto
Esta aplicación Java realiza un proceso de ETL (Extracción, Transformación y Carga) diseñado para migrar una estructura de datos compleja desde un entorno relacional (MySQL) a una base de datos orientada a objetos (Neodatis). El sistema gestiona la lógica de negocio de una red de gimnasios, incluyendo la administración de monitores, clases y responsabilidades jerárquicas.

Características Principales
Migración Automatizada: Traspaso completo de tablas relacionales a colecciones de objetos, manteniendo la integridad referencial y evitando duplicados mediante validaciones personalizadas.

Gestión de Relaciones Complejas: Implementación de relaciones 1:1 (Responsables de centro) y N:M (Monitores asignados a múltiples clases y gimnasios) utilizando estructuras de datos como Set y HashSet.

Motor de Consultas Avanzado: Sistema de reportes interactivo que utiliza la API de criterios de Neodatis para realizar:

Filtrado y ordenación avanzada (Consultas por provincia y localidad).

Cálculos estadísticos (Media de duración de clases por dificultad).

Consultas con operadores lógicos y de agregación (AND, NOT LIKE, GROUP BY).

Arquitectura Robusta: Manejo de excepciones para conexiones SQL y operaciones de persistencia, garantizando que el flujo de datos sea seguro.

Stack Tecnológico
Lenguaje: Java 8+

Bases de Datos: MySQL (Relacional) y Neodatis (Orientada a Objetos).

Librerías: JDBC para la conexión relacional y Neodatis ODB para la persistencia de objetos.
