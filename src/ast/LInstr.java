package ast;

public abstract class LInstr {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Muchas_instr extends LInstr {
        public LInstr instrs;
        public Instr instr;

        public Muchas_instr(LInstr instrs, Instr instr) {
            this.instrs = instrs;
            this.instr = instr;
        }

        

        public String imprime() {
            return instrs.imprime() + ";\n" + instr.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Una_instr extends LInstr {
        public Instr instr;

        public Una_instr(Instr instr) {
            this.instr = instr;
        }

        

        public String imprime() {
            return instr.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}
