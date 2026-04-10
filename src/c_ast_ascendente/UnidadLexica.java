package c_ast_ascendente;

import java_cup.runtime.Symbol;

public class UnidadLexica extends Symbol {
  private int fila;
  private int columna;
  private String lexema;

  public UnidadLexica(int fila, int columna, int clase, String lexema) {
    super(clase);
    this.fila = fila;
    this.columna = columna;
    this.lexema = lexema;
    this.value = this;
  }

  public int clase() {
    return sym;
  }

  public String lexema() {
    return lexema;
  }

  public int fila() {
    return fila;
  }

  public int columna() {
    return columna;
  }
}
