package asint_cup;

import alex.AnalizadorLexicoTiny;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import java.io.InputStreamReader;
import java.io.Reader;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
        AnalizadorSintacticoTinyDJ asint = new AnalizadorSintacticoTinyDJ(alex);
        try {
            asint.debug_parse();
        } catch (ErrorLexico e) {
            System.out.println("ERROR_LEXICO");
        } catch (ErrorSintactico e) {
            System.out.println("ERROR_SINTACTICO");
        }
    }
}
