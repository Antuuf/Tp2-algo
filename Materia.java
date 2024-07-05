package aed;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Materia {
    private int[] docentes;
    private ListaEnlazada<String> alumnos;
    private ParCarreraMateria[] otrosNombres; //(nombredeltrie,nombremateria) //lista de los parCarrera
    private ArrayList<Trie<Materia>> referencias; //guarda la referencia del trie para poder llamarlo despues y que no se vaya la complejidad

    public Materia(ParCarreraMateria[] info){
        this.docentes= new int[4];
        this.alumnos= new ListaEnlazada<>();
        this.otrosNombres= info;
        this.referencias= new ArrayList<Trie<Materia>>();
    }

    public void agregarReferencia(Trie<Materia> referencia){ 
        this.referencias.add(referencia);
    }

    public ArrayList<Trie<Materia>> darReferencia(){
        return referencias;
    }
    
    public void agregarAlumno(String alumno){
        this.alumnos.agregarAtras(alumno);
    }

    public void agregarDocente(String cargo){
        if (cargo.equals("PROF")){
            this.docentes[0]++;
        }
        if (cargo.equals("JTP")){
            this.docentes[1]++;
        }
        if (cargo.equals("AY1")){
            this.docentes[2]++;
        }
        if (cargo.equals("AY2")){
            this.docentes[3]++;
        }
    }

    public int[] obtenerDocentes(){
        return this.docentes;
    }

    public ListaEnlazada<String> obtenerListaAlumnos(){
        return alumnos;
    }

    public ParCarreraMateria[] obtenerNombres(){
        return otrosNombres;
    }
   
    public int obtenerCantidadAlumnos(){
        return this.alumnos.longitud();
    }

    public boolean excedeCupo() {
        int cant = obtenerCantidadAlumnos();
        return (docentes[0] * 250 < cant) || (docentes[1] * 100 < cant) || (docentes[2] * 20 < cant) || (docentes[3] * 30 < cant);
    }
   

 // Método para obtener todas las materias equivalentes por nombre
 public String[] obtenerNombresEquivalentes() {
    String[] nombres = new String[otrosNombres.length];
    for (int i = 0; i < otrosNombres.length; i++) {
        nombres[i] = otrosNombres[i].getNombreMateria();
    }
    return nombres;
}
}