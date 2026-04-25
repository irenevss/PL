package semantica;

import asint.ProcesamientoDef;
import asint.SintaxisAbstractaTiny.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Pretipado extends ProcesamientoDef {
    private final ErroresSemanticos errores;
    private final InfoSemantica info;
    private final Deque<Set<String>> pilaCampos = new ArrayDeque<>();

    public Pretipado(ErroresSemanticos errores, InfoSemantica info) {
        this.errores = errores;
        this.info = info;
    }

    public void procesa(Prog p) {
        p.process(this);
    }

    @Override
    public void process(Prog p) {
        if (p.decs != null) {
            p.decs.process(this);
        }
        if (p.instrs != null) {
            p.instrs.process(this);
        }
    }

    @Override
    public void process(Si_dec ld) {
        if (ld.decs != null) {
            ld.decs.process(this);
        }
    }

    @Override
    public void process(Muchas_decs ld) {
        if (ld.decs != null) {
            ld.decs.process(this);
        }
        if (ld.dec != null) {
            ld.dec.process(this);
        }
    }

    @Override
    public void process(Una_dec ld) {
        if (ld.dec != null) {
            ld.dec.process(this);
        }
    }

    @Override
    public void process(Dec_var d) {
        if (d.tipo != null) {
            d.tipo.process(this);
        }
    }

    @Override
    public void process(Dec_tipo d) {
        if (d.tipo != null) {
            d.tipo.process(this);
        }
    }

    @Override
    public void process(Dec_proc d) {
        if (d.params != null) {
            d.params.process(this);
        }
        if (d.decs != null) {
            d.decs.process(this);
        }
        if (d.instrs != null) {
            d.instrs.process(this);
        }
    }

    @Override
    public void process(Si_procparam ld) {
        if (ld.params != null) {
            ld.params.process(this);
        }
    }

    @Override
    public void process(Muchos_procparam ld) {
        if (ld.params != null) {
            ld.params.process(this);
        }
        if (ld.param != null) {
            ld.param.process(this);
        }
    }

    @Override
    public void process(Un_procparam ld) {
        if (ld.param != null) {
            ld.param.process(this);
        }
    }

    @Override
    public void process(Param_ref p) {
        if (p.tipo != null) {
            p.tipo.process(this);
        }
    }

    @Override
    public void process(Param_val p) {
        if (p.tipo != null) {
            p.tipo.process(this);
        }
    }

    @Override
    public void process(Tipo_id t) {
        Nodo d = info.vinculoDe(t);
        if (!(d instanceof Dec_tipo)) {
            errores.error(t, t.id + " no esta declarado como un tipo");
        }
    }

    @Override
    public void process(Tipo_array t) {
        try {
            int dim = Integer.parseInt(t.dim);
            if (dim < 0) {
                errores.error(t, "la dimension no puede ser negativa");
            }
        } catch (NumberFormatException ex) {
            errores.error(t, "la dimension no es un entero valido");
        }
        if (t.tipo != null) {
            t.tipo.process(this);
        }
    }

    @Override
    public void process(Tipo_pointer t) {
        if (t.tipo != null) {
            t.tipo.process(this);
        }
    }

    @Override
    public void process(Tipo_record t) {
        pilaCampos.push(new HashSet<>());
        if (t.lista != null) {
            t.lista.process(this);
        }
        pilaCampos.pop();
    }

    @Override
    public void process(Muchos_camposrecord ld) {
        if (ld.lista != null) {
            ld.lista.process(this);
        }
        if (ld.campo != null) {
            ld.campo.process(this);
        }
    }

    @Override
    public void process(Un_camporecord ld) {
        if (ld.campo != null) {
            ld.campo.process(this);
        }
    }

    @Override
    public void process(CamposRecord c) {
        Set<String> ids = pilaCampos.peek();
        if (ids != null && !ids.add(c.id)) {
            errores.error(c, "campo duplicado:" + c.id);
        }
        if (c.tipo != null) {
            c.tipo.process(this);
        }
    }

    @Override
    public void process(Si_instr ld) {
        if (ld.instrs != null) {
            ld.instrs.process(this);
        }
    }

    @Override
    public void process(Muchas_instr ld) {
        if (ld.instrs != null) {
            ld.instrs.process(this);
        }
        if (ld.instr != null) {
            ld.instr.process(this);
        }
    }

    @Override
    public void process(Una_instr ld) {
        if (ld.instr != null) {
            ld.instr.process(this);
        }
    }

    @Override
    public void process(Instr_if i) {
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_ifelse i) {
        if (i.instrs1 != null) {
            i.instrs1.process(this);
        }
        if (i.instrs2 != null) {
            i.instrs2.process(this);
        }
    }

    @Override
    public void process(Instr_while i) {
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }

    @Override
    public void process(Instr_compuesta i) {
        if (i.decs != null) {
            i.decs.process(this);
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
    }
}
