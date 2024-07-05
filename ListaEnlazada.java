package aed;

import java.util.NoSuchElementException;

public class ListaEnlazada<T> implements Secuencia<T> {
    private class Nodo {
        T elemento;
        Nodo siguiente;
        Nodo anterior;

        public Nodo(T elemento) {
            this.elemento = elemento;
            this.siguiente = null;
            this.anterior = null;
        }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int size;

    

    public ListaEnlazada() {
    
        this.cabeza = null;
        this.cola = null;
        this.size = 0;
    }

    public int longitud() {
        return size;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevoNodo = new Nodo(elem);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            nuevoNodo.siguiente = cabeza;
            cabeza.anterior = nuevoNodo;
            cabeza = nuevoNodo;
        }
        size++;
    }

    public void agregarAtras(T elem) {
        Nodo nuevoNodo = new Nodo(elem);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
        size++;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Ãndice fuera de rango");
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.elemento;
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Ãndice fuera de rango");
        }
        Nodo actual = cabeza;
        if (size == 1) {
            cabeza = null;
            cola = null;
        } else if (indice == 0) {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        } else if (indice == size - 1) {
            cola = cola.anterior;
            cola.siguiente = null;
        } else {
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
            actual.anterior.siguiente = actual.siguiente;
            actual.siguiente.anterior = actual.anterior;
        }
        size--;
    }

    public void modificarPosicion(int indice, T elem) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Ãndice fuera de rango");
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        actual.elemento = elem;
    }

    public ListaEnlazada<T> copiar() {
        ListaEnlazada<T> copia = new ListaEnlazada<>();
        Nodo actual = cabeza;
        while (actual != null) {
            copia.agregarAtras(actual.elemento);
            actual = actual.siguiente;
        }
        return copia;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        Nodo actual = lista.cabeza;
        while (actual != null) {
            this.agregarAtras(actual.elemento);
            actual = actual.siguiente;
            }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Nodo actual = cabeza;
        while (actual != null) {
            sb.append(actual.elemento);
            actual = actual.siguiente;
            if (actual != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private class ListaIterador implements Iterador<T> {
    	private Nodo actual;
        private boolean enUltimo;

        public ListaIterador() {
            this.actual = cabeza;
            this.enUltimo = false;
        }


        public boolean haySiguiente() {
	        return actual != null && !enUltimo;
        }
        
        public boolean hayAnterior() {
	        return actual != null && actual.anterior != null;
        }

        public T siguiente() {
	        if (!haySiguiente()) {
                throw new NoSuchElementException("No hay elemento siguiente");
            }
            T elemento = actual.elemento;
            if (actual.siguiente == null) {
                enUltimo = true;
            } else {
                actual = actual.siguiente;
            }
            return elemento;
        }
        

        public T anterior() {
	        if (!hayAnterior()) {
                throw new NoSuchElementException("No hay elemento anterior");
            }
            if (enUltimo) {
                enUltimo = false;
            } else {
                actual = actual.anterior;
            }
            T elemento = actual.elemento;
            return elemento;
        }
    }

    public Iterador<T> iterador() {
	    return new ListaIterador();
    }

}