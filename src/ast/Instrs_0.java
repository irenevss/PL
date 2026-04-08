package ast;

public abstract class Instrs_0 {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
}

class Si_instr extends Instrs_0 {
    public LInstr instrs;

    public Si_instr(LInstr instrs) {
        this.instrs = instrs;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return instrs.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}

class No_instr extends Instrs_0 {
    public No_instr() {}

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