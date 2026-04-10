package ast;

public abstract class LExps_0 {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Si_exps extends LExps_0 {
        public LExps exps;

        public Si_exps(LExps exps) {
            this.exps = exps;
        }

        

        public String imprime() {
            return exps.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class No_exps extends LExps_0 {
        public No_exps() {}

        

        public String imprime() {
            return "";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}