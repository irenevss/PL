package ast;

public abstract class LExps {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Muchas_exps extends LExps {
        public LExps exps;
        public Exp exp;

        public Muchas_exps(LExps exps, Exp exp) {
            this.exps = exps;
            this.exp = exp;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return exps.imprime() + ",\n" + exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Una_exp extends LExps {
        public Exp exp;

        public Una_exp(Exp exp) {
            this.exp = exp;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return exp.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}