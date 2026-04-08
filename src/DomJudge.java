import java.io.InputStreamReader;
import java.io.Reader;
import asint.ConstructorASTsTinyDJ;
import asint.SintaxisAbstractaTiny.Prog;
import asint.TokenMgrError;
import asint.ParseException;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        int selector = input.read();
        while (selector == '\n' || selector == '\r') selector = input.read();

        Prog ast = null;

        try {
            if (selector == 'd') {
                System.out.println("CONSTRUCCION AST DESCENDENTE");
                ConstructorASTsTinyDJ parser = new ConstructorASTsTinyDJ(input);
                ast = parser.analiza();

            } else if (selector == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                // Llamar implementación de CUP + JFlex
            }

            if (ast != null) {
                System.out.println("IMPRESION RECURSIVA");
                //ast.impresionRecursiva(); 

                System.out.println("IMPRESION INTERPRETE");
                //ast.impresionInterprete();

                System.out.println("IMPRESION VISITANTE");
                // ast.accept(new ImpresionVisitante());
            }
            
        } catch (TokenMgrError e) {
            System.out.println("ERROR_LEXICO");
        } catch (ParseException e) {
            System.out.println("ERROR_SINTACTICO");
        } catch (Exception e) {
            System.out.println("ERROR_SINTACTICO");
        }
    }
}