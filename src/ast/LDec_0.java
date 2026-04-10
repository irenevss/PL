package ast;

public abstract class LDec_0 {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Si_dec extends LDec_0 {
        public LDec decs;

        public Si_dec(LDec decs) {
            this.decs = decs;
        }

        

        public String imprime() {
            return decs.imprime() + "--\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class No_dec extends LDec_0 {
        public No_dec() {}

        

        public String imprime() {
            return "";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}