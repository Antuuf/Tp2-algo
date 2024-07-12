package aed;

public class Estudiante {
    private String libretaUniversitaria;
    private int materiasInscriptas;

    public Estudiante(String libretaUniversitaria) {
        this.libretaUniversitaria = libretaUniversitaria;
        this.materiasInscriptas = 0;
    }

    public String getLibretaUniversitaria() {
        return libretaUniversitaria;
    }

    public int getMateriasInscriptas() {
        return materiasInscriptas;
    }

    public void incrementarMateriasInscriptas() {
        materiasInscriptas++;
    }

    public void decrementarMateriasInscriptas() {
        materiasInscriptas--;
    }
}


/* Invariante de representación
- La libreta universitaria debe ser válida 
- Materias inscriptas es un número no negativo
 
 */
