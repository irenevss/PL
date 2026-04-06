package ast;

public interface Procesamiento {
    void process(Prog p);
    void process(LDec_0 ld);
    void process(Dec d);
    void process(Exp e);
    void process(Tipo t);
    void process(CamposRecord c);
    void process(ListaRecord lr);
    void process(LInstr_0 l);
    void process(LInstr li);
    // Add more as needed
}