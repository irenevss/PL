package asint;

public class ClaseSemanticaTiny extends SintaxisAbstractaTiny {

    public ClaseSemanticaTiny() {
        super();
    }

    public Exp exp_binaria(String op, Exp opnd1, Exp opnd2, int fila, int col) {
        Exp res;
        switch(op) {
            case "+":  res = exp_suma(opnd1, opnd2); break;
            case "-":  res = exp_resta(opnd1, opnd2); break;
            case "*":  res = exp_mul(opnd1, opnd2); break;
            case "/":  res = exp_div(opnd1, opnd2); break;
            case "%":  res = exp_mod(opnd1, opnd2); break;
            case "&":  res = exp_and(opnd1, opnd2); break;
            case "|":  res = exp_or(opnd1, opnd2); break;
            case "<":  res = exp_menor(opnd1, opnd2); break;
            case ">":  res = exp_mayor(opnd1, opnd2); break;
            case "<=": res = exp_menor_igual(opnd1, opnd2); break;
            case ">=": res = exp_mayor_igual(opnd1, opnd2); break;
            case "=":  res = exp_igual(opnd1, opnd2); break;
            case "<>": res = exp_distinto(opnd1, opnd2); break;
            default: throw new UnsupportedOperationException("Operador no soportado: " + op);
        }
        return (Exp) res.ponFila(fila).ponCol(col);
    }

    public Exp aplicar_postfijo(String op, Exp base, Exp indice, String id, int fila, int col) {
        Exp res;
        switch(op) {
            case "[]": res = exp_array(base, indice); break;
            case ".":  res = exp_campo(base, id); break;
            case "->": res = exp_flecha(base, id); break;
            default: throw new UnsupportedOperationException("Postfijo no soportado: " + op);
        }
        return (Exp) res.ponFila(fila).ponCol(col);
    }
}