package aed;

import java.util.List;

public class Carrera {
    private String nombre;
    private Trie<Materia> materias;

    public Carrera(String nombre) {
        this.nombre = nombre;
        this.materias = new Trie<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Trie<Materia> getMaterias() {
        return materias;
    }

    public void agregarMateria(String nombre, Materia materia) {
        materias.insertar(nombre, materia);
    }

    public Materia obtenerMateria(String nombre) {
        return materias.obtenerDef(nombre);
    }

    public void eliminarMateria(String nombre) {
        materias.borrar(nombre);
    }

    public List<String> listarMaterias() {
        return materias.obtenerClaves();
    }
}

/*Invariante de representación
 -El nombre de la carrera debe ser válido (no nulo ni vacío)
 -El trie materias debe contener materias validas. Cada materia dentro del trie es una instancia valida de la clase Materia 
 */
