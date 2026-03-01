import asint.AnalizadorSintacticoTinyDJ;
import asint.ParseException;
import asint.TokenMgrError;

public class DomJudge {
    public static void main(String[] args) {
        try {
            AnalizadorSintacticoTinyDJ parser = new AnalizadorSintacticoTinyDJ(System.in);
            parser.S();
        } catch (TokenMgrError e) {
            System.out.println("ERROR_LEXICO");
        } catch (ParseException e) {
            System.out.println("ERROR_SINTACTICO");
        } catch (Exception e) {
            System.out.println("ERROR_SINTACTICO");
        }
    }
}
