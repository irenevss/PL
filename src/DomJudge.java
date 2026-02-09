import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class DomJudge {
    private static void imprime(UnidadLexica unidad) {
        switch (unidad.clase()) {
            case IDENT:
            case LIT_ENTERO:
            case LIT_REAL:
            case LIT_BOOL:
                System.out.println(unidad.lexema());
                break;
            default:
                System.out.println(unidad.clase().getImage());
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Reader input = new InputStreamReader(System.in);
        AnalizadorLexicoTiny al = new AnalizadorLexicoTiny(input);
        UnidadLexica unidad = null;
        boolean error;
        do {
            error = false;
            try {
                unidad = al.sigToken();
                imprime(unidad);
            } catch (AnalizadorLexicoTiny.ECaracterInesperado e) {
                System.out.println("ERROR");
                error = true;
            }
        } while (error || unidad.clase() != ClaseLexica.EOF);
    }

}
