package asint;

import alex.UnidadLexica;

import java.io.IOException;
import java.io.Reader;

public class AnalizadorSintacticoTinyDJ extends AnalizadorSintacticoTiny {

    public AnalizadorSintacticoTinyDJ(Reader input) throws IOException {
        super(input);
    }

    @Override
    protected void traza_emparejamiento(UnidadLexica unidad) {
        switch (unidad.clase()) {
            case IDENT:
            case LIT_ENTERO:
            case LIT_REAL:
            case LIT_BOOL:
                System.out.println(unidad.lexema());
                break;
            default:
                System.out.println(unidad.clase().getImage());
        }
    }
}