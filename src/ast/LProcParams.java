package ast;

public abstract class LProcParams {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Muchos_procparam extends LProcParams {
        public LProcParams params;
        public Param param;

        public Muchos_procparam(LProcParams params, Param param) {
            this.params = params;
            this.param = param;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return params.imprime() + ",\n" + param.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Un_procparam extends LProcParams {
        public Param param;

        public Un_procparam(Param param) {
            this.param = param;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return param.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}