public enum ClaseLexica {
    IDENT, LIT_ENTERO, LIT_REAL, LIT_BOOL,

    PROGRAM("<program>"), END_PROGRAM("<end_program>"), DECVAR("<decvar>"),

    INT("<int>"), REAL("<real>"), BOOL("<bool>"),

    ASIG(":="), MAS("+"), MENOS("-"), MUL("*"), DIV("/"), MENOS_UNARIO("-"),
    AND("&"), OR("|"), NOT("!"),
    MENOR("<"), MAYOR(">"), MENOR_IGUAL("<="), MAYOR_IGUAL(">="), IGUAL("="), DISTINTO("<>"),

    PAP("("), PCIE(")"), PUNTO_COMA(";"), DOS_PUNTOS(":"), DOBLE_GUION("--"),

    EOF("EOF");

    private String image;

    public String getImage() {
        return image;
    }

    private ClaseLexica() {
        image = toString();
    }

    private ClaseLexica(String image) {
        this.image = image;
    }

}
