package ast;

public abstract class LProcParams_0 {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
}

class Si_procparam extends LProcParams_0 {
    public LProcParams params;

    public Si_procparam(LProcParams params) {
        this.params = params;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return params.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}

class No_procparam extends LProcParams_0 {
    public No_procparam() {}

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