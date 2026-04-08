package ast;

public abstract class ListaRecord {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
}

public class Muchos_camposrecord extends ListaRecord {
    public ListaRecord lista;
    public CamposRecord campo;

    public Muchos_camposrecord(ListaRecord lista, CamposRecord campo) {
        this.lista = lista;
        this.campo = campo;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return lista.imprime() + ";\n" + campo.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}

public class Un_camporecord extends ListaRecord {
    public CamposRecord campo;

    public Un_camporecord(CamposRecord campo) {
        this.campo = campo;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public String imprime() {
        return campo.imprime();
    }

    public void process(Procesamiento p) {
        p.process(this);
    }
}
