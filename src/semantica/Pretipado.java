package semantica;

import asint.SintaxisAbstractaTiny.*;

import java.util.HashSet;
import java.util.Set;

public class Pretipado {
    private final ErroresSemanticos errores;
    private final InfoSemantica info;

    public Pretipado(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        pretipaDecs(p.decs);
        pretipaInstrs(p.instrs);
    }

    private void pretipaDecs(LDec_0 decs0) {
        if (decs0 instanceof Si_dec) {
            pretipaDecs(((Si_dec) decs0).decs);
        }
    }

    private void pretipaDecs(LDec decs) {
        if (decs instanceof Muchas_decs) {
            Muchas_decs m = (Muchas_decs) decs;
            pretipaDecs(m.decs);
            pretipaDec(m.dec);
        } else if (decs instanceof Una_dec) {
            pretipaDec(((Una_dec) decs).dec);
        }
    }

    private void pretipaDec(Dec dec) {
        if (dec instanceof Dec_var) {
            pretipaTipo(((Dec_var) dec).tipo);
        } else if (dec instanceof Dec_tipo) {
            pretipaTipo(((Dec_tipo) dec).tipo);
        } else if (dec instanceof Dec_proc) {
            Dec_proc d = (Dec_proc) dec;
            pretipaParams(d.params);
            pretipaDecs(d.decs);
            pretipaInstrs(d.instrs);
        }
    }

    private void pretipaParams(LProcParams_0 params0) {
        if (params0 instanceof Si_procparam) {
            pretipaParams(((Si_procparam) params0).params);
        }
    }

    private void pretipaParams(LProcParams params) {
        if (params instanceof Muchos_procparam) {
            Muchos_procparam m = (Muchos_procparam) params;
            pretipaParams(m.params);
            pretipaParam(m.param);
        } else if (params instanceof Un_procparam) {
            pretipaParam(((Un_procparam) params).param);
        }
    }

    private void pretipaParam(Param p) {
        if (p instanceof Param_ref) {
            pretipaTipo(((Param_ref) p).tipo);
        } else if (p instanceof Param_val) {
            pretipaTipo(((Param_val) p).tipo);
        }
    }

    private void pretipaTipo(Tipo t) {
        if (t instanceof Tipo_id) {
            Tipo_id tid = (Tipo_id) t;
            Nodo d = info.vinculoDe(tid);
            if (!(d instanceof Dec_tipo)) {
                errores.error(tid, tid.id + " no esta declarado como un tipo");
            }
        } else if (t instanceof Tipo_array) {
            Tipo_array ta = (Tipo_array) t;
            try {
                int dim = Integer.parseInt(ta.dim);
                if (dim < 0) {
                    errores.error(ta, "la dimension no puede ser negativa");
                }
            } catch (NumberFormatException ex) {
                errores.error(ta, "la dimension no es un entero valido");
            }
            pretipaTipo(ta.tipo);
        } else if (t instanceof Tipo_pointer) {
            pretipaTipo(((Tipo_pointer) t).tipo);
        } else if (t instanceof Tipo_record) {
            pretipaRecord(((Tipo_record) t).lista, new HashSet<>());
        }
    }

    private void pretipaRecord(ListaRecord lista, Set<String> ids) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            pretipaRecord(m.lista, ids);
            pretipaCampo(m.campo, ids);
        } else if (lista instanceof Un_camporecord) {
            pretipaCampo(((Un_camporecord) lista).campo, ids);
        }
    }

    private void pretipaCampo(CamposRecord c, Set<String> ids) {
        if (!ids.add(c.id)) {
            errores.error(c, "campo duplicado:" + c.id);
        }
        pretipaTipo(c.tipo);
    }

    private void pretipaInstrs(Instrs_0 instrs0) {
        if (instrs0 instanceof Si_instr) {
            pretipaInstrs(((Si_instr) instrs0).instrs);
        }
    }

    private void pretipaInstrs(LInstr instrs) {
        if (instrs instanceof Muchas_instr) {
            Muchas_instr m = (Muchas_instr) instrs;
            pretipaInstrs(m.instrs);
            pretipaInstr(m.instr);
        } else if (instrs instanceof Una_instr) {
            pretipaInstr(((Una_instr) instrs).instr);
        }
    }

    private void pretipaInstr(Instr i) {
        if (i instanceof Instr_compuesta) {
            Instr_compuesta b = (Instr_compuesta) i;
            pretipaDecs(b.decs);
            pretipaInstrs(b.instrs);
        } else if (i instanceof Instr_if) {
            pretipaInstrs(((Instr_if) i).instrs);
        } else if (i instanceof Instr_ifelse) {
            Instr_ifelse x = (Instr_ifelse) i;
            pretipaInstrs(x.instrs1);
            pretipaInstrs(x.instrs2);
        } else if (i instanceof Instr_while) {
            pretipaInstrs(((Instr_while) i).instrs);
        }
    }
}
