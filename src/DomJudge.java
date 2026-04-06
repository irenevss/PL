import alex.AnalizadorLexicoTiny;
import asint.AnalizadorSintacticoTinyDJ;
import asint.ParseException;
import asint.TokenMgrError;
import asint_cup.ASTNode;
import asint_cup.ProcesamientoInterprete;
import asint_cup.ProcesamientoRecursivo;
import asint_cup.ProcesamientoVisitante;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class DomJudge {
    public static void main(String[] args) throws Exception {
        String rawInput = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = rawInput.split("\\R", 2);
        if (parts.length == 0) {
            return;
        }

        String selector = parts[0].trim();
        String source = parts.length > 1 ? parts[1] : "";
        // Maintain original line numbering from judge input where line 1 is selector a/d.
        source = "\n" + source;
        if (selector.isEmpty()) {
            System.out.println("ERROR_SINTACTICO");
            return;
        }

        ASTNode ast;
        try {
            if (selector.charAt(0) == 'a') {
                ast = construyeAscendente(source);
            } else if (selector.charAt(0) == 'd') {
                construyeDescendente(source);
                ast = construyeAstSilencioso(source);
            } else {
                System.out.println("ERROR_SINTACTICO");
                return;
            }

            if (ast == null) {
                return;
            }

            ProcesamientoRecursivo recursivo = new ProcesamientoRecursivo();
            ProcesamientoInterprete interprete = new ProcesamientoInterprete();
            ProcesamientoVisitante visitante = new ProcesamientoVisitante();

            System.out.println("IMPRESION RECURSIVA");
            System.out.print(recursivo.imprime(ast));

            System.out.println("IMPRESION INTERPRETE");
            System.out.print(interprete.imprime(ast));

            System.out.println("IMPRESION VISITANTE");
            System.out.print(visitante.imprime(ast));
        } catch (ErrorLexico e) {
            System.out.println("ERROR_LEXICO");
        } catch (ErrorSintactico e) {
            System.out.println("ERROR_SINTACTICO");
        } catch (TokenMgrError e) {
            System.out.println("ERROR_LEXICO");
        } catch (ParseException e) {
            System.out.println("ERROR_SINTACTICO");
        }
    }

    private static ASTNode construyeAscendente(String source) throws Exception {
        System.out.println("CONSTRUCCION AST ASCENDENTE");
        Reader input = new StringReader(source);
        AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
        asint_cup.AnalizadorSintacticoTinyDJ asint = new asint_cup.AnalizadorSintacticoTinyDJ(alex);
        asint.debug_parse();
        return asint.getAST();
    }

    private static void construyeDescendente(String source) throws ParseException, TokenMgrError {
        System.out.println("CONSTRUCCION AST DESCENDENTE");
        Reader input = new StringReader(source);
        AnalizadorSintacticoTinyDJ asint = new AnalizadorSintacticoTinyDJ(input);
        asint.S();
    }

    private static ASTNode construyeAstSilencioso(String source) throws Exception {
        Reader input = new StringReader(source);
        AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
        asint_cup.AnalizadorSintacticoTiny asint = new asint_cup.AnalizadorSintacticoTiny(alex);
        asint.parse();
        return asint.getAST();
    }
}
