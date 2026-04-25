package semantica;

import asint.ProcesamientoDef;
import asint.SintaxisAbstractaTiny.*;

public class Vinculacion extends ProcesamientoDef {
    private final ErroresSemanticos errores;
    private final InfoSemantica info;
    private final TablaSimbolos ts = new TablaSimbolos();
    private boolean segundaPasada;

    public Vinculacion(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        segundaPasada = false;
        ts.abreAmbito();
        p.process(this);
        ts.cierraAmbito();
    }

    private void inserta(String id, Nodo dec, Nodo n) {
        if (ts.contieneEnActual(id)) {
            errores.error(n, "declaracion duplicada:" + id);
        } else {
            ts.inserta(id, dec);
        }
    }

    private void vinculaUso(Nodo uso, String id) {
        Nodo vinculo = ts.vinculoDe(id);
        if (vinculo == null) {
            errores.error(uso, "identificador no declarado:" + id);
            return;
        }
        if (uso instanceof Tipo_id) {
            info.vincula((Tipo_id) uso, vinculo);
        } else if (uso instanceof Iden) {
            info.vincula((Iden) uso, vinculo);
        } else if (uso instanceof Instr_invocar) {
            info.vincula((Instr_invocar) uso, vinculo);
        }
    }

    @Override
    public void process(Prog p) {
        if (p.decs != null) {
            p.decs.process(this);
        }
        segundaPasada = true;
        if (p.decs != null) {
            p.decs.process(this);
        }
        segundaPasada = false;
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
    public void process(Dec_var d) {
        if (d.tipo != null) {
            d.tipo.process(this);
        }
        if (!segundaPasada) {
            inserta(d.id, d, d);
        }
    }

    @Override
    public void process(Dec_tipo d) {
        if (d.tipo != null) {
            d.tipo.process(this);
        }
        if (!segundaPasada) {
            inserta(d.id, d, d);
        }
    }

    @Override
    public void process(Dec_proc d) {
        if (segundaPasada) {
            ts.abreAmbito();
            if (d.params != null) {
                d.params.process(this);
            }
            if (d.decs != null) {
                d.decs.process(this);
            }
            ts.cierraAmbito();
            return;
        }

        inserta(d.id, d, d);

        ts.abreAmbito();
        if (d.params != null) {
            d.params.process(this);
        }
        if (d.decs != null) {
            d.decs.process(this);
        }

        boolean prev = segundaPasada;
        segundaPasada = true;
        if (d.decs != null) {
            d.decs.process(this);
        }
        segundaPasada = prev;

        if (d.instrs != null) {
            d.instrs.process(this);
        }
        ts.cierraAmbito();
    }

    @Override
    public void process(Si_procparam ld) {
        if (ld.params != null) {
            ld.params.process(this);
        }
    }

    @Override
    public void process(Muchos_procparam ld) {
        if (ld.params != null) {
            ld.params.process(this);
        }
        if (ld.param != null) {
            ld.param.process(this);
        }
    }

    @Override
    public void process(Un_procparam ld) {
        if (ld.param != null) {
            ld.param.process(this);
        }
    }

    @Override
    public void process(Param_ref p) {
        if (p.tipo != null) {
            p.tipo.process(this);
        }
        if (!segundaPasada) {
            inserta(p.id, p, p);
        }
    }

    @Override
    public void process(Param_val p) {
        if (p.tipo != null) {
            p.tipo.process(this);
        }
        if (!segundaPasada) {
            inserta(p.id, p, p);
        }
    }

    @Override
    public void process(Tipo_id t) {
        if (!segundaPasada) {
            vinculaUso(t, t.id);
        }
    }

    @Override
    public void process(Tipo_array t) {
        if (t.tipo != null) {
            t.tipo.process(this);
        }
    }

    @Override
    public void process(Tipo_pointer t) {
        if (!segundaPasada) {
            if (!(t.tipo instanceof Tipo_id) && t.tipo != null) {
                t.tipo.process(this);
            }
        } else {
            if (t.tipo instanceof Tipo_id) {
                Tipo_id tid = (Tipo_id) t.tipo;
                vinculaUso(tid, tid.id);
            } else if (t.tipo != null) {
                t.tipo.process(this);
            }
        }
    }

    @Override
    public void process(Tipo_record t) {
        if (t.lista != null) {
            t.lista.process(this);
        }
    }

    @Override
    public void process(Muchos_camposrecord ld) {
        if (ld.lista != null) {
            ld.lista.process(this);
        }
        if (ld.campo != null) {
            ld.campo.process(this);
        }
    }

    @Override
    public void process(Un_camporecord ld) {
        if (ld.campo != null) {
            ld.campo.process(this);
        }
    }

    @Override
    public void process(CamposRecord cr) {
        if (cr.tipo != null) {
            cr.tipo.process(this);
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
        if (i.exp1 != null) {
            i.exp1.process(this);
        }
        if (i.exp2 != null) {
            i.exp2.process(this);
        }
    }

    @Override
    public void process(Instr_if i) {
        if (i.exp != null) {
            i.exp.process(this);
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_ifelse i) {
        if (i.exp != null) {
            i.exp.process(this);
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
        if (i.exp != null) {
            i.exp.process(this);
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_lectura i) {
        if (i.exp != null) {
            i.exp.process(this);
        }
    }

    @Override
    public void process(Instr_escritura i) {
        if (i.exp != null) {
            i.exp.process(this);
        }
    }

    @Override
    public void process(Instr_reserva i) {
        if (i.exp != null) {
            i.exp.process(this);
        }
    }

    @Override
    public void process(Instr_liberacion i) {
        if (i.exp != null) {
            i.exp.process(this);
        }
    }

    @Override
    public void process(Instr_invocar i) {
        vinculaUso(i, i.id);
        if (i.exps != null) {
            i.exps.process(this);
        }
    }

    @Override
    public void process(Instr_compuesta i) {
        ts.abreAmbito();
        if (i.decs != null) {
            i.decs.process(this);
        }

        boolean prev = segundaPasada;
        segundaPasada = true;
        if (i.decs != null) {
            i.decs.process(this);
        }
        segundaPasada = prev;

        if (i.instrs != null) {
            i.instrs.process(this);
        }
        ts.cierraAmbito();
    }

    @Override
    public void process(Si_exps ld) {
        if (ld.exps != null) {
            ld.exps.process(this);
        }
    }

    @Override
    public void process(Muchas_exps ld) {
        if (ld.exps != null) {
            ld.exps.process(this);
        }
        if (ld.exp != null) {
            ld.exp.process(this);
        }
    }

    @Override
    public void process(Una_exp ld) {
        if (ld.exp != null) {
            ld.exp.process(this);
        }
    }

    @Override
    public void process(Exp_suma e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_resta e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_mul e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_div e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_mod e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_and e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_or e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_mayor e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_menor e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_mayor_igual e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_menor_igual e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_igual e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_distinto e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }

    @Override
    public void process(Exp_menos_unario e) {
        e.opnd.process(this);
    }

    @Override
    public void process(Exp_not e) {
        e.opnd.process(this);
    }

    @Override
    public void process(Exp_asterisco_unario e) {
        e.opnd.process(this);
    }

    @Override
    public void process(Iden e) {
        vinculaUso(e, e.id);
    }

    @Override
    public void process(Exp_campo e) {
        e.base.process(this);
    }

    @Override
    public void process(Exp_flecha e) {
        e.base.process(this);
    }

    @Override
    public void process(Exp_array e) {
        e.opnd0.process(this);
        e.opnd1.process(this);
    }
}
