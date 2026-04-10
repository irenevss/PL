package ast;

public abstract class Instr {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Instr_asig extends Instr {
        public Exp exp1;
        public Exp exp2;

        public Instr_asig(Exp exp1, Exp exp2) {
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

        

        public String imprime() {
            return exp1.imprime() + ":=\n" + exp2.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_if extends Instr {
        public Exp exp;
        public LInstr_0 instrs;

        public Instr_if(Exp exp, LInstr_0 instrs) {
            this.exp = exp;
            this.instrs = instrs;
        }

        

        public String imprime() {
            return "<if>\n" + exp.imprime() + ":\n" + instrs.imprime() + "<end_if>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_ifelse extends Instr {
        public Exp exp;
        public LInstr_0 instrs1;
        public LInstr_0 instrs2;

        public Instr_ifelse(Exp exp, LInstr_0 instrs1, LInstr_0 instrs2) {
            this.exp = exp;
            this.instrs1 = instrs1;
            this.instrs2 = instrs2;
        }

        

        public String imprime() {
            return "<if>\n" + exp.imprime() + ":\n" + instrs1.imprime() + "<else>\n" + instrs2.imprime() + "<end_if>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_while extends Instr {
        public Exp exp;
        public LInstr_0 instrs;

        public Instr_while(Exp exp, LInstr_0 instrs) {
            this.exp = exp;
            this.instrs = instrs;
        }

        

        public String imprime() {
            return "<while>\n" + exp.imprime() + ":\n" + instrs.imprime() + "<end_while>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_lectura extends Instr {
        public Exp exp;

        public Instr_lectura(Exp exp) {
            this.exp = exp;
        }

        

        public String imprime() {
            return "<input>\n" + exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_escritura extends Instr {
        public Exp exp;

        public Instr_escritura(Exp exp) {
            this.exp = exp;
        }

        

        public String imprime() {
            return "<output>\n" + exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_reserva extends Instr {
        public Exp exp;

        public Instr_reserva(Exp exp) {
            this.exp = exp;
        }

        

        public String imprime() {
            return "<new>\n" + exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_liberacion extends Instr {
        public Exp exp;

        public Instr_liberacion(Exp exp) {
            this.exp = exp;
        }

        

        public String imprime() {
            return "<dispose>\n" + exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_compuesta extends Instr {
        public LDec_0 decs;
        public LInstr_0 instrs;

        public Instr_compuesta(LDec_0 decs, LInstr_0 instrs) {
            this.decs = decs;
            this.instrs = instrs;
        }

        

        public String imprime() {
            return "<block>\n" + decs.imprime() + instrs.imprime() + "<end_block>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Instr_invocar extends Instr {
        public String id;
        public LExps_0 exps;
        public int fila, col;

        public Instr_invocar(String id, LExps_0 exps, int fila, int col) {
            this.id = id;
            this.exps = exps;
            this.fila = fila;
            this.col = col;
        }

        

        public String imprime() {
            return "@\n" + id + "$f:" + fila + ",c:" + col + "$\n(\n" + exps.imprime() + ")\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}