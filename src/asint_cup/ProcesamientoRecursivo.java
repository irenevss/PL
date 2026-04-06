package asint_cup;

import java.util.List;

public class ProcesamientoRecursivo {
    private final StringBuilder out = new StringBuilder();

    public String imprime(ASTNode raiz) {
        out.setLength(0);
        imprimeNodo(raiz);
        return out.toString();
    }

    private void imprimeNodo(ASTNode n) {
        String k = n.kind();
        if ("prog".equals(k)) {
            emit("<program>");
            imprimeNodo(n.children().get(0));
            imprimeNodo(n.children().get(1));
            emit("<end_program>");
            return;
        }

        if ("si_dec".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("no_dec".equals(k)) {
            return;
        }
        if ("muchas_decs".equals(k)) {
            imprimeNodo(n.children().get(0));
            emit(";");
            imprimeNodo(n.children().get(1));
            return;
        }
        if ("una_dec".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }

        if ("dec_var".equals(k)) {
            emit("<decvar>");
            emitLink(n.lexema(), n);
            emit(":");
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("dec_tipo".equals(k)) {
            emit("<dectype>");
            emitLink(n.lexema(), n);
            emit(":");
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("dec_proc".equals(k)) {
            emit("<decproc>");
            emitLink(n.lexema(), n);
            emit("(");
            imprimeNodo(n.children().get(0));
            emit(")");
            imprimeNodo(n.children().get(1));
            imprimeNodo(n.children().get(2));
            emit("<end_proc>");
            return;
        }

        if ("si_procparam".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("no_procparam".equals(k)) {
            return;
        }
        if ("muchos_procparam".equals(k)) {
            imprimeNodo(n.children().get(0));
            emit(",");
            imprimeNodo(n.children().get(1));
            return;
        }
        if ("un_procparam".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("param_ref".equals(k)) {
            emit("<ref>");
            emitLink(n.lexema(), n);
            emit(":");
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("param_val".equals(k)) {
            emitLink(n.lexema(), n);
            emit(":");
            imprimeNodo(n.children().get(0));
            return;
        }

        if ("tipo_int".equals(k)) {
            emit("<int>");
            return;
        }
        if ("tipo_real".equals(k)) {
            emit("<real>");
            return;
        }
        if ("tipo_bool".equals(k)) {
            emit("<bool>");
            return;
        }
        if ("tipo_string".equals(k)) {
            emit("<string>");
            return;
        }
        if ("tipo_id".equals(k)) {
            emitLink(n.lexema(), n);
            return;
        }
        if ("tipo_array".equals(k)) {
            emit("<array>");
            emit("[");
            emit(n.lexema());
            emitLink("]", n);
            emit("<of>");
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("tipo_record".equals(k)) {
            emit("<record>");
            imprimeNodo(n.children().get(0));
            emit("<end_record>");
            return;
        }
        if ("tipo_pointer".equals(k)) {
            emit("<pointer>");
            imprimeNodo(n.children().get(0));
            return;
        }

        if ("muchos_camposrecord".equals(k)) {
            imprimeNodo(n.children().get(0));
            emit(";");
            imprimeNodo(n.children().get(1));
            return;
        }
        if ("un_camporecord".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("camporecord".equals(k)) {
            emitLink(n.lexema(), n);
            emit(":");
            imprimeNodo(n.children().get(0));
            return;
        }

        if ("si_instr".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("no_instr".equals(k)) {
            return;
        }
        if ("muchas_instr".equals(k)) {
            imprimeNodo(n.children().get(0));
            emit(";");
            imprimeNodo(n.children().get(1));
            return;
        }
        if ("una_instr".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }

        if ("instr_asig".equals(k)) {
            imprimeExp(n.children().get(0));
            emit(":=");
            imprimeExp(n.children().get(1));
            return;
        }
        if ("instr_if".equals(k)) {
            emit("<if>");
            imprimeExp(n.children().get(0));
            emit(":");
            imprimeNodo(n.children().get(1));
            emit("<end_if>");
            return;
        }
        if ("instr_ifelse".equals(k)) {
            emit("<if>");
            imprimeExp(n.children().get(0));
            emit(":");
            imprimeNodo(n.children().get(1));
            emit("<else>");
            imprimeNodo(n.children().get(2));
            emit("<end_if>");
            return;
        }
        if ("instr_while".equals(k)) {
            emit("<while>");
            imprimeExp(n.children().get(0));
            emit(":");
            imprimeNodo(n.children().get(1));
            emit("<end_while>");
            return;
        }
        if ("instr_lectura".equals(k)) {
            emit("<input>");
            imprimeExp(n.children().get(0));
            return;
        }
        if ("instr_escritura".equals(k)) {
            emit("<output>");
            imprimeExp(n.children().get(0));
            return;
        }
        if ("instr_reserva".equals(k)) {
            emit("<new>");
            imprimeExp(n.children().get(0));
            return;
        }
        if ("instr_liberacion".equals(k)) {
            emit("<dispose>");
            imprimeExp(n.children().get(0));
            return;
        }
        if ("instr_invocar".equals(k)) {
            emit("@");
            emitLink(n.lexema(), n);
            emit("(");
            imprimeNodo(n.children().get(0));
            emit(")");
            return;
        }
        if ("instr_compuesta".equals(k)) {
            emit("<block>");
            imprimeNodo(n.children().get(0));
            imprimeNodo(n.children().get(1));
            emit("<end_block>");
            return;
        }

        if ("si_exps".equals(k)) {
            imprimeNodo(n.children().get(0));
            return;
        }
        if ("no_exps".equals(k)) {
            return;
        }
        if ("muchas_exps".equals(k)) {
            imprimeNodo(n.children().get(0));
            emit(",");
            imprimeExp(n.children().get(1));
            return;
        }
        if ("una_exp".equals(k)) {
            imprimeExp(n.children().get(0));
            return;
        }

        if (esExpresion(k)) {
            imprimeExp(n);
            return;
        }

        List<ASTNode> hijos = n.children();
        for (ASTNode h : hijos) {
            imprimeNodo(h);
        }
    }

    private void imprimeExp(ASTNode e) {
        String k = e.kind();
        if (esAtomo(k)) {
            emitLink(e.lexema(), e);
            return;
        }

        if (esBinaria(k)) {
            ASTNode left = e.children().get(0);
            ASTNode right = e.children().get(1);
            imprimeOpnd(e, left, false);
            emitLink(operadorBinario(k), e);
            imprimeOpnd(e, right, true);
            return;
        }

        if ("exp_menos_unario".equals(k) || "exp_not".equals(k) || "exp_indirec".equals(k)) {
            emitLink(operadorUnario(k), e);
            imprimeOpnd(e, e.children().get(0), true);
            return;
        }

        if ("exp_array".equals(k)) {
            imprimeOpnd(e, e.children().get(0), false);
            emitLink("[", e);
            imprimeExp(e.children().get(1));
            emit("]");
            return;
        }

        if ("exp_campo".equals(k)) {
            imprimeOpnd(e, e.children().get(0), false);
            emit(".");
            emitLink(e.lexema(), e);
            return;
        }

        if ("exp_flecha".equals(k)) {
            imprimeOpnd(e, e.children().get(0), false);
            emit("->");
            emitLink(e.lexema(), e);
            return;
        }
    }

    private void imprimeOpnd(ASTNode parent, ASTNode opnd, boolean isRight) {
        int minPrior = prioridad(parent);
        int p = prioridad(opnd);
        boolean paren = false;
        if (p < minPrior) {
            paren = true;
        }
        if (p == minPrior && isRight && esIzqAsociativa(parent)) {
            paren = true;
        }
        if (p == minPrior && esBinaria(parent.kind()) && esBinaria(opnd.kind())
                && !parent.kind().equals(opnd.kind())) {
            paren = true;
        }
        if (p == minPrior && prioridad(parent) == 1 && esBinaria(opnd.kind())) {
            paren = true;
        }
        if (paren) {
            emit("(");
        }
        imprimeExp(opnd);
        if (paren) {
            emit(")");
        }
    }

    private boolean esIzqAsociativa(ASTNode n) {
        int p = prioridad(n);
        return p == 1 || p == 2 || p == 3 || p == 5;
    }

    private int prioridad(ASTNode e) {
        String k = e.kind();
        if (esAtomo(k)) return 6;
        if ("exp_array".equals(k) || "exp_campo".equals(k) || "exp_flecha".equals(k)) return 5;
        if ("exp_indirec".equals(k) || "exp_menos_unario".equals(k) || "exp_not".equals(k)) return 4;
        if ("exp_mul".equals(k) || "exp_div".equals(k) || "exp_mod".equals(k) || "exp_and".equals(k)) return 3;
        if ("exp_suma".equals(k) || "exp_resta".equals(k) || "exp_or".equals(k)) return 2;
        if ("exp_menor".equals(k) || "exp_mayor".equals(k) || "exp_menor_igual".equals(k)
                || "exp_mayor_igual".equals(k) || "exp_igual".equals(k) || "exp_distinto".equals(k)) return 1;
        return 0;
    }

    private boolean esExpresion(String k) {
        return esAtomo(k) || esBinaria(k)
                || "exp_menos_unario".equals(k) || "exp_not".equals(k) || "exp_indirec".equals(k)
                || "exp_array".equals(k) || "exp_campo".equals(k) || "exp_flecha".equals(k);
    }

    private boolean esAtomo(String k) {
        return "iden".equals(k) || "lit_int".equals(k) || "lit_real".equals(k)
                || "lit_bool".equals(k) || "lit_string".equals(k) || "exp_null".equals(k);
    }

    private boolean esBinaria(String k) {
        return "exp_suma".equals(k) || "exp_resta".equals(k) || "exp_mul".equals(k) || "exp_div".equals(k)
                || "exp_mod".equals(k) || "exp_and".equals(k) || "exp_or".equals(k)
                || "exp_menor".equals(k) || "exp_mayor".equals(k)
                || "exp_menor_igual".equals(k) || "exp_mayor_igual".equals(k)
                || "exp_igual".equals(k) || "exp_distinto".equals(k);
    }

    private String operadorBinario(String k) {
        if ("exp_suma".equals(k)) return "+";
        if ("exp_resta".equals(k)) return "-";
        if ("exp_mul".equals(k)) return "*";
        if ("exp_div".equals(k)) return "/";
        if ("exp_mod".equals(k)) return "%";
        if ("exp_and".equals(k)) return "&";
        if ("exp_or".equals(k)) return "|";
        if ("exp_menor".equals(k)) return "<";
        if ("exp_mayor".equals(k)) return ">";
        if ("exp_menor_igual".equals(k)) return "<=";
        if ("exp_mayor_igual".equals(k)) return ">=";
        if ("exp_igual".equals(k)) return "=";
        return "<>";
    }

    private String operadorUnario(String k) {
        if ("exp_not".equals(k)) return "!";
        if ("exp_indirec".equals(k)) return "*";
        return "-";
    }

    private void emit(String token) {
        out.append(token).append('\n');
    }

    private void emitLink(String token, ASTNode n) {
        out.append(token);
        if (n.fila() > 0 && n.columna() > 0) {
            out.append("$f:").append(n.fila()).append(",c:").append(n.columna()).append("$");
        }
        out.append('\n');
    }
}
