package aed;

import java.util.ArrayList;
import java.util.List;

public class Materia {
    private int[] docentes;
    private List<String> alumnos;
    private List<Trie<Materia>> referencias;
    private List<String> otrosNombres;

    public Materia() {
        this.docentes = new int[4];
        this.alumnos = new ArrayList<>();
        this.referencias = new ArrayList<>();
        this.otrosNombres = new ArrayList<>();
    }

    public void agregarReferencia(Trie<Materia> referencia) {
        this.referencias.add(referencia);
    }

    public List<Trie<Materia>> darReferencia() {
        return referencias;
    }

    public void agregarAlumno(String alumno) {
        this.alumnos.add(alumno);
    }

    public void agregarDocente(String cargo) {
        switch (cargo) {
            case "PROF":
                this.docentes[0]++;
                break;
            case "JTP":
                this.docentes[1]++;
                break;
            case "AY1":
                this.docentes[2]++;
                break;
            case "AY2":
                this.docentes[3]++;
                break;
        }
    }

    public int[] obtenerDocentes() {
        return this.docentes;
    }

    public List<String> obtenerListaAlumnos() {
        return alumnos;
    }

    public int obtenerCantidadAlumnos() {
        return this.alumnos.size();
    }

    public boolean excedeCupo() {
        int cant = obtenerCantidadAlumnos();
        int cupoProf = docentes[0] * 250;
        int cupoJTP = docentes[1] * 100;
        int cupoAY1 = docentes[2] * 20;
        int cupoAY2 = docentes[3] * 30;

        int cupoMinimo = cupoProf;
        if (cupoJTP < cupoMinimo) {
            cupoMinimo = cupoJTP;
        }
        if (cupoAY1 < cupoMinimo) {
            cupoMinimo = cupoAY1;
        }
        if (cupoAY2 < cupoMinimo) {
            cupoMinimo = cupoAY2;
        }

        return cant > cupoMinimo;
    }

    public List<String> obtenerNombresEquivalentes() {
        return otrosNombres;
    }

    public void agregarNombreEquivalente(String nombre) {
        if (!otrosNombres.contains(nombre)) {
            otrosNombres.add(nombre);
        }
    }
}
