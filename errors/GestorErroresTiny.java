package errors;

import java.util.Set;
import alex.ClaseLexica;

public class GestorErroresTiny {

    public static class ErrorLexico extends RuntimeException {
        public ErrorLexico(String msg) { super(msg); }
    }

    public static class ErrorSintactico extends RuntimeException {
        public ErrorSintactico(String msg) { super(msg); }
    }

    public void errorLexico(int fila, int col, String detalle) {
        throw new ErrorLexico("ERROR_LEXICO (" + fila + "," + col + "): " + detalle);
    }

    public void errorSintactico(int fila, int col, ClaseLexica encontrada, Set<ClaseLexica> esperados) {
        throw new ErrorSintactico(
                "ERROR_SINTACTICO (" + fila + "," + col + "): encontrado " + encontrada +
                ", se esperaba " + esperados
        );
    }
    public void errorFatal(Exception e) {
        System.out.println(e);
        e.printStackTrace();
        System.exit(1);
    }
}