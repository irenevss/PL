import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import impresion.ImpresionInterprete;
import impresion.ImpresionRecursiva;
import impresion.ImpresionVisitante;
import alex.AnalizadorLexicoTiny;
import c_ast_descendente.ConstructorASTsTinyDJ;
import asint.SintaxisAbstractaTiny;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import c_ast_descendente.ParseException;
import c_ast_descendente.TokenMgrError;

public class DomJudge {
    private static String readRemaining(Reader input) throws Exception {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[2048];
        int n;
        while ((n = input.read(buf)) != -1) {
            sb.append(buf, 0, n);
        }
        return sb.toString();
    }

    private static void imprimeProcesamientos(asint.SintaxisAbstractaTiny.Prog astProg) {
        ImpresionRecursiva rec = new ImpresionRecursiva();
        ImpresionInterprete intr = new ImpresionInterprete();
        ImpresionVisitante vis = new ImpresionVisitante();

        System.out.println("IMPRESION RECURSIVA");
        System.out.print(rec.imprime(astProg));
        System.out.println("IMPRESION INTERPRETE");
        System.out.print(intr.imprime(astProg));
        System.out.println("IMPRESION VISITANTE");
        System.out.print(vis.imprime(astProg));
    }

    public static void main(String[] args) throws Exception {
        Reader input = new InputStreamReader(System.in);
        int selector = input.read();
        String source = readRemaining(input);

        try {
            if (selector == 'd') {
                System.out.println("CONSTRUCCION AST DESCENDENTE");
                ConstructorASTsTinyDJ parser = new ConstructorASTsTinyDJ(new StringReader(source));
                parser.enable_tracing();
                SintaxisAbstractaTiny.Prog astJJ = parser.analiza();
                if (astJJ != null) {
                    imprimeProcesamientos(astJJ);
                }

            } else if (selector == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(new StringReader(source));
                c_ast_ascendente.AnalizadorSintacticoTinyDJ asintCup = new c_ast_ascendente.AnalizadorSintacticoTinyDJ(alex);
                asint.SintaxisAbstractaTiny.Prog astProg = (asint.SintaxisAbstractaTiny.Prog) asintCup.debug_parse().value;
                if (astProg != null) {
                    imprimeProcesamientos(astProg);
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
