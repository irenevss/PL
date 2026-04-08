package ast;

public abstract class LDec {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Muchas_decs extends LDec {
        public LDec decs;
        public Dec dec;

        public Muchas_decs(LDec decs, Dec dec) {
            this.decs = decs;
            this.dec = dec;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return decs.imprime() + ";\n" + dec.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Una_dec extends LDec {
        public Dec dec;

        public Una_dec(Dec dec) {
            this.dec = dec;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return dec.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}