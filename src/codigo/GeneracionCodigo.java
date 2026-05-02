package codigo;

import asint.ProcesamientoDef;
import asint.SintaxisAbstractaTiny.*;
import maquinap.MaquinaP.Instr;
import maquinap.MaquinaP.Label;
import maquinap.MaquinaP.Op;
import maquinap.MaquinaP;
import semantica.AsignacionEspacio;
import semantica.InfoSemantica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneracionCodigo extends ProcesamientoDef {
    private final MaquinaP maquina;
    private final InfoSemantica info;
    private final AsignacionEspacio espacio;
    private final Map<Dec_proc, Label> labelsProcedimientos = new LinkedHashMap<>();

    public GeneracionCodigo(MaquinaP maquina, InfoSemantica info, AsignacionEspacio espacio) {
        this.maquina = maquina;
        this.info = info;
        this.espacio = espacio;
    }

    public void procesa(Prog p) {
        maquina.cargaPrograma(new ArrayList<>());
        collectProcedimientos(p.decs);
        if (p.decs != null) {
            p.decs.process(this);
        }
        if (p.instrs != null) {
            p.instrs.process(this);
        }
        maquina.addInstr(maquina.instruccion(Op.STOP));
        for (Dec_proc proc : labelsProcedimientos.keySet()) {
            Label label = labelsProcedimientos.get(proc);
            maquina.setLabelAddress(label);
            if (proc.instrs != null) {
                proc.instrs.process(this);
            }
            maquina.addInstr(maquina.instruccion(Op.DESACTIVA, espacio.nivel(proc), espacio.tamDatos(proc)));
            maquina.addInstr(maquina.instruccion(Op.IR_IND));
        }
    }

    private void collectProcedimientos(LDec_0 decs0) {
        if (decs0 instanceof Si_dec) {
            collectProcedimientos(((Si_dec) decs0).decs);
        }
    }

    private void collectProcedimientos(LDec decs) {
        if (decs instanceof Muchas_decs) {
            Muchas_decs m = (Muchas_decs) decs;
            collectProcedimientos(m.decs);
            collectProcedimientos(m.dec);
        } else if (decs instanceof Una_dec) {
            collectProcedimientos(((Una_dec) decs).dec);
        }
    }

    private void collectProcedimientos(Dec dec) {
        if (dec instanceof Dec_proc) {
            Dec_proc proc = (Dec_proc) dec;
            labelsProcedimientos.put(proc, new Label());
            if (proc.decs != null) {
                collectProcedimientos(proc.decs);
            }
        }
    }

    @Override
    public void process(Si_dec ld) {
        if (ld.decs != null) {
            ld.decs.process(this);
        }
    }

    @Override
    public void process(Muchas_decs ld) {
        if (ld.decs != null) {
            ld.decs.process(this);
        }
        if (ld.dec != null) {
            ld.dec.process(this);
        }
    }

    @Override
    public void process(Una_dec ld) {
        if (ld.dec != null) {
            ld.dec.process(this);
        }
    }

    @Override
    public void process(Dec_proc d) {
        // Declarations do not generate code at this point.
    }

    @Override
    public void process(Si_procparam ld) {
        // No code generation for parameter declarations.
    }

    @Override
    public void process(Muchos_procparam ld) {
        // No code generation for parameter declarations.
    }

    @Override
    public void process(Un_procparam ld) {
        // No code generation for parameter declarations.
    }

    @Override
    public void process(Param_ref p) {
        // No code generation for parameter declarations.
    }

    @Override
    public void process(Param_val p) {
        // No code generation for parameter declarations.
    }

    @Override
    public void process(Si_instr ld) {
        if (ld.instrs != null) {
            ld.instrs.process(this);
        }
    }

    @Override
    public void process(Muchas_instr ld) {
        if (ld.instrs != null) {
            ld.instrs.process(this);
        }
        if (ld.instr != null) {
            ld.instr.process(this);
        }
    }

    @Override
    public void process(Una_instr ld) {
        if (ld.instr != null) {
            ld.instr.process(this);
        }
    }

    @Override
    public void process(Instr_asig i) {
        TipoValue t1 = tipoExp(i.exp1);
        genCod(i.exp1, true);
        genCod(i.exp2, false);
        if (esEstructurado(t1)) {
            maquina.addInstr(maquina.instruccion(Op.COPIA, tamanioTipo(t1.tipo)));
        } else {
            maquina.addInstr(maquina.instruccion(Op.DESAPILA_IND));
        }
    }

    @Override
    public void process(Instr_if i) {
        genCod(i.exp, false);
        Label falseLabel = new Label();
        Label endLabel = new Label();
        maquina.addInstr(maquina.instruccion(Op.IR_F, falseLabel));
        if (i.instrs != null) {
            i.instrs.process(this);
        }
        maquina.addInstr(maquina.instruccion(Op.IR_A, endLabel));
        maquina.setLabelAddress(falseLabel);
        maquina.setLabelAddress(endLabel);
    }

    @Override
    public void process(Instr_ifelse i) {
        genCod(i.exp, false);
        Label elseLabel = new Label();
        Label endLabel = new Label();
        maquina.addInstr(maquina.instruccion(Op.IR_F, elseLabel));
        if (i.instrs1 != null) {
            i.instrs1.process(this);
        }
        maquina.addInstr(maquina.instruccion(Op.IR_A, endLabel));
        maquina.setLabelAddress(elseLabel);
        if (i.instrs2 != null) {
            i.instrs2.process(this);
        }
        maquina.setLabelAddress(endLabel);
    }

    @Override
    public void process(Instr_while i) {
        Label begin = new Label();
        Label end = new Label();
        maquina.setLabelAddress(begin);
        genCod(i.exp, false);
        maquina.addInstr(maquina.instruccion(Op.IR_F, end));
        if (i.instrs != null) {
            i.instrs.process(this);
        }
        maquina.addInstr(maquina.instruccion(Op.IR_A, begin));
        maquina.setLabelAddress(end);
    }

    @Override
    public void process(Instr_lectura i) {
        TipoValue t = tipoExp(i.exp);
        genCod(i.exp, true);
        if (t.kind == Kind.INT) {
            maquina.addInstr(maquina.instruccion(Op.READ_INT));
        } else if (t.kind == Kind.REAL) {
            maquina.addInstr(maquina.instruccion(Op.READ_REAL));
        } else if (t.kind == Kind.STRING) {
            maquina.addInstr(maquina.instruccion(Op.READ_STRING));
        } else if (t.kind == Kind.BOOL) {
            maquina.addInstr(maquina.instruccion(Op.READ_STRING));
        } else {
            maquina.addInstr(maquina.instruccion(Op.READ_STRING));
        }
        maquina.addInstr(maquina.instruccion(Op.DESAPILA_IND));
    }

    @Override
    public void process(Instr_escritura i) {
        genCod(i.exp, false);
        maquina.addInstr(maquina.instruccion(Op.PRINT));
    }

    @Override
    public void process(Instr_reserva i) {
        TipoValue t = tipoExp(i.exp);
        genCod(i.exp, true);
        int tam = tamanioTipo(desreferencia(t.tipo).tipo);
        maquina.addInstr(maquina.instruccion(Op.ALLOC, tam));
        maquina.addInstr(maquina.instruccion(Op.DESAPILA_IND));
    }

    @Override
    public void process(Instr_liberacion i) {
        TipoValue t = tipoExp(i.exp);
        genCod(i.exp, false);
        int tam = tamanioTipo(desreferencia(t.tipo).tipo);
        maquina.addInstr(maquina.instruccion(Op.DEALLOC, tam));
    }

    @Override
    public void process(Instr_invocar i) {
        Nodo vinculacion = info.vinculoDe(i);
        if (!(vinculacion instanceof Dec_proc)) {
            return;
        }
        Dec_proc proc = (Dec_proc) vinculacion;
        Label label = labelsProcedimientos.get(proc);
        if (label == null) {
            label = new Label();
            labelsProcedimientos.put(proc, label);
        }
        int nivel = espacio.nivel(proc);
        int tam = espacio.tamDatos(proc);
        Label returnLabel = new Label();
        maquina.addInstr(maquina.instruccion(Op.ACTIVA, nivel, tam, returnLabel));
        generaPasoParametros(proc.params, i.exps);
        maquina.addInstr(maquina.instruccion(Op.FIJAD, nivel));
        maquina.addInstr(maquina.instruccion(Op.IR_A, label));
        maquina.setLabelAddress(returnLabel);
    }

    @Override
    public void process(Instr_compuesta i) {
        if (i.decs != null) {
            // no runtime effect for declarations
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Exp_suma e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_resta e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_mul e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_div e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_mod e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_and e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_or e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_mayor e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_menor e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_mayor_igual e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_menor_igual e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_igual e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_distinto e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_menos_unario e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_not e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_asterisco_unario e) {
        genCod(e, false);
    }

    @Override
    public void process(Iden e) {
        genCod(e, false);
    }

    @Override
    public void process(Lit_int e) {
        genCod(e, false);
    }

    @Override
    public void process(Lit_real e) {
        genCod(e, false);
    }

    @Override
    public void process(Lit_bool e) {
        genCod(e, false);
    }

    @Override
    public void process(Lit_string e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_null e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_campo e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_flecha e) {
        genCod(e, false);
    }

    @Override
    public void process(Exp_array e) {
        genCod(e, false);
    }

    private void genCod(Exp exp, boolean wantAddress) {
        if (wantAddress) {
            genCodAddress(exp);
            return;
        }
        if (esDesignador(exp)) {
            genCodAddress(exp);
            if (!esEstructurado(tipoExp(exp))) {
                maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            }
            return;
        }
        if (exp instanceof Lit_int) {
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, Integer.parseInt(((Lit_int) exp).val)));
            return;
        }
        if (exp instanceof Lit_real) {
            maquina.addInstr(maquina.instruccion(Op.APILA_REAL, Double.parseDouble(((Lit_real) exp).val)));
            return;
        }
        if (exp instanceof Lit_bool) {
            String val = ((Lit_bool) exp).val;
            boolean b = val.equals("<true>") || val.equals("true");
            maquina.addInstr(maquina.instruccion(Op.APILA_BOOL, b));
            return;
        }
        if (exp instanceof Lit_string) {
            maquina.addInstr(maquina.instruccion(Op.APILA_STRING, ((Lit_string) exp).val));
            return;
        }
        if (exp instanceof Exp_null) {
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, -1));
            return;
        }
        if (exp instanceof Exp_suma) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_resta) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.RESTA));
            return;
        }
        if (exp instanceof Exp_mul) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MUL));
            return;
        }
        if (exp instanceof Exp_div) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.DIV));
            return;
        }
        if (exp instanceof Exp_mod) {
            Exp_mod e = (Exp_mod) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MOD));
            return;
        }
        if (exp instanceof Exp_and) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.AND));
            return;
        }
        if (exp instanceof Exp_or) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.OR));
            return;
        }
        if (exp instanceof Exp_menor) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MENOR));
            return;
        }
        if (exp instanceof Exp_mayor) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MAYOR));
            return;
        }
        if (exp instanceof Exp_menor_igual) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MENOR_IGUAL));
            return;
        }
        if (exp instanceof Exp_mayor_igual) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.MAYOR_IGUAL));
            return;
        }
        if (exp instanceof Exp_igual) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.IGUAL));
            return;
        }
        if (exp instanceof Exp_distinto) {
            ExpBin e = (ExpBin) exp;
            genCod(e.opnd0, false);
            genCod(e.opnd1, false);
            maquina.addInstr(maquina.instruccion(Op.DISTINTO));
            return;
        }
        if (exp instanceof Exp_menos_unario) {
            ExpUni e = (ExpUni) exp;
            genCod(e.opnd, false);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, 0));
            maquina.addInstr(maquina.instruccion(Op.RESTA));
            return;
        }
        if (exp instanceof Exp_not) {
            ExpUni e = (ExpUni) exp;
            genCod(e.opnd, false);
            maquina.addInstr(maquina.instruccion(Op.NOT));
            return;
        }
        if (exp instanceof Exp_asterisco_unario) {
            ExpUni e = (ExpUni) exp;
            genCod(e.opnd, true);
            maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            return;
        }
        if (exp instanceof Exp_array) {
            Exp_array a = (Exp_array) exp;
            genCod(a.opnd0, true);
            genCod(a.opnd1, false);
            int elemSize = tamanioTipo(resuelveTipo(tipoDeTipo(((Tipo_array) tipoExp(a.opnd0).tipo).tipo)).tipo);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, elemSize));
            maquina.addInstr(maquina.instruccion(Op.MUL));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_campo) {
            Exp_campo c = (Exp_campo) exp;
            genCod(c.base, true);
            int offset = desplazamientoCampo(resuelveTipo(tipoExp(c.base)).tipo, c.id);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, offset));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_flecha) {
            Exp_flecha c = (Exp_flecha) exp;
            genCod(c.base, true);
            maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            TipoValue baseType = resuelveTipo(tipoExp(c.base));
            TipoValue pointedType = desreferencia(baseType.tipo);
            int offset = desplazamientoCampo(resuelveTipo(pointedType).tipo, c.id);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, offset));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Iden) {
            genCodAddress(exp);
            maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            return;
        }
    }

    private void genCodAddress(Exp exp) {
        if (exp instanceof Iden) {
            Iden id = (Iden) exp;
            Nodo dec = info.vinculoDe(id);
            if (dec instanceof Dec_var) {
                Dec_var d = (Dec_var) dec;
                if (espacio.nivel(d) == 0) {
                    maquina.addInstr(maquina.instruccion(Op.APILA_INT, espacio.dir(d)));
                } else {
                    maquina.addInstr(maquina.instruccion(Op.APILAD, espacio.nivel(d), espacio.dir(d)));
                }
                return;
            }
            if (dec instanceof Param_val) {
                Param_val p = (Param_val) dec;
                maquina.addInstr(maquina.instruccion(Op.APILAD, espacio.nivel(p), espacio.dir(p)));
                return;
            }
            if (dec instanceof Param_ref) {
                Param_ref p = (Param_ref) dec;
                maquina.addInstr(maquina.instruccion(Op.APILAD, espacio.nivel(p), espacio.dir(p)));
                maquina.addInstr(maquina.instruccion(Op.APILA_IND));
                return;
            }
        }
        if (exp instanceof Exp_array) {
            Exp_array a = (Exp_array) exp;
            genCod(a.opnd0, true);
            genCod(a.opnd1, false);
            int elemSize = tamanioTipo(resuelveTipo(tipoDeTipo(((Tipo_array) tipoExp(a.opnd0).tipo).tipo)).tipo);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, elemSize));
            maquina.addInstr(maquina.instruccion(Op.MUL));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_campo) {
            Exp_campo c = (Exp_campo) exp;
            genCod(c.base, true);
            int offset = desplazamientoCampo(resuelveTipo(tipoExp(c.base)).tipo, c.id);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, offset));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_flecha) {
            Exp_flecha c = (Exp_flecha) exp;
            genCod(c.base, true);
            maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            TipoValue baseType = resuelveTipo(tipoExp(c.base));
            TipoValue pointedType = desreferencia(baseType.tipo);
            int offset = desplazamientoCampo(resuelveTipo(pointedType).tipo, c.id);
            maquina.addInstr(maquina.instruccion(Op.APILA_INT, offset));
            maquina.addInstr(maquina.instruccion(Op.SUMA));
            return;
        }
        if (exp instanceof Exp_asterisco_unario) {
            Exp_asterisco_unario e = (Exp_asterisco_unario) exp;
            genCod(e.opnd, true);
            maquina.addInstr(maquina.instruccion(Op.APILA_IND));
            return;
        }
        throw new RuntimeException(
                "No se puede generar direccion para la expresion: " + exp.getClass().getSimpleName());
    }

    private void generaPasoParametros(LProcParams_0 formales, LExps_0 reales) {
        List<Param> listaFormales = new ArrayList<>();
        if (formales instanceof Si_procparam) {
            extraeFormales(((Si_procparam) formales).params, listaFormales);
        }
        List<Exp> listaReales = new ArrayList<>();
        if (reales instanceof Si_exps) {
            extraeReales(((Si_exps) reales).exps, listaReales);
        }
        for (int i = 0; i < listaFormales.size(); i++) {
            Param param = listaFormales.get(i);
            Exp actual = listaReales.get(i);
            genCod(param, actual);
        }
    }

    private void extraeFormales(LProcParams params, List<Param> list) {
        if (params instanceof Muchos_procparam) {
            Muchos_procparam m = (Muchos_procparam) params;
            extraeFormales(m.params, list);
            list.add(m.param);
        } else if (params instanceof Un_procparam) {
            list.add(((Un_procparam) params).param);
        }
    }

    private void extraeReales(LExps expList, List<Exp> list) {
        if (expList instanceof Muchas_exps) {
            Muchas_exps m = (Muchas_exps) expList;
            extraeReales(m.exps, list);
            list.add(m.exp);
        } else if (expList instanceof Una_exp) {
            list.add(((Una_exp) expList).exp);
        }
    }

    private void genCod(Param formal, Exp actual) {
        int dir = espacio.dir(formal);
        maquina.addInstr(maquina.instruccion(Op.DUP));
        maquina.addInstr(maquina.instruccion(Op.APILA_INT, dir));
        maquina.addInstr(maquina.instruccion(Op.SUMA));
        if (formal instanceof Param_ref) {
            genCod(actual, true);
            maquina.addInstr(maquina.instruccion(Op.DESAPILA_IND));
            return;
        }
        TipoValue tv = tipoExp(actual);
        if (esEstructurado(tv)) {
            genCod(actual, true);
            maquina.addInstr(maquina.instruccion(Op.COPIA, tamanioTipo(tv.tipo)));
            return;
        }
        genCod(actual, false);
        maquina.addInstr(maquina.instruccion(Op.DESAPILA_IND));
    }

    private boolean esDesignador(Exp exp) {
        return exp instanceof Iden || exp instanceof Exp_array || exp instanceof Exp_campo
                || exp instanceof Exp_flecha || exp instanceof Exp_asterisco_unario;
    }

    private boolean esEstructurado(TipoValue tipo) {
        return tipo.kind == Kind.ARRAY || tipo.kind == Kind.RECORD;
    }

    private TipoValue tipoExp(Exp e) {
        if (e instanceof Lit_int) {
            return new TipoValue(Kind.INT, new Tipo_int());
        }
        if (e instanceof Lit_real) {
            return new TipoValue(Kind.REAL, new Tipo_real());
        }
        if (e instanceof Lit_bool) {
            return new TipoValue(Kind.BOOL, new Tipo_bool());
        }
        if (e instanceof Lit_string) {
            return new TipoValue(Kind.STRING, new Tipo_string());
        }
        if (e instanceof Exp_null) {
            return new TipoValue(Kind.NULL, null);
        }
        if (e instanceof Iden) {
            Nodo d = info.vinculoDe((Iden) e);
            if (d instanceof Dec_var) {
                return tipoDeTipo(((Dec_var) d).tipo);
            }
            if (d instanceof Param_ref) {
                return tipoDeTipo(((Param_ref) d).tipo);
            }
            if (d instanceof Param_val) {
                return tipoDeTipo(((Param_val) d).tipo);
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_suma || e instanceof Exp_resta || e instanceof Exp_mul || e instanceof Exp_div) {
            ExpBin b = (ExpBin) e;
            TipoValue t0 = tipoExp(b.opnd0);
            TipoValue t1 = tipoExp(b.opnd1);
            if (esNumerico(t0.kind) && esNumerico(t1.kind)) {
                if (t0.kind == Kind.REAL || t1.kind == Kind.REAL) {
                    return new TipoValue(Kind.REAL, new Tipo_real());
                }
                return new TipoValue(Kind.INT, new Tipo_int());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_mod) {
            Exp_mod b = (Exp_mod) e;
            TipoValue t0 = tipoExp(b.opnd0);
            TipoValue t1 = tipoExp(b.opnd1);
            if (t0.kind == Kind.INT && t1.kind == Kind.INT) {
                return new TipoValue(Kind.INT, new Tipo_int());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_and || e instanceof Exp_or) {
            ExpBin b = (ExpBin) e;
            TipoValue t0 = tipoExp(b.opnd0);
            TipoValue t1 = tipoExp(b.opnd1);
            if (t0.kind == Kind.BOOL && t1.kind == Kind.BOOL) {
                return new TipoValue(Kind.BOOL, new Tipo_bool());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_menor || e instanceof Exp_mayor || e instanceof Exp_menor_igual
                || e instanceof Exp_mayor_igual) {
            ExpBin b = (ExpBin) e;
            TipoValue t0 = tipoExp(b.opnd0);
            TipoValue t1 = tipoExp(b.opnd1);
            if ((esNumerico(t0.kind) && esNumerico(t1.kind)) || (t0.kind == Kind.STRING && t1.kind == Kind.STRING)
                    || (t0.kind == Kind.BOOL && t1.kind == Kind.BOOL)) {
                return new TipoValue(Kind.BOOL, new Tipo_bool());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_igual || e instanceof Exp_distinto) {
            ExpBin b = (ExpBin) e;
            TipoValue t0 = tipoExp(b.opnd0);
            TipoValue t1 = tipoExp(b.opnd1);
            if (compatiblesAsignacion(t0, t1) || compatiblesAsignacion(t1, t0)) {
                return new TipoValue(Kind.BOOL, new Tipo_bool());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_menos_unario) {
            TipoValue t = tipoExp(((Exp_menos_unario) e).opnd);
            if (esNumerico(t.kind)) {
                return t;
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_not) {
            TipoValue t = tipoExp(((Exp_not) e).opnd);
            if (t.kind == Kind.BOOL) {
                return new TipoValue(Kind.BOOL, new Tipo_bool());
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_asterisco_unario) {
            TipoValue t = tipoExp(((Exp_asterisco_unario) e).opnd);
            if (t.kind == Kind.POINTER) {
                return desreferencia(t.tipo);
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_array) {
            Exp_array a = (Exp_array) e;
            TipoValue b = tipoExp(a.opnd0);
            if (b.kind == Kind.ARRAY) {
                return tipoDeTipo(((Tipo_array) b.tipo).tipo);
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_campo) {
            Exp_campo c = (Exp_campo) e;
            TipoValue b = tipoExp(c.base);
            if (b.kind == Kind.RECORD) {
                CamposRecord campo = buscaCampo(((Tipo_record) b.tipo).lista, c.id);
                if (campo != null) {
                    return tipoDeTipo(campo.tipo);
                }
            }
            return new TipoValue(Kind.ERROR, null);
        }
        if (e instanceof Exp_flecha) {
            Exp_flecha c = (Exp_flecha) e;
            TipoValue b = tipoExp(c.base);
            if (b.kind == Kind.POINTER) {
                Tipo apuntado = ((Tipo_pointer) b.tipo).tipo;
                TipoValue ta = tipoDeTipo(apuntado);
                if (ta.kind == Kind.RECORD) {
                    CamposRecord campo = buscaCampo(((Tipo_record) ta.tipo).lista, c.id);
                    if (campo != null) {
                        return tipoDeTipo(campo.tipo);
                    }
                }
            }
            return new TipoValue(Kind.ERROR, null);
        }
        return new TipoValue(Kind.ERROR, null);
    }

    private TipoValue tipoDeTipo(Tipo t) {
        if (t instanceof Tipo_int) {
            return new TipoValue(Kind.INT, t);
        }
        if (t instanceof Tipo_real) {
            return new TipoValue(Kind.REAL, t);
        }
        if (t instanceof Tipo_bool) {
            return new TipoValue(Kind.BOOL, t);
        }
        if (t instanceof Tipo_string) {
            return new TipoValue(Kind.STRING, t);
        }
        if (t instanceof Tipo_array) {
            return new TipoValue(Kind.ARRAY, t);
        }
        if (t instanceof Tipo_pointer) {
            return new TipoValue(Kind.POINTER, t);
        }
        if (t instanceof Tipo_record) {
            return new TipoValue(Kind.RECORD, t);
        }
        if (t instanceof Tipo_id) {
            Nodo d = info.vinculoDe((Tipo_id) t);
            if (d instanceof Dec_tipo) {
                return tipoDeTipo(((Dec_tipo) d).tipo);
            }
            return new TipoValue(Kind.ERROR, null);
        }
        return new TipoValue(Kind.ERROR, null);
    }

    private TipoValue resuelveTipo(TipoValue tv) {
        if (tv.tipo instanceof Tipo_id) {
            return tipoDeTipo(((Tipo_id) tv.tipo));
        }
        return tv;
    }

    private TipoValue desreferencia(Tipo t) {
        TipoValue tv = tipoDeTipo(t);
        if (tv.kind == Kind.POINTER) {
            return tipoDeTipo(((Tipo_pointer) tv.tipo).tipo);
        }
        return new TipoValue(Kind.ERROR, null);
    }

    private int tamanioTipo(Tipo t) {
        if (t instanceof Tipo_int || t instanceof Tipo_real || t instanceof Tipo_bool || t instanceof Tipo_string) {
            return 1;
        }
        if (t instanceof Tipo_pointer) {
            return 1;
        }
        if (t instanceof Tipo_array) {
            int dim = 0;
            try {
                dim = Integer.parseInt(((Tipo_array) t).dim);
            } catch (NumberFormatException ignored) {
            }
            return dim * tamanioTipo(((Tipo_array) t).tipo);
        }
        if (t instanceof Tipo_record) {
            return tamanioLista(((Tipo_record) t).lista);
        }
        if (t instanceof Tipo_id) {
            Nodo d = info.vinculoDe((Tipo_id) t);
            if (d instanceof Dec_tipo) {
                return tamanioTipo(((Dec_tipo) d).tipo);
            }
        }
        return 1;
    }

    private int tamanioLista(ListaRecord lista) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            return tamanioLista(m.lista) + tamanioTipo(m.campo.tipo);
        }
        if (lista instanceof Un_camporecord) {
            return tamanioTipo(((Un_camporecord) lista).campo.tipo);
        }
        return 0;
    }

    private int desplazamientoCampo(Tipo tipo, String id) {
        ListaRecord lista = null;
        if (tipo instanceof Tipo_record) {
            lista = ((Tipo_record) tipo).lista;
        } else if (tipo instanceof Tipo_id) {
            Nodo d = info.vinculoDe((Tipo_id) tipo);
            if (d instanceof Dec_tipo) {
                Tipo declarado = ((Dec_tipo) d).tipo;
                if (declarado instanceof Tipo_record) {
                    lista = ((Tipo_record) declarado).lista;
                }
            }
        }
        if (lista == null) {
            return 0;
        }
        return desplazamientoCampoLista(lista, id);
    }

    private int desplazamientoCampoLista(ListaRecord lista, String id) {
        int offset = 0;
        for (CamposRecord campo : camposDeRecord(lista)) {
            if (campo.id.equals(id)) {
                return offset;
            }
            offset += tamanioTipo(campo.tipo);
        }
        return -1;
    }

    private boolean esNumerico(Kind k) {
        return k == Kind.INT || k == Kind.REAL;
    }

    private boolean compatiblesAsignacion(TipoValue destino, TipoValue origen) {
        if (destino.kind == Kind.ERROR || origen.kind == Kind.ERROR) {
            return true;
        }
        if (destino.kind == origen.kind) {
            if (destino.kind == Kind.ARRAY) {
                Tipo_array td = (Tipo_array) destino.tipo;
                Tipo_array to = (Tipo_array) origen.tipo;
                return td.dim.equals(to.dim) && compatiblesAsignacion(tipoDeTipo(td.tipo), tipoDeTipo(to.tipo));
            }
            if (destino.kind == Kind.RECORD) {
                return compatiblesRecord((Tipo_record) destino.tipo, (Tipo_record) origen.tipo);
            }
            if (destino.kind == Kind.POINTER) {
                Tipo_pointer pd = (Tipo_pointer) destino.tipo;
                Tipo_pointer po = (Tipo_pointer) origen.tipo;
                return compatiblesAsignacion(tipoDeTipo(pd.tipo), tipoDeTipo(po.tipo));
            }
            return true;
        }
        if (destino.kind == Kind.REAL && origen.kind == Kind.INT) {
            return true;
        }
        if (destino.kind == Kind.POINTER && origen.kind == Kind.NULL) {
            return true;
        }
        return false;
    }

    private boolean compatiblesRecord(Tipo_record t1, Tipo_record t2) {
        List<CamposRecord> c1 = camposDeRecord(t1.lista);
        List<CamposRecord> c2 = camposDeRecord(t2.lista);
        if (c1.size() != c2.size()) {
            return false;
        }
        for (int i = 0; i < c1.size(); i++) {
            if (!c1.get(i).id.equals(c2.get(i).id)) {
                return false;
            }
            if (!compatiblesAsignacion(tipoDeTipo(c1.get(i).tipo), tipoDeTipo(c2.get(i).tipo))) {
                return false;
            }
        }
        return true;
    }

    private List<CamposRecord> camposDeRecord(ListaRecord lista) {
        List<CamposRecord> out = new ArrayList<>();
        camposDeRecordRec(lista, out);
        return out;
    }

    private void camposDeRecordRec(ListaRecord lista, List<CamposRecord> out) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            camposDeRecordRec(m.lista, out);
            out.add(m.campo);
        } else if (lista instanceof Un_camporecord) {
            out.add(((Un_camporecord) lista).campo);
        }
    }

    private CamposRecord buscaCampo(ListaRecord lista, String id) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            CamposRecord r = buscaCampo(m.lista, id);
            if (r != null) {
                return r;
            }
            if (m.campo.id.equals(id)) {
                return m.campo;
            }
            return null;
        }
        if (lista instanceof Un_camporecord) {
            CamposRecord c = ((Un_camporecord) lista).campo;
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }

    private enum Kind {
        INT,
        REAL,
        BOOL,
        STRING,
        NULL,
        ARRAY,
        POINTER,
        RECORD,
        ERROR
    }

    private static class TipoValue {
        final Kind kind;
        final Tipo tipo;

        TipoValue(Kind kind, Tipo tipo) {
            this.kind = kind;
            this.tipo = tipo;
        }
    }
}
