# Sistema de Gestión Universitaria (SIU) - AED II

Este proyecto es una implementación eficiente de un sistema de inscripción y gestión universitaria (inspirado en el SIU Guaraní) desarrollado como el **Trabajo Práctico 2** para la materia **Algoritmos y Estructuras de Datos II** (FCEN-UBA).

El objetivo principal es modelar las operaciones típicas de una universidad (carreras, materias, inscripciones de alumnos, asignaciones docentes y control de cupos) garantizando **complejidades algorítmicas óptimas** mediante el uso de estructuras de datos avanzadas, específicamente árboles de tipo **Trie** (árboles de prefijos).

---

##  Características Principales

1. **Gestión de Carreras y Materias**: Permite estructurar las materias dentro de sus respectivas carreras.
2. **Equivalencia de Materias**: Soporta que una misma materia física se dicte en múltiples carreras con nombres distintos (ej. *Álgebra I* en Matemática y *Álgebra* en Computación). Ambas comparten el mismo cupo, alumnos y plantel docente.
3. **Inscripción de Estudiantes**: Permite inscribir estudiantes utilizando su Libreta Universitaria (LU), controlando la cantidad de materias en las que está inscripto cada uno.
4. **Plantel Docente**: Permite asignar cargos docentes (`PROF` - Profesor, `JTP` - Jefe de Trabajos Prácticos, `AY1` - Ayudante de Primera, y `AY2` - Ayudante de Segunda).
5. **Control de Cupos**: Verifica en tiempo real si una materia excede su cupo de alumnos permitido según las relaciones docentes/alumnos establecidas:
   - **1 Profesor** por cada **250** alumnos.
   - **1 JTP** por cada **100** alumnos.
   - **1 Ayudante de 1ra** por cada **20** alumnos.
   - **1 Ayudante de 2da** por cada **30** alumnos.
   *El cupo se excede si no se cumple alguna de estas proporciones.*
6. **Cierre de Materias**: Da de baja una materia y sus equivalentes en todas las carreras asociadas, decrementando automáticamente la cantidad de inscripciones de los estudiantes afectados.

---

##  Estructura del Proyecto

El código está organizado bajo el paquete `aed` y consta de los siguientes archivos clave:

- **[Trie.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/Trie.java)**: Implementación genérica de un árbol de prefijos (Trie) optimizado para caracteres ASCII (alfabeto de tamaño 256). Proporciona operaciones de inserción, búsqueda, borrado y listado ordenado de claves con complejidad dependiente únicamente de la longitud de la clave ($O(|clave|)$).
- **[SistemaSIU.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/SistemaSIU.java)**: Clase principal que orquesta el sistema. Utiliza una estructura de Tries anidados (`Trie<Trie<Materia>>`) para modelar la relación carrera-materia de forma eficiente.
- **[Materia.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/Materia.java)**: Representa una materia física. Almacena las estadísticas del plantel docente, la lista de alumnos inscriptos, y referencias a los Tries de las carreras donde se dicta para gestionar equivalencias y borrados rápidos.
- **[Carrera.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/Carrera.java)** & **[Estudiante.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/Estudiante.java)**: Modelos alternativos que estructuran los datos de carreras y estudiantes.
- **[InfoMateria.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/InfoMateria.java)** & **[ParCarreraMateria.java](file:///c:/Users/Antonella/Proyectos/Tp2-algo/ParCarreraMateria.java)**: Clases auxiliares utilizadas para el pasaje de parámetros e inicialización del sistema.

---

##  Análisis de Complejidad

Para cumplir con las restricciones de rendimiento del trabajo práctico, todas las operaciones fueron diseñadas para respetar las siguientes cotas de complejidad temporal:

| Operación | Complejidad | Descripción |
| :--- | :--- | :--- |
| **Constructor** (`SistemaSIU`) | $O(\sum_{c \in C} \lvert c \rvert \cdot \lvert M_c \rvert + \sum_{m \in M} \sum_{n \in N_m} \lvert n \rvert + E_m)$ | Inicialización del sistema con carreras, materias y alumnos. |
| **Inscribir** | $O(\lvert c \rvert + \lvert m \rvert)$ | Inscribe un estudiante en una materia de una carrera. |
| **Agregar Docente** | $O(\lvert c \rvert + \lvert m \rvert)$ | Agrega un cargo docente a una materia determinada. |
| **Plantel Docente** | $O(\lvert c \rvert + \lvert m \rvert)$ | Obtiene la cantidad de docentes por categoría en la materia. |
| **Cerrar Materia** | $O(\lvert c \rvert + \lvert m \rvert + \sum_{n \in N_m} \lvert n \rvert + E_m)$ | Da de baja una materia y actualiza la cantidad de materias de sus inscriptos. |
| **Inscriptos** | $O(\lvert c \rvert + \lvert m \rvert)$ | Devuelve la cantidad de alumnos anotados en la materia. |
| **Excede Cupo** | $O(\lvert c \rvert + \lvert m \rvert)$ | Verifica si la relación docente-alumno supera las capacidades. |
| **Carreras** | $O(\sum_{c \in C} \lvert c \rvert)$ | Obtiene todas las carreras en orden lexicográfico. |
| **Materias** | $O(\lvert c \rvert + \sum_{m_c \in M_c} \lvert m_c \rvert)$ | Obtiene todas las materias de una carrera en orden lexicográfico. |
| **Materias Inscriptas** | $O(1)$ | Consulta cuántas materias tiene cursando un estudiante (por LU). |

### Referencia de variables:
- $C$: Conjunto de todas las carreras.
- $M$: Conjunto de todas las materias.
- $M_c$: Materias pertenecientes a la carrera $c$.
- $N_m$: Nombres equivalentes que posee la materia $m$.
- $E_m$: Estudiantes inscriptos en la materia $m$.
- $\lvert x \rvert$: Longitud del string $x$.

---

##  Invariantes de Representación (InvRep)

Cada componente clave posee invariantes de representación bien definidos para asegurar la consistencia del estado del sistema:

### `SistemaSIU`
- El trie `carreras` contiene nombres únicos de carreras como claves y un `Trie<Materia>` como definición.
- El trie `estudiantes` contiene como claves las Libretas Universitarias válidas e inicializadas, asociadas a un valor entero $\ge 0$ que indica la cantidad de materias a las que están inscriptos.
- Las materias compartidas (equivalentes) apuntan a la misma instancia de `Materia`, garantizando consistencia centralizada de datos.

### `Trie<T>`
- Cada nodo cuenta con un arreglo `siguientes` de tamaño fijo (256) inicializado con referencias nulas.
- Las claves almacenadas terminan en un nodo donde `definicion` no es nula.

---

## ⚙️ Requisitos y Compilación

- **Java Development Kit (JDK)**: 8 o superior.
- **IDE Recomendado**: IntelliJ IDEA, Eclipse o Visual Studio Code con soporte para Java.
