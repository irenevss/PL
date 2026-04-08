package asint;

import java.io.InputStream;
import java.io.Reader;

public class ConstructorASTsTinyDJ extends ConstructorASTsTiny {

    public ConstructorASTsTinyDJ(InputStream stream) {
        super(stream);
        disable_tracing();
    }

    public ConstructorASTsTinyDJ(Reader stream) {
        super(stream);
        disable_tracing();
    }

    @Override
    protected void trace_token(Token t, String where) {
        if (t.kind == EOF) {
            System.out.println("EOF");
            return;
        }

        switch (t.kind) {
            case PROGRAM:
                System.out.println("<program>");
                break;
            case END_PROGRAM:
                System.out.println("<end_program>");
                break;
            case INT:
                System.out.println("<int>");
                break;
            case REAL:
                System.out.println("<real>");
                break;
            case BOOL:
                System.out.println("<bool>");
                break;
            case STRING:
                System.out.println("<string>");
                break;
            case NULL:
                System.out.println("<null>");
                break;
            case TRUE:
                System.out.println("<true>");
                break;
            case FALSE:
                System.out.println("<false>");
                break;
            case DECTYPE:
                System.out.println("<dectype>");
                break;
            case DECVAR:
                System.out.println("<decvar>");
                break;
            case DECPROC:
                System.out.println("<decproc>");
                break;
            case END_PROC:
                System.out.println("<end_proc>");
                break;
            case REF:
                System.out.println("<ref>");
                break;
            case IF:
                System.out.println("<if>");
                break;
            case END_IF:
                System.out.println("<end_if>");
                break;
            case ELSE:
                System.out.println("<else>");
                break;
            case WHILE:
                System.out.println("<while>");
                break;
            case END_WHILE:
                System.out.println("<end_while>");
                break;
            case ARRAY:
                System.out.println("<array>");
                break;
            case OF:
                System.out.println("<of>");
                break;
            case RECORD:
                System.out.println("<record>");
                break;
            case END_RECORD:
                System.out.println("<end_record>");
                break;
            case POINTER:
                System.out.println("<pointer>");
                break;
            case NEW:
                System.out.println("<new>");
                break;
            case DISPOSE:
                System.out.println("<dispose>");
                break;
            case INPUT:
                System.out.println("<input>");
                break;
            case OUTPUT:
                System.out.println("<output>");
                break;
            case BLOCK:
                System.out.println("<block>");
                break;
            case END_BLOCK:
                System.out.println("<end_block>");
                break;
            case MAS:
                System.out.println("+");
                break;
            case MENOS:
                System.out.println("-");
                break;
            case POR:
                System.out.println("*");
                break;
            case DIV:
                System.out.println("/");
                break;
            case MOD:
                System.out.println("%");
                break;
            case MENOR:
                System.out.println("<");
                break;
            case MAYOR:
                System.out.println(">");
                break;
            case MENOR_IGUAL:
                System.out.println("<=");
                break;
            case MAYOR_IGUAL:
                System.out.println(">=");
                break;
            case IGUAL:
                System.out.println("=");
                break;
            case DISTINTO:
                System.out.println("<>");
                break;
            case PAP:
                System.out.println("(");
                break;
            case PCIERRE:
                System.out.println(")");
                break;
            case PUNTO_COMA:
                System.out.println(";");
                break;
            case DOS_PUNTOS:
                System.out.println(":");
                break;
            case ASIGNACION:
                System.out.println(":=");
                break;
            case CAP:
                System.out.println("[");
                break;
            case CCIERRE:
                System.out.println("]");
                break;
            case PUNTO:
                System.out.println(".");
                break;
            case FLECHA:
                System.out.println("->");
                break;
            case AND:
                System.out.println("&");
                break;
            case OR:
                System.out.println("|");
                break;
            case NOT:
                System.out.println("!");
                break;
            case ARROBA:
                System.out.println("@");
                break;
            case COMA:
                System.out.println(",");
                break;
            case SEPARADOR:
                System.out.println("--");
                break;
            default:
                System.out.println(t.image);
                break;
        }
    }
}