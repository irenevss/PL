package asint;

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
    }

    // GENEROS

    // Seccion programa

    public static class Prog extends Nodo {
        private LDec_0 decs;
        private Instrs_0 instrs;

        public Prog(LDec_0 decs, Instrs_0 instrs) {
            this.decs = decs;
            this.instrs = instrs;
        }

        public String toString() {
            return "prog(" + decs + "," + instrs + ")";
        }
    }

    // Seccion declaraciones

    public static abstract class LDec_0 extends Nodo {}
    public static class Si_dec extends LDec_0 {
        private LDec decs;
        public Si_dec(LDec decs) { this.decs = decs; }
        public String toString() { return "si_dec(" + decs + ")"; }
    }
    public static class No_dec extends LDec_0 {
        public String toString() { return "no_dec()"; }
    }

    public static abstract class LDec extends Nodo {}
    public static class Muchas_decs extends LDec {
        private LDec decs;
        private Dec dec;
        public Muchas_decs(LDec decs, Dec dec) {
            this.decs = decs; this.dec = dec;
        }
        public String toString() { return "muchas_decs(" + decs + "," + dec + ")"; }
    }
    public static class Una_dec extends LDec {
        private Dec dec;
        public Una_dec(Dec dec) { this.dec = dec; }
        public String toString() { return "una_dec(" + dec + ")"; }
    }

    public static abstract class Dec extends Nodo {}

    public static class Dec_var extends Dec {
        private String id;
        private Tipo tipo;
        public Dec_var(String id, Tipo tipo) { this.id = id; this.tipo = tipo; }
        public String toString() { return "dec_var(" + id + "[" + leeFila() + "," + leeCol() + "]," + tipo + ")"; }
    }

    public static class Dec_tipo extends Dec {
        private String id;
        private Tipo tipo;
        public Dec_tipo(String id, Tipo tipo) { this.id = id; this.tipo = tipo; }
        public String toString() { return "dec_tipo(" + id + "[" + leeFila() + "," + leeCol() + "]," + tipo + ")"; }
    }

    public static class Dec_proc extends Dec {
        private String id;
        private LProcParams_0 params;
        private LDec_0 decs;
        private Instrs_0 instrs;
        public Dec_proc(String id, LProcParams_0 params, LDec_0 decs, Instrs_0 instrs) {
            this.id = id; this.params = params; this.decs = decs; this.instrs = instrs;
        }
        public String toString() {
            return "dec_proc(" + id + "[" + leeFila() + "," + leeCol() + "]," + params + "," + decs + "," + instrs + ")";
        }
    }

    // Seccion tipos

    public static abstract class Tipo extends Nodo {}

    public static class Tipo_int extends Tipo {
        public String toString() { return "tipo_int()"; }
    }
    public static class Tipo_real extends Tipo {
        public String toString() { return "tipo_real()"; }
    }
    public static class Tipo_bool extends Tipo {
        public String toString() { return "tipo_bool()"; }
    }
    public static class Tipo_string extends Tipo {
        public String toString() { return "tipo_string()"; }
    }
    public static class Tipo_id extends Tipo {
        private String id;
        public Tipo_id(String id) { this.id = id; }
        public String toString() { return "tipo_id(" + id + "[" + leeFila() + "," + leeCol() + "])"; }
    }
    public static class Tipo_array extends Tipo {
        private String tam;
        private Tipo tipo;
        public Tipo_array(String tam, Tipo tipo) { this.tam = tam; this.tipo = tipo; }
        public String toString() { return "tipo_array(" + tam + "," + tipo + ")"; }
    }
    public static class Tipo_pointer extends Tipo {
        private Tipo tipo;
        public Tipo_pointer(Tipo tipo) { this.tipo = tipo; }
        public String toString() { return "tipo_pointer(" + tipo + ")"; }
    }
    public static class Tipo_record extends Tipo {
        private ListaRecord campos;
        public Tipo_record(ListaRecord campos) { this.campos = campos; }
        public String toString() { return "tipo_record(" + campos + ")"; }
    }

    public static abstract class ListaRecord extends Nodo {}
    public static class Muchos_camposrecord extends ListaRecord {
        private ListaRecord lista;
        private CamposRecord campo;
        public Muchos_camposrecord(ListaRecord lista, CamposRecord campo) {
            this.lista = lista; this.campo = campo;
        }
        public String toString() { return "muchos_camposrecord(" + lista + "," + campo + ")"; }
    }
    public static class Un_camporecord extends ListaRecord {
        private CamposRecord campo;
        public Un_camporecord(CamposRecord campo) { this.campo = campo; }
        public String toString() { return "un_camporecord(" + campo + ")"; }
    }

    public static class CamposRecord extends Nodo {
        private String id;
        private Tipo tipo;
        public CamposRecord(String id, Tipo tipo) { this.id = id; this.tipo = tipo; }
        public String toString() { return "camporecord(" + id + "[" + leeFila() + "," + leeCol() + "]," + tipo + ")"; }
    }

    // Seccion parametros formales

    public static abstract class LProcParams_0 extends Nodo {}
    public static class Si_procparam extends LProcParams_0 {
        private LProcParams params;
        public Si_procparam(LProcParams params) { this.params = params; }
        public String toString() { return "si_procparam(" + params + ")"; }
    }
    public static class No_procparam extends LProcParams_0 {
        public String toString() { return "no_procparam()"; }
    }

    public static abstract class LProcParams extends Nodo {}
    public static class Muchos_procparam extends LProcParams {
        private LProcParams lista;
        private Param param;
        public Muchos_procparam(LProcParams lista, Param param) {
            this.lista = lista; this.param = param;
        }
        public String toString() { return "muchos_procparam(" + lista + "," + param + ")"; }
    }
    public static class Un_procparam extends LProcParams {
        private Param param;
        public Un_procparam(Param param) { this.param = param; }
        public String toString() { return "un_procparam(" + param + ")"; }
    }

    public static abstract class Param extends Nodo {}
    public static class Param_ref extends Param {
        private String id;
        private Tipo tipo;
        public Param_ref(String id, Tipo tipo) { this.id = id; this.tipo = tipo; }
        public String toString() { return "param_ref(" + id + "[" + leeFila() + "," + leeCol() + "]," + tipo + ")"; }
    }
    public static class Param_val extends Param {
        private String id;
        private Tipo tipo;
        public Param_val(String id, Tipo tipo) { this.id = id; this.tipo = tipo; }
        public String toString() { return "param_val(" + id + "[" + leeFila() + "," + leeCol() + "]," + tipo + ")"; }
    }

    // Seccion instrucciones

    public static abstract class Instrs_0 extends Nodo {}
    public static class Si_instr extends Instrs_0 {
        private LInstr instrs;
        public Si_instr(LInstr instrs) { this.instrs = instrs; }
        public String toString() { return "si_instr(" + instrs + ")"; }
    }
    public static class No_instr extends Instrs_0 {
        public String toString() { return "no_instr()"; }
    }

    public static abstract class LInstr_0 extends Nodo {}
    public static class Si_instr0 extends LInstr_0 {
        private LInstr instrs;
        public Si_instr0(LInstr instrs) { this.instrs = instrs; }
        public String toString() { return "si_instr0(" + instrs + ")"; }
    }
    public static class No_instr0 extends LInstr_0 {
        public String toString() { return "no_instr0()"; }
    }

    public static abstract class LInstr extends Nodo {}
    public static class Muchas_instr extends LInstr {
        private LInstr lista;
        private Instr instr;
        public Muchas_instr(LInstr lista, Instr instr) {
            this.lista = lista; this.instr = instr;
        }
        public String toString() { return "muchas_instr(" + lista + "," + instr + ")"; }
    }
    public static class Una_instr extends LInstr {
        private Instr instr;
        public Una_instr(Instr instr) { this.instr = instr; }
        public String toString() { return "una_instr(" + instr + ")"; }
    }

    public static abstract class Instr extends Nodo {}

    public static class Instr_asig extends Instr {
        private Exp e1, e2;
        public Instr_asig(Exp e1, Exp e2) { this.e1 = e1; this.e2 = e2; }
        public String toString() { return "instr_asig(" + e1 + "," + e2 + ")"; }
    }

    public static class Instr_if extends Instr {
        private Exp exp;
        private LInstr_0 instrs;
        public Instr_if(Exp exp, LInstr_0 instrs) { this.exp = exp; this.instrs = instrs; }
        public String toString() { return "instr_if(" + exp + "," + instrs + ")"; }
    }

    public static class Instr_ifelse extends Instr {
        private Exp exp;
        private LInstr_0 i1, i2;
        public Instr_ifelse(Exp exp, LInstr_0 i1, LInstr_0 i2) {
            this.exp = exp; this.i1 = i1; this.i2 = i2;
        }
        public String toString() { return "instr_ifelse(" + exp + "," + i1 + "," + i2 + ")"; }
    }

    public static class Instr_while extends Instr {
        private Exp exp;
        private LInstr_0 instrs;
        public Instr_while(Exp exp, LInstr_0 instrs) { this.exp = exp; this.instrs = instrs; }
        public String toString() { return "instr_while(" + exp + "," + instrs + ")"; }
    }

    public static class Instr_lectura extends Instr {
        private Exp exp;
        public Instr_lectura(Exp exp) { this.exp = exp; }
        public String toString() { return "instr_lectura(" + exp + ")"; }
    }

    public static class Instr_escritura extends Instr {
        private Exp exp;
        public Instr_escritura(Exp exp) { this.exp = exp; }
        public String toString() { return "instr_escritura(" + exp + ")"; }
    }

    public static class Instr_reserva extends Instr {
        private Exp exp;
        public Instr_reserva(Exp exp) { this.exp = exp; }
        public String toString() { return "instr_reserva(" + exp + ")"; }
    }

    public static class Instr_liberacion extends Instr {
        private Exp exp;
        public Instr_liberacion(Exp exp) { this.exp = exp; }
        public String toString() { return "instr_liberacion(" + exp + ")"; }
    }

    public static class Instr_invocar extends Instr {
        private String id;
        private LExps_0 params;
        public Instr_invocar(String id, LExps_0 params) {
            this.id = id; this.params = params;
        }
        public String toString() { return "instr_invocar(" + id + "[" + leeFila() + "," + leeCol() + "]," + params + ")"; }
    }

    public static class Instr_compuesta extends Instr {
        private LDec_0 decs;
        private Instrs_0 instrs;
        public Instr_compuesta(LDec_0 decs, Instrs_0 instrs) {
            this.decs = decs; this.instrs = instrs;
        }
        public String toString() { return "instr_compuesta(" + decs + "," + instrs + ")"; }
    }

    public static abstract class LExps_0 extends Nodo {}

    public static class Si_exps extends LExps_0 {
        private LExps exps;
        public Si_exps(LExps exps) { this.exps = exps; }
        public String toString() { return "si_exps(" + exps + ")"; }
    }

    public static class No_exps extends LExps_0 { public String toString() { return "no_exps()"; } }

    public static abstract class LExps extends Nodo {}

    public static class Muchas_exps extends LExps {
        private LExps lista; private Exp exp;
        public Muchas_exps(LExps lista, Exp exp) { this.lista = lista; this.exp = exp; }
        public String toString() { return "muchas_exps(" + lista + "," + exp + ")"; }
    }
    public static class Una_exp extends LExps {
        private Exp exp;
        public Una_exp(Exp exp) { this.exp = exp; }
        public String toString() { return "una_exp(" + exp + ")"; }
    }

    // Seccion expresiones

    public static abstract class Exp extends Nodo {}

    private static abstract class ExpBin extends Exp {
        protected Exp opnd0;
        protected Exp opnd1;
        public ExpBin(Exp opnd0, Exp opnd1) {
            this.opnd0 = opnd0;
            this.opnd1 = opnd1;
        }
    }

    public static class Exp_suma extends ExpBin {
        public Exp_suma(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_suma(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_resta extends ExpBin {
        public Exp_resta(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_resta(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_mul extends ExpBin {
        public Exp_mul(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_mul(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_div extends ExpBin {
        public Exp_div(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_div(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_mod extends ExpBin {
        public Exp_mod(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_mod(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_and extends ExpBin {
        public Exp_and(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_and(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_or extends ExpBin {
        public Exp_or(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_or(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_menor extends ExpBin {
        public Exp_menor(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_menor(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_mayor extends ExpBin {
        public Exp_mayor(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_mayor(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_menor_igual extends ExpBin {
        public Exp_menor_igual(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_menor_igual(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_mayor_igual extends ExpBin {
        public Exp_mayor_igual(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_mayor_igual(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_igual extends ExpBin {
        public Exp_igual(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_igual(" + opnd0 + "," + opnd1 + ")"; }
    }
    public static class Exp_distinto extends ExpBin {
        public Exp_distinto(Exp o0, Exp o1) { super(o0,o1); }
        public String toString() { return "exp_distinto(" + opnd0 + "," + opnd1 + ")"; }
    }

    public static class Exp_menos_unario extends Exp {
        private Exp exp;
        public Exp_menos_unario(Exp exp) { this.exp = exp; }
        public String toString() { return "exp_menos_unario(" + exp + ")"; }
    }

    public static class Exp_not extends Exp {
        private Exp exp;
        public Exp_not(Exp exp) { this.exp = exp; }
        public String toString() { return "exp_not(" + exp + ")"; }
    }

    public static class Exp_asterisco_unario extends Exp {
        private Exp exp;
        public Exp_asterisco_unario(Exp exp) { this.exp = exp; }
        public String toString() { return "exp_asterisco_unario(" + exp + ")"; }
    }

    public static class Exp_array extends Exp {
        private Exp exp;
        private Exp idx;
        public Exp_array(Exp exp, Exp idx) { this.exp = exp; this.idx = idx; }
        public String toString() { return "exp_array(" + exp + "," + idx + ")"; }
    }

    public static class Exp_campo extends Exp {
        private Exp exp;
        private String id;
        public Exp_campo(Exp exp, String id) { this.exp = exp; this.id = id; }
        public String toString() { return "exp_campo(" + exp + "," + id + ")"; }
    }

    public static class Exp_flecha extends Exp {
        private Exp exp;
        private String id;
        public Exp_flecha(Exp exp, String id) { this.exp = exp; this.id = id; }
        public String toString() { return "exp_flecha(" + exp + "," + id + ")"; }
    }

    public static class Iden extends Exp {
        private String id;
        public Iden(String id) { this.id = id; }
        public String toString() { return "iden(" + id + "[" + leeFila() + "," + leeCol() + "])"; }
    }

    public static class Lit_int extends Exp {
        private String num;
        public Lit_int(String num) { this.num = num; }
        public String toString() { return "lit_int(" + num + "[" + leeFila() + "," + leeCol() + "])"; }
    }

    public static class Lit_real extends Exp {
        private String num;
        public Lit_real(String num) { this.num = num; }
        public String toString() { return "lit_real(" + num + "[" + leeFila() + "," + leeCol() + "])"; }
    }

    public static class Lit_bool extends Exp {
        private String valor;
        public Lit_bool(String valor) { this.valor = valor; }
        public String toString() { return "lit_bool(" + valor + "[" + leeFila() + "," + leeCol() + "])"; }
    }

    public static class Lit_string extends Exp {
        private String valor;
        public Lit_string(String valor) { this.valor = valor; }
        public String toString() { return "lit_string(" + valor + "[" + leeFila() + "," + leeCol() + "])"; }
    }

    public static class Lit_null extends Exp {
        public String toString() { return "lit_null()"; }
    }

    // CONSTRUCTORAS

    public Prog prog(LDec_0 decs, Instrs_0 instrs) { return new Prog(decs, instrs); }

    public LDec_0 si_dec(LDec decs) { return new Si_dec(decs); }
    public LDec_0 no_dec() { return new No_dec(); }

    public LDec muchas_decs(LDec decs, Dec dec) { return new Muchas_decs(decs, dec); }
    public LDec una_dec(Dec dec) { return new Una_dec(dec); }

    public Dec dec_var(String id, Tipo tipo) { return new Dec_var(id, tipo); }
    public Dec dec_tipo(String id, Tipo tipo) { return new Dec_tipo(id, tipo); }
    public Dec dec_proc(String id, LProcParams_0 params, LDec_0 decs, Instrs_0 instrs) {
        return new Dec_proc(id, params, decs, instrs);
    }

    public Tipo tipo_int() { return new Tipo_int(); }
    public Tipo tipo_real() { return new Tipo_real(); }
    public Tipo tipo_bool() { return new Tipo_bool(); }
    public Tipo tipo_string() { return new Tipo_string(); }
    public Tipo tipo_id(String id) { return new Tipo_id(id); }
    public Tipo tipo_array(String tam, Tipo tipo) { return new Tipo_array(tam, tipo); }
    public Tipo tipo_pointer(Tipo tipo) { return new Tipo_pointer(tipo); }
    public Tipo tipo_record(ListaRecord lr) { return new Tipo_record(lr); }

    public ListaRecord muchos_camposrecord(ListaRecord l, CamposRecord c) { return new Muchos_camposrecord(l, c); }
    public ListaRecord un_camporecord(CamposRecord c) { return new Un_camporecord(c); }
    public CamposRecord camporecord(String id, Tipo tipo) { return new CamposRecord(id, tipo); }

    public LProcParams_0 si_procparam(LProcParams p) { return new Si_procparam(p); }
    public LProcParams_0 no_procparam() { return new No_procparam(); }
    public LProcParams muchos_procparam(LProcParams l, Param p) { return new Muchos_procparam(l, p); }
    public LProcParams un_procparam(Param p) { return new Un_procparam(p); }
    public Param param_ref(String id, Tipo t) { return new Param_ref(id, t); }
    public Param param_val(String id, Tipo t) { return new Param_val(id, t); }

    public Instrs_0 si_instr(LInstr l) { return new Si_instr(l); }
    public Instrs_0 no_instr() { return new No_instr(); }

    public LInstr_0 si_instr0(LInstr l) { return new Si_instr0(l); }
    public LInstr_0 no_instr0() { return new No_instr0(); }

    public LInstr muchas_instr(LInstr l, Instr i) { return new Muchas_instr(l, i); }
    public LInstr una_instr(Instr i) { return new Una_instr(i); }

    public Instr instr_asig(Exp e1, Exp e2) { return new Instr_asig(e1, e2); }
    public Instr instr_if(Exp e, LInstr_0 l) { return new Instr_if(e, l); }
    public Instr instr_ifelse(Exp e, LInstr_0 l1, LInstr_0 l2) { return new Instr_ifelse(e, l1, l2); }
    public Instr instr_while(Exp e, LInstr_0 l) { return new Instr_while(e, l); }
    public Instr instr_lectura(Exp e) { return new Instr_lectura(e); }
    public Instr instr_escritura(Exp e) { return new Instr_escritura(e); }
    public Instr instr_reserva(Exp e) { return new Instr_reserva(e); }
    public Instr instr_liberacion(Exp e) { return new Instr_liberacion(e); }
    public Instr instr_invocar(String id, LExps_0 p) { return new Instr_invocar(id, p); }
    public Instr instr_compuesta(LDec_0 d, Instrs_0 i) { return new Instr_compuesta(d, i); }

    public LExps_0 si_exps(LExps p) { return new Si_exps(p); }
    public LExps_0 no_exps() { return new No_exps(); }
    public LExps muchas_exps(LExps l, Exp e) { return new Muchas_exps(l, e); }
    public LExps una_exp(Exp e) { return new Una_exp(e); }

    public Exp exp_suma(Exp e1, Exp e2) { return new Exp_suma(e1, e2); }
    public Exp exp_resta(Exp e1, Exp e2) { return new Exp_resta(e1, e2); }
    public Exp exp_mul(Exp e1, Exp e2) { return new Exp_mul(e1, e2); }
    public Exp exp_div(Exp e1, Exp e2) { return new Exp_div(e1, e2); }
    public Exp exp_mod(Exp e1, Exp e2) { return new Exp_mod(e1, e2); }
    public Exp exp_and(Exp e1, Exp e2) { return new Exp_and(e1, e2); }
    public Exp exp_or(Exp e1, Exp e2) { return new Exp_or(e1, e2); }
    public Exp exp_menor(Exp e1, Exp e2) { return new Exp_menor(e1, e2); }
    public Exp exp_mayor(Exp e1, Exp e2) { return new Exp_mayor(e1, e2); }
    public Exp exp_menor_igual(Exp e1, Exp e2) { return new Exp_menor_igual(e1, e2); }
    public Exp exp_mayor_igual(Exp e1, Exp e2) { return new Exp_mayor_igual(e1, e2); }
    public Exp exp_igual(Exp e1, Exp e2) { return new Exp_igual(e1, e2); }
    public Exp exp_distinto(Exp e1, Exp e2) { return new Exp_distinto(e1, e2); }

    public Exp exp_menos_unario(Exp e) { return new Exp_menos_unario(e); }
    public Exp exp_not(Exp e) { return new Exp_not(e); }
    public Exp exp_asterisco_unario(Exp e) { return new Exp_asterisco_unario(e); }

    public Exp exp_array(Exp e, Exp i) { return new Exp_array(e, i); }
    public Exp exp_campo(Exp e, String id) { return new Exp_campo(e, id); }
    public Exp exp_flecha(Exp e, String id) { return new Exp_flecha(e, id); }

    public Exp iden(String id) { return new Iden(id); }
    public Exp lit_int(String n) { return new Lit_int(n); }
    public Exp lit_real(String n) { return new Lit_real(n); }
    public Exp lit_bool(String b) { return new Lit_bool(b); }
    public Exp lit_string(String s) { return new Lit_string(s); }
    public Exp lit_null() { return new Lit_null(); }
}