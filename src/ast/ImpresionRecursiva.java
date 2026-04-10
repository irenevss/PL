package ast;

import static asint.SintaxisAbstractaTiny.*;

public class ImpresionRecursiva extends ProcesamientoDef {
    public String imprime(Prog p) {
        return prog(p);
    }

    private String prog(Prog p) {
        return "<program>\n" + (p.decs != null ? ldec0(p.decs) : "") + (p.instrs != null ? instrs0(p.instrs) : "") + "<end_program>\n";
    }

    private String ldec0(LDec_0 d) {
        if (d instanceof Si_dec) {
            return ldec(((Si_dec) d).decs) + "--\n";
        }
        return "";
    }

    private String ldec(LDec d) {
        if (d instanceof Una_dec) {
            return dec(((Una_dec) d).dec);
        }
        Muchas_decs md = (Muchas_decs) d;
        return ldec(md.decs) + ";\n" + dec(md.dec);
    }

    private String dec(Dec d) {
        if (d instanceof Dec_var) {
            Dec_var dv = (Dec_var) d;
            return "<decvar>\n" + dv.id + "$f:" + dv.leeFila() + ",c:" + dv.leeCol() + "$\n:\n" + tipo(dv.tipo);
        }
        if (d instanceof Dec_tipo) {
            Dec_tipo dt = (Dec_tipo) d;
            return "<dectype>\n" + dt.id + "$f:" + dt.leeFila() + ",c:" + dt.leeCol() + "$\n:\n" + tipo(dt.tipo);
        }
        Dec_proc dp = (Dec_proc) d;
        return "<decproc>\n" + dp.id + "$f:" + dp.leeFila() + ",c:" + dp.leeCol() + "$\n(\n" + lprocparams0(dp.params) + ")\n"
                + ldec0(dp.decs) + instrs0(dp.instrs) + "<end_proc>\n";
    }

    private String tipo(Tipo t) {
        if (t instanceof Tipo_int) {
            return "<int>\n";
        }
        if (t instanceof Tipo_real) {
            return "<real>\n";
        }
        if (t instanceof Tipo_bool) {
            return "<bool>\n";
        }
        if (t instanceof Tipo_string) {
            return "<string>\n";
        }
        if (t instanceof Tipo_id) {
            Tipo_id ti = (Tipo_id) t;
            return ti.id + "$f:" + ti.leeFila() + ",c:" + ti.leeCol() + "$\n";
        }
        if (t instanceof Tipo_array) {
            Tipo_array ta = (Tipo_array) t;
            return "<array>\n[\n" + ta.dim + "\n]$f:" + ta.leeFila() + ",c:" + ta.leeCol() + "$\n<of>\n" + tipo(ta.tipo);
        }
        if (t instanceof Tipo_pointer) {
            return "<pointer>\n" + tipo(((Tipo_pointer) t).tipo);
        }
        Tipo_record tr = (Tipo_record) t;
        return "<record>\n" + listarecord(tr.lista) + "<end_record>\n";
    }

    private String listarecord(ListaRecord lr) {
        if (lr instanceof Un_camporecord) {
            return camposrecord(((Un_camporecord) lr).campo);
        }
        Muchos_camposrecord mr = (Muchos_camposrecord) lr;
        return listarecord(mr.lista) + ";\n" + camposrecord(mr.campo);
    }

    private String camposrecord(CamposRecord cr) {
        return cr.id + "$f:" + cr.leeFila() + ",c:" + cr.leeCol() + "$\n:\n" + tipo(cr.tipo);
    }

    private String lprocparams0(LProcParams_0 p) {
        if (p instanceof Si_procparam) {
            return lprocparams(((Si_procparam) p).params);
        }
        return "";
    }

    private String lprocparams(LProcParams p) {
        if (p instanceof Un_procparam) {
            return param(((Un_procparam) p).param);
        }
        Muchos_procparam mp = (Muchos_procparam) p;
        return lprocparams(mp.params) + ",\n" + param(mp.param);
    }

    private String param(Param p) {
        if (p instanceof Param_ref) {
            Param_ref pr = (Param_ref) p;
            return "<ref>\n" + pr.id + "$f:" + pr.leeFila() + ",c:" + pr.leeCol() + "$\n:\n" + tipo(pr.tipo);
        }
        Param_val pv = (Param_val) p;
        return pv.id + "$f:" + pv.leeFila() + ",c:" + pv.leeCol() + "$\n:\n" + tipo(pv.tipo);
    }

    private String instrs0(Instrs_0 i0) {
        if (i0 instanceof Si_instr) {
            return linstr(((Si_instr) i0).instrs);
        }
        return "";
    }

    // linstr0 removed

    private String linstr(LInstr i) {
        if (i instanceof Una_instr) {
            return instr(((Una_instr) i).instr);
        }
        Muchas_instr mi = (Muchas_instr) i;
        return linstr(mi.instrs) + ";\n" + instr(mi.instr);
    }

    private String instr(Instr i) {
        if (i instanceof Instr_asig) {
            Instr_asig ia = (Instr_asig) i;
            return exp(ia.exp1) + ":=\n" + exp(ia.exp2);
        }
        if (i instanceof Instr_if) {
            Instr_if ii = (Instr_if) i;
            return "<if>\n" + exp(ii.exp) + ":\n" + instrs0(ii.instrs) + "<end_if>\n";
        }
        if (i instanceof Instr_ifelse) {
            Instr_ifelse ie = (Instr_ifelse) i;
            return "<if>\n" + exp(ie.exp) + ":\n" + instrs0(ie.instrs1) + "<else>\n" + instrs0(ie.instrs2) + "<end_if>\n";
        }
        if (i instanceof Instr_while) {
            Instr_while iw = (Instr_while) i;
            return "<while>\n" + exp(iw.exp) + ":\n" + instrs0(iw.instrs) + "<end_while>\n";
        }
        if (i instanceof Instr_lectura) {
            return "<input>\n" + exp(((Instr_lectura) i).exp);
        }
        if (i instanceof Instr_escritura) {
            return "<output>\n" + exp(((Instr_escritura) i).exp);
        }
        if (i instanceof Instr_reserva) {
            return "<new>\n" + exp(((Instr_reserva) i).exp);
        }
        if (i instanceof Instr_liberacion) {
            return "<dispose>\n" + exp(((Instr_liberacion) i).exp);
        }
        if (i instanceof Instr_invocar) {
            Instr_invocar iv = (Instr_invocar) i;
            return "@\n" + iv.id + "$f:" + iv.leeFila() + ",c:" + iv.leeCol() + "$\n(\n" + lexps0(iv.exps) + ")\n";
        }
        Instr_compuesta ic = (Instr_compuesta) i;
        return "<block>\n" + ldec0(ic.decs) + instrs0(ic.instrs) + "<end_block>\n";
    }

    private String lexps0(LExps_0 e0) {
        if (e0 instanceof Si_exps) {
            return lexps(((Si_exps) e0).exps);
        }
        return "";
    }

    private String lexps(LExps e) {
        if (e instanceof Una_exp) {
            return exp(((Una_exp) e).exp);
        }
        Muchas_exps me = (Muchas_exps) e;
        return lexps(me.exps) + ",\n" + exp(me.exp);
    }

    private String exp(Exp e) {
        if (e instanceof Exp_suma) {
            Exp_suma x = (Exp_suma) e;
            return imprimeOpnd(x.opnd0, 1, false) + "+$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 1, true);
        }
        if (e instanceof Exp_resta) {
            Exp_resta x = (Exp_resta) e;
            return imprimeOpnd(x.opnd0, 1, false) + "-$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 1, true);
        }
        if (e instanceof Exp_mul) {
            Exp_mul x = (Exp_mul) e;
            return imprimeOpnd(x.opnd0, 3, false) + "*$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp_div) {
            Exp_div x = (Exp_div) e;
            return imprimeOpnd(x.opnd0, 3, false) + "/$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp_mod) {
            Exp_mod x = (Exp_mod) e;
            return imprimeOpnd(x.opnd0, 3, false) + "%$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp_and) {
            Exp_and x = (Exp_and) e;
            return imprimeOpnd(x.opnd0, 4, false) + "&$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 4, true);
        }
        if (e instanceof Exp_or) {
            Exp_or x = (Exp_or) e;
            return imprimeOpnd(x.opnd0, 2, false) + "|$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 2, true);
        }
        if (e instanceof Exp_menor) {
            Exp_menor x = (Exp_menor) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_mayor) {
            Exp_mayor x = (Exp_mayor) e;
            return imprimeOpnd(x.opnd0, 0, false) + ">$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_menor_igual) {
            Exp_menor_igual x = (Exp_menor_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<=$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_mayor_igual) {
            Exp_mayor_igual x = (Exp_mayor_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + ">=$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_igual) {
            Exp_igual x = (Exp_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + "=$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_distinto) {
            Exp_distinto x = (Exp_distinto) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<>$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp_menos_unario) {
            Exp_menos_unario x = (Exp_menos_unario) e;
            return "-$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd, 5, true);
        }
        if (e instanceof Exp_not) {
            Exp_not x = (Exp_not) e;
            return "!$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd, 5, true);
        }
        if (e instanceof Exp_asterisco_unario) {
            Exp_asterisco_unario x = (Exp_asterisco_unario) e;
            return "*$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + imprimeOpnd(x.opnd, 7, true);
        }
        if (e instanceof Iden) {
            Iden x = (Iden) e;
            return x.id + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Lit_int) {
            Lit_int x = (Lit_int) e;
            return x.val + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Lit_real) {
            Lit_real x = (Lit_real) e;
            return x.val + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Lit_bool) {
            Lit_bool x = (Lit_bool) e;
            return x.val + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Lit_string) {
            Lit_string x = (Lit_string) e;
            return x.val + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Exp_null) {
            Exp_null x = (Exp_null) e;
            return "<null>$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Exp_campo) {
            Exp_campo x = (Exp_campo) e;
            return imprimeOpnd(x.base, 6, false) + ".\n" + x.id + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Exp_flecha) {
            Exp_flecha x = (Exp_flecha) e;
            return imprimeOpnd(x.base, 6, false) + "->\n" + x.id + "$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n";
        }
        if (e instanceof Exp_array) {
            Exp_array x = (Exp_array) e;
            return imprimeOpnd(x.opnd0, 6, false) + "[$f:" + x.leeFila() + ",c:" + x.leeCol() + "$\n" + exp(x.opnd1) + "]\n";
        }
        return "";
    }

    private String imprimeOpnd(Exp opnd, int minPrior, boolean isRight) {
        int priorOpnd = opnd.prioridad();
        boolean needsParens = false;

        if (priorOpnd < minPrior) {
            needsParens = true;
        } else if (priorOpnd == minPrior) {
            if (isRight) {
                if (minPrior <= 4) {
                    needsParens = true;
                }
            } else {
                if (minPrior == 0 || minPrior == 2) {
                    needsParens = true;
                }
            }
        }

        if (needsParens) {
            return "(\n" + exp(opnd) + ")\n";
        }
        return exp(opnd);
    }
}