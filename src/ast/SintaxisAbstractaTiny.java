package ast;

public class SintaxisAbstractaTiny {

    public static abstract class Nodo {
        public Nodo() {
            fila = col = -1;
        }

        private int fila;
        private int col;

        public Nodo ponFila(int fila) {
            this.fila = fila;
            return this;
        }

        public Nodo ponCol(int col) {
            this.col = col;
            return this;
        }

        public int leeFila() {
            return fila;
        }

        public int leeCol() {
            return col;
        }

        public abstract String imprime();
        public abstract void process(Procesamiento p);
    }

    // --- Prog ---
    public static class Prog extends Nodo {
        public LDec_0 decs;
        public Instrs_0 instrs;

        public Prog(LDec_0 decs, Instrs_0 instrs) {
            this.decs = decs;
            this.instrs = instrs;
        }

        @Override
        public String imprime() {
            return "<program>\n" + (decs != null ? decs.imprime() : "") + (instrs != null ? instrs.imprime() : "") + "<end_program>\n";
        }

        @Override
        public void process(Procesamiento p) {
            p.process(this);
        }
    }

    // --- Decs y Listas ---
    public static abstract class LDec_0 extends Nodo {}

    public static class Si_dec extends LDec_0 {
        public LDec decs;
        public Si_dec(LDec decs) { this.decs = decs; }
        @Override
        public String imprime() { return decs.imprime() + "--\n"; }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    public static class No_dec extends LDec_0 {
        public No_dec() {}
        @Override
        public String imprime() { return ""; }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class LDec extends Nodo {}

    public static class Muchas_decs extends LDec {
        public LDec decs;
        public Dec dec;
        public Muchas_decs(LDec decs, Dec dec) { this.decs = decs; this.dec = dec; }
        @Override
        public String imprime() { return decs.imprime() + ";\n" + dec.imprime(); }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    public static class Una_dec extends LDec {
        public Dec dec;
        public Una_dec(Dec dec) { this.dec = dec; }
        @Override
        public String imprime() { return dec.imprime(); }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    // --- Dec ---
    public static abstract class Dec extends Nodo {}

    public static class Dec_var extends Dec {
        public String id;
        public Tipo tipo;
        public Dec_var(String id, Tipo tipo, int fila, int col) {
            this.id = id; this.tipo = tipo; this.ponFila(fila); this.ponCol(col);
        }
        @Override
        public String imprime() { return "<decvar>\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n:\n" + tipo.imprime(); }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    public static class Dec_tipo extends Dec {
        public String id;
        public Tipo tipo;
        public Dec_tipo(String id, Tipo tipo, int fila, int col) {
            this.id = id; this.tipo = tipo; this.ponFila(fila); this.ponCol(col);
        }
        @Override
        public String imprime() { return "<dectype>\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n:\n" + tipo.imprime(); }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    public static class Dec_proc extends Dec {
        public String id;
        public LProcParams_0 params;
        public LDec_0 decs;
        public Instrs_0 instrs;
        public Dec_proc(String id, LProcParams_0 params, LDec_0 decs, Instrs_0 instrs, int fila, int col) {
            this.id = id; this.params = params; this.decs = decs; this.instrs = instrs; this.ponFila(fila); this.ponCol(col);
        }
        @Override
        public String imprime() {
            return "<decproc>\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n(\n" + params.imprime() + ")\n"
                    + decs.imprime() + instrs.imprime() + "<end_proc>\n";
        }
        @Override
        public void process(Procesamiento p) { p.process(this); }
    }

    // --- Parametros ---
    public static abstract class LProcParams_0 extends Nodo {}
    public static class Si_procparam extends LProcParams_0 {
        public LProcParams params;
        public Si_procparam(LProcParams params) { this.params = params; }
        @Override public String imprime() { return params.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class No_procparam extends LProcParams_0 {
        public No_procparam() {}
        @Override public String imprime() { return ""; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class LProcParams extends Nodo {}
    public static class Muchos_procparam extends LProcParams {
        public LProcParams params;
        public Param param;
        public Muchos_procparam(LProcParams params, Param param) { this.params = params; this.param = param; }
        @Override public String imprime() { return params.imprime() + ",\n" + param.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Un_procparam extends LProcParams {
        public Param param;
        public Un_procparam(Param param) { this.param = param; }
        @Override public String imprime() { return param.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class Param extends Nodo {}
    public static class Param_ref extends Param {
        public String id;
        public Tipo tipo;
        public Param_ref(String id, Tipo tipo, int fila, int col) {
            this.id = id; this.tipo = tipo; this.ponFila(fila); this.ponCol(col);
        }
        @Override public String imprime() { return "<ref>\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n:\n" + tipo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Param_val extends Param {
        public String id;
        public Tipo tipo;
        public Param_val(String id, Tipo tipo, int fila, int col) {
            this.id = id; this.tipo = tipo; this.ponFila(fila); this.ponCol(col);
        }
        @Override public String imprime() { return id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n:\n" + tipo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    // --- Tipos ---
    public static abstract class Tipo extends Nodo {}
    public static class Tipo_int extends Tipo {
        @Override public String imprime() { return "<int>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_real extends Tipo {
        @Override public String imprime() { return "<real>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_bool extends Tipo {
        @Override public String imprime() { return "<bool>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_string extends Tipo {
        @Override public String imprime() { return "<string>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_id extends Tipo {
        public String id;
        public Tipo_id(String id, int fila, int col) { this.id = id; this.ponFila(fila); this.ponCol(col); }
        @Override public String imprime() { return id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_array extends Tipo {
        public String dim;
        public Tipo tipo;
        public Tipo_array(String dim, Tipo tipo, int fila, int col) { this.dim = dim; this.tipo = tipo; this.ponFila(fila); this.ponCol(col); }
        @Override public String imprime() { return "<array>\n[\n" + dim + "\n]$f:" + leeFila() + ",c:" + leeCol() + "$\n<of>\n" + tipo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_pointer extends Tipo {
        public Tipo tipo;
        public Tipo_pointer(Tipo tipo) { this.tipo = tipo; }
        @Override public String imprime() { return "<pointer>\n" + tipo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Tipo_record extends Tipo {
        public ListaRecord lista;
        public Tipo_record(ListaRecord lista) { this.lista = lista; }
        @Override public String imprime() { return "<record>\n" + lista.imprime() + "<end_record>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class ListaRecord extends Nodo {}
    public static class Muchos_camposrecord extends ListaRecord {
        public ListaRecord lista;
        public CamposRecord campo;
        public Muchos_camposrecord(ListaRecord lista, CamposRecord campo) { this.lista = lista; this.campo = campo; }
        @Override public String imprime() { return lista.imprime() + ";\n" + campo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Un_camporecord extends ListaRecord {
        public CamposRecord campo;
        public Un_camporecord(CamposRecord campo) { this.campo = campo; }
        @Override public String imprime() { return campo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static class CamposRecord extends Nodo {
        public String id;
        public Tipo tipo;
        public CamposRecord(String id, Tipo tipo, int fila, int col) { this.id = id; this.tipo = tipo; this.ponFila(fila); this.ponCol(col); }
        @Override public String imprime() { return id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n:\n" + tipo.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    // --- Instrucciones ---
    public static abstract class Instrs_0 extends Nodo {}
    public static class Si_instr extends Instrs_0 {
        public LInstr instrs;
        public Si_instr(LInstr instrs) { this.instrs = instrs; }
        @Override public String imprime() { return instrs.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class No_instr extends Instrs_0 {
        @Override public String imprime() { return ""; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class LInstr_0 extends Nodo {}
    public static class Si_instr_l extends LInstr_0 {
        public LInstr instrs;
        public Si_instr_l(LInstr instrs) { this.instrs = instrs; }
        @Override public String imprime() { return instrs.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class No_instr_l extends LInstr_0 {
        @Override public String imprime() { return ""; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class LInstr extends Nodo {}
    public static class Muchas_instr extends LInstr {
        public LInstr instrs;
        public Instr instr;
        public Muchas_instr(LInstr instrs, Instr instr) { this.instrs = instrs; this.instr = instr; }
        @Override public String imprime() { return instrs.imprime() + ";\n" + instr.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Una_instr extends LInstr {
        public Instr instr;
        public Una_instr(Instr instr) { this.instr = instr; }
        @Override public String imprime() { return instr.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class Instr extends Nodo {}
    public static class Instr_asig extends Instr {
        public Exp exp1, exp2;
        public Instr_asig(Exp exp1, Exp exp2) { this.exp1 = exp1; this.exp2 = exp2; }
        @Override public String imprime() { return exp1.imprime() + ":=\n" + exp2.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_if extends Instr {
        public Exp exp;
        public LInstr_0 instrs;
        public Instr_if(Exp exp, LInstr_0 instrs) { this.exp = exp; this.instrs = instrs; }
        @Override public String imprime() { return "<if>\n" + exp.imprime() + ":\n" + instrs.imprime() + "<end_if>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_ifelse extends Instr {
        public Exp exp;
        public LInstr_0 instrs1, instrs2;
        public Instr_ifelse(Exp exp, LInstr_0 instrs1, LInstr_0 instrs2) { this.exp = exp; this.instrs1 = instrs1; this.instrs2 = instrs2; }
        @Override public String imprime() { return "<if>\n" + exp.imprime() + ":\n" + instrs1.imprime() + "<else>\n" + instrs2.imprime() + "<end_if>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_while extends Instr {
        public Exp exp;
        public LInstr_0 instrs;
        public Instr_while(Exp exp, LInstr_0 instrs) { this.exp = exp; this.instrs = instrs; }
        @Override public String imprime() { return "<while>\n" + exp.imprime() + ":\n" + instrs.imprime() + "<end_while>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_lectura extends Instr {
        public Exp exp;
        public Instr_lectura(Exp exp) { this.exp = exp; }
        @Override public String imprime() { return "<input>\n" + exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_escritura extends Instr {
        public Exp exp;
        public Instr_escritura(Exp exp) { this.exp = exp; }
        @Override public String imprime() { return "<output>\n" + exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_reserva extends Instr {
        public Exp exp;
        public Instr_reserva(Exp exp) { this.exp = exp; }
        @Override public String imprime() { return "<new>\n" + exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_liberacion extends Instr {
        public Exp exp;
        public Instr_liberacion(Exp exp) { this.exp = exp; }
        @Override public String imprime() { return "<dispose>\n" + exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_invocar extends Instr {
        public String id;
        public LExps_0 exps;
        public Instr_invocar(String id, LExps_0 exps, int fila, int col) { this.id = id; this.exps = exps; this.ponFila(fila); this.ponCol(col); }
        @Override public String imprime() { return "@\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n(\n" + exps.imprime() + ")\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Instr_compuesta extends Instr {
        public LDec_0 decs;
        public LInstr_0 instrs;
        public Instr_compuesta(LDec_0 decs, LInstr_0 instrs) { this.decs = decs; this.instrs = instrs; }
        @Override public String imprime() { return "<block>\n" + decs.imprime() + instrs.imprime() + "<end_block>\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    // --- Expresiones ---
    public static abstract class LExps_0 extends Nodo {}
    public static class Si_exps extends LExps_0 {
        public LExps exps;
        public Si_exps(LExps exps) { this.exps = exps; }
        @Override public String imprime() { return exps.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class No_exps extends LExps_0 {
        @Override public String imprime() { return ""; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class LExps extends Nodo {}
    public static class Muchas_exps extends LExps {
        public LExps exps;
        public Exp exp;
        public Muchas_exps(LExps exps, Exp exp) { this.exps = exps; this.exp = exp; }
        @Override public String imprime() { return exps.imprime() + ",\n" + exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Una_exp extends LExps {
        public Exp exp;
        public Una_exp(Exp exp) { this.exp = exp; }
        @Override public String imprime() { return exp.imprime(); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class Exp extends Nodo {
        public abstract int prioridad();
        protected String imprimeOpnd(Exp opnd, int minPrior, boolean isRight) {
            int prior_opnd = opnd.prioridad();
            boolean needs_parens = false;
            if (prior_opnd < minPrior) { needs_parens = true; }
            else if (prior_opnd == minPrior) {
                if (isRight) { if (minPrior <= 4) needs_parens = true; }
                else { if (minPrior == 0 || minPrior == 2) needs_parens = true; }
            }
            if (needs_parens) return "(\n" + opnd.imprime() + ")\n";
            else return opnd.imprime();
        }
    }

    public static abstract class ExpBin extends Exp {
        public Exp opnd0, opnd1;
        public ExpBin(Exp opnd0, Exp opnd1, int fila, int col) {
            this.opnd0 = opnd0; this.opnd1 = opnd1; this.ponFila(fila); this.ponCol(col);
        }
    }

    public static class Exp_suma extends ExpBin {
        public Exp_suma(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 1; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 1, false) + "+$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 1, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_resta extends ExpBin {
        public Exp_resta(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 1; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 1, false) + "-$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 1, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_mul extends ExpBin {
        public Exp_mul(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 3; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 3, false) + "*$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 3, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_div extends ExpBin {
        public Exp_div(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 3; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 3, false) + "/$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 3, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_mod extends ExpBin {
        public Exp_mod(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 3; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 3, false) + "%$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 3, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_and extends ExpBin {
        public Exp_and(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 4; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 4, false) + "&$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 4, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_or extends ExpBin {
        public Exp_or(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 2; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 2, false) + "|$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 2, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_menor extends ExpBin {
        public Exp_menor(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + "<$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_mayor extends ExpBin {
        public Exp_mayor(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + ">$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_menor_igual extends ExpBin {
        public Exp_menor_igual(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + "<=$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_mayor_igual extends ExpBin {
        public Exp_mayor_igual(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + ">=$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_igual extends ExpBin {
        public Exp_igual(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + "=$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_distinto extends ExpBin {
        public Exp_distinto(Exp o0, Exp o1, int f, int c) { super(o0, o1, f, c); }
        @Override public int prioridad() { return 0; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 0, false) + "<>$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd1, 0, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class ExpUni extends Exp {
        public Exp opnd;
        public ExpUni(Exp opnd, int fila, int col) {
            this.opnd = opnd; this.ponFila(fila); this.ponCol(col);
        }
    }

    public static class Exp_menos_unario extends ExpUni {
        public Exp_menos_unario(Exp o, int f, int c) { super(o, f, c); }
        @Override public int prioridad() { return 5; }
        @Override public String imprime() { return "-$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd, 5, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_not extends ExpUni {
        public Exp_not(Exp o, int f, int c) { super(o, f, c); }
        @Override public int prioridad() { return 5; }
        @Override public String imprime() { return "!$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd, 5, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_asterisco_unario extends ExpUni {
        public Exp_asterisco_unario(Exp o, int f, int c) { super(o, f, c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return "*$f:" + leeFila() + ",c:" + leeCol() + "$\n" + imprimeOpnd(opnd, 7, true); }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static class Iden extends Exp {
        public String id;
        public Iden(String id, int f, int c) { this.id = id; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Lit_int extends Exp {
        public String val;
        public Lit_int(String val, int f, int c) { this.val = val; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return val + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Lit_real extends Exp {
        public String val;
        public Lit_real(String val, int f, int c) { this.val = val; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return val + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Lit_bool extends Exp {
        public String val;
        public Lit_bool(String val, int f, int c) { this.val = val; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return val + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Lit_string extends Exp {
        public String val;
        public Lit_string(String val, int f, int c) { this.val = val; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return val + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_null extends Exp {
        public Exp_null(int f, int c) { this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 7; }
        @Override public String imprime() { return "<null>$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_campo extends Exp {
        public Exp base; public String id;
        public Exp_campo(Exp b, String i, int f, int c) { this.base = b; this.id = i; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 6; }
        @Override public String imprime() { return imprimeOpnd(base, 6, false) + ".\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_flecha extends Exp {
        public Exp base; public String id;
        public Exp_flecha(Exp b, String i, int f, int c) { this.base = b; this.id = i; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 6; }
        @Override public String imprime() { return imprimeOpnd(base, 6, false) + "->\n" + id + "$f:" + leeFila() + ",c:" + leeCol() + "$\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }
    public static class Exp_array extends Exp {
        public Exp opnd0, opnd1;
        public Exp_array(Exp o0, Exp o1, int f, int c) { this.opnd0 = o0; this.opnd1 = o1; this.ponFila(f); this.ponCol(c); }
        @Override public int prioridad() { return 6; }
        @Override public String imprime() { return imprimeOpnd(opnd0, 6, false) + "[$f:" + leeFila() + ",c:" + leeCol() + "$\n" + opnd1.imprime() + "]\n"; }
        @Override public void process(Procesamiento p) { p.process(this); }
    }

    public static abstract class PostfixOp {
        public abstract Exp apply(Exp base);
    }

    public static class Op_campo extends PostfixOp {
        public String id; public int fila, col;
        public Op_campo(String id, int fila, int col) { this.id = id; this.fila = fila; this.col = col; }
        public Exp apply(Exp base) { return new Exp_campo(base, id, fila, col); }
    }

    public static class Op_flecha extends PostfixOp {
        public String id; public int fila, col;
        public Op_flecha(String id, int fila, int col) { this.id = id; this.fila = fila; this.col = col; }
        public Exp apply(Exp base) { return new Exp_flecha(base, id, fila, col); }
    }

    public static class Op_array extends PostfixOp {
        public Exp index; public int fila, col;
        public Op_array(Exp index, int fila, int col) { this.index = index; this.fila = fila; this.col = col; }
        public Exp apply(Exp base) { return new Exp_array(base, index, fila, col); }
    }
}
