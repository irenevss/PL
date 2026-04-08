import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;

// JavaCC Imports
import asint.ConstructorASTsTinyDJ;
import asint.SintaxisAbstractaTiny;
import asint.TokenMgrError;
import asint.ParseException;

// JFlex/CUP Imports
import alex.AnalizadorLexicoTiny;
import asint_cup.AnalizadorSintacticoTinyDJ;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import ast.Prog;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        int selector = input.read();
        while (selector == '\n' || selector == '\r') selector = input.read();

        try {
            if (selector == 'd') {
                System.out.println("CONSTRUCCION AST DESCENDENTE");
                ConstructorASTsTinyDJ parser = new ConstructorASTsTinyDJ(input);
                asint.SintaxisAbstractaTiny.Prog astJJ = parser.analiza();
                if (astJJ != null) {
                    System.out.println("IMPRESION RECURSIVA");
                    System.out.println(astJJ.toString());
                    System.out.println("IMPRESION INTERPRETE");
                    System.out.println(astJJ.toString());
                    System.out.println("IMPRESION VISITANTE");
                    System.out.println(astJJ.toString());
                }

            } else if (selector == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
                AnalizadorSintacticoTinyDJ asintCup = new AnalizadorSintacticoTinyDJ(alex);
                ast.Prog astProg = (ast.Prog) asintCup.parse().value;
                if (astProg != null) {
                    System.out.println("IMPRESION RECURSIVA");
                    System.out.println(astProg.imprime());
                    System.out.println("IMPRESION INTERPRETE");
                    System.out.println(astProg.imprime());
                    System.out.println("IMPRESION VISITANTE");
                    System.out.println(astProg.imprime());
                }
            }
        } catch (TokenMgrError e) {
             System.out.println("ERROR_LEXICO");
        } catch (ParseException e) {
             System.out.println("ERROR_SINTACTICO");
        } catch (ErrorLexico e) {
             System.out.println("ERROR_LEXICO");
        } catch (ErrorSintactico e) {
             System.out.println("ERROR_SINTACTICO");
        } catch (Exception e) {
             System.out.println("ERROR_SINTACTICO");
        }
    }
}