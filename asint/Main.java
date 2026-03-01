package asint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import errors.GestorErroresTiny.ErrorLexico;
import errors.GestorErroresTiny.ErrorSintactico;

public class Main {
     public static void main(String[] args) throws FileNotFoundException, Exception {
        try (Reader input = new InputStreamReader(new FileInputStream(args[0]))) {
            AnalizadorSintacticoTiny asint = new AnalizadorSintacticoTiny(input);
            asint.analiza();
            System.out.println("OK");
        } catch (ErrorLexico e) {
            System.out.println(e.getMessage());
        } catch (ErrorSintactico e) {
            System.out.println(e.getMessage());
        }
    }
}
