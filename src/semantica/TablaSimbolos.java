package semantica;

import asint.SintaxisAbstractaTiny.Nodo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Deque<Map<String, Nodo>> ambitos = new ArrayDeque<>();

    public void abreAmbito() {
        ambitos.push(new HashMap<>());
    }

    public void cierraAmbito() {
        ambitos.pop();
    }

    public boolean contieneEnActual(String id) {
        return ambitos.peek().containsKey(id);
    }

    public void inserta(String id, Nodo valor) {
        ambitos.peek().put(id, valor);
    }

    public Nodo vinculoDe(String id) {
        for (Map<String, Nodo> ambito : ambitos) {
            Nodo d = ambito.get(id);
            if (d != null) {
                return d;
            }
        }
        return null;
    }
}