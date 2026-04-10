package ast;

import static ast.SintaxisAbstractaTiny.*;

public class ImpresionVisitante extends ProcesamientoDef {
    private final StringBuilder out = new StringBuilder();

    public String imprime(Prog p) {
        out.setLength(0);
        if (p != null) {
            p.process(this);
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
        opnd.process(this);
        if (needsParens) {
            out.append(")\n");
        }
    }

    @Override
    public void process(Prog p) {
        out.append("<program>\n");
        if (p.decs != null) {
            p.decs.process(this);
        }
        if (p.instrs != null) {
            p.instrs.process(this);
        }
        out.append("<end_program>\n");
    }

    @Override
    public void process(Si_dec ld) {
        ld.decs.process(this);
        out.append("--\n");
    }

    @Override
    public void process(No_dec ld) {
    }

    @Override
    public void process(Muchas_decs ld) {
        ld.decs.process(this);
        out.append(";\n");
        ld.dec.process(this);
    }

    @Override
    public void process(Una_dec ld) {
        ld.dec.process(this);
    }

    @Override
    public void process(Si_instr ld) {
        ld.instrs.process(this);
    }

    @Override
    public void process(No_instr ld) {
    }

    @Override
    public void process(Si_instr_l ld) {
        ld.instrs.process(this);
    }

    @Override
    public void process(No_instr_l ld) {
    }

    @Override
    public void process(Muchas_instr ld) {
        ld.instrs.process(this);
        out.append(";\n");
        ld.instr.process(this);
    }

    @Override
    public void process(Una_instr ld) {
        ld.instr.process(this);
    }

    @Override
    public void process(Si_procparam ld) {
        ld.params.process(this);
    }

    @Override
    public void process(No_procparam ld) {
    }

    @Override
    public void process(Muchos_procparam ld) {
        ld.params.process(this);
        out.append(",\n");
        ld.param.process(this);
    }

    @Override
    public void process(Un_procparam ld) {
        ld.param.process(this);
    }

    @Override
    public void process(Si_exps ld) {
        ld.exps.process(this);
    }

    @Override
    public void process(No_exps ld) {
    }

    @Override
    public void process(Muchas_exps ld) {
        ld.exps.process(this);
        out.append(",\n");
        ld.exp.process(this);
    }

    @Override
    public void process(Una_exp ld) {
        ld.exp.process(this);
    }

    @Override
    public void process(Muchos_camposrecord ld) {
        ld.lista.process(this);
        out.append(";\n");
        ld.campo.process(this);
    }

    @Override
    public void process(Un_camporecord ld) {
        ld.campo.process(this);
    }

    @Override
    public void process(Dec_var d) {
        out.append("<decvar>\n").append(d.id).append("$f:").append(d.leeFila()).append(",c:").append(d.leeCol())
                .append("$\n:\n");
        d.tipo.process(this);
    }

    @Override
    public void process(Dec_tipo d) {
        out.append("<dectype>\n").append(d.id).append("$f:").append(d.leeFila()).append(",c:").append(d.leeCol())
                .append("$\n:\n");
        d.tipo.process(this);
    }

    @Override
    public void process(Dec_proc d) {
        out.append("<decproc>\n").append(d.id).append("$f:").append(d.leeFila()).append(",c:").append(d.leeCol())
                .append("$\n(\n");
        d.params.process(this);
        out.append(")\n");
        d.decs.process(this);
        d.instrs.process(this);
        out.append("<end_proc>\n");
    }

    @Override
    public void process(Instr_asig i) {
        i.exp1.process(this);
        out.append(":=\n");
        i.exp2.process(this);
    }

    @Override
    public void process(Instr_if i) {
        out.append("<if>\n");
        i.exp.process(this);
        out.append(":\n");
        i.instrs.process(this);
        out.append("<end_if>\n");
    }

    @Override
    public void process(Instr_ifelse i) {
        out.append("<if>\n");
        i.exp.process(this);
        out.append(":\n");
        i.instrs1.process(this);
        out.append("<else>\n");
        i.instrs2.process(this);
        out.append("<end_if>\n");
    }

    @Override
    public void process(Instr_while i) {
        out.append("<while>\n");
        i.exp.process(this);
        out.append(":\n");
        i.instrs.process(this);
        out.append("<end_while>\n");
    }

    @Override
    public void process(Instr_lectura i) {
        out.append("<input>\n");
        i.exp.process(this);
    }

    @Override
    public void process(Instr_escritura i) {
        out.append("<output>\n");
        i.exp.process(this);
    }

    @Override
    public void process(Instr_reserva i) {
        out.append("<new>\n");
        i.exp.process(this);
    }

    @Override
    public void process(Instr_liberacion i) {
        out.append("<dispose>\n");
        i.exp.process(this);
    }

    @Override
    public void process(Instr_invocar i) {
        out.append("@\n").append(i.id).append("$f:").append(i.leeFila()).append(",c:").append(i.leeCol())
                .append("$\n(\n");
        i.exps.process(this);
        out.append(")\n");
    }

    @Override
    public void process(Instr_compuesta i) {
        out.append("<block>\n");
        i.decs.process(this);
        i.instrs.process(this);
        out.append("<end_block>\n");
    }

    @Override
    public void process(Exp_suma e) {
        imprimeOpnd(e.opnd0, 1, false);
        out.append("+$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 1, true);
    }
    @Override
    public void process(Exp_resta e) {
        imprimeOpnd(e.opnd0, 1, false);
        out.append("-$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 1, true);
    }
    @Override
    public void process(Exp_mul e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("*$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }
    @Override
    public void process(Exp_div e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("/$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }
    @Override
    public void process(Exp_mod e) {
        imprimeOpnd(e.opnd0, 3, false);
        out.append("%$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 3, true);
    }
    @Override
    public void process(Exp_and e) {
        imprimeOpnd(e.opnd0, 4, false);
        out.append("&$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 4, true);
    }
    @Override
    public void process(Exp_or e) {
        imprimeOpnd(e.opnd0, 2, false);
        out.append("|$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 2, true);
    }
    @Override
    public void process(Exp_mayor e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append(">$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }
    @Override
    public void process(Exp_menor e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }
    @Override
    public void process(Exp_mayor_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append(">=$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }
    @Override
    public void process(Exp_menor_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<=$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }
    @Override
    public void process(Exp_igual e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("=$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }
    @Override
    public void process(Exp_distinto e) {
        imprimeOpnd(e.opnd0, 0, false);
        out.append("<>$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd1, 0, true);
    }

    @Override
    public void process(Exp_menos_unario e) {
        out.append("-$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd, 5, true);
    }
    @Override
    public void process(Exp_not e) {
        out.append("!$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd, 5, true);
    }
    @Override
    public void process(Exp_asterisco_unario e) {
        out.append("*$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        imprimeOpnd(e.opnd, 7, true);
    }

    @Override
    public void process(Iden e) {
        out.append(e.id).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Lit_int e) {
        out.append(e.val).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Lit_real e) {
        out.append(e.val).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Lit_bool e) {
        out.append(e.val).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Lit_string e) {
        out.append(e.val).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Exp_null e) {
        out.append("<null>$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Exp_campo e) {
        imprimeOpnd(e.base, 6, false);
        out.append(".\n").append(e.id).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Exp_flecha e) {
        imprimeOpnd(e.base, 6, false);
        out.append("->\n").append(e.id).append("$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
    }

    @Override
    public void process(Exp_array e) {
        imprimeOpnd(e.opnd0, 6, false);
        out.append("[$f:").append(e.leeFila()).append(",c:").append(e.leeCol()).append("$\n");
        e.opnd1.process(this);
        out.append("]\n");
    }

    @Override
    public void process(Tipo_int t) {
        out.append("<int>\n");
    }

    @Override
    public void process(Tipo_real t) {
        out.append("<real>\n");
    }

    @Override
    public void process(Tipo_bool t) {
        out.append("<bool>\n");
    }

    @Override
    public void process(Tipo_string t) {
        out.append("<string>\n");
    }

    @Override
    public void process(Tipo_id t) {
        out.append(t.id).append("$f:").append(t.leeFila()).append(",c:").append(t.leeCol()).append("$\n");
    }

    @Override
    public void process(Tipo_array t) {
        out.append("<array>\n[\n").append(t.dim).append("\n]$f:").append(t.leeFila()).append(",c:").append(t.leeCol())
                .append("$\n<of>\n");
        t.tipo.process(this);
    }

    @Override
    public void process(Tipo_pointer t) {
        out.append("<pointer>\n");
        t.tipo.process(this);
    }

    @Override
    public void process(Tipo_record t) {
        out.append("<record>\n");
        t.lista.process(this);
        out.append("<end_record>\n");
    }

    @Override
    public void process(Param_ref p) {
        out.append("<ref>\n").append(p.id).append("$f:").append(p.leeFila()).append(",c:").append(p.leeCol())
                .append("$\n:\n");
        p.tipo.process(this);
    }

    @Override
    public void process(Param_val p) {
        out.append(p.id).append("$f:").append(p.leeFila()).append(",c:").append(p.leeCol()).append("$\n:\n");
        p.tipo.process(this);
    }

    @Override
    public void process(CamposRecord cr) {
        out.append(cr.id).append("$f:").append(cr.leeFila()).append(",c:").append(cr.leeCol()).append("$\n:\n");
        cr.tipo.process(this);
    }
}
