package aed;

import java.util.ArrayList;
import java.util.List;

public class Estudiantes {
    private List<ParCarreraMateria> materiasInscriptas;

    public Estudiantes() {
        this.materiasInscriptas = new ArrayList<>();
    }

    public void inscribirMateria(String materia, ParCarreraMateria[] info) {
        for (ParCarreraMateria par : info) {
            if (par.getNombreMateria().equals(materia)) {
                materiasInscriptas.add(par);
                break;
            }
        }
    }

    public List<String> materiasInscriptas() {
        List<String> nombresMaterias = new ArrayList<>();
        for (ParCarreraMateria par : materiasInscriptas) {
            nombresMaterias.add(par.getNombreMateria());
        }
        return nombresMaterias;
    }
}