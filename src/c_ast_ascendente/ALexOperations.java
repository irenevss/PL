package c_ast_ascendente;

public class ALexOperations {

    private AnalizadorLexicoTiny alex;

    public ALexOperations(AnalizadorLexicoTiny alex) {
        this.alex = alex;
    }

    public UnidadLexica unievaluada(int clase) {
        return new UnidadLexica(alex.fila(), alex.columna(), clase, lexema(clase));
    }

    public UnidadLexica multivaluada(int clase, String lexema) {
        return new UnidadLexica(alex.fila(), alex.columna(), clase, lexema);
    }

    public UnidadLexica unidadEof() {
        return new UnidadLexica(alex.fila(), alex.columna(), ClaseLexica.EOF, "EOF");
    }

    private String lexema(int clase) {
        switch (clase) {
            case ClaseLexica.PROGRAM:
                return "<program>";
            case ClaseLexica.END_PROGRAM:
                return "<end_program>";
            case ClaseLexica.INT:
                return "<int>";
            case ClaseLexica.REAL:
                return "<real>";
            case ClaseLexica.BOOL:
                return "<bool>";
            case ClaseLexica.STRING:
                return "<string>";
            case ClaseLexica.NULL:
                return "<null>";
            case ClaseLexica.TRUE:
                return "<true>";
            case ClaseLexica.FALSE:
                return "<false>";
            case ClaseLexica.DECTYPE:
                return "<dectype>";
            case ClaseLexica.DECVAR:
                return "<decvar>";
            case ClaseLexica.DECPROC:
                return "<decproc>";
            case ClaseLexica.END_PROC:
                return "<end_proc>";
            case ClaseLexica.REF:
                return "<ref>";
            case ClaseLexica.IF:
                return "<if>";
            case ClaseLexica.END_IF:
                return "<end_if>";
            case ClaseLexica.ELSE:
                return "<else>";
            case ClaseLexica.WHILE:
                return "<while>";
            case ClaseLexica.END_WHILE:
                return "<end_while>";
            case ClaseLexica.ARRAY:
                return "<array>";
            case ClaseLexica.OF:
                return "<of>";
            case ClaseLexica.RECORD:
                return "<record>";
            case ClaseLexica.END_RECORD:
                return "<end_record>";
            case ClaseLexica.POINTER:
                return "<pointer>";
            case ClaseLexica.NEW:
                return "<new>";
            case ClaseLexica.DISPOSE:
                return "<dispose>";
            case ClaseLexica.INPUT:
                return "<input>";
            case ClaseLexica.OUTPUT:
                return "<output>";
            case ClaseLexica.BLOCK:
                return "<block>";
            case ClaseLexica.END_BLOCK:
                return "<end_block>";
            case ClaseLexica.MAS:
                return "+";
            case ClaseLexica.MENOS:
                return "-";
            case ClaseLexica.POR:
                return "*";
            case ClaseLexica.DIV:
                return "/";
            case ClaseLexica.MOD:
                return "%";
            case ClaseLexica.MENOR:
                return "<";
            case ClaseLexica.MAYOR:
                return ">";
            case ClaseLexica.MENOR_IGUAL:
                return "<=";
            case ClaseLexica.MAYOR_IGUAL:
                return ">=";
            case ClaseLexica.IGUAL:
                return "=";
            case ClaseLexica.DISTINTO:
                return "<>";
            case ClaseLexica.PAP:
                return "(";
            case ClaseLexica.PCIERRE:
                return ")";
            case ClaseLexica.PUNTO_COMA:
                return ";";
            case ClaseLexica.DOS_PUNTOS:
                return ":";
            case ClaseLexica.ASIGNACION:
                return ":=";
            case ClaseLexica.CAP:
                return "[";
            case ClaseLexica.CCIERRE:
                return "]";
            case ClaseLexica.PUNTO:
                return ".";
            case ClaseLexica.FLECHA:
                return "->";
            case ClaseLexica.AND:
                return "&";
            case ClaseLexica.OR:
                return "|";
            case ClaseLexica.NOT:
                return "!";
            case ClaseLexica.ARROBA:
                return "@";
            case ClaseLexica.COMA:
                return ",";
            case ClaseLexica.SEPARADOR:
                return "--";
            case ClaseLexica.EOF:
                return "EOF";
            default:
                return "";
        }
    }
}
