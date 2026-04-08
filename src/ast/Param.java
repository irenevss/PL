package ast;

public abstract class Param {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
}

class Param_ref extends Param {
    public String id;
    public Tipo tipo;
    public int fila, col;

    public Param_ref(String id, Tipo tipo, int fila, int col) {
        this.id = id;
        this.tipo = tipo;
        this.fila = fila;
        this.col = col;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return "<ref>\n" + id + "$f:" + fila + ",c:" + col + "$\n:\n" + tipo.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}

class Param_val extends Param {
    public String id;
    public Tipo tipo;
    public int fila, col;

    public Param_val(String id, Tipo tipo, int fila, int col) {
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