package aed;

import java.util.List;

public class Carreras {
    private String nombre;
    private Trie<Materia> materias;

    public Carreras(String nombre) {
        this.nombre = nombre;
        this.materias = new Trie<>();
    }

    public void agregarMateria(String nombreMateria, ParCarreraMateria[] info) {
        Materia nuevaMateria = new Materia(info);
        materias.insertar(nombreMateria, nuevaMateria);
    }

    public Materia obtenerMateria(String nombreMateria) {
        return materias.obtenerDef(nombreMateria);
    }

    public List<String> obtenerTodasLasMaterias() {
        return materias.obtenerClaves();
    }

    public String getNombre() {
        return nombre;
    }
}