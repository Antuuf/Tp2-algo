
package aed;

import java.util.List;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras;
    private Trie<Integer> estudiantes;

    enum CargoDocente {
        AY2, AY1, JTP, PROF
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.estudiantes = new Trie<>();
        for (String libreta : libretasUniversitarias) {
            estudiantes.insertar(libreta, 0);
        }

        this.carreras = new Trie<>();
        for (InfoMateria info : infoMaterias) {
            ParCarreraMateria[] parCarreraMaterias = info.getParesCarreraMateria();
            Materia mismaMateria = new Materia();
            for (ParCarreraMateria par : parCarreraMaterias) {
                String carrera = par.getCarrera();
                String materia = par.getNombreMateria();
                mismaMateria.agregarNombreEquivalente(materia);
                if (!carreras.pertenece(carrera)) {
                    Trie<Materia> materiaCarrera = new Trie<>();
                    mismaMateria.agregarReferencia(materiaCarrera);
                    materiaCarrera.insertar(materia, mismaMateria);
                    carreras.insertar(carrera, materiaCarrera);
                } else {
                    Trie<Materia> materiaCarrera = carreras.obtenerDef(carrera);
                    materiaCarrera.insertar(materia, mismaMateria);
                    mismaMateria.agregarReferencia(materiaCarrera);
                }
            }
        }
    }
    
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// Procesar equivalentes y cerrar materia depende del número de equivalentes y estudiantes inscritos, O(∑m∈M ∑n∈Nm​ ∣n∣ + Em)
// Por lo tanto, O(∑c∈C​ ∣c∣ ⋅ ∣Mc∣ + ∑m∈M ∑n∈Nm​ ∣n∣ + Em).

    public void inscribir(String estudiante, String carrera, String materia) {
        if (estudiante != null && carrera != null && materia != null) {
            Trie<Materia> car = carreras.obtenerDef(carrera);
            if (car != null) {
                Materia mat = car.obtenerDef(materia);
                if (mat != null) {
                    if (estudiantes.pertenece(estudiante)) {
                        estudiantes.insertar(estudiante, estudiantes.obtenerDef(estudiante) + 1);
                    } else {
                        estudiantes.insertar(estudiante, 1);
                    }
                    mat.agregarAlumno(estudiante);
                }
            }
        }
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// Verificar y actualizar estudiantes tiene una complejidad constante promedio O(1)
// O (|c| + |m|)

    public void agregarDocente(CargoDocente cargo, String carrera, String materia) {
        Trie<Materia> carreraParaAgregar = carreras.obtenerDef(carrera);
        if (carreraParaAgregar != null) {
            Materia materiaParaAgregar = carreraParaAgregar.obtenerDef(materia);
            if (materiaParaAgregar != null) {
                String cargoAgregar = cargo.toString();
                materiaParaAgregar.agregarDocente(cargoAgregar);
            }
        }
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// Agregar docente y procesar equivalentes depende del número de equivalentes, lo cual es constante en el peor caso
// O (|c| + |m|)

    public int[] plantelDocente(String materia, String carrera) {
        Trie<Materia> car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerDef(materia);
            if (mat != null) {
                return mat.obtenerDocentes();
            }
        }
        return new int[]{0, 0, 0, 0};
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// O (|c| + |m|)

    public void cerrarMateria(String materia, String carrera) {
        Trie<Materia> carrerasTrie = carreras.obtenerDef(carrera);
        if (carrerasTrie != null) {
            Materia mat = carrerasTrie.obtenerDef(materia);

            if (mat != null) {
                carrerasTrie.borrar(materia);

                List<String> nombresEquivalentes = mat.obtenerNombresEquivalentes();

                List<String> todasLasCarreras = carreras.obtenerClaves();
                for (String otraCarrera : todasLasCarreras) {
                    Trie<Materia> otraCarreraTrie = carreras.obtenerDef(otraCarrera);
                    if (otraCarreraTrie != null) {
                        for (String nombreEquivalente : nombresEquivalentes) {
                            if (otraCarreraTrie.pertenece(nombreEquivalente)) {
                                otraCarreraTrie.borrar(nombreEquivalente);
                            }
                        }
                    }
                }

                List<String> estudiantesInscritos = mat.obtenerListaAlumnos();
                for (String estudiante : estudiantesInscritos) {
                    if (estudiantes.pertenece(estudiante)) {
                        int inscripciones = estudiantes.obtenerDef(estudiante);
                        estudiantes.insertar(estudiante, inscripciones - 1);
                    }
                }
            }
        }
    }

// = O (|c| + |m| + ∑n∈Nm ∣n∣ + Em)

    public int inscriptos(String materia, String carrera) {
        Trie<Materia> car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerDef(materia);
            if (mat != null) {
                return mat.obtenerCantidadAlumnos();
            }
        }
        return 0;
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// O (|c| + |m|)

    public boolean excedeCupo(String materia, String carrera) {
        Trie<Materia> car = carreras.obtenerDef(carrera);
        if (car != null) {
            Materia mat = car.obtenerDef(materia);
            if (mat != null) {
                return mat.excedeCupo();
            }
        }
        return false;
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// O (|c| + |m|)

    public String[] carreras() {
        List<String> listaCarreras = carreras.obtenerClaves();
        return listaCarreras.toArray(new String[0]);
    }
// Obtener todas las claves del trie carreras tiene complejidad O(∑ c∈C ∣c∣),
// Ordenar la lista de carreras tiene complejidad O(n log n)
// Por lo tanto: O (∑ c∈C ∣c∣)

    public String[] materias(String carrera) {
        Trie<Materia> car = carreras.obtenerDef(carrera);
        if (car != null) {
            List<String> listaMaterias = car.obtenerClaves();
            return listaMaterias.toArray(new String[0]);
        }
        return new String[0];
    }
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener todas las claves del trie car tiene complejidad O(∑ mc∈Mc ∣mc∣),
// Ordenar la lista de materias tiene complejidad O(m log m)
// Por lo tanto: O (|c| + ∑mc∈Mc ∣mc∣)

    public int materiasInscriptas(String estudiante) {
        if (estudiantes.pertenece(estudiante)) {
            return estudiantes.obtenerDef(estudiante);
        }
        return 0;
    }
//Obtener el valor del trie estudiantes tiene complejidad O(1)
// Por lo tanto: O(1)
}

/*
1. nuevoSistema:
// InvRep:
// - Cada libretaUniversitaria debe estar en el trie estudiantes con un valor inicial de 0.
// - Cada InfoMateria debe estar correctamente representada en el trie carreras.
// - Para cada carrera en carreras, debe existir un trie de materias.
// - Para cada materia en una carrera, debe existir una instancia de Materia que tenga referencias a todas sus equivalentes.
2. incribir
// InvRep:
// - El estudiante debe estar inscrito en la materia y su contador en el trie estudiantes debe estar actualizado correctamente.
// - La carrera y la materia deben existir en el trie carreras.
// - La materia debe existir dentro de la carrera en el trie carreras.
3.inscriptos
// InvRep:
// - La carrera y la materia deben existir en el trie carreras.
// - La cantidad de estudiantes inscriptos en una materia debe ser consistente con las inscripciones realizadas.
4. agregarDocente
// InvRep:
// - La carrera y la materia deben existir en el trie carreras.
// - El docente debe ser agregado a la materia y a todas sus equivalentes.
// - Las referencias entre materias equivalentes deben estar actualizadas.
5. plantelDocente
// InvRep:
// - La carrera y la materia deben existir en el trie carreras.
// - La lista de docentes debe estar actualizada y reflejar el estado actual de la materia.
6. excedeCupo?
// InvRep:
// - La carrera y la materia deben existir en el trie carreras.
// - La cantidad de estudiantes inscriptos debe ser precisa y reflejar las inscripciones actuales.
7. carreras
// InvRep:
// - El trie carreras debe estar completo y contener todas las carreras.
// - El orden lexicográfico debe ser correcto.
8. materias 
// InvRep:
// - La carrera debe existir en el trie carreras.
// - El trie de materias para la carrera debe contener todas las materias de la carrera.
// - El orden lexicográfico debe ser correcto.
9. materiasInscriptas
// InvRep:
// - El estudiante debe existir en el trie estudiantes.
// - La cantidad de materias en las que el estudiante está inscrito debe ser precisa.
10. cerrarMateria
// InvRep:
// - La carrera y la materia deben existir en el trie carreras.
// - Todas las materias equivalentes deben ser cerradas correctamente.
// - La información sobre estudiantes y docentes debe ser actualizada para reflejar el cierre de la materia.
 */
