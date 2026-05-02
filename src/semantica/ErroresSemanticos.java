package semantica;

import asint.SintaxisAbstractaTiny.Nodo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ErroresSemanticos {
    public static class ErrorSemantico {
        public final int fila;
        public final int col;
        public final String mensaje;

        public ErrorSemantico(int fila, int col, String mensaje) {
            this.fila = fila;
            this.col = col;
            this.mensaje = mensaje;
        }
    }

    private final List<ErrorSemantico> errores = new ArrayList<>();

    public void error(Nodo n, String mensaje) {
        if (n == null) {
            errores.add(new ErrorSemantico(-1, -1, mensaje));
            return;
        }
        errores.add(new ErrorSemantico(n.leeFila(), n.leeCol(), mensaje));
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }

    public void imprimeErrores() {
        errores.stream()
                .sorted(Comparator.comparingInt((ErrorSemantico e) -> e.fila)
                        .thenComparingInt(e -> e.col)
                        .thenComparing(e -> e.mensaje))
                .forEach(e -> System.out.println(e.fila + "," + e.col + ":" + e.mensaje));
    }

    public void imprimeErroresDomJudge(String prefijo) {
        Set<String> lineas = new LinkedHashSet<>();
        errores.stream()
                .sorted(Comparator.comparingInt((ErrorSemantico e) -> e.fila)
                        .thenComparingInt(e -> e.col))
                .forEach(e -> lineas.add(prefijo + " fila:" + e.fila + " col:" + e.col));
        for (String linea : lineas) {
            System.out.println(linea);
        }
    }
}
