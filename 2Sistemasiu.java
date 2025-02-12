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
// Obtener carrera del trie carreras tiene complejidad O(|c|).
// Obtener materia del trie car tiene complejidad O(|m|).
// Procesar equivalentes y cerrar materia depende del número de equivalentes y estudiantes inscritos, O(∑m∈M ∑n∈Nm​ ∣n∣ + Em)
// Por lo tanto, O(∑c∈C​ ∣c∣ ⋅ ∣Mc∣ + ∑m∈M ∑n∈Nm​ ∣n∣ + Em).


public void inscribir(String estudianteLU, String carreraNombre, String materiaNombre) {
    Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
    Carrera carrera = carreras.obtenerDef(carreraNombre);
    Materia materia = carrera.obtenerMateria(materiaNombre);
    
    estudiante.incrementarMateriasInscriptas();
    materia.agregarAlumno(estudianteLU);
}

    /*
    * Buscar al estudiante en el trie de estudiantes es complejidad O(1), ya que la longitud de las libretas es acotada 
    * Buscar Carrera en el trie trae complejidad O(|c|)
     * Buscar Materia en el trie de la carrera trae complejidad O(|m|)
     * Se incrementa el contador de materias inscriptas del estudiante y se lo agrega a la lista de la materia, complejidad O(1)
     * Complejidad esperada: O(|c| + |m|)
     */

     public void agregarDocente(CargoDocente cargo, String carreraNombre, String materiaNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        Materia materia = carrera.obtenerMateria(materiaNombre);
        
        materia.agregarDocente(cargo.toString());
    }
/*
 * Se busca la carrera en el trie de carreras, complejidad O(|c|)
 * Se busca la  materia en el trie de la carrera, complejidad O(|m|)
 * Se agrega el docente a la materia, complejidad O(1)
 * Complejidad esperada: O(|c| + |m|)
 * 
 */
public int[] plantelDocente(String materiaNombre, String carreraNombre) {
    Carrera carrera = carreras.obtenerDef(carreraNombre);
    Materia materia = carrera.obtenerMateria(materiaNombre);
    
    return materia.obtenerDocentes();
}
/*
 * Se busca la carrera en el trie de carreras, complejidad O(|c|)
 * Se busca la materia en el trie de la carrera, complejidad O(|m|)
 * Se obtiene el arreglo de docentes, complejidad O(1)
 * Complejjidad esperada: O(|c|+ |m|)
 */
public void cerrarMateria(String materiaNombre, String carreraNombre) {
    Carrera carrera = carreras.obtenerDef(carreraNombre);
    Materia materia = carrera.obtenerMateria(materiaNombre);
    
    carrera.eliminarMateria(materiaNombre);
    List<String> nombresEquivalentes = materia.obtenerNombresEquivalentes();
    List<String> todasLasCarreras = carreras.obtenerClaves();

    for (String otraCarreraNombre : todasLasCarreras) {
        Carrera otraCarrera = carreras.obtenerDef(otraCarreraNombre);
        for (String nombreEquivalente : nombresEquivalentes) {
            otraCarrera.eliminarMateria(nombreEquivalente);
        }
    }

    List<String> estudiantesInscritos = materia.obtenerListaAlumnos();
    for (String estudianteLU : estudiantesInscritos) {
        Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
        estudiante.decrementarMateriasInscriptas();
    }
}

    /*
     * Obtener carrera del trie carreras trae complejidad O(|c|)
     * Se busca la materia en el trie de la carrera, complejidad O(|m|)
     * Se elimina la materia de la carrera y sus equivalentes en otras carreras, complejidad O(∑ (n∈Nm) ∣n∣).
     * Se decrementa la cantidad de inscripciones de los estudiantes de la materia, complejidad O(Em)
     * Complejidad esperada O(|c|+ |m| + (∑ (n∈Nm) ∣n∣) + Em )
     */

     public int inscriptos(String materiaNombre, String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        Materia materia = carrera.obtenerMateria(materiaNombre);
        
        return materia.obtenerCantidadAlumnos();
    }
/* 
 * Se busca la carrera en el trie carreras, complejidad O(|c|)
 * Se busca la materia en el trie de la carrera, complejidad O(|m|)
 * Se obtiene la cantidad de inscriptos, complejidad O(1)
 * Complejidad esperada: O(|c| + |m|)
 */

 public boolean excedeCupo(String materiaNombre, String carreraNombre) {
    Carrera carrera = carreras.obtenerDef(carreraNombre);
    Materia materia = carrera.obtenerMateria(materiaNombre);
    
    return materia.excedeCupo();
}
/* 
 * Se busca la carrera en el trie de carreras, complejidad O(|c|)
 * Se busca la materia en el trie de la carrera, complejidad O(|m|)
 * Se compara la cantidad de inscriptos con el cupo de la materia, complejidad O(1)
 * Complejidad esperada: O(|c|+|m|)
 */

 public String[] carreras() {
    List<String> listaCarreras = carreras.obtenerClaves();
    return listaCarreras.toArray(new String[0]);
}
    /*
     * Obtener todas las claves del trie carreras tiene complejidad O(∑ c∈C ∣c∣),
     * Complejidad esperada: O(∑ c∈C ∣c∣)
     */

     public String[] materias(String carreraNombre) {
        Carrera carrera = carreras.obtenerDef(carreraNombre);
        List<String> listaMaterias = carrera.listarMaterias();
        return listaMaterias.toArray(new String[0]);
    }
    /*
     * Obtener carrera del trie carreras trae complejidad O(|c|)
     * Obtener todas las claves del trie de la carrera tiene complejidad O(∑ mc∈Mc ∣mc∣)
     * Complejidad esperada:  O(∑ mc∈Mc ∣mc∣)
     */

     public int materiasInscriptas(String estudianteLU) {
        Estudiante estudiante = estudiantes.obtenerDef(estudianteLU);
        return estudiante.getMateriasInscriptas();
    }
}

/*
 * Se busca el estudiante en el trie de estudiantess, complejidad O(1)
 * Se obtiene la cantidad de inscripciones del estudiante, complejidad O(1)
 * Complejidad esperada: O(1)
 */


/* Invrep CLASE sistema siu
 - El trie carreras debe contener nombres únicos de carreras y cada valor debe ser una instancia válida de Carrera.
 - El trie estudiantes debe contener libretas universitarias únicas 
 -


 InvReps de los Métodos 
 
Método inscribir
El estudiante identificado por estudianteLU debe existir en el Trie de estudiantes.
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera en el Trie.
El número de materias inscriptas del estudiante debe incrementarse después de la inscripción.

Método AgregarDocente
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
La cantidad de docentes de cada tipo para esa materia debe incrementarse en función del cargo.

Método PlantelDocente
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser un arreglo que contiene el número de docentes por cada tipo (PROFE, JTP, AY1, AY2).

Método CerrarMateria
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
Al cerrar una materia, todas las materias equivalentes en otras carreras deben ser eliminadas.
El número de materias inscriptas por cada estudiante que estaba inscrito en la materia cerrada debe decrecer.

Método Inscriptos
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser la cantidad de estudiantes inscritos en la materia.

Método Excedecupo
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
La materia identificada por materiaNombre debe estar asociada a la carrera.
El resultado debe ser un booleano que indica si el número de inscriptos excede el cupo permitido.

Método Carreras
El resultado debe ser un arreglo ordenado alfabéticamente que contiene todos los nombres de las carreras almacenadas en el Trie, cada uno único.

Método materias 
La carrera identificada por carreraNombre debe existir en el Trie de carreras.
El resultado debe ser un arreglo ordenado alfabéticamente que contiene todos los nombres de las materias asociadas a la carrera, cada uno único.

Método materiasInscriptas
El estudiante identificado por estudianteLU debe existir en el Trie de estudiantes.
El resultado debe ser el número de materias en las que el estudiante está inscrito.
 */
