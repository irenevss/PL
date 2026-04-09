package ast;

public abstract class Exp {
    public abstract void accept(Visitor v);
    public abstract String imprime();
    public abstract void process(Procesamiento p);
    public abstract int prioridad();

    protected String imprimeOpnd(Exp opnd, int minPrior, boolean isRight) {
        int prior_opnd = opnd.prioridad();
        boolean needs_parens = false;

        if (prior_opnd < minPrior) {
            needs_parens = true;
        } else if (prior_opnd == minPrior) {
            if (isRight) {
                if (minPrior <= 4) {
                    needs_parens = true;
                }
            } else {
                if (minPrior == 0 || minPrior == 2) {
                    needs_parens = true;
                }
            }
        }

        if (needs_parens) {
            return "(\n" + opnd.imprime() + ")\n";
        } else {
            return opnd.imprime();
        }
    }

    public static class Exp_suma extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_suma(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 1, false) + "+$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 1, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 1;
        }
    }

    public static class Exp_resta extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_resta(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 1, false) + "-$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 1, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 1;
        }
    }

    public static class Exp_mul extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_mul(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 3, false) + "*$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 3, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 3;
        }
    }

    public static class Exp_div extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_div(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 3, false) + "/$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 3, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 3;
        }
    }

    public static class Exp_mod extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_mod(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 3, false) + "%$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 3, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 3;
        }
    }

    public static class Exp_and extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_and(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 4, false) + "&$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 4, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 4;
        }
    }

    public static class Exp_or extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_or(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 2, false) + "|$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 2, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 2;
        }
    }

    public static class Exp_menor extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_menor(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + "<$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_mayor extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_mayor(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + ">$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_menor_igual extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_menor_igual(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + "<=$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_mayor_igual extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_mayor_igual(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + ">=$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_igual extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_igual(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + "=$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_distinto extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_distinto(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 0, false) + "<>$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd1, 0, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 0;
        }
    }

    public static class Exp_menos_unario extends Exp {
        public Exp opnd;
        public int fila, col;

        public Exp_menos_unario(Exp opnd, int fila, int col) {
            this.opnd = opnd;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "-$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd, 5, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 5;
        }
    }

    public static class Exp_not extends Exp {
        public Exp opnd;
        public int fila, col;

        public Exp_not(Exp opnd, int fila, int col) {
            this.opnd = opnd;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "!$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd, 5, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 5;
        }
    }

    public static class Exp_asterisco_unario extends Exp {
        public Exp opnd;
        public int fila, col;

        public Exp_asterisco_unario(Exp opnd, int fila, int col) {
            this.opnd = opnd;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "*$f:" + fila + ",c:" + col + "$\n" + imprimeOpnd(opnd, 7, true);
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 7;
        }
    }

    public static class Iden extends Exp {
        public String id;
        public int fila, col;

        public Iden(String id, int fila, int col) {
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return id + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Lit_int extends Exp {
        public String val;
        public int fila, col;

        public Lit_int(String val, int fila, int col) {
            this.val = val;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return val + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Lit_real extends Exp {
        public String val;
        public int fila, col;

        public Lit_real(String val, int fila, int col) {
            this.val = val;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return val + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Lit_bool extends Exp {
        public String val;
        public int fila, col;

        public Lit_bool(String val, int fila, int col) {
            this.val = val;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return val + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Lit_string extends Exp {
        public String val;
        public int fila, col;

        public Lit_string(String val, int fila, int col) {
            this.val = val;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return val + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Exp_null extends Exp {
        public int fila, col;

        public Exp_null(int fila, int col) {
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return "<null>$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 8;
        }
    }

    public static class Exp_campo extends Exp {
        public Exp base;
        public String id;
        public int fila, col;

        public Exp_campo(Exp base, String id, int fila, int col) {
            this.base = base;
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(base, 6, false) + ".\n" + id + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 6;
        }
    }

    public static class Exp_flecha extends Exp {
        public Exp base;
        public String id;
        public int fila, col;

        public Exp_flecha(Exp base, String id, int fila, int col) {
            this.base = base;
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(base, 6, false) + "->\n" + id + "$f:" + fila + ",c:" + col + "$\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 6;
        }
    }

    public static class Exp_array extends Exp {
        public Exp opnd0;
        public Exp opnd1;
        public int fila, col;

        public Exp_array(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
            this.fila = fila;
            this.col = col;
        }

        public void accept(Visitor v) {
            v.visit(this);
        }

        public String imprime() {
            return imprimeOpnd(opnd0, 6, false) + "[$f:" + fila + ",c:" + col + "$\n" + opnd1.imprime() + "]\n";
        }

        public void process(Procesamiento p) {
            p.process(this);
        }

        public int prioridad() {
            return 6;
        }
    }
    public static abstract class PostfixOp {
        public abstract Exp apply(Exp base);
    }

    public static class Op_campo extends PostfixOp {
        public String id;
        public int fila, col;

        public Op_campo(String id, int fila, int col) {
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        public Exp apply(Exp base) {
            return new Exp_campo(base, id, fila, col);
        }
    }

    public static class Op_flecha extends PostfixOp {
        public String id;
        public int fila, col;

        public Op_flecha(String id, int fila, int col) {
            this.id = id;
            this.fila = fila;
            this.col = col;
        }

        public Exp apply(Exp base) {
            return new Exp_flecha(base, id, fila, col);
        }
    }

    public static class Op_array extends PostfixOp {
        public Exp index;
        public int fila, col;

        public Op_array(Exp index, int fila, int col) {
            this.index = index;
            this.fila = fila;
            this.col = col;
        }

        public Exp apply(Exp base) {
            return new Exp_array(base, index, fila, col);
        }
    }
}
