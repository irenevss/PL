package ast;

public class CamposRecord {
    public String id;
    public Tipo tipo;
    public int fila, col;

    public CamposRecord(String id, Tipo tipo, int fila, int col) {
        this.id = id;
        this.tipo = tipo;
        this.fila = fila;
        this.col = col;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return id + "$f:" + fila + ",c:" + col + "$\n:\n" + tipo.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}