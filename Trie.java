package aed;

import java.util.ArrayList;
import java.util.List;

public class Trie<T> {
    private Nodo raiz;

    
    private class Nodo { // clase Nodo representando cada nodo del trie
        ArrayList<Nodo> siguientes;  
        String valor; 
        T definicion; 

        Nodo() {
            definicion = null; // al empezar  no hay definición
            valor = ""; // al empezar no hay valor, se puede eliminar si no se usa
            siguientes = new ArrayList<>(256);
            // arranca la lista de nodos hijos con tamaño 256 
            for (int i = 0; i < 256; i++) { 
                siguientes.add(null); 
                // arranca cada posición con null 
            }
        }
    }

    public Trie() { // constructor inicializando la raíz 
        raiz = new Nodo(); //O(1)
    } 
    //complejidad O(1)

    
    public void insertar(String clave, T significado) {// metodo para insertar una clave con su significado
        insertarRec(raiz, clave, significado, 0); // llamamos al método recursivo de inserción,complejidad O(|clave|)
    }
    // complejidad O(|clave|)

    
    private void insertarRec(Nodo nodo, String clave, T significado, int indice) { // metodo recursivo para insertar
        if (indice == clave.length()) { 
            nodo.definicion = significado; //O(1)
            // si recorrido toda la clave, asignamos el significado al nodo actual
        } else {
            char charAgregar = clave.charAt(indice); // obtenemos el carácter en la posición actual O(1)
            int charAscii = (int) charAgregar; // convertimos el carácter a su valor O(1)
            if (nodo.siguientes.get(charAscii) == null) { 
                Nodo nuevo = new Nodo(); //O(1)
                // Si no hay un nodo hijo para este carácter, creamos un nuevo nodo
                nodo.siguientes.set(charAscii, nuevo); //O(1)
                // lo asignamos en la posición correspondiente 
            }
            insertarRec(nodo.siguientes.get(charAscii), clave, significado, indice + 1); // hace recursion hasta que llega a la longitud de la clave, O(|clave|)
            // llamamos recursivamente al siguiente nivel 
        }
    }
    //complejidad O(|clave|)

    
    public boolean pertenece(String clave) { // metodo para verificar si una clave pertenece al trie
        if (raiz == null) { // si la raíz es null, el trie está vacío, O(1)
            return false;
        }
        Nodo actual = raiz; // empieza desde la raíz, O(1)
        for (int i = 0; i < clave.length(); i++) { //como mucho se ejecuta la longitud de la clave entonces O(|clave|)
            // recorre cada carácter de la clave
            char charAgregar = clave.charAt(i); //O(1)
            // obtiene el carácter en la posición actual
            int charAscii = (int) charAgregar; //O(1)
            // cambia el carácter a su valor 
            if (actual.siguientes.get(charAscii) == null) { //O(1)
                // si no hay un nodo hijo para este carácter, la clave no pertenece al trie
                return false; 
            }
            actual = actual.siguientes.get(charAscii); 
            // pasa al siguiente nodo
        }
        return actual.definicion != null; 
        // verifica si el nodo final tiene una definición
    }
    //complejidad O(|clave|)

    
    public T obtenerDef(String clave) { // metodo para obtener la definición de una clave
        if (raiz == null) { // si la raíz es null, el trie está vacío, O(1)
            return null;
        }
        Nodo actual = raiz; // empieza desde la raíz, O(1)
        for (int i = 0; i < clave.length(); i++) { //O(|clave|)
            // recorre cada carácter de la clave
            char charAgregar = clave.charAt(i); //O(1)
            // obtiene el carácter en la posición actual
            int charAscii = (int) charAgregar; //O(1)
            // cambia el carácter a su valor 
            if (actual.siguientes.get(charAscii) == null) { //O(1)
                // si no hay un nodo hijo para este carácter, a clave no se encuentra en el trie
                return null; 
            }
            actual = actual.siguientes.get(charAscii); //O(1)
            // pasa al siguiente nodo
        }
        return actual.definicion; 
        // devuelve la definición asociada a la clave
    }
    //complejidad O(|clave|)

    public void borrar(String clave) { 
        borrarRec(raiz, clave, 0); // llama al método recursivo de borrado desde la raíz
    }
    
    private boolean borrarRec(Nodo nodo, String clave, int indice) {
        if (indice == clave.length()) { 
            if (nodo.definicion == null) {
                // Si hemos llego al final de la clave, si no hay definición asociada en este nodo
                return false; 
                // la clave no está presente en el trie
            }
            nodo.definicion = null; 
            // elimina la definición asociada
            return nodoNoTieneHijos(nodo); 
        }
    
        char charAgregar = clave.charAt(indice); // caracter actual de la clave
        int charAscii = (int) charAgregar; // Valor ASCII del carácter
        Nodo siguienteNodo = nodo.siguientes.get(charAscii); // nodo siguiente correspondiente al carácter
        if (siguienteNodo == null) { 
            // si no existe un nodo siguiente para este carácter, la clave no está presente en el Trie
            return false; 
        }
    
        boolean debeEliminarNodo = borrarRec(siguienteNodo, clave, indice + 1); 
        // llama recursivamente para continuar con la búsqueda y borrado
    
        if (debeEliminarNodo) { 
            nodo.siguientes.set(charAscii, null); 
            // si se debe eliminar el nodo actual, // elimina la referencia al nodo hijo
            return nodoNoTieneHijos(nodo); 
            // verifica si el nodo actual ya no tiene más hijos y puede ser eliminado
        }
    
        return false; // indica que no se eliminó ningún nodo
    }
    
    private boolean nodoNoTieneHijos(Nodo nodo) {
        for (Nodo siguiente : nodo.siguientes) { 
            // itera sobre todos los nodos hijos del nodo actual
            if (siguiente != null) { 
                // Si encontramos al menos un nodo hijo, el nodo actual tiene hijos
                return false; 
            }
        }
        return true; // el nodo actual no tiene hijos
    }
    public List<String> obtenerClaves() {
        List<String> claves = new ArrayList<>();
        obtenerClavesRec(raiz, "", claves);
        return claves;
    }

    private void obtenerClavesRec(Nodo nodo, String prefijo, List<String> claves) {
        if (nodo.definicion != null) {
            claves.add(prefijo);
        }
        for (int i = 0; i < 256; i++) {
            Nodo siguiente = nodo.siguientes.get(i);
            if (siguiente != null) {
                char charAgregar = (char) i;
                obtenerClavesRec(siguiente, prefijo + charAgregar, claves);
            }
        }
    }
    
}


// Complejidades:
// insertar, insertarRec, pertenece, obtenerDef, borrar -> Complejidad: O(m)
// donde m es la longitud de la clave.