import java.io.InputStreamReader;
import java.io.Reader;
import alex.AnalizadorLexicoTiny;
import asint.ConstructorASTsTinyDJ;
import asint.SintaxisAbstractaTiny;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import asint.ParseException;
import asint.TokenMgrError;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        int selector = input.read();

        try {
            if (selector == 'd') {
                System.out.println("CONSTRUCCION AST DESCENDENTE");
                ConstructorASTsTinyDJ parser = new ConstructorASTsTinyDJ(input);
                parser.enable_tracing();
                SintaxisAbstractaTiny.Prog astJJ = parser.analiza();
                if (astJJ != null) {
                    System.out.println("IMPRESION RECURSIVA");
                    System.out.print(astJJ.imprime());
                    System.out.println("IMPRESION INTERPRETE");
                    System.out.print(astJJ.imprime());
                    System.out.println("IMPRESION VISITANTE");
                    System.out.print(astJJ.imprime());
                }

            } else if (selector == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
                asint_cup.AnalizadorSintacticoTinyDJ asintCup = new asint_cup.AnalizadorSintacticoTinyDJ(alex);
                ast.Prog astProg = (ast.Prog) asintCup.debug_parse().value;
                if (astProg != null) {
                    System.out.println("IMPRESION RECURSIVA");
                    System.out.print(astProg.imprime());
                    System.out.println("IMPRESION INTERPRETE");
                    System.out.print(astProg.imprime());
                    System.out.println("IMPRESION VISITANTE");
                    System.out.print(astProg.imprime());
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
