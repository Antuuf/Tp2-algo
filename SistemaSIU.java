package aed;

import java.util.List;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras;
    private Trie<Integer> estudiantes;

    enum CargoDocente {
        AY2, AY1, JTP, PROF
    }

    // constructor con InfoMateria y libretas universitarias
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        this.estudiantes = new Trie<>();
        for (String libreta : libretasUniversitarias) {
            estudiantes.insertar(libreta, 0);
        }

        this.carreras = new Trie<>();
        for (InfoMateria info : infoMaterias) {
            ParCarreraMateria[] parCarreraMaterias = info.getParesCarreraMateria();
            Materia mismaMateria = new Materia(parCarreraMaterias);
            for (ParCarreraMateria par : parCarreraMaterias) {
                String carrera = par.getCarrera();
                String materia = par.getNombreMateria();
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

   
    public void cerrarMateria(String materia, String carrera) {
        Trie<Materia> carrerasTrie = carreras.obtenerDef(carrera);
        if (carrerasTrie != null) {
            Materia mat = carrerasTrie.obtenerDef(materia);
            
            if (mat != null) {
                // Borrar la materia de la carrera actual
                carrerasTrie.borrar(materia);
    
                // Obtener todos los nombres equivalentes de la materia
                String[] nombresEquivalentes = mat.obtenerNombresEquivalentes();
                
                // Recorrer todas las carreras y borrar las materias equivalentes
                List<String> todasLasCarreras = carreras.obtenerClaves();
                for (String otraCarrera : todasLasCarreras) {
                    if (!otraCarrera.equals(carrera)) {
                        Trie<Materia> otraCarreraTrie = carreras.obtenerDef(otraCarrera);
                        if (otraCarreraTrie != null) {
                            for (String nombreEquivalente : nombresEquivalentes) {
                                if (otraCarreraTrie.pertenece(nombreEquivalente)) {
                                    otraCarreraTrie.borrar(nombreEquivalente);
                                }
                            }
                        }
                    }
                }
    
                // Vaciar el plantel docente
                int[] docentes = mat.obtenerDocentes();
                for (int i = 0; i < docentes.length; i++) {
                    docentes[i] = 0;
                }
    
                // Decrementar inscripciones en 1 para cada estudiante inscrito en la materia
                ListaEnlazada<String> estudiantesInscritos = mat.obtenerListaAlumnos();
                Iterador<String> iterador = estudiantesInscritos.iterador();
                while (iterador.haySiguiente()) {
                    String estudiante = iterador.siguiente();
                    if (estudiantes.pertenece(estudiante)) {
                        int inscripciones = estudiantes.obtenerDef(estudiante);
                        estudiantes.insertar(estudiante, inscripciones - 1);
                    }
                }
            }
        }
    }
    
                    
                    



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

    public String[] carreras() {
        List<String> listaCarreras = carreras.obtenerClaves(); 
        return listaCarreras.toArray(new String[0]);
    }

    public String[] materias(String carrera) {
        Trie<Materia> car = carreras.obtenerDef(carrera);
        if (car != null) {
            List<String> listaMaterias = car.obtenerClaves();
            return listaMaterias.toArray(new String[0]);
        }
        return new String[0];
    }

    public int materiasInscriptas(String estudiante) {
        if (estudiantes.pertenece(estudiante)) {
            return estudiantes.obtenerDef(estudiante);
        }
        return 0;
    }
}
