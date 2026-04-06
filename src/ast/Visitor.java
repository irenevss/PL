package ast;

public interface Visitor {
    void visit(Prog p);
    void visit(LDec_0 ld);
    void visit(Dec d);
    void visit(Exp e);
    void visit(Tipo t);
    void visit(CamposRecord c);
    void visit(ListaRecord lr);
    void visit(LInstr_0 l);
    void visit(LInstr li);
    // Add more as needed
}