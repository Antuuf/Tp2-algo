package aed;

import java.util.ArrayList;
import java.util.List;

public class Materia {
    private int[] docentes;
    private List<String> alumnos;
    private List<String> otrosNombres;

    public Materia() {
        this.docentes = new int[4];
        this.alumnos = new ArrayList<>();
        this.otrosNombres = new ArrayList<>();
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


/* Invariante de representación 
 - El arreglo Docentes debe tener 4 elementos, representando las cantidades de PROFE, JTP, AY1 y AY2. Cada valor en el arreglo docentes debe ser no negativo.
 - La lista de alumnos debe contener valores únicos. No hay elementos duplicados.
 - Los nombres en otrosNombres deben corresponder a nombres válidos de materias en el sistema. Cada nombre en otrosNombres debe ser un string no nulo y no vacío. Los nombres en otrosNombres deben corresponder a nombres válidos de materias en el sistema.
 */
