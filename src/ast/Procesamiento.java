package ast;

import static asint.SintaxisAbstractaTiny.*;

public interface Procesamiento {
    void process(Si_dec ld);
    void process(No_dec ld);
    void process(Muchas_decs ld);
    void process(Una_dec ld);
    void process(Si_instr ld);
    void process(No_instr ld);
    // Removed LInstr_0 methods
    void process(Muchas_instr ld);
    void process(Una_instr ld);
    void process(Si_procparam ld);
    void process(No_procparam ld);
    void process(Muchos_procparam ld);
    void process(Un_procparam ld);
    void process(Si_exps ld);
    void process(No_exps ld);
    void process(Muchas_exps ld);
    void process(Una_exp ld);
    void process(Muchos_camposrecord ld);
    void process(Un_camporecord ld);
    
    void process(Dec_var d);
    void process(Dec_tipo d);
    void process(Dec_proc d);
    
    void process(Instr_asig i);
    void process(Instr_if i);
    void process(Instr_ifelse i);
    void process(Instr_while i);
    void process(Instr_lectura i);
    void process(Instr_escritura i);
    void process(Instr_reserva i);
    void process(Instr_liberacion i);
    void process(Instr_invocar i);
    void process(Instr_compuesta i);
    
    void process(Exp_suma e);
    void process(Exp_resta e);
    void process(Exp_mul e);
    void process(Exp_div e);
    void process(Exp_mod e);
    void process(Exp_and e);
    void process(Exp_or e);
    void process(Exp_mayor e);
    void process(Exp_menor e);
    void process(Exp_mayor_igual e);
    void process(Exp_menor_igual e);
    void process(Exp_igual e);
    void process(Exp_distinto e);
    void process(Exp_menos_unario e);
    void process(Exp_not e);
    void process(Exp_asterisco_unario e);
    void process(Iden e);
    void process(Lit_int e);
    void process(Lit_real e);
    void process(Lit_bool e);
    void process(Lit_string e);
    void process(Exp_null e);
    void process(Exp_campo e);
    void process(Exp_flecha e);
    void process(Exp_array e);
    
    void process(Tipo_int t);
    void process(Tipo_real t);
    void process(Tipo_bool t);
    void process(Tipo_string t);
    void process(Tipo_id t);
    void process(Tipo_array t);
    void process(Tipo_pointer t);
    void process(Tipo_record t);
    
    void process(Param_ref p);
    void process(Param_val p);
    void process(Prog p);
    void process(CamposRecord cr);
}