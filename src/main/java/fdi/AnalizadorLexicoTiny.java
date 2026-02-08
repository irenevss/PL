package fdi;

import java.io.IOException;
import java.io.Reader;

public class AnalizadorLexicoTiny {

    private Reader input; // Flujo de entrada
    private StringBuffer lex; // Lexema del componente que se está reconociendo
    private int sigCar; // Siguiente carácter a procesar
    private int filaInicio; // Fila de inicio del componente léxico
    private int columnaInicio; // Columna de inicio del componente léxico
    private int filaActual; // Fila en el punto de lectura actual
    private int columnaActual; // Columna en el punto de lectura actual
    private Estado estado; // Estado del autómata

    private static String NL = System.getProperty("line.separator");

    public AnalizadorLexicoTiny(Reader input) throws IOException{
        this.input = input;
        lex = new StringBuffer();
        sigCar = input.read();
        filaActual=1;
        columnaActual=1;
    }

    public UnidadLexica sigToken() throws IOException {
        estado = Estado.INICIO;
        filaInicio = filaActual;
        columnaInicio = columnaActual;
        lex.delete(0,lex.length());
        while(true) {
            switch (estado) {
                case INICIO:
                    if (hayIgnorable()) transitaIgnorando(Estado.INICIO);
                    else if (hayLetra()) transita(Estado.REC_ID);
                    else if (hayDigitoPos()) transita(Estado.REC_ENT);
                    else if (hayCero()) transita(Estado.REC_0);
                    else if (hayMas()) transita(Estado.REC_MAS);
                    else if (hayMenos()) transita(Estado.REC_MENOS);
                    else if (hayMult()) transita(Estado.REC_MUL);
                    else if (hayDiv()) transita(Estado.REC_DIV);
                    else if (hayIgual()) transita(Estado.REC_IGUAL);
                    else if (hayAnd()) transita(Estado.REC_AND);
                    else if (hayNot()) transita(Estado.REC_NOT);
                    else if (hayOr()) transita(Estado.REC_OR);
                    else if (hayMenor()) transita(Estado.REC_MENOR);
                    else if (hayMayor()) transita(Estado.REC_MAYOR);
                    else if (hayPuntoComa()) transita(Estado.REC_PUNTOCOMA);
                    else if (hayDosPuntos()) transita(Estado.REC_DOSPUNTOS);
                    else if (hayPap()) transita(Estado.REC_PAP);
                    else if (hayPcie()) transita(Estado.REC_PCIE);
                    else if (hayEOF()) unidadEof();
                    else error();
                    break;

                case REC_MUL: return unidadMul();
                case REC_DIV: return unidadDiv();
                case REC_IGUAL: return unidadIgual();
                case REC_AND: return unidadAnd();
                case REC_OR:  return unidadOr();
                case REC_NOT: return unidadNot();

                case REC_MENOR:
                    if (hayIgual()) {
                        transita(Estado.REC_MENOR_IGUAL);
                        return unidadMenorIgual();
                    }
                    if (hayMayor()) {
                        transita(Estado.REC_DISTINTO);
                        return unidadDistinto();
                    }
                    return unidadMenor();

                case REC_MAYOR:
                    if (hayIgual()) {
                        transita(Estado.REC_MAYOR_IGUAL);
                        return unidadMayorIgual();
                    }
                    return unidadMayor();

                case REC_PUNTOCOMA: return unidadPuntoComa();

                case REC_DOSPUNTOS:
                    if (hayIgual()) {
                        transita(Estado.REC_ASIG);
                        return unidadAsig();
                    }
                    return unidadDosPuntos();

                case REC_PAP: return unidadPap();
                case REC_PCIE: return unidadPcie();

                case REC_MAS:
                    if(hayDigitoPos()) {
                        transita(Estado.REC_ENT);
                        break;
                    }
                    else if (hayCero()) {
                        transita(Estado.REC_0);
                        break;
                    }
                    return unidadMas();

                case REC_ENT:
                    if (hayDigito()) transita(Estado.REC_ENT);
                    else if (hayPunto()) transita(Estado.REC_IDEC);
                    else if (hayExponente()) transita(Estado.REC_EXP);
                    else return unidadEnt();
                    break;

                case REC_MENOS:
                    if(hayDigitoPos()) {
                        transita(Estado.REC_ENT);
                        break;
                    }
                    else if (hayCero()) {
                        transita(Estado.REC_0);
                        break;
                    }
                    else if (hayMenos()) {
                        transitaIgnorando(Estado.REC_DOBLEGUION);
                        break;
                    }
                    return unidadMenos();

                case REC_DOBLEGUION:
                    if (hayNL() || hayEOF()) transitaIgnorando(Estado.INICIO);
                    else transitaIgnorando(Estado.REC_DOBLEGUION);
                    break;

                case REC_IDEC:
                    if (hayDigito()) transita(Estado.REC_DEC);
                    else error();
                    break;

                case REC_0DEC:
                    if (hayCero()) transita(Estado.REC_0DEC);
                    else if (hayDigitoPos()) transita(Estado.REC_DEC);
                    else error();
                    break;

                case REC_DEC:
                    if (hayDigitoPos()) transita(Estado.REC_DEC);
                    else if (hayCero()) transita(Estado.REC_0DEC);
                    else if (hayExponente()) transita(Estado.REC_EXP);
                    else return unidadReal();
                    break;

                case REC_0:
                    if (hayPunto()) {
                        transita(Estado.REC_IDEC);
                        break;
                    }
                    return unidadEnt();

                case REC_EXP:
                    if (hayMas() || hayMenos()) transita(Estado.REC_EXP_SIGNO);
                    else if (hayDigitoPos()) transita(Estado.REC_EXP_ENT);
                    else if (hayCero()) transita(Estado.REC_EXP0);
                    else return unidadReal();
                    break;

                case REC_EXP_SIGNO:
                    if (hayDigitoPos()) transita(Estado.REC_EXP_ENT);
                    else error();
                    break;

                case REC_EXP_ENT:
                    if (hayDigito()) transita(Estado.REC_EXP_ENT);
                    else return unidadReal();
                    break;

                case REC_ID:
                    if (hayLetra() || hayDigito()) transita(Estado.REC_ID);
                    else return unidadId();
                    break;

                default:
                    error();
                    break;
            }
        }
    }

    private boolean hayAnd() { return sigCar == '&'; }
    private boolean hayNot() { return sigCar == '!'; }
    private boolean hayOr() { return sigCar == '|'; }
    private boolean hayEOF() { return sigCar == -1; }
    private boolean hayMayor() { return sigCar == '>'; }
    private boolean hayMenor() { return sigCar == '<'; }
    private boolean hayIgual() { return sigCar == '='; }
    private boolean hayDiv() { return sigCar == '/'; }
    private boolean hayMult() { return sigCar == '*'; }
    private boolean hayLetra() {return sigCar >= 'a' && sigCar <= 'z' || sigCar >= 'A' && sigCar <= 'Z';}
    private boolean hayDigito() { return sigCar >= '0' && sigCar <= '9'; }
    private boolean hayDigitoPos() { return sigCar >= '1' && sigCar <= '9'; }
    private boolean hayCero() { return sigCar == '0'; }
    private boolean hayPunto() { return sigCar == '.'; }
    private boolean hayExponente() { return sigCar == 'e' || sigCar == 'E'; }
    private boolean hayMas() { return sigCar == '+'; }
    private boolean hayMenos() { return sigCar == '-'; }
    private boolean hayPap() { return sigCar == '('; }
    private boolean hayPcie() { return sigCar == ')'; }
    private boolean hayPuntoComa() { return sigCar == ';'; }
    private boolean hayDosPuntos() { return sigCar == ':'; }
    private boolean hayIgnorable() {
        return sigCar == ' '  || sigCar == '\t' || sigCar == '\n' || sigCar == '\r' || sigCar == '\b';
    }
    private boolean hayNL() { return sigCar == '\n'; }

    private UnidadLexica unidadId() {
        switch(lex.toString().toLowerCase()) {
            case "program":     return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.PROGRAM);
            case "endprogram":  return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.END_PROGRAM);
            case "decvar":      return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.DECVAR);
            case "int":         return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.INT);
            case "real":        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.REAL);
            case "bool":        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.BOOL);
            case "true":        return new UnidadLexicaMultivaluada(filaInicio, columnaInicio, ClaseLexica.LIT_BOOL, "true");
            case "false":       return new UnidadLexicaMultivaluada(filaInicio, columnaInicio, ClaseLexica.LIT_BOOL, "false");
            default:
                return new UnidadLexicaMultivaluada(filaInicio, columnaInicio, ClaseLexica.IDENT, lex.toString());
        }
    }
    private UnidadLexica unidadEof(){
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.EOF);
    }
    private UnidadLexica unidadMul() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.MUL);
    }
    private UnidadLexica unidadDiv() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.DIV);
    }
    private UnidadLexica unidadIgual() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.IGUAL);
    }
    private UnidadLexica unidadAnd() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.AND);
    }
    private UnidadLexica unidadOr() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.OR);
    }
    private UnidadLexica unidadNot() {
        return new UnidadLexicaUnivaluada(filaInicio, columnaInicio, ClaseLexica.NOT);
    }
    private UnidadLexica unidadMenor() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MENOR);
    }
    private UnidadLexica unidadMenorIgual() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MENOR_IGUAL);
    }
    private UnidadLexica unidadDistinto() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.DISTINTO);
    }
    private UnidadLexica unidadMayor() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MAYOR);
    }
    private UnidadLexica unidadMayorIgual() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MAYOR_IGUAL);
    }
    private UnidadLexica unidadPuntoComa() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.PUNTO_COMA);
    }
    private UnidadLexica unidadDosPuntos() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.DOS_PUNTOS);
    }
    private UnidadLexica unidadAsig() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.ASIG);
    }
    private UnidadLexica unidadPap() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.PAP);
    }
    private UnidadLexica unidadPcie() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.PCIE);
    }
    private UnidadLexica unidadEnt() {
        return new UnidadLexicaMultivaluada(filaInicio,columnaInicio,ClaseLexica.LIT_ENTERO,lex.toString());
    }
    private UnidadLexica unidadMas() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MAS);
    }
    private UnidadLexica unidadMenos() {
        return new UnidadLexicaUnivaluada(filaInicio,columnaInicio,ClaseLexica.MENOS);
    }
    private UnidadLexica unidadReal() {
        return new UnidadLexicaMultivaluada(filaInicio, columnaInicio, ClaseLexica.LIT_REAL, lex.toString());
    }

    private void transita(Estado sig) throws IOException {
        lex.append((char)sigCar);
        sigCar();
        estado = sig;
    }

    private void transitaIgnorando(Estado sig) throws IOException {
        sigCar();
        filaInicio = filaActual;
        columnaInicio = columnaActual;
        estado = sig;
    }

    private void sigCar() throws IOException {
        sigCar = input.read();
        if (sigCar == NL.charAt(0)) saltaFinDeLinea();
        if (sigCar == '\n') {
            filaActual++;
            columnaActual=0;
        }
        else {
            columnaActual++;
        }
    }

    private void saltaFinDeLinea() throws IOException {
        for (int i=1; i < NL.length(); i++) {
            sigCar = input.read();
            if (sigCar != NL.charAt(i)) error();
        }
        sigCar = '\n';
    }

    private void error() {
        System.err.println("("+filaActual+','+columnaActual+"):Caracter inexperado");
        System.exit(1);
    }
}