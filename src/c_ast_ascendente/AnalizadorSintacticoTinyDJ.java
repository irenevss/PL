package c_ast_ascendente;

import java_cup.runtime.Symbol;
import java_cup.runtime.Scanner;
import alex.UnidadLexica;

public class AnalizadorSintacticoTinyDJ extends AnalizadorSintacticoTiny {
    public AnalizadorSintacticoTinyDJ(Scanner s) {
        super(s);
    }

    @Override
    public void debug_shift(Symbol token) {
        if (token instanceof UnidadLexica) {
            System.out.println(((UnidadLexica) token).lexema());
        } else if (token.sym == ClaseLexica.EOF) {
            System.out.println("EOF");
        }
    }

    @Override
    public void debug_message(String message) {
    }
}
