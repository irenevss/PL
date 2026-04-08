package ast;

public abstract class Dec {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Dec_var extends Dec {
        public String id;
        public Tipo tipo;
        public int fila, col;

        public Dec_var(String id, Tipo tipo, int fila, int col) {
            this.id = id;
            this.tipo = tipo;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "<decvar>\n" + id + "$f:" + fila + ",c:" + col + "$\n:\n" + tipo.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Dec_tipo extends Dec {
        public String id;
        public Tipo tipo;
        public int fila, col;

        public Dec_tipo(String id, Tipo tipo, int fila, int col) {
            this.id = id;
            this.tipo = tipo;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "<dectype>\n" + id + "$f:" + fila + ",c:" + col + "$\n:\n" + tipo.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Dec_proc extends Dec {
        public String id;
        public int fila, col;
        public LProcParams_0 params;
        public LDec_0 decs;
        public Instrs_0 instrs;

        public Dec_proc(String id, int fila, int col, LProcParams_0 params, LDec_0 decs, Instrs_0 instrs) {
            this.id = id;
            this.fila = fila;
            this.col = col;
            this.params = params;
            this.decs = decs;
            this.instrs = instrs;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "<decproc>\n" + id + "$f:" + fila + ",c:" + col + "$\n(\n" + params.imprime() + ")\n" + decs.imprime() + instrs.imprime() + "<end_proc>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}
