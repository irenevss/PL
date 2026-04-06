package ast;

public abstract class LDec_0 {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
}

class Si_dec extends LDec_0 {
    public LDec decs;

    public Si_dec(LDec decs) {
        this.decs = decs;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return decs.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}

class No_dec extends LDec_0 {
    public No_dec() {}

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return "";
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}