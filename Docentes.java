package aed;

public class Docentes {
    private String tipo;
    private int cantidad;

    public Docentes(String tipo) {
        this.tipo = tipo;
        this.cantidad = 0;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void agregarDocente() {
        cantidad++;
    }

    public void quitarDocente() {
        cantidad--;
        if (cantidad < 0) {
            cantidad = 0;
        }
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + ", Cantidad: " + cantidad;
    }
}