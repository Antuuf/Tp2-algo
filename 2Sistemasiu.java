package aed;

import java.util.List;

public class SistemaSIU {
    private Trie<Carrera> carreras;
    private Trie<Estudiante> estudiantes;

    
    enum CargoDocente {
        AY2, AY1, JTP, PROF
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.estudiantes = new Trie<>();
        for (String libreta : libretasUniversitarias) {
            estudiantes.insertar(libreta, new Estudiante(libreta));
        }

        this.carreras = new Trie<>();
        for (InfoMateria info : infoMaterias) {
            ParCarreraMateria[] parCarreraMaterias = info.getParesCarreraMateria();
            Materia mismaMateria = new Materia();
            for (ParCarreraMateria par : parCarreraMaterias) {
                String carreraNombre = par.getCarrera();
                String materiaNombre = par.getNombreMateria();
                mismaMateria.agregarNombreEquivalente(materiaNombre);
                
                Carrera carrera = carreras.obtenerDef(carreraNombre);
                if (carrera == null) {
                    carrera = new Carrera(carreraNombre);
                    carreras.insertar(carreraNombre, carrera);
                }
                
                carrera.agregarMateria(materiaNombre, mismaMateria);
            }
        }
    }

    public void inscribir(String estudianteLU, String carreraNombre, String materiaNombre) {
        Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (estudiante != null && carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                estudiante.incrementarMateriasInscriptas();
                materia.agregarAlumno(estudianteLU);
            }
        }
    }

    public void agregarDocente(CargoDocente cargo, String carreraNombre, String materiaNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                materia.agregarDocente(cargo.toString());
            }
        }
    }

    public int[] plantelDocente(String materiaNombre, String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                return materia.obtenerDocentes();
            }
        }
        return new int[]{0, 0, 0, 0};
    }

    public void cerrarMateria(String materiaNombre, String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                carrera.eliminarMateria(materiaNombre);
                List<String> nombresEquivalentes = materia.obtenerNombresEquivalentes();
                List<String> todasLasCarreras = carreras.obtenerClaves();

                for (String otraCarreraNombre : todasLasCarreras) {
                    Carrera otraCarrera = carreras.obtenerDef(otraCarreraNombre);
                    if (otraCarrera != null) {
                        for (String nombreEquivalente : nombresEquivalentes) {
                            otraCarrera.eliminarMateria(nombreEquivalente);
                        }
                    }
                }

                List<String> estudiantesInscritos = materia.obtenerListaAlumnos();
                for (String estudianteLU : estudiantesInscritos) {
                    Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
                    if (estudiante != null) {
                        estudiante.decrementarMateriasInscriptas();
                    }
                }
            }
        }
    }

    public int inscriptos(String materiaNombre, String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                return materia.obtenerCantidadAlumnos();
            }
        }
        return 0;
    }

    public boolean excedeCupo(String materiaNombre, String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            Materia materia = carrera.obtenerMateria(materiaNombre);
            if (materia != null) {
                return materia.excedeCupo();
            }
        }
        return false;
    }

    public String[] carreras() {
        List<String> listaCarreras = carreras.obtenerClaves();
        return listaCarreras.toArray(new String[0]);
    }

    public String[] materias(String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        if (carrera != null) {
            List<String> listaMaterias = carrera.listarMaterias();
            return listaMaterias.toArray(new String[0]);
        }
        return new String[0];
    }

    public int materiasInscriptas(String estudianteLU) {
        Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
        if (estudiante != null) {
            return estudiante.getMateriasInscriptas();
        }
        return 0;
    }
}


/* Invrep CLASE sistema siu
 - El trie carreras debe contener nombres únicos de carreras y cada valor debe ser una instancia válida de Carrera.
 - El trie estudiantes debe contener libretas universitarias únicas 
 -


 InvReps de los metodos 
 
Metodo inscribir
El estudiante identificado por estudianteLU debe existir en el Trie de estudiantes.
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera en el Trie.
El número de materias inscriptas del estudiante debe incrementarse después de la inscripción.

Metodo AgregarDocente
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
La cantidad de docentes de cada tipo para esa materia debe incrementarse en función del cargo.

Metodo PlantelDocente
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser un arreglo que contiene el número de docentes por cada tipo (PROFE, JTP, AY1, AY2).

Metodo CerrarMateria
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
Al cerrar una materia, todas las materias equivalentes en otras carreras deben ser eliminadas.
El número de materias inscriptas por cada estudiante que estaba inscrito en la materia cerrada debe decrecer.

Metodo Inscriptos
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser la cantidad de estudiantes inscritos en la materia.

Metodo Excedecupo
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser un booleano que indica si el número de inscriptos excede el cupo permitido.

Metodo Carreras
El resultado debe ser un arreglo ordenado alfabeticamente que contiene todos los nombres de las carreras almacenadas en el Trie, cada uno único.

Metodo materias 
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
El resultado debe ser un arreglo ordenado alfabeticamente que contiene todos los nombres de las materias asociadas a la carrera, cada uno único.

Metodo materiasInscriptas
El estudiante identificado por estudianteLU debe existir en el Trie de estudiantes.
El resultado debe ser el número de materias en las que el estudiante está inscrito.
 */
