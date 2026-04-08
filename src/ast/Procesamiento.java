package ast;

public interface Procesamiento {
    void process(LDec_0.Si_dec ld);
    void process(LDec_0.No_dec ld);
    void process(LDec.Muchas_decs ld);
    void process(LDec.Una_dec ld);
    void process(Instrs_0.Si_instr ld);
    void process(Instrs_0.No_instr ld);
    void process(LInstr_0.Si_instr_l ld);
    void process(LInstr_0.No_instr_l ld);
    void process(LInstr.Muchas_instr ld);
    void process(LInstr.Una_instr ld);
    void process(LProcParams_0.Si_procparam ld);
    void process(LProcParams_0.No_procparam ld);
    void process(LProcParams.Muchos_procparam ld);
    void process(LProcParams.Un_procparam ld);
    void process(LExps_0.Si_exps ld);
    void process(LExps_0.No_exps ld);
    void process(LExps.Muchas_exps ld);
    void process(LExps.Una_exp ld);
    void process(ListaRecord.Muchos_camposrecord ld);
    void process(ListaRecord.Un_camporecord ld);
    
    void process(Dec.Dec_var d);
    void process(Dec.Dec_tipo d);
    void process(Dec.Dec_proc d);
    
    void process(Instr.Instr_asig i);
    void process(Instr.Instr_if i);
    void process(Instr.Instr_ifelse i);
    void process(Instr.Instr_while i);
    void process(Instr.Instr_lectura i);
    void process(Instr.Instr_escritura i);
    void process(Instr.Instr_reserva i);
    void process(Instr.Instr_liberacion i);
    void process(Instr.Instr_invocar i);
    void process(Instr.Instr_compuesta i);
    
    void process(Exp.Exp_suma e);
    void process(Exp.Exp_resta e);
    void process(Exp.Exp_mul e);
    void process(Exp.Exp_div e);
    void process(Exp.Exp_mod e);
    void process(Exp.Exp_and e);
    void process(Exp.Exp_or e);
    void process(Exp.Exp_mayor e);
    void process(Exp.Exp_menor e);
    void process(Exp.Exp_mayor_igual e);
    void process(Exp.Exp_menor_igual e);
    void process(Exp.Exp_igual e);
    void process(Exp.Exp_distinto e);
    void process(Exp.Exp_menos_unario e);
    void process(Exp.Exp_not e);
    void process(Exp.Exp_asterisco_unario e);
    void process(Exp.Iden e);
    void process(Exp.Lit_int e);
    void process(Exp.Lit_real e);
    void process(Exp.Lit_bool e);
    void process(Exp.Lit_string e);
    void process(Exp.Exp_null e);
    void process(Exp.Exp_campo e);
    void process(Exp.Exp_flecha e);
    void process(Exp.Exp_array e);
    
    void process(Tipo.Tipo_int t);
    void process(Tipo.Tipo_real t);
    void process(Tipo.Tipo_bool t);
    void process(Tipo.Tipo_string t);
    void process(Tipo.Tipo_id t);
    void process(Tipo.Tipo_array t);
    void process(Tipo.Tipo_pointer t);
    void process(Tipo.Tipo_record t);
    
    void process(Param.Param_ref p);
    void process(Param.Param_val p);
    void process(Prog p);
    void process(CamposRecord cr);
}