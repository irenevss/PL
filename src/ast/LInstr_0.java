package ast;

public abstract class LInstr_0 {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Si_instr_l extends LInstr_0 {
        public LInstr instrs;

        public Si_instr_l(LInstr instrs) {
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

    public static class No_instr_l extends LInstr_0 {
        public No_instr_l() {}

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
}
