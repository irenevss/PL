import alex.AnalizadorLexicoTiny;
import asint_cup.AnalizadorSintacticoTinyDJ;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import ast.*;
import java.io.InputStreamReader;
import java.io.Reader;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
        AnalizadorSintacticoTinyDJ asint = new AnalizadorSintacticoTinyDJ(alex);
        try {
            char mode = (char) input.read();
            if (mode == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                Prog ast = (Prog) asint.parse().value;
                System.out.println("IMPRESION RECURSIVA");
                System.out.println(ast.imprime());
                System.out.println("IMPRESION INTERPRETE");
                // Implement interpreter
                System.out.println("IMPRESION VISITANTE");
                // Implement visitor
            } else if (mode == 'd') {
                System.out.println("CONSTRUCCION AST DESCENDENTE");
                // For descending parser, need to implement
            }
        } catch (ErrorLexico e) {
            System.out.println("ERROR_LEXICO");
        } catch (ErrorSintactico e) {
            System.out.println("ERROR_SINTACTICO");
        }
    }
}
