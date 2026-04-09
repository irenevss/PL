package ast;

public class ImpresionInterprete {
    public String imprime(Prog p) {
        return p != null ? p.imprime() : "";
    }
}