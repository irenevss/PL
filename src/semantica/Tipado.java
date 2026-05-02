package semantica;

import asint.ProcesamientoDef;
import asint.SintaxisAbstractaTiny.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Tipado extends ProcesamientoDef {
    private final ErroresSemanticos errores;
    private final InfoSemantica info;

    private enum Kind {
        INT,
        REAL,
        BOOL,
        STRING,
        NULL,
        ARRAY,
        POINTER,
        RECORD,
        PROC,
        ERROR
    }

    private static class TVal {
        final Kind kind;
        final Tipo origen;

        TVal(Kind kind, Tipo origen) {
            this.kind = kind;
            this.origen = origen;
        }
    }

    private static class TExp {
        final TVal tipo;
        final boolean designador;

        TExp(TVal tipo, boolean designador) {
            this.tipo = tipo;
            this.designador = designador;
        }
    }

    private static class TipoPair {
        final Tipo destino;
        final Tipo origen;

        TipoPair(Tipo destino, Tipo origen) {
            this.destino = destino;
            this.origen = origen;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TipoPair)) {
                return false;
            }
            TipoPair that = (TipoPair) o;
            return destino == that.destino && origen == that.origen;
        }

        @Override
        public int hashCode() {
            return Objects.hash(System.identityHashCode(destino), System.identityHashCode(origen));
        }
    }

    public Tipado(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        p.process(this);
    }

    @Override
    public void process(Prog p) {
        if (p.decs != null) {
            p.decs.process(this);
        }
        if (p.instrs != null) {
            p.instrs.process(this);
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
        if (d.decs != null) {
            d.decs.process(this);
        }
        if (d.instrs != null) {
            d.instrs.process(this);
        }
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
        TExp t1 = tipoExp(i.exp1);
        TExp t2 = tipoExp(i.exp2);
        if (!t1.designador) {
            errores.error(i, "la parte izquierda debe ser un designador");
        }
        if (!compatiblesAsignacion(t1.tipo, t2.tipo)) {
            errores.error(i, "tipos incompatibles en asignacion");
        }
    }

    @Override
    public void process(Instr_if i) {
        TExp c = tipoExp(i.exp);
        if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "esperada expresion booleana");
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_ifelse i) {
        TExp c = tipoExp(i.exp);
        if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "esperada expresion booleana");
        }
        if (i.instrs1 != null) {
            i.instrs1.process(this);
        }
        if (i.instrs2 != null) {
            i.instrs2.process(this);
        }
    }

    @Override
    public void process(Instr_while i) {
        TExp c = tipoExp(i.exp);
        if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "esperada expresion booleana");
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_lectura i) {
        TExp t = tipoExp(i.exp);
        if (!t.designador) {
            errores.error(i.exp, "designador esperado");
        }
        if (!legible(t.tipo)) {
            errores.error(i.exp, "valor no legible");
        }
    }

    @Override
    public void process(Instr_escritura i) {
        TExp t = tipoExp(i.exp);
        if (!imprimible(t.tipo) && t.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "valor no imprimible");
        }
    }

    @Override
    public void process(Instr_reserva i) {
        TExp t = tipoExp(i.exp);
        if (!t.designador) {
            errores.error(i.exp, "designador esperado");
        }
        if (t.tipo.kind != Kind.POINTER && t.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "esperado tipo puntero");
        }
    }

    @Override
    public void process(Instr_liberacion i) {
        TExp t = tipoExp(i.exp);
        if (t.tipo.kind != Kind.POINTER && t.tipo.kind != Kind.ERROR) {
            errores.error(i.exp, "esperado tipo puntero");
        }
    }

    @Override
    public void process(Instr_invocar i) {
        tipaInvocacion(i);
    }

    @Override
    public void process(Instr_compuesta i) {
        if (i.decs != null) {
            i.decs.process(this);
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    private void tipaInvocacion(Instr_invocar inv) {
        Nodo d = info.vinculoDe(inv);
        if (!(d instanceof Dec_proc)) {
            errores.error(inv, inv.id + " no es un subprograma");
            List<Exp> reales = extraeExps(inv.exps);
            for (Exp real : reales) {
                tipoExp(real);
            }
            return;
        }

        Dec_proc proc = (Dec_proc) d;
        List<Param> formales = extraeParams(proc.params);
        List<Exp> reales = extraeExps(inv.exps);

        if (formales.size() != reales.size()) {
            errores.error(inv, "el numero de parametros reales no coincide con el numero de parametros formales");
            return;
        }

        for (int idx = 0; idx < formales.size(); idx++) {
            Param formal = formales.get(idx);
            Exp real = reales.get(idx);
            TExp tr = tipoExp(real);
            TVal tf = tipoDeParam(formal);

            if (formal instanceof Param_ref) {
                if (!tr.designador) {
                    errores.error(real, "designador esperado");
                    continue;
                }
                if (!compatiblesAsignacion(tf, tr.tipo) || !compatiblesAsignacion(tr.tipo, tf)) {
                    errores.error(real, "tipo incompatible con tipo de parametro formal");
                }
                continue;
            }
            if (!compatiblesAsignacion(tf, tr.tipo)) {
                errores.error(real, "tipo incompatible con tipo de parametro formal");
            }
        }
    }

    private List<Param> extraeParams(LProcParams_0 params0) {
        List<Param> r = new ArrayList<>();
        if (params0 instanceof Si_procparam) {
            extraeParams(((Si_procparam) params0).params, r);
        }
        return r;
    }

    private void extraeParams(LProcParams ps, List<Param> out) {
        if (ps instanceof Muchos_procparam) {
            Muchos_procparam m = (Muchos_procparam) ps;
            extraeParams(m.params, out);
            out.add(m.param);
        } else if (ps instanceof Un_procparam) {
            out.add(((Un_procparam) ps).param);
        }
    }

    private List<Exp> extraeExps(LExps_0 exps0) {
        List<Exp> r = new ArrayList<>();
        if (exps0 instanceof Si_exps) {
            extraeExps(((Si_exps) exps0).exps, r);
        }
        return r;
    }

    private void extraeExps(LExps exps, List<Exp> out) {
        if (exps instanceof Muchas_exps) {
            Muchas_exps m = (Muchas_exps) exps;
            extraeExps(m.exps, out);
            out.add(m.exp);
        } else if (exps instanceof Una_exp) {
            out.add(((Una_exp) exps).exp);
        }
    }

    private TVal tipoDeParam(Param p) {
        if (p instanceof Param_ref) {
            return tipoDeTipo(((Param_ref) p).tipo);
        }
        return tipoDeTipo(((Param_val) p).tipo);
    }

    private TExp tipoExp(Exp e) {
        if (e instanceof Lit_int) {
            return new TExp(new TVal(Kind.INT, null), false);
        }
        if (e instanceof Lit_real) {
            return new TExp(new TVal(Kind.REAL, null), false);
        }
        if (e instanceof Lit_bool) {
            return new TExp(new TVal(Kind.BOOL, null), false);
        }
        if (e instanceof Lit_string) {
            return new TExp(new TVal(Kind.STRING, null), false);
        }
        if (e instanceof Exp_null) {
            return new TExp(new TVal(Kind.NULL, null), false);
        }
        if (e instanceof Iden) {
            return tipoIden((Iden) e);
        }
        if (e instanceof Exp_suma || e instanceof Exp_resta || e instanceof Exp_mul || e instanceof Exp_div) {
            ExpBin b = (ExpBin) e;
            TExp t0 = tipoExp(b.opnd0);
            TExp t1 = tipoExp(b.opnd1);
            if (esNumerico(t0.tipo.kind) && esNumerico(t1.tipo.kind)) {
                if (t0.tipo.kind == Kind.REAL || t1.tipo.kind == Kind.REAL) {
                    return new TExp(new TVal(Kind.REAL, null), false);
                }
                return new TExp(new TVal(Kind.INT, null), false);
            }
            if (t0.tipo.kind != Kind.ERROR && t1.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_mod) {
            Exp_mod m = (Exp_mod) e;
            TExp t0 = tipoExp(m.opnd0);
            TExp t1 = tipoExp(m.opnd1);
            if (t0.tipo.kind == Kind.INT && t1.tipo.kind == Kind.INT) {
                return new TExp(new TVal(Kind.INT, null), false);
            }
            if (t0.tipo.kind != Kind.ERROR && t1.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_and || e instanceof Exp_or) {
            ExpBin b = (ExpBin) e;
            TExp t0 = tipoExp(b.opnd0);
            TExp t1 = tipoExp(b.opnd1);
            if (t0.tipo.kind == Kind.BOOL && t1.tipo.kind == Kind.BOOL) {
                return new TExp(new TVal(Kind.BOOL, null), false);
            }
            if (t0.tipo.kind != Kind.ERROR && t1.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_mayor || e instanceof Exp_menor || e instanceof Exp_mayor_igual
                || e instanceof Exp_menor_igual) {
            ExpBin b = (ExpBin) e;
            TExp t0 = tipoExp(b.opnd0);
            TExp t1 = tipoExp(b.opnd1);
            if ((esNumerico(t0.tipo.kind) && esNumerico(t1.tipo.kind))
                    || (t0.tipo.kind == Kind.STRING && t1.tipo.kind == Kind.STRING)) {
                return new TExp(new TVal(Kind.BOOL, null), false);
            }
            if (t0.tipo.kind != Kind.ERROR && t1.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_igual || e instanceof Exp_distinto) {
            ExpBin b = (ExpBin) e;
            TExp t0 = tipoExp(b.opnd0);
            TExp t1 = tipoExp(b.opnd1);
            if (compatiblesAsignacion(t0.tipo, t1.tipo) || compatiblesAsignacion(t1.tipo, t0.tipo)
                    || (t0.tipo.kind == Kind.POINTER && t1.tipo.kind == Kind.POINTER)
                    || (t0.tipo.kind == Kind.POINTER && t1.tipo.kind == Kind.NULL)
                    || (t0.tipo.kind == Kind.NULL && t1.tipo.kind == Kind.POINTER)) {
                return new TExp(new TVal(Kind.BOOL, null), false);
            }
            if (t0.tipo.kind != Kind.ERROR && t1.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_menos_unario) {
            TExp t = tipoExp(((Exp_menos_unario) e).opnd);
            if (esNumerico(t.tipo.kind)) {
                return new TExp(t.tipo, false);
            }
            if (t.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipo incompatible en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_not) {
            TExp t = tipoExp(((Exp_not) e).opnd);
            if (t.tipo.kind == Kind.BOOL) {
                return new TExp(new TVal(Kind.BOOL, null), false);
            }
            if (t.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipo incompatible en operacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_asterisco_unario) {
            TExp t = tipoExp(((Exp_asterisco_unario) e).opnd);
            if (t.tipo.kind == Kind.POINTER) {
                TVal base = tipoDeTipo(((Tipo_pointer) t.tipo.origen).tipo);
                return new TExp(base, true);
            }
            if (t.tipo.kind != Kind.ERROR) {
                errores.error(e, "esperado tipo puntero");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_array) {
            Exp_array a = (Exp_array) e;
            TExp b = tipoExp(a.opnd0);
            TExp idx = tipoExp(a.opnd1);
            if (idx.tipo.kind != Kind.INT && idx.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en indexacion");
            }
            if (b.tipo.kind == Kind.ARRAY) {
                Tipo_array ta = (Tipo_array) b.tipo.origen;
                return new TExp(tipoDeTipo(ta.tipo), true);
            }
            if (b.tipo.kind != Kind.ERROR) {
                errores.error(e, "tipos incompatibles en indexacion");
            }
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (e instanceof Exp_campo) {
            Exp_campo c = (Exp_campo) e;
            TExp b = tipoExp(c.base);
            if (b.tipo.kind != Kind.RECORD) {
                if (b.tipo.kind != Kind.ERROR) {
                    errores.error(e, "se trata de acceder a un campo de un objeto que no es un registro");
                }
                return new TExp(new TVal(Kind.ERROR, null), false);
            }
            CamposRecord campo = buscaCampo(((Tipo_record) b.tipo.origen).lista, c.id);
            if (campo == null) {
                errores.error(e, "campo inexistente:" + c.id);
                return new TExp(new TVal(Kind.ERROR, null), false);
            }
            return new TExp(tipoDeTipo(campo.tipo), true);
        }
        if (e instanceof Exp_flecha) {
            Exp_flecha c = (Exp_flecha) e;
            TExp b = tipoExp(c.base);
            if (b.tipo.kind != Kind.POINTER) {
                if (b.tipo.kind != Kind.ERROR) {
                    errores.error(e, "esperado tipo puntero");
                }
                return new TExp(new TVal(Kind.ERROR, null), false);
            }
            Tipo apuntado = ((Tipo_pointer) b.tipo.origen).tipo;
            TVal tAp = tipoDeTipo(apuntado);
            if (tAp.kind != Kind.RECORD) {
                if (tAp.kind != Kind.ERROR) {
                    errores.error(e, "se trata de acceder a un campo de un objeto que no es un registro");
                }
                return new TExp(new TVal(Kind.ERROR, null), false);
            }
            CamposRecord campo = buscaCampo(((Tipo_record) tAp.origen).lista, c.id);
            if (campo == null) {
                errores.error(e, "campo inexistente:" + c.id);
                return new TExp(new TVal(Kind.ERROR, null), false);
            }
            return new TExp(tipoDeTipo(campo.tipo), true);
        }

        return new TExp(new TVal(Kind.ERROR, null), false);
    }

    private TExp tipoIden(Iden id) {
        Nodo d = info.vinculoDe(id);
        if (d instanceof Dec_var) {
            return new TExp(tipoDeTipo(((Dec_var) d).tipo), true);
        }
        if (d instanceof Param_ref) {
            return new TExp(tipoDeTipo(((Param_ref) d).tipo), true);
        }
        if (d instanceof Param_val) {
            return new TExp(tipoDeTipo(((Param_val) d).tipo), true);
        }
        if (d instanceof Dec_tipo) {
            errores.error(id, id.id + " no es variable ni parametro");
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        if (d instanceof Dec_proc) {
            errores.error(id, id.id + " no es variable ni parametro");
            return new TExp(new TVal(Kind.ERROR, null), false);
        }
        return new TExp(new TVal(Kind.ERROR, null), false);
    }

    private TVal tipoDeTipo(Tipo t) {
        if (t instanceof Tipo_int) {
            return new TVal(Kind.INT, t);
        }
        if (t instanceof Tipo_real) {
            return new TVal(Kind.REAL, t);
        }
        if (t instanceof Tipo_bool) {
            return new TVal(Kind.BOOL, t);
        }
        if (t instanceof Tipo_string) {
            return new TVal(Kind.STRING, t);
        }
        if (t instanceof Tipo_array) {
            return new TVal(Kind.ARRAY, t);
        }
        if (t instanceof Tipo_pointer) {
            return new TVal(Kind.POINTER, t);
        }
        if (t instanceof Tipo_record) {
            return new TVal(Kind.RECORD, t);
        }
        if (t instanceof Tipo_id) {
            Nodo d = info.vinculoDe((Tipo_id) t);
            if (d instanceof Dec_tipo) {
                return tipoDeTipo(((Dec_tipo) d).tipo);
            }
            return new TVal(Kind.ERROR, null);
        }
        return new TVal(Kind.ERROR, null);
    }

    private boolean esNumerico(Kind k) {
        return k == Kind.INT || k == Kind.REAL;
    }

    private boolean legible(TVal t) {
        return t.kind == Kind.INT || t.kind == Kind.REAL || t.kind == Kind.STRING || t.kind == Kind.BOOL;
    }

    private boolean imprimible(TVal t) {
        return t.kind == Kind.INT || t.kind == Kind.REAL || t.kind == Kind.STRING || t.kind == Kind.BOOL;
    }

    private boolean compatiblesAsignacion(TVal destino, TVal origen) {
        return compatiblesAsignacion(destino, origen, new HashSet<>());
    }

    private boolean compatiblesAsignacion(TVal destino, TVal origen, Set<TipoPair> visitados) {
        if (destino.kind == Kind.ERROR || origen.kind == Kind.ERROR) {
            return true;
        }

        if (destino.origen != null && origen.origen != null) {
            TipoPair par = new TipoPair(destino.origen, origen.origen);
            if (!visitados.add(par)) {
                return true;
            }
        }

        if (destino.kind == origen.kind) {
            if (destino.kind == Kind.ARRAY) {
                Tipo_array td = (Tipo_array) destino.origen;
                Tipo_array to = (Tipo_array) origen.origen;
                return td.dim.equals(to.dim)
                        && compatiblesAsignacion(tipoDeTipo(td.tipo), tipoDeTipo(to.tipo), visitados);
            }
            if (destino.kind == Kind.RECORD) {
                return compatiblesRecord((Tipo_record) destino.origen, (Tipo_record) origen.origen, visitados);
            }
            if (destino.kind == Kind.POINTER) {
                return true;
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

    private boolean compatiblesRecord(Tipo_record t1, Tipo_record t2, Set<TipoPair> visitados) {
        List<CamposRecord> c1 = camposDeRecord(t1.lista);
        List<CamposRecord> c2 = camposDeRecord(t2.lista);
        if (c1.size() != c2.size()) {
            return false;
        }
        for (int i = 0; i < c1.size(); i++) {
            CamposRecord f1 = c1.get(i);
            CamposRecord f2 = c2.get(i);
            if (!f1.id.equals(f2.id)) {
                return false;
            }
            if (!compatiblesAsignacion(tipoDeTipo(f1.tipo), tipoDeTipo(f2.tipo), visitados)) {
                return false;
            }
        }
        return true;
    }

    private CamposRecord buscaCampo(ListaRecord lista, String id) {
        for (CamposRecord c : camposDeRecord(lista)) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }

    private List<CamposRecord> camposDeRecord(ListaRecord lista) {
        List<CamposRecord> out = new ArrayList<>();
        camposDeRecord(lista, out);
        return out;
    }

    private void camposDeRecord(ListaRecord lista, List<CamposRecord> out) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            camposDeRecord(m.lista, out);
            out.add(m.campo);
        } else if (lista instanceof Un_camporecord) {
            out.add(((Un_camporecord) lista).campo);
        }
    }
}
