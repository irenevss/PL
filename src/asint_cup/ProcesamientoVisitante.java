package asint_cup;

public class ProcesamientoVisitante {
    public interface Visitante {
        void visita(ASTNode n);
    }

    public interface Visitable {
        void acepta(Visitante v);
    }

    private static class NodoVisitable implements Visitable {
        private final ASTNode nodo;

        NodoVisitable(ASTNode nodo) {
            this.nodo = nodo;
        }

        @Override
        public void acepta(Visitante v) {
            v.visita(nodo);
        }
    }

    private static class VisitanteImpresion implements Visitante {
        private final ProcesamientoRecursivo motor = new ProcesamientoRecursivo();
        private String salida;

        @Override
        public void visita(ASTNode n) {
            salida = motor.imprime(n);
        }

        String salida() {
            return salida;
        }
    }

    public String imprime(ASTNode raiz) {
        Visitable visitable = new NodoVisitable(raiz);
        VisitanteImpresion visitante = new VisitanteImpresion();
        visitable.acepta(visitante);
        return visitante.salida();
    }
}
