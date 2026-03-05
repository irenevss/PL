package asint_cup;

import java_cup.runtime.Symbol;
import java_cup.runtime.Scanner;

public class AnalizadorSintacticoTinyDJ extends AnalizadorSintacticoTiny {
    public AnalizadorSintacticoTinyDJ(Scanner s) {
        super(s);
    }

    @Override
    public void debug_shift(Symbol token) {
        if (token instanceof alex.UnidadLexica) {
            System.out.println(((alex.UnidadLexica) token).lexema());
        } else if (token.sym == ClaseLexica.EOF) {
            System.out.println("EOF");
        } else if (token.value != null) {
            System.out.println(token.value);
        } else {
            System.out.println("TOKEN_" + token.sym);
        }
    }

    @Override
    public void debug_message(String message) {
    }
}
