package ast;

public class ImpresionVisitante implements Visitor {
    private final StringBuilder out = new StringBuilder();

    public String imprime(Prog p) {
        out.setLength(0);
        if (p != null) {
            p.accept(this);
        }
        return out.toString();
    }

    private void imprimeOpnd(Exp opnd, int minPrior, boolean isRight) {
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
            out.append("(\n");
        }
        opnd.accept(this);
        if (needsParens) {
            out.append(")\n");
        }
    }

    public void visit(Prog p) {
        out.append("<program>\n");
        if (p.decs != null) {
            p.decs.accept(this);
        }
        if (p.instrs != null) {
            p.instrs.accept(this);
        }
        out.append("<end_program>\n");
    }

    public void visit(LDec_0.Si_dec ld) {
        ld.decs.accept(this);
        out.append("--\n");
    }

    public void visit(LDec_0.No_dec ld) {
    }

    public void visit(LDec.Muchas_decs ld) {
        ld.decs.accept(this);
        out.append(";\n");
        ld.dec.accept(this);
    }

    public void visit(LDec.Una_dec ld) {
        ld.dec.accept(this);
    }

    public void visit(Instrs_0.Si_instr ld) {
        ld.instrs.accept(this);
    }

    public void visit(Instrs_0.No_instr ld) {
    }

    public void visit(LInstr_0.Si_instr_l ld) {
        ld.instrs.accept(this);
    }

    public void visit(LInstr_0.No_instr_l ld) {
    }

    public void visit(LInstr.Muchas_instr ld) {
        ld.instrs.accept(this);
        out.append(";\n");
        ld.instr.accept(this);
    }

    public void visit(LInstr.Una_instr ld) {
        ld.instr.accept(this);
    }

    public void visit(LProcParams_0.Si_procparam ld) {
        ld.params.accept(this);
    }

    public void visit(LProcParams_0.No_procparam ld) {
    }

    public void visit(LProcParams.Muchos_procparam ld) {
        ld.params.accept(this);
        out.append(",\n");
        ld.param.accept(this);
    }

    public void visit(LProcParams.Un_procparam ld) {
        ld.param.accept(this);
    }

    public void visit(LExps_0.Si_exps ld) {
        ld.exps.accept(this);
    }

    public void visit(LExps_0.No_exps ld) {
    }

    public void visit(LExps.Muchas_exps ld) {
        ld.exps.accept(this);
        out.append(",\n");
        ld.exp.accept(this);
    }

    public void visit(LExps.Una_exp ld) {
        ld.exp.accept(this);
    }

    public void visit(ListaRecord.Muchos_camposrecord ld) {
        ld.lista.accept(this);
        out.append(";\n");
        ld.campo.accept(this);
    }

    public void visit(ListaRecord.Un_camporecord ld) {
        ld.campo.accept(this);
    }

    public void visit(CamposRecord ld) {
        out.append(ld.id).append("$f:").append(ld.fila).append(",c:").append(ld.col).append("$\n:\n");
        ld.tipo.accept(this);
    }

    public void visit(Dec.Dec_var d) {
        out.append("<decvar>\n").append(d.id).append("$f:").append(d.fila).append(",c:").append(d.col).append("$\n:\n");
        d.tipo.accept(this);
    }

    public void visit(Dec.Dec_tipo d) {
        out.append("<dectype>\n").append(d.id).append("$f:").append(d.fila).append(",c:").append(d.col).append("$\n:\n");
        d.tipo.accept(this);
    }

    public void visit(Dec.Dec_proc d) {
        out.append("<decproc>\n").append(d.id).append("$f:").append(d.fila).append(",c:").append(d.col).append("$\n(\n");
        d.params.accept(this);
        out.append(")\n");
        d.decs.accept(this);
        d.instrs.accept(this);
        out.append("<end_proc>\n");
    }

    public void visit(Instr.Instr_asig i) {
        i.exp1.accept(this);
        out.append(":=\n");
        i.exp2.accept(this);
    }

    public void visit(Instr.Instr_if i) {
        out.append("<if>\n");
        i.exp.accept(this);
        out.append(":\n");
        i.instrs.accept(this);
        out.append("<end_if>\n");
    }

    public void visit(Instr.Instr_ifelse i) {
        out.append("<if>\n");
        i.exp.accept(this);
        out.append(":\n");
        i.instrs1.accept(this);
        out.append("<else>\n");
        i.instrs2.accept(this);
        out.append("<end_if>\n");
    }

    public void visit(Instr.Instr_while i) {
        out.append("<while>\n");
        i.exp.accept(this);
        out.append(":\n");
        i.instrs.accept(this);
        out.append("<end_while>\n");
    }

    public void visit(Instr.Instr_lectura i) {
        out.append("<input>\n");
        i.exp.accept(this);
    }

    public void visit(Instr.Instr_escritura i) {
        out.append("<output>\n");
        i.exp.accept(this);
    }

    public void visit(Instr.Instr_reserva i) {
        out.append("<new>\n");
        i.exp.accept(this);
    }

    public void visit(Instr.Instr_liberacion i) {
        out.append("<dispose>\n");
        i.exp.accept(this);
    }

    public void visit(Instr.Instr_invocar i) {
        out.append("@\n").append(i.id).append("$f:").append(i.fila).append(",c:").append(i.col).append("$\n(\n");
        i.exps.accept(this);
        out.append(")\n");
    }

    public void visit(Instr.Instr_compuesta i) {
        out.append("<block>\n");
        i.decs.accept(this);
        i.instrs.accept(this);
        out.append("<end_block>\n");
    }

    public void visit(Exp.Exp_suma e) {
        imprimeOpnd(e.opnd0, 1, false);
        out.append("+$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 1, true);
    }

    public void visit(Exp.Exp_resta e) {
        imprimeOpnd(e.opnd0, 1, false);
        out.append("-$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 1, true);
    }

    public void visit(Exp.Exp_mul e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("*$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }

    public void visit(Exp.Exp_div e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("/$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }

    public void visit(Exp.Exp_mod e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("%$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }

    public void visit(Exp.Exp_and e) {
        imprimeOpnd(e.opnd0, 4, false);
        out.append("&$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 4, true);
    }

    public void visit(Exp.Exp_or e) {
        imprimeOpnd(e.opnd0, 2, false);
        out.append("|$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 2, true);
    }

    public void visit(Exp.Exp_mayor e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append(">$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_menor e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_mayor_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append(">=$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_menor_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<=$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("=$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_distinto e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<>$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    public void visit(Exp.Exp_menos_unario e) {
        out.append("-$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd, 5, true);
    }

    public void visit(Exp.Exp_not e) {
        out.append("!$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd, 5, true);
    }

    public void visit(Exp.Exp_asterisco_unario e) {
        out.append("*$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        imprimeOpnd(e.opnd, 7, true);
    }

    public void visit(Exp.Iden e) {
        out.append(e.id).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Lit_int e) {
        out.append(e.val).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Lit_real e) {
        out.append(e.val).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Lit_bool e) {
        out.append(e.val).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Lit_string e) {
        out.append(e.val).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Exp_null e) {
        out.append("<null>$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Exp_campo e) {
        imprimeOpnd(e.base, 6, false);
        out.append(".\n").append(e.id).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Exp_flecha e) {
        imprimeOpnd(e.base, 6, false);
        out.append("->\n").append(e.id).append("$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
    }

    public void visit(Exp.Exp_array e) {
        imprimeOpnd(e.opnd0, 6, false);
        out.append("[$f:").append(e.fila).append(",c:").append(e.col).append("$\n");
        e.opnd1.accept(this);
        out.append("]\n");
    }

    public void visit(Tipo.Tipo_int t) {
        out.append("<int>\n");
    }

    public void visit(Tipo.Tipo_real t) {
        out.append("<real>\n");
    }

    public void visit(Tipo.Tipo_bool t) {
        out.append("<bool>\n");
    }

    public void visit(Tipo.Tipo_string t) {
        out.append("<string>\n");
    }

    public void visit(Tipo.Tipo_id t) {
        out.append(t.id).append("$f:").append(t.fila).append(",c:").append(t.col).append("$\n");
    }

    public void visit(Tipo.Tipo_array t) {
        out.append("<array>\n[\n").append(t.dim).append("\n]$f:").append(t.fila).append(",c:").append(t.col).append("$\n<of>\n");
        t.tipo.accept(this);
    }

    public void visit(Tipo.Tipo_pointer t) {
        out.append("<pointer>\n");
        t.tipo.accept(this);
    }

    public void visit(Tipo.Tipo_record t) {
        out.append("<record>\n");
        t.lista.accept(this);
        out.append("<end_record>\n");
    }

    public void visit(Param.Param_ref p) {
        out.append("<ref>\n").append(p.id).append("$f:").append(p.fila).append(",c:").append(p.col).append("$\n:\n");
        p.tipo.accept(this);
    }

    public void visit(Param.Param_val p) {
        out.append(p.id).append("$f:").append(p.fila).append(",c:").append(p.col).append("$\n:\n");
        p.tipo.accept(this);
    }
}