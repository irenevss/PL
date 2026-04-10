package ast;

public abstract class Instrs_0 {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Si_instr extends Instrs_0 {
        public LInstr instrs;

        public Si_instr(LInstr instrs) {
            this.instrs = instrs;
        }

        

        public String imprime() {
            return instrs.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class No_instr extends Instrs_0 {
        public No_instr() {}

        

        public String imprime() {
            return "";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}