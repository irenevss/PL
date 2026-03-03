package asint_cup;

import java_cup.runtime.Symbol;
import java_cup.runtime.Scanner;

public class AnalizadorSintacticoTinyDJ extends AnalizadorSintacticoTiny {
    public AnalizadorSintacticoTinyDJ(Scanner s) {
        super(s);
    }

    @Override
    public void debug_shift(Symbol token) {
        System.out.println(token.value);
    }

    @Override
    public void debug_message(String message) {
    }
}
