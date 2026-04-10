package ast;

public abstract class Tipo {
    
    public abstract String imprime();
    public abstract void process(Procesamiento p);

    public static class Tipo_int extends Tipo {
        public Tipo_int() {}

        

        public String imprime() {
            return "<int>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_real extends Tipo {
        public Tipo_real() {}

        

        public String imprime() {
            return "<real>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_bool extends Tipo {
        public Tipo_bool() {}

        

        public String imprime() {
            return "<bool>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_string extends Tipo {
        public Tipo_string() {}

        

        public String imprime() {
            return "<string>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_id extends Tipo {
        public String id;
        public int fila, col;

        public Tipo_id(String id, int fila, int col) {
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        

        public String imprime() {
            return id + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_array extends Tipo {
        public String dim;
        public Tipo tipo;
        public int fila, col;

        public Tipo_array(String dim, Tipo tipo, int fila, int col) {
            this.dim = dim;
            this.tipo = tipo;
            this.fila = fila;
            this.col = col;
        }

        

        public String imprime() {
            return "<array>\n[\n" + dim + "\n]$f:" + fila + ",c:" + col + "$\n<of>\n" + tipo.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_pointer extends Tipo {
        public Tipo tipo;

        public Tipo_pointer(Tipo tipo) {
            this.tipo = tipo;
        }

        

        public String imprime() {
            return "<pointer>\n" + tipo.imprime();
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    public static class Tipo_record extends Tipo {
        public ListaRecord lista;

        public Tipo_record(ListaRecord lista) {
            this.lista = lista;
        }

        

        public String imprime() {
            return "<record>\n" + lista.imprime() + "<end_record>\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }
    }
}
