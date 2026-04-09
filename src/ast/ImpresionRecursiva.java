package ast;

public class ImpresionRecursiva {
    public String imprime(Prog p) {
        return prog(p);
    }

    private String prog(Prog p) {
        return "<program>\n" + (p.decs != null ? ldec0(p.decs) : "") + (p.instrs != null ? instrs0(p.instrs) : "") + "<end_program>\n";
    }

    private String ldec0(LDec_0 d) {
        if (d instanceof LDec_0.Si_dec) {
            return ldec(((LDec_0.Si_dec) d).decs) + "--\n";
        }
        return "";
    }

    private String ldec(LDec d) {
        if (d instanceof LDec.Una_dec) {
            return dec(((LDec.Una_dec) d).dec);
        }
        LDec.Muchas_decs md = (LDec.Muchas_decs) d;
        return ldec(md.decs) + ";\n" + dec(md.dec);
    }

    private String dec(Dec d) {
        if (d instanceof Dec.Dec_var) {
            Dec.Dec_var dv = (Dec.Dec_var) d;
            return "<decvar>\n" + dv.id + "$f:" + dv.fila + ",c:" + dv.col + "$\n:\n" + tipo(dv.tipo);
        }
        if (d instanceof Dec.Dec_tipo) {
            Dec.Dec_tipo dt = (Dec.Dec_tipo) d;
            return "<dectype>\n" + dt.id + "$f:" + dt.fila + ",c:" + dt.col + "$\n:\n" + tipo(dt.tipo);
        }
        Dec.Dec_proc dp = (Dec.Dec_proc) d;
        return "<decproc>\n" + dp.id + "$f:" + dp.fila + ",c:" + dp.col + "$\n(\n" + lprocparams0(dp.params) + ")\n"
                + ldec0(dp.decs) + instrs0(dp.instrs) + "<end_proc>\n";
    }

    private String tipo(Tipo t) {
        if (t instanceof Tipo.Tipo_int) {
            return "<int>\n";
        }
        if (t instanceof Tipo.Tipo_real) {
            return "<real>\n";
        }
        if (t instanceof Tipo.Tipo_bool) {
            return "<bool>\n";
        }
        if (t instanceof Tipo.Tipo_string) {
            return "<string>\n";
        }
        if (t instanceof Tipo.Tipo_id) {
            Tipo.Tipo_id ti = (Tipo.Tipo_id) t;
            return ti.id + "$f:" + ti.fila + ",c:" + ti.col + "$\n";
        }
        if (t instanceof Tipo.Tipo_array) {
            Tipo.Tipo_array ta = (Tipo.Tipo_array) t;
            return "<array>\n[\n" + ta.dim + "\n]$f:" + ta.fila + ",c:" + ta.col + "$\n<of>\n" + tipo(ta.tipo);
        }
        if (t instanceof Tipo.Tipo_pointer) {
            return "<pointer>\n" + tipo(((Tipo.Tipo_pointer) t).tipo);
        }
        Tipo.Tipo_record tr = (Tipo.Tipo_record) t;
        return "<record>\n" + listarecord(tr.lista) + "<end_record>\n";
    }

    private String listarecord(ListaRecord lr) {
        if (lr instanceof ListaRecord.Un_camporecord) {
            return camposrecord(((ListaRecord.Un_camporecord) lr).campo);
        }
        ListaRecord.Muchos_camposrecord mr = (ListaRecord.Muchos_camposrecord) lr;
        return listarecord(mr.lista) + ";\n" + camposrecord(mr.campo);
    }

    private String camposrecord(CamposRecord cr) {
        return cr.id + "$f:" + cr.fila + ",c:" + cr.col + "$\n:\n" + tipo(cr.tipo);
    }

    private String lprocparams0(LProcParams_0 p) {
        if (p instanceof LProcParams_0.Si_procparam) {
            return lprocparams(((LProcParams_0.Si_procparam) p).params);
        }
        return "";
    }

    private String lprocparams(LProcParams p) {
        if (p instanceof LProcParams.Un_procparam) {
            return param(((LProcParams.Un_procparam) p).param);
        }
        LProcParams.Muchos_procparam mp = (LProcParams.Muchos_procparam) p;
        return lprocparams(mp.params) + ",\n" + param(mp.param);
    }

    private String param(Param p) {
        if (p instanceof Param.Param_ref) {
            Param.Param_ref pr = (Param.Param_ref) p;
            return "<ref>\n" + pr.id + "$f:" + pr.fila + ",c:" + pr.col + "$\n:\n" + tipo(pr.tipo);
        }
        Param.Param_val pv = (Param.Param_val) p;
        return pv.id + "$f:" + pv.fila + ",c:" + pv.col + "$\n:\n" + tipo(pv.tipo);
    }

    private String instrs0(Instrs_0 i0) {
        if (i0 instanceof Instrs_0.Si_instr) {
            return linstr(((Instrs_0.Si_instr) i0).instrs);
        }
        return "";
    }

    private String linstr0(LInstr_0 i0) {
        if (i0 instanceof LInstr_0.Si_instr_l) {
            return linstr(((LInstr_0.Si_instr_l) i0).instrs);
        }
        return "";
    }

    private String linstr(LInstr i) {
        if (i instanceof LInstr.Una_instr) {
            return instr(((LInstr.Una_instr) i).instr);
        }
        LInstr.Muchas_instr mi = (LInstr.Muchas_instr) i;
        return linstr(mi.instrs) + ";\n" + instr(mi.instr);
    }

    private String instr(Instr i) {
        if (i instanceof Instr.Instr_asig) {
            Instr.Instr_asig ia = (Instr.Instr_asig) i;
            return exp(ia.exp1) + ":=\n" + exp(ia.exp2);
        }
        if (i instanceof Instr.Instr_if) {
            Instr.Instr_if ii = (Instr.Instr_if) i;
            return "<if>\n" + exp(ii.exp) + ":\n" + linstr0(ii.instrs) + "<end_if>\n";
        }
        if (i instanceof Instr.Instr_ifelse) {
            Instr.Instr_ifelse ie = (Instr.Instr_ifelse) i;
            return "<if>\n" + exp(ie.exp) + ":\n" + linstr0(ie.instrs1) + "<else>\n" + linstr0(ie.instrs2) + "<end_if>\n";
        }
        if (i instanceof Instr.Instr_while) {
            Instr.Instr_while iw = (Instr.Instr_while) i;
            return "<while>\n" + exp(iw.exp) + ":\n" + linstr0(iw.instrs) + "<end_while>\n";
        }
        if (i instanceof Instr.Instr_lectura) {
            return "<input>\n" + exp(((Instr.Instr_lectura) i).exp);
        }
        if (i instanceof Instr.Instr_escritura) {
            return "<output>\n" + exp(((Instr.Instr_escritura) i).exp);
        }
        if (i instanceof Instr.Instr_reserva) {
            return "<new>\n" + exp(((Instr.Instr_reserva) i).exp);
        }
        if (i instanceof Instr.Instr_liberacion) {
            return "<dispose>\n" + exp(((Instr.Instr_liberacion) i).exp);
        }
        if (i instanceof Instr.Instr_invocar) {
            Instr.Instr_invocar iv = (Instr.Instr_invocar) i;
            return "@\n" + iv.id + "$f:" + iv.fila + ",c:" + iv.col + "$\n(\n" + lexps0(iv.exps) + ")\n";
        }
        Instr.Instr_compuesta ic = (Instr.Instr_compuesta) i;
        return "<block>\n" + ldec0(ic.decs) + linstr0(ic.instrs) + "<end_block>\n";
    }

    private String lexps0(LExps_0 e0) {
        if (e0 instanceof LExps_0.Si_exps) {
            return lexps(((LExps_0.Si_exps) e0).exps);
        }
        return "";
    }

    private String lexps(LExps e) {
        if (e instanceof LExps.Una_exp) {
            return exp(((LExps.Una_exp) e).exp);
        }
        LExps.Muchas_exps me = (LExps.Muchas_exps) e;
        return lexps(me.exps) + ",\n" + exp(me.exp);
    }

    private String exp(Exp e) {
        if (e instanceof Exp.Exp_suma) {
            Exp.Exp_suma x = (Exp.Exp_suma) e;
            return imprimeOpnd(x.opnd0, 1, false) + "+$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 1, true);
        }
        if (e instanceof Exp.Exp_resta) {
            Exp.Exp_resta x = (Exp.Exp_resta) e;
            return imprimeOpnd(x.opnd0, 1, false) + "-$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 1, true);
        }
        if (e instanceof Exp.Exp_mul) {
            Exp.Exp_mul x = (Exp.Exp_mul) e;
            return imprimeOpnd(x.opnd0, 3, false) + "*$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp.Exp_div) {
            Exp.Exp_div x = (Exp.Exp_div) e;
            return imprimeOpnd(x.opnd0, 3, false) + "/$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp.Exp_mod) {
            Exp.Exp_mod x = (Exp.Exp_mod) e;
            return imprimeOpnd(x.opnd0, 3, false) + "%$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 3, true);
        }
        if (e instanceof Exp.Exp_and) {
            Exp.Exp_and x = (Exp.Exp_and) e;
            return imprimeOpnd(x.opnd0, 4, false) + "&$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 4, true);
        }
        if (e instanceof Exp.Exp_or) {
            Exp.Exp_or x = (Exp.Exp_or) e;
            return imprimeOpnd(x.opnd0, 2, false) + "|$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 2, true);
        }
        if (e instanceof Exp.Exp_menor) {
            Exp.Exp_menor x = (Exp.Exp_menor) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_mayor) {
            Exp.Exp_mayor x = (Exp.Exp_mayor) e;
            return imprimeOpnd(x.opnd0, 0, false) + ">$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_menor_igual) {
            Exp.Exp_menor_igual x = (Exp.Exp_menor_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<=$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_mayor_igual) {
            Exp.Exp_mayor_igual x = (Exp.Exp_mayor_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + ">=$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_igual) {
            Exp.Exp_igual x = (Exp.Exp_igual) e;
            return imprimeOpnd(x.opnd0, 0, false) + "=$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_distinto) {
            Exp.Exp_distinto x = (Exp.Exp_distinto) e;
            return imprimeOpnd(x.opnd0, 0, false) + "<>$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd1, 0, true);
        }
        if (e instanceof Exp.Exp_menos_unario) {
            Exp.Exp_menos_unario x = (Exp.Exp_menos_unario) e;
            return "-$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd, 5, true);
        }
        if (e instanceof Exp.Exp_not) {
            Exp.Exp_not x = (Exp.Exp_not) e;
            return "!$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd, 5, true);
        }
        if (e instanceof Exp.Exp_asterisco_unario) {
            Exp.Exp_asterisco_unario x = (Exp.Exp_asterisco_unario) e;
            return "*$f:" + x.fila + ",c:" + x.col + "$\n" + imprimeOpnd(x.opnd, 7, true);
        }
        if (e instanceof Exp.Iden) {
            Exp.Iden x = (Exp.Iden) e;
            return x.id + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Lit_int) {
            Exp.Lit_int x = (Exp.Lit_int) e;
            return x.val + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Lit_real) {
            Exp.Lit_real x = (Exp.Lit_real) e;
            return x.val + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Lit_bool) {
            Exp.Lit_bool x = (Exp.Lit_bool) e;
            return x.val + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Lit_string) {
            Exp.Lit_string x = (Exp.Lit_string) e;
            return x.val + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Exp_null) {
            Exp.Exp_null x = (Exp.Exp_null) e;
            return "<null>$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Exp_campo) {
            Exp.Exp_campo x = (Exp.Exp_campo) e;
            return imprimeOpnd(x.base, 6, false) + ".\n" + x.id + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        if (e instanceof Exp.Exp_flecha) {
            Exp.Exp_flecha x = (Exp.Exp_flecha) e;
            return imprimeOpnd(x.base, 6, false) + "->\n" + x.id + "$f:" + x.fila + ",c:" + x.col + "$\n";
        }
        Exp.Exp_array x = (Exp.Exp_array) e;
        return imprimeOpnd(x.opnd0, 6, false) + "[$f:" + x.fila + ",c:" + x.col + "$\n" + exp(x.opnd1) + "]\n";
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