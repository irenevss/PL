package alex;

public enum ClaseLexica {
    PROGRAM("<program>"), END_PROGRAM("<end_program>"), INT("<int>"), REAL("<real>"),
    BOOL("<bool>"), STRING("<string>"), NULL("<null>"), TRUE("<true>"),
    FALSE("<false>"), DECTYPE("<dectype>"), DECVAR("<decvar>"), DECPROC("<decproc>"),
    END_PROC("<end_proc>"), REF("<ref>"), IF("<if>"), END_IF("<end_if>"),
    ELSE("<else>"), WHILE("<while>"), END_WHILE("<end_while>"), ARRAY("<array>"),
    OF("<of>"), RECORD("<record>"), END_RECORD("<end_record>"), POINTER("<pointer>"),
    NEW("<new>"), DISPOSE("<dispose>"), INPUT("<input>"), OUTPUT("<output>"),
    BLOCK("<block>"), END_BLOCK("<end_block>"),
    MAS("+"), MENOS("-"), POR("*"), DIV("/"), MOD("%"),
    MENOR("<"), MAYOR(">"), MENOR_IGUAL("<="), MAYOR_IGUAL(">="),
    IGUAL("="), DISTINTO("<>"), PAP("("), PCIERRE(")"), PUNTO_COMA(";"),
    DOS_PUNTOS(":"), ASIGNACION(":="), CAP("["), CCIERRE("]"), PUNTO("."),
    FLECHA("->"), AND("&"), OR("|"), NOT("!"), ARROBA("@"), COMA(","),
    SEPARADOR("--"),
    ID, LITERAL_ENTERO, LITERAL_REAL, CADENA_LITERAL, EOF;

    private String lexema;

    private ClaseLexica() {
        this.lexema = null;
    }

    private ClaseLexica(String lexema) {
        this.lexema = lexema;
    }

    public String getLexema() {
        return lexema;
    }
}
