package semantica;

import asint.SintaxisAbstractaTiny.*;

import java.util.ArrayList;
import java.util.List;

public class Tipado {
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

    public Tipado(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        tipaDecs(p.decs);
        tipaInstrs(p.instrs);
    }

    private void tipaDecs(LDec_0 decs0) {
        if (decs0 instanceof Si_dec) {
            tipaDecs(((Si_dec) decs0).decs);
        }
    }

    private void tipaDecs(LDec decs) {
        if (decs instanceof Muchas_decs) {
            Muchas_decs m = (Muchas_decs) decs;
            tipaDecs(m.decs);
            tipaDec(m.dec);
        } else if (decs instanceof Una_dec) {
            tipaDec(((Una_dec) decs).dec);
        }
    }

    private void tipaDec(Dec d) {
        if (d instanceof Dec_proc) {
            Dec_proc p = (Dec_proc) d;
            tipaDecs(p.decs);
            tipaInstrs(p.instrs);
        }
    }

    private void tipaInstrs(Instrs_0 instrs0) {
        if (instrs0 instanceof Si_instr) {
            tipaInstrs(((Si_instr) instrs0).instrs);
        }
    }

    private void tipaInstrs(LInstr instrs) {
        if (instrs instanceof Muchas_instr) {
            Muchas_instr m = (Muchas_instr) instrs;
            tipaInstrs(m.instrs);
            tipaInstr(m.instr);
        } else if (instrs instanceof Una_instr) {
            tipaInstr(((Una_instr) instrs).instr);
        }
    }

    private void tipaInstr(Instr i) {
        if (i instanceof Instr_asig) {
            Instr_asig a = (Instr_asig) i;
            TExp t1 = tipoExp(a.exp1);
            TExp t2 = tipoExp(a.exp2);
            if (!t1.designador) {
                errores.error(a.exp1, "la parte izquierda debe ser un designador");
            }
            if (!compatiblesAsignacion(t1.tipo, t2.tipo)) {
                errores.error(i, "tipos incompatibles en asignacion");
            }
        } else if (i instanceof Instr_if) {
            Instr_if x = (Instr_if) i;
            TExp c = tipoExp(x.exp);
            if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
                errores.error(x.exp, "esperada expresion booleana");
            }
            tipaInstrs(x.instrs);
        } else if (i instanceof Instr_ifelse) {
            Instr_ifelse x = (Instr_ifelse) i;
            TExp c = tipoExp(x.exp);
            if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
                errores.error(x.exp, "esperada expresion booleana");
            }
            tipaInstrs(x.instrs1);
            tipaInstrs(x.instrs2);
        } else if (i instanceof Instr_while) {
            Instr_while x = (Instr_while) i;
            TExp c = tipoExp(x.exp);
            if (c.tipo.kind != Kind.BOOL && c.tipo.kind != Kind.ERROR) {
                errores.error(x.exp, "esperada expresion booleana");
            }
            tipaInstrs(x.instrs);
        } else if (i instanceof Instr_lectura) {
            Instr_lectura x = (Instr_lectura) i;
            TExp t = tipoExp(x.exp);
            if (!t.designador) {
                errores.error(x.exp, "designador esperado");
            }
            if (!legible(t.tipo)) {
                errores.error(x.exp, "valor no legible");
            }
        } else if (i instanceof Instr_escritura) {
            Instr_escritura x = (Instr_escritura) i;
            TExp t = tipoExp(x.exp);
            if (!imprimible(t.tipo)) {
                errores.error(x.exp, "valor no imprimible");
            }
        } else if (i instanceof Instr_reserva) {
            Instr_reserva x = (Instr_reserva) i;
            TExp t = tipoExp(x.exp);
            if (!t.designador) {
                errores.error(x.exp, "designador esperado");
            }
            if (t.tipo.kind != Kind.POINTER && t.tipo.kind != Kind.ERROR) {
                errores.error(x.exp, "esperado tipo puntero");
            }
        } else if (i instanceof Instr_liberacion) {
            Instr_liberacion x = (Instr_liberacion) i;
            TExp t = tipoExp(x.exp);
            if (t.tipo.kind != Kind.POINTER && t.tipo.kind != Kind.ERROR) {
                errores.error(x.exp, "esperado tipo puntero");
            }
        } else if (i instanceof Instr_invocar) {
            tipaInvocacion((Instr_invocar) i);
        } else if (i instanceof Instr_compuesta) {
            Instr_compuesta b = (Instr_compuesta) i;
            tipaDecs(b.decs);
            tipaInstrs(b.instrs);
        }
    }

    private void tipaInvocacion(Instr_invocar inv) {
        Nodo d = info.vinculoDe(inv);
        if (!(d instanceof Dec_proc)) {
            errores.error(inv, inv.id + " no es un subprograma");
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

            if (formal instanceof Param_ref && !tr.designador) {
                errores.error(real, "designador esperado");
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
        if (e instanceof Exp_mayor || e instanceof Exp_menor || e instanceof Exp_mayor_igual || e instanceof Exp_menor_igual) {
            ExpBin b = (ExpBin) e;
            TExp t0 = tipoExp(b.opnd0);
            TExp t1 = tipoExp(b.opnd1);
            if (esNumerico(t0.tipo.kind) && esNumerico(t1.tipo.kind)) {
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
            if (compatiblesAsignacion(t0.tipo, t1.tipo) || compatiblesAsignacion(t1.tipo, t0.tipo)) {
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
                errores.error(a.opnd1, "tipos incompatibles en indexacion");
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
            return new TExp(new TVal(Kind.PROC, null), false);
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
        if (destino.kind == Kind.ERROR || origen.kind == Kind.ERROR) {
            return true;
        }
        if (destino.kind == origen.kind) {
            if (destino.kind == Kind.ARRAY) {
                Tipo_array td = (Tipo_array) destino.origen;
                Tipo_array to = (Tipo_array) origen.origen;
                return td.dim.equals(to.dim) && compatiblesAsignacion(tipoDeTipo(td.tipo), tipoDeTipo(to.tipo));
            }
            if (destino.kind == Kind.RECORD) {
                return compatiblesRecord((Tipo_record) destino.origen, (Tipo_record) origen.origen);
            }
            if (destino.kind == Kind.POINTER) {
                Tipo_pointer pd = (Tipo_pointer) destino.origen;
                Tipo_pointer po = (Tipo_pointer) origen.origen;
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
            CamposRecord f1 = c1.get(i);
            CamposRecord f2 = c2.get(i);
            if (!f1.id.equals(f2.id)) {
                return false;
            }
            if (!compatiblesAsignacion(tipoDeTipo(f1.tipo), tipoDeTipo(f2.tipo))) {
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
