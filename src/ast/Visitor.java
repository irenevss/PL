package ast;

public interface Visitor {
    void visit(Prog p);
    void visit(LDec_0.Si_dec ld);
    void visit(LDec_0.No_dec ld);
    void visit(LDec.Muchas_decs ld);
    void visit(LDec.Una_dec ld);
    void visit(Instrs_0.Si_instr ld);
    void visit(Instrs_0.No_instr ld);
    void visit(LInstr_0.Si_instr_l ld);
    void visit(LInstr_0.No_instr_l ld);
    void visit(LInstr.Muchas_instr ld);
    void visit(LInstr.Una_instr ld);
    void visit(LProcParams_0.Si_procparam ld);
    void visit(LProcParams_0.No_procparam ld);
    void visit(LProcParams.Muchos_procparam ld);
    void visit(LProcParams.Un_procparam ld);
    void visit(LExps_0.Si_exps ld);
    void visit(LExps_0.No_exps ld);
    void visit(LExps.Muchas_exps ld);
    void visit(LExps.Una_exp ld);
    void visit(ListaRecord.Muchos_camposrecord ld);
    void visit(ListaRecord.Un_camporecord ld);
    void visit(CamposRecord ld);
    
    void visit(Dec.Dec_var d);
    void visit(Dec.Dec_tipo d);
    void visit(Dec.Dec_proc d);
    
    void visit(Instr.Instr_asig i);
    void visit(Instr.Instr_if i);
    void visit(Instr.Instr_ifelse i);
    void visit(Instr.Instr_while i);
    void visit(Instr.Instr_lectura i);
    void visit(Instr.Instr_escritura i);
    void visit(Instr.Instr_reserva i);
    void visit(Instr.Instr_liberacion i);
    void visit(Instr.Instr_invocar i);
    void visit(Instr.Instr_compuesta i);
    
    void visit(Exp.Exp_suma e);
    void visit(Exp.Exp_resta e);
    void visit(Exp.Exp_mul e);
    void visit(Exp.Exp_div e);
    void visit(Exp.Exp_mod e);
    void visit(Exp.Exp_and e);
    void visit(Exp.Exp_or e);
    void visit(Exp.Exp_mayor e);
    void visit(Exp.Exp_menor e);
    void visit(Exp.Exp_mayor_igual e);
    void visit(Exp.Exp_menor_igual e);
    void visit(Exp.Exp_igual e);
    void visit(Exp.Exp_distinto e);
    void visit(Exp.Exp_menos_unario e);
    void visit(Exp.Exp_not e);
    void visit(Exp.Exp_asterisco_unario e);
    void visit(Exp.Iden e);
    void visit(Exp.Lit_int e);
    void visit(Exp.Lit_real e);
    void visit(Exp.Lit_bool e);
    void visit(Exp.Lit_string e);
    void visit(Exp.Exp_null e);
    void visit(Exp.Exp_campo e);
    void visit(Exp.Exp_flecha e);
    void visit(Exp.Exp_array e);
    
    void visit(Tipo.Tipo_int t);
    void visit(Tipo.Tipo_real t);
    void visit(Tipo.Tipo_bool t);
    void visit(Tipo.Tipo_string t);
    void visit(Tipo.Tipo_id t);
    void visit(Tipo.Tipo_array t);
    void visit(Tipo.Tipo_pointer t);
    void visit(Tipo.Tipo_record t);
    
    void visit(Param.Param_ref p);
    void visit(Param.Param_val p);
}