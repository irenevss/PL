import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import c_ast_ascendente.AnalizadorLexicoTiny;
import c_ast_descendente.ConstructorASTsTiny;
import asint.SintaxisAbstractaTiny;
import errors.GestionErroresTiny.ErrorLexico;
import errors.GestionErroresTiny.ErrorSintactico;
import c_ast_descendente.ParseException;
import c_ast_descendente.TokenMgrError;
import codigo.GeneracionCodigo;
import maquinap.MaquinaP;
import semantica.AsignacionEspacio;
import semantica.ErroresSemanticos;
import semantica.InfoSemantica;
import semantica.Pretipado;
import semantica.Tipado;
import semantica.Vinculacion;

public class DomJudge {

    private static boolean procesaSemantica(SintaxisAbstractaTiny.Prog p, InfoSemantica info) {
        ErroresSemanticos erroresVinculacion = new ErroresSemanticos();
        new Vinculacion(erroresVinculacion, info).procesa(p);
        if (erroresVinculacion.hayErrores()) {
            erroresVinculacion.imprimeErroresDomJudge("Errores_vinculacion");
            return false;
        }

        ErroresSemanticos erroresPretipado = new ErroresSemanticos();
        new Pretipado(erroresPretipado, info).procesa(p);
        if (erroresPretipado.hayErrores()) {
            erroresPretipado.imprimeErroresDomJudge("Errores_pretipado");
            return false;
        }

        ErroresSemanticos erroresTipado = new ErroresSemanticos();
        new Tipado(erroresTipado, info).procesa(p);
        if (erroresTipado.hayErrores()) {
            erroresTipado.imprimeErroresDomJudge("Errores_tipado");
            return false;
        }

        return true;
    }

    private static void procesaCodigo(SintaxisAbstractaTiny.Prog p, InfoSemantica info, Reader input) {
        AsignacionEspacio espacio = new AsignacionEspacio(info);
        espacio.procesa(p);

        int tamDatos = 20000;
        int tamPila = 10000000;
        int tamHeap = 10000000;
        int nDisplays = 10;

        MaquinaP maquina = new MaquinaP(input, espacio.tamGlobal(), tamPila, tamHeap, nDisplays);
        new GeneracionCodigo(maquina, info, espacio).procesa(p);
        maquina.ejecuta();
    }

    public static void main(String[] args) throws Exception {
        System.setOut(new java.io.PrintStream(System.out, true, "ISO-8859-1"));
        char selector = (char) System.in.read();
        Reader input = new BISReader(System.in, "ISO-8859-1");

        try {
            if (selector == 'd') {
                ConstructorASTsTiny parser = new ConstructorASTsTiny(input);
                parser.disable_tracing();
                SintaxisAbstractaTiny.Prog astJJ = parser.analiza();
                if (astJJ != null) {
                    InfoSemantica info = new InfoSemantica();
                    if (procesaSemantica(astJJ, info)) {
                        procesaCodigo(astJJ, info, input);
                    }
                }

            } else if (selector == 'a') {
                AnalizadorLexicoTiny alex = new AnalizadorLexicoTiny(input);
                c_ast_ascendente.AnalizadorSintacticoTiny asintCup = new c_ast_ascendente.AnalizadorSintacticoTiny(
                        alex);
                asint.SintaxisAbstractaTiny.Prog astProg = (asint.SintaxisAbstractaTiny.Prog) asintCup
                        .parse().value;
                if (astProg != null) {
                    InfoSemantica info = new InfoSemantica();
                    if (procesaSemantica(astProg, info)) {
                        procesaCodigo(astProg, info, input);
                    }
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
