package ast;

public abstract class LProcParams_0 {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Si_procparam extends LProcParams_0 {
        public LProcParams params;

        public Si_procparam(LProcParams params) {
            this.params = params;
        }

        

        public String imprime() {
            return params.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class No_procparam extends LProcParams_0 {
        public No_procparam() {}

        

        public String imprime() {
            return "";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}