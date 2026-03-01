import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import asint.AnalizadorSintacticoTinyDJ;
import errors.GestorErroresTiny;

public class DomJudge {

    public static void main(String[] args) throws IOException {
        Reader input = new InputStreamReader(System.in);
        try{  
            AnalizadorSintacticoTinyDJ asint = new AnalizadorSintacticoTinyDJ(input);
            asint.analiza();
        } catch(GestorErroresTiny.ErrorSintactico e) {
            System.out.println("ERROR_SINTACTICO"); 
        } catch(GestorErroresTiny.ErrorLexico e) {
            System.out.println("ERROR_LEXICO"); 
        }
    }
}