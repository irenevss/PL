package asint;

import java.io.IOException;
import java.io.Reader;
import java.util.EnumSet;
import java.util.Set;

import alex.AnalizadorLexicoTiny;
import alex.ClaseLexica;
import alex.UnidadLexica;
import errors.GestorErroresTiny;

public class AnalizadorSintacticoTiny {
    private UnidadLexica anticipo;
    private AnalizadorLexicoTiny alex;
    private GestorErroresTiny gestor;
    private Set<ClaseLexica> esperados;

    public AnalizadorSintacticoTiny(Reader input) throws IOException {
        gestor = new GestorErroresTiny();
        alex = new AnalizadorLexicoTiny(input, gestor);
        esperados = EnumSet.noneOf(ClaseLexica.class);
        sigToken();
    }

    public void analiza() {
        S();
        empareja(ClaseLexica.EOF);
    }

    private void S() {
        empareja(ClaseLexica.PROGRAM);
        LDec_0();
        Instrs_0();
        empareja(ClaseLexica.END_PROGRAM);
    }

    // LDec_0 → LDec -- | ε
    private void LDec_0() {
        switch (anticipo.clase()) {
            case DECVAR:
                LDec();
                empareja(ClaseLexica.DOBLE_GUION);
                break;
            default:
                // ε
                break;
        }
    }

    // LDec → Dec RLDec
    private void LDec() {
        Dec();
        RLDec();
    }

    // RLDec → ; Dec RLDec | ε
    private void RLDec() {
        switch (anticipo.clase()) {
            case PUNTO_COMA:
                empareja(ClaseLexica.PUNTO_COMA);
                Dec();
                RLDec();
                break;
            default:
                // ε
                break;
        }
    }

    // Dec → decvar identificador : Tipo
    private void Dec() {
        empareja(ClaseLexica.DECVAR);
        empareja(ClaseLexica.IDENT);
        empareja(ClaseLexica.DOS_PUNTOS);
        Tipo();
    }

    // Tipo → int | real | bool
    private void Tipo() {
        switch (anticipo.clase()) {
            case INT:  empareja(ClaseLexica.INT); break;
            case REAL: empareja(ClaseLexica.REAL); break;
            case BOOL: empareja(ClaseLexica.BOOL); break;
            default:
                esperados(ClaseLexica.INT, ClaseLexica.REAL, ClaseLexica.BOOL);
                error();
        }
    }

    // Instrs_0 → LInstr | ε
    private void Instrs_0() {
        switch (anticipo.clase()) {
            case IDENT:
                LInstr();
                break;
            default:
                // ε
                break;
        }
    }

    // LInstr → Instr RLInstr
    private void LInstr() {
        Instr();
        RLInstr();
    }

    // RLInstr → ; Instr RLInstr | ε
    private void RLInstr() {
        switch (anticipo.clase()) {
            case PUNTO_COMA:
                empareja(ClaseLexica.PUNTO_COMA);
                Instr();
                RLInstr();
                break;
            default:
                // ε
                break;
        }
    }

    // Instr → identificador := E0
    private void Instr() {
        empareja(ClaseLexica.IDENT);
        empareja(ClaseLexica.ASIG);
        E0();
    }

    // E0 → E1 RE0
    private void E0() {
        E1();
        RE0();
    }

    // RE0 → Op0 E1 | ε
    private void RE0() {
        switch (anticipo.clase()) {
            case MENOR: case MAYOR: case MENOR_IGUAL:
            case MAYOR_IGUAL: case IGUAL: case DISTINTO:
                Op0();
                E1();
                break;
            default:
                // ε
                break;
        }
    }

    // Op0 → < | > | <= | >= | = | <>
    private void Op0() {
        switch (anticipo.clase()) {
            case MENOR:        empareja(ClaseLexica.MENOR); break;
            case MAYOR:        empareja(ClaseLexica.MAYOR); break;
            case MENOR_IGUAL:  empareja(ClaseLexica.MENOR_IGUAL); break;
            case MAYOR_IGUAL:  empareja(ClaseLexica.MAYOR_IGUAL); break;
            case IGUAL:        empareja(ClaseLexica.IGUAL); break;
            case DISTINTO:     empareja(ClaseLexica.DISTINTO); break;
            default:
                esperados(ClaseLexica.MENOR, ClaseLexica.MAYOR, ClaseLexica.MENOR_IGUAL,
                          ClaseLexica.MAYOR_IGUAL, ClaseLexica.IGUAL, ClaseLexica.DISTINTO);
                error();
        }
    }

    // E1 → E2 RE1 RE1'
    private void E1() {
        E2();
        RE1();
        RE1P();
    }

    // RE1  → | E2 | ε
    private void RE1() {
        switch (anticipo.clase()) {
            case OR:
                empareja(ClaseLexica.OR);
                E2();
                break;
            default:
                // ε
                break;
        }
    }

    // RE1' → Op1 E2 RE1' | ε
    private void RE1P() {
        switch (anticipo.clase()) {
            case MAS: case MENOS:
                Op1();
                E2();
                RE1P();
                break;
            default:
                // ε
                break;
        }
    }

    // Op1 → + | -
    private void Op1() {
        switch (anticipo.clase()) {
            case MAS:   empareja(ClaseLexica.MAS); break;
            case MENOS: empareja(ClaseLexica.MENOS); break;
            default:
                esperados(ClaseLexica.MAS, ClaseLexica.MENOS);
                error();
        }
    }

    // E2 → E3 RE2 RE2'
    private void E2() {
        E3();
        RE2();
        RE2P();
    }

    // RE2  → & E3 | ε
    private void RE2() {
        switch (anticipo.clase()) {
            case AND:
                empareja(ClaseLexica.AND);
                E3();
                break;
            default:
                // ε
                break;
        }
    }

    // RE2' → Op2 E3 RE2' | ε
    private void RE2P() {
        switch (anticipo.clase()) {
            case MUL: case DIV:
                Op2();
                E3();
                RE2P();
                break;
            default:
                // ε
                break;
        }
    }

    // Op1 → * | /
    private void Op2() {
        switch (anticipo.clase()) {
            case MUL: empareja(ClaseLexica.MUL); break;
            case DIV: empareja(ClaseLexica.DIV); break;
            default:
                esperados(ClaseLexica.MUL, ClaseLexica.DIV);
                error();
        }
    }

    // E3 → - E3 | ! E4 | E4
    private void E3() {
        switch (anticipo.clase()) {
            case MENOS:
                empareja(ClaseLexica.MENOS);
                E3();
                break;
            case NOT:
                empareja(ClaseLexica.NOT);
                E4();
                break;
            default:
                E4();
                break;
        }
    }

    // E4 → identificador | litEntero | litReal | litBool | ( E0 )
    private void E4() {
        switch (anticipo.clase()) {
            case IDENT:
                empareja(ClaseLexica.IDENT);
                break;
            case LIT_ENTERO:
                empareja(ClaseLexica.LIT_ENTERO);
                break;
            case LIT_REAL:
                empareja(ClaseLexica.LIT_REAL);
                break;
            case LIT_BOOL:
                empareja(ClaseLexica.LIT_BOOL);
                break;
            case PAP:
                empareja(ClaseLexica.PAP);
                E0();
                empareja(ClaseLexica.PCIE);
                break;
            default:
                esperados(ClaseLexica.IDENT, ClaseLexica.LIT_ENTERO, ClaseLexica.LIT_REAL,
                          ClaseLexica.LIT_BOOL, ClaseLexica.PAP);
                error();
        }
    }
    
    private void esperados(ClaseLexica... esp) {
        for (ClaseLexica c : esp) esperados.add(c);
    }

    private void empareja(ClaseLexica claseEsperada) {
        if (anticipo.clase() == claseEsperada) {
            traza_emparejamiento(anticipo);
            sigToken();
        } else {
            esperados(claseEsperada);
            error();
        }
    }

    private void sigToken() {
        try {
            anticipo = alex.sigToken();
            esperados.clear();
        } catch (IOException e) {
            gestor.errorFatal(e);
        }
    }

    private void error() {
        gestor.errorSintactico(anticipo.fila(), anticipo.columna(), anticipo.clase(), esperados);
    }

    // Para DomJudge se sobreescribe
    protected void traza_emparejamiento(UnidadLexica anticipo) {}
}