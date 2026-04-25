package semantica;

import asint.SintaxisAbstractaTiny;
import asint.SintaxisAbstractaTiny.*;

public class Vinculacion {
    private final ErroresSemanticos errores;
    private final InfoSemantica info;
    private final TablaSimbolos ts = new TablaSimbolos();

    public Vinculacion(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        abreAmbito();
        vinculaDecsPrimeraPasada(p.decs);
        vinculaDecsSegundaPasada(p.decs);
        vinculaInstrs(p.instrs);
        cierraAmbito();
    }

    private void abreAmbito() {
        ts.abreAmbito();
    }

    private void cierraAmbito() {
        ts.cierraAmbito();
    }

    private boolean contieneEnActual(String id) {
        return ts.contieneEnActual(id);
    }

    private void inserta(String id, Nodo dec, Nodo n) {
        if (contieneEnActual(id)) {
            errores.error(n, "declaracion duplicada:" + id);
        } else {
            ts.inserta(id, dec);
        }
    }

    private Nodo vinculoDe(String id) {
        return ts.vinculoDe(id);
    }

    private void vinculaDecsPrimeraPasada(LDec_0 decs0) {
        if (decs0 instanceof Si_dec) {
            vinculaDecsPrimeraPasada(((Si_dec) decs0).decs);
        }
    }

    private void vinculaDecsPrimeraPasada(LDec decs) {
        if (decs instanceof Muchas_decs) {
            Muchas_decs m = (Muchas_decs) decs;
            vinculaDecsPrimeraPasada(m.decs);
            vinculaDecPrimeraPasada(m.dec);
        } else if (decs instanceof Una_dec) {
            vinculaDecPrimeraPasada(((Una_dec) decs).dec);
        }
    }

    private void vinculaDecPrimeraPasada(Dec dec) {
        if (dec instanceof Dec_var) {
            Dec_var d = (Dec_var) dec;
            vinculaTipoPrimeraPasada(d.tipo);
            inserta(d.id, d, d);
        } else if (dec instanceof Dec_tipo) {
            Dec_tipo d = (Dec_tipo) dec;
            vinculaTipoPrimeraPasada(d.tipo);
            inserta(d.id, d, d);
        } else if (dec instanceof Dec_proc) {
            Dec_proc d = (Dec_proc) dec;
            inserta(d.id, d, d);

            abreAmbito();
            vinculaProcParamsPrimeraPasada(d.params);
            vinculaDecsPrimeraPasada(d.decs);
            vinculaDecsSegundaPasada(d.decs);
            vinculaInstrs(d.instrs);
            cierraAmbito();
        }
    }

    private void vinculaProcParamsPrimeraPasada(LProcParams_0 params0) {
        if (params0 instanceof Si_procparam) {
            vinculaProcParamsPrimeraPasada(((Si_procparam) params0).params);
        }
    }

    private void vinculaProcParamsPrimeraPasada(LProcParams params) {
        if (params instanceof Muchos_procparam) {
            Muchos_procparam m = (Muchos_procparam) params;
            vinculaProcParamsPrimeraPasada(m.params);
            vinculaProcParamPrimeraPasada(m.param);
        } else if (params instanceof Un_procparam) {
            vinculaProcParamPrimeraPasada(((Un_procparam) params).param);
        }
    }

    private void vinculaProcParamPrimeraPasada(Param param) {
        if (param instanceof Param_ref) {
            Param_ref p = (Param_ref) param;
            vinculaTipoPrimeraPasada(p.tipo);
            inserta(p.id, p, p);
        } else if (param instanceof Param_val) {
            Param_val p = (Param_val) param;
            vinculaTipoPrimeraPasada(p.tipo);
            inserta(p.id, p, p);
        }
    }

    private void vinculaDecsSegundaPasada(LDec_0 decs0) {
        if (decs0 instanceof Si_dec) {
            vinculaDecsSegundaPasada(((Si_dec) decs0).decs);
        }
    }

    private void vinculaDecsSegundaPasada(LDec decs) {
        if (decs instanceof Muchas_decs) {
            Muchas_decs m = (Muchas_decs) decs;
            vinculaDecsSegundaPasada(m.decs);
            vinculaDecSegundaPasada(m.dec);
        } else if (decs instanceof Una_dec) {
            vinculaDecSegundaPasada(((Una_dec) decs).dec);
        }
    }

    private void vinculaDecSegundaPasada(Dec dec) {
        if (dec instanceof Dec_var) {
            vinculaTipoSegundaPasada(((Dec_var) dec).tipo);
        } else if (dec instanceof Dec_tipo) {
            vinculaTipoSegundaPasada(((Dec_tipo) dec).tipo);
        }
    }

    private void vinculaTipoPrimeraPasada(Tipo tipo) {
        if (tipo instanceof Tipo_id) {
            Tipo_id t = (Tipo_id) tipo;
            Nodo vinculo = vinculoDe(t.id);
            if (vinculo == null) {
                errores.error(t, "identificador no declarado:" + t.id);
            } else {
                info.vincula(t, vinculo);
            }
        } else if (tipo instanceof Tipo_array) {
            vinculaTipoPrimeraPasada(((Tipo_array) tipo).tipo);
        } else if (tipo instanceof Tipo_pointer) {
            Tipo apuntado = ((Tipo_pointer) tipo).tipo;
            if (!(apuntado instanceof Tipo_id)) {
                vinculaTipoPrimeraPasada(apuntado);
            }
        } else if (tipo instanceof Tipo_record) {
            vinculaCamposRecordPrimeraPasada(((Tipo_record) tipo).lista);
        }
    }

    private void vinculaCamposRecordPrimeraPasada(ListaRecord lista) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            vinculaCamposRecordPrimeraPasada(m.lista);
            vinculaTipoPrimeraPasada(m.campo.tipo);
        } else if (lista instanceof Un_camporecord) {
            vinculaTipoPrimeraPasada(((Un_camporecord) lista).campo.tipo);
        }
    }

    private void vinculaTipoSegundaPasada(Tipo tipo) {
        if (tipo instanceof Tipo_array) {
            vinculaTipoSegundaPasada(((Tipo_array) tipo).tipo);
        } else if (tipo instanceof Tipo_pointer) {
            Tipo apuntado = ((Tipo_pointer) tipo).tipo;
            if (apuntado instanceof Tipo_id) {
                Tipo_id t = (Tipo_id) apuntado;
                Nodo vinculo = vinculoDe(t.id);
                if (vinculo == null) {
                    errores.error(t, "identificador no declarado:" + t.id);
                } else {
                    info.vincula(t, vinculo);
                }
            } else {
                vinculaTipoSegundaPasada(apuntado);
            }
        } else if (tipo instanceof Tipo_record) {
            vinculaCamposRecordSegundaPasada(((Tipo_record) tipo).lista);
        }
    }

    private void vinculaCamposRecordSegundaPasada(ListaRecord lista) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            vinculaCamposRecordSegundaPasada(m.lista);
            vinculaTipoSegundaPasada(m.campo.tipo);
        } else if (lista instanceof Un_camporecord) {
            vinculaTipoSegundaPasada(((Un_camporecord) lista).campo.tipo);
        }
    }

    private void vinculaInstrs(Instrs_0 instrs0) {
        if (instrs0 instanceof Si_instr) {
            vinculaInstrs(((Si_instr) instrs0).instrs);
        }
    }

    private void vinculaInstrs(LInstr instrs) {
        if (instrs instanceof Muchas_instr) {
            Muchas_instr m = (Muchas_instr) instrs;
            vinculaInstrs(m.instrs);
            vinculaInstr(m.instr);
        } else if (instrs instanceof Una_instr) {
            vinculaInstr(((Una_instr) instrs).instr);
        }
    }

    private void vinculaInstr(Instr i) {
        if (i instanceof Instr_asig) {
            Instr_asig a = (Instr_asig) i;
            vinculaExp(a.exp1);
            vinculaExp(a.exp2);
        } else if (i instanceof Instr_if) {
            Instr_if x = (Instr_if) i;
            vinculaExp(x.exp);
            vinculaInstrs(x.instrs);
        } else if (i instanceof Instr_ifelse) {
            Instr_ifelse x = (Instr_ifelse) i;
            vinculaExp(x.exp);
            vinculaInstrs(x.instrs1);
            vinculaInstrs(x.instrs2);
        } else if (i instanceof Instr_while) {
            Instr_while x = (Instr_while) i;
            vinculaExp(x.exp);
            vinculaInstrs(x.instrs);
        } else if (i instanceof Instr_lectura) {
            vinculaExp(((Instr_lectura) i).exp);
        } else if (i instanceof Instr_escritura) {
            vinculaExp(((Instr_escritura) i).exp);
        } else if (i instanceof Instr_reserva) {
            vinculaExp(((Instr_reserva) i).exp);
        } else if (i instanceof Instr_liberacion) {
            vinculaExp(((Instr_liberacion) i).exp);
        } else if (i instanceof Instr_invocar) {
            Instr_invocar inv = (Instr_invocar) i;
            Nodo vinculo = vinculoDe(inv.id);
            if (vinculo == null) {
                errores.error(inv, "identificador no declarado:" + inv.id);
            } else {
                info.vincula(inv, vinculo);
            }
            vinculaExps(inv.exps);
        } else if (i instanceof Instr_compuesta) {
            Instr_compuesta b = (Instr_compuesta) i;
            abreAmbito();
            vinculaDecsPrimeraPasada(b.decs);
            vinculaDecsSegundaPasada(b.decs);
            vinculaInstrs(b.instrs);
            cierraAmbito();
        }
    }

    private void vinculaExps(LExps_0 exps0) {
        if (exps0 instanceof Si_exps) {
            vinculaExps(((Si_exps) exps0).exps);
        }
    }

    private void vinculaExps(LExps exps) {
        if (exps instanceof Muchas_exps) {
            Muchas_exps m = (Muchas_exps) exps;
            vinculaExps(m.exps);
            vinculaExp(m.exp);
        } else if (exps instanceof Una_exp) {
            vinculaExp(((Una_exp) exps).exp);
        }
    }

    private void vinculaExp(Exp e) {
        if (e instanceof ExpBin) {
            ExpBin b = (ExpBin) e;
            vinculaExp(b.opnd0);
            vinculaExp(b.opnd1);
        } else if (e instanceof ExpUni) {
            vinculaExp(((ExpUni) e).opnd);
        } else if (e instanceof Exp_array) {
            Exp_array a = (Exp_array) e;
            vinculaExp(a.opnd0);
            vinculaExp(a.opnd1);
        } else if (e instanceof Exp_campo) {
            vinculaExp(((Exp_campo) e).base);
        } else if (e instanceof Exp_flecha) {
            vinculaExp(((Exp_flecha) e).base);
        } else if (e instanceof Iden) {
            Iden id = (Iden) e;
            Nodo vinculo = vinculoDe(id.id);
            if (vinculo == null) {
                errores.error(id, "identificador no declarado:" + id.id);
            } else {
                info.vincula(id, vinculo);
            }
        }
    }
}
