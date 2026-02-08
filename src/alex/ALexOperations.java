package alex;

public class ALexOperations {
    public static class ECaracterInesperado extends RuntimeException {
        public ECaracterInesperado(String msg) {
            super(msg);
        }
    }

    private AnalizadorLexicoTiny alex;

    public ALexOperations(AnalizadorLexicoTiny alex) {
        this.alex = alex;
    }

    public UnidadLexica unievaluada(ClaseLexica clase) {
        return new UnidadLexicaUnivaluada(alex.fila(), alex.columna(), clase);
    }

    public UnidadLexica multivaluada(ClaseLexica clase, String lexema) {
        return new UnidadLexicaMultivaluada(alex.fila(), alex.columna(), clase, lexema);
    }

    public void error() {
        throw new ECaracterInesperado(
                "***" + alex.fila() + "," + alex.columna() + ": Caracter inesperado: " + alex.lexema());
    }
}
