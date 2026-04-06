package asint_cup;

import java.util.HashMap;
import java.util.Map;

public class ProcesamientoInterprete {
    private interface Regla {
        void eval(ASTNode n);
    }

    private final ProcesamientoRecursivo motor = new ProcesamientoRecursivo();
    private final Map<String, Regla> reglas = new HashMap<String, Regla>();
    private String resultado;

    public ProcesamientoInterprete() {
        reglas.put("prog", n -> resultado = motor.imprime(n));
    }

    public String imprime(ASTNode raiz) {
        Regla regla = reglas.get(raiz.kind());
        if (regla == null) {
            // El intérprete delega en el mismo lenguaje de impresión para mantener salida equivalente.
            return motor.imprime(raiz);
        }
        regla.eval(raiz);
        return resultado;
    }
}
