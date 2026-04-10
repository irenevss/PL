import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import ast.ImpresionInterprete;
import ast.ImpresionRecursiva;
import ast.ImpresionVisitante;
import alex.AnalizadorLexicoTiny;
import asint.ConstructorASTsTinyDJ;
import asint.SintaxisAbstractaTiny;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import asint.ParseException;
import asint.TokenMgrError;

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

    private static void imprimeProcesamientos(ast.SintaxisAbstractaTiny.Prog astProg) {
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
                    AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(new StringReader(source));
                    asint_cup.AnalizadorSintacticoTinyDJ asintCup = new asint_cup.AnalizadorSintacticoTinyDJ(alex);
                    ast.SintaxisAbstractaTiny.Prog astProg = (ast.SintaxisAbstractaTiny.Prog) asintCup.parse().value;
                    if (astProg != null) {
                        imprimeProcesamientos(astProg);
                    }
                }

            } else if (selector == 'a') {
                System.out.println("CONSTRUCCION AST ASCENDENTE");
                AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(new StringReader(source));
                asint_cup.AnalizadorSintacticoTinyDJ asintCup = new asint_cup.AnalizadorSintacticoTinyDJ(alex);
                ast.SintaxisAbstractaTiny.Prog astProg = (ast.SintaxisAbstractaTiny.Prog) asintCup.debug_parse().value;
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
