import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import alex.ALexOperations.ECaracterInesperado;
import alex.AnalizadorLexicoTiny;
import alex.UnidadLexica;
import alex.UnidadLexicaUnivaluada;

public class DomJudge {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Reader input = new InputStreamReader(System.in);
        AnalizadorLexicoTiny al = new AnalizadorLexicoTiny(input);
        UnidadLexica unidad;
        while (true) {
            try {
                unidad = al.yylex();
                if (unidad == null) {
                    System.out.println("EOF");
                    break;
                }
                if (unidad instanceof UnidadLexicaUnivaluada) {
                    System.out.println(unidad.clase().getLexema());
                } else {
                    System.out.println(unidad.lexema());
                }
            } catch (ECaracterInesperado e) {
                System.out.println("ERROR");
            }
        }
    }
}
