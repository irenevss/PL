package ast;

public class Prog {
    public LDec_0 decs;
    public Instrs_0 instrs;

    public Prog(LDec_0 decs, Instrs_0 instrs) {
        this.decs = decs;
        this.instrs = instrs;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        // Implement recursive printing
        return "<program>\n" + (decs != null ? decs.imprime() : "") + (instrs != null ? instrs.imprime() : "") + "<end_program>\n";
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}