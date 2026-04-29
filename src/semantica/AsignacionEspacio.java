package semantica;

import asint.ProcesamientoDef;
import asint.SintaxisAbstractaTiny.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AsignacionEspacio extends ProcesamientoDef {
    private final InfoSemantica info;
    private int dir;
    private int maxDir;
    private int nivel;
    private boolean segundaPasada;

    private final Map<Dec_var, Integer> dirVar = new HashMap<>();
    private final Map<Dec_var, Integer> nivelVar = new HashMap<>();
    private final Map<Param, Integer> dirParam = new HashMap<>();
    private final Map<Param, Integer> nivelParam = new HashMap<>();
    private final Map<Dec_proc, Integer> nivelProc = new HashMap<>();
    private final Map<Dec_proc, Integer> tamDatosProc = new HashMap<>();
    private int tamGlobal;

    public AsignacionEspacio(InfoSemantica info) {
        this.info = info;
    }

    public void procesa(Prog p) {
        nivel = 0;
        dir = 0;
        maxDir = 0;
        segundaPasada = false;
        if (p.decs != null) {
            p.decs.process(this);
        }
        if (p.decs != null) {
            segundaPasada = true;
            p.decs.process(this);
            segundaPasada = false;
        }
        tamGlobal = maxDir;
        if (p.instrs != null) {
            p.instrs.process(this);
        }
    }

    public int dir(Dec_var d) {
        return dirVar.getOrDefault(d, 0);
    }

    public int nivel(Dec_var d) {
        return nivelVar.getOrDefault(d, 0);
    }

    public int dir(Param p) {
        return dirParam.getOrDefault(p, 0);
    }

    public int nivel(Param p) {
        return nivelParam.getOrDefault(p, 0);
    }

    public int nivel(Dec_proc p) {
        return nivelProc.getOrDefault(p, 0);
    }

    public int tamDatos(Dec_proc p) {
        return tamDatosProc.getOrDefault(p, 0);
    }

    public int tamGlobal() {
        return tamGlobal;
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
        if (!segundaPasada) {
            dirVar.put(d, dir);
            nivelVar.put(d, nivel);
            int tam = tamTipo(d.tipo, new HashSet<>());
            dir += tam;
            if (dir > maxDir) {
                maxDir = dir;
            }
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
        if (!segundaPasada) {
            int dirAnt = dir;
            int maxDirAnt = maxDir;
            nivel++;
            dir = 0;
            maxDir = 0;
            if (d.params != null) {
                d.params.process(this);
            }
            if (d.decs != null) {
                d.decs.process(this);
            }
            nivelProc.put(d, nivel);
            tamDatosProc.put(d, maxDir);
            dir = dirAnt;
            maxDir = maxDirAnt;
            nivel--;
        } else {
            int dirAnt = dir;
            int maxDirAnt = maxDir;
            nivel++;
            dir = 0;
            maxDir = 0;
            if (d.params != null) {
                d.params.process(this);
            }
            if (d.decs != null) {
                d.decs.process(this);
            }
            dir = dirAnt;
            maxDir = maxDirAnt;
            nivel--;
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
        if (!segundaPasada) {
            dirParam.put(p, dir);
            nivelParam.put(p, nivel);
            dir += 1;
            if (dir > maxDir) {
                maxDir = dir;
            }
        }
    }

    @Override
    public void process(Param_val p) {
        if (p.tipo != null) {
            p.tipo.process(this);
        }
        if (!segundaPasada) {
            dirParam.put(p, dir);
            nivelParam.put(p, nivel);
            int tam = tamTipo(p.tipo, new HashSet<>());
            dir += tam;
            if (dir > maxDir) {
                maxDir = dir;
            }
        }
    }

    @Override
    public void process(Tipo_array t) {
        if (t.tipo != null) {
            t.tipo.process(this);
        }
    }

    @Override
    public void process(Tipo_pointer t) {
        if (!segundaPasada) {
            if (!(t.tipo instanceof Tipo_id) && t.tipo != null) {
                t.tipo.process(this);
            }
        } else {
            if (t.tipo != null) {
                t.tipo.process(this);
            }
        }
    }

    @Override
    public void process(Tipo_record t) {
        if (t.lista != null) {
            t.lista.process(this);
        }
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
        int dirAnt = dir;
        if (i.decs != null) {
            i.decs.process(this);
        }
        if (i.instrs != null) {
            i.instrs.process(this);
        }
        dir = dirAnt;
    }

    @Override
    public void process(Tipo_id t) {
        if (t != null) {
            if (!segundaPasada) {
                // Nothing else required in the first pass.
            }
        }
    }

    @Override
    public void process(Exp_array e) {
        if (e.opnd0 != null) {
            e.opnd0.process(this);
        }
        if (e.opnd1 != null) {
            e.opnd1.process(this);
        }
    }

    @Override
    public void process(Exp_campo e) {
        if (e.base != null) {
            e.base.process(this);
        }
    }

    @Override
    public void process(Exp_flecha e) {
        if (e.base != null) {
            e.base.process(this);
        }
    }

    @Override
    public void process(Exp_asterisco_unario e) {
        if (e.opnd != null) {
            e.opnd.process(this);
        }
    }

    private int tamTipo(Tipo tipo, Set<Tipo_id> visitados) {
        if (tipo instanceof Tipo_int || tipo instanceof Tipo_real || tipo instanceof Tipo_bool
                || tipo instanceof Tipo_string) {
            return 1;
        }
        if (tipo instanceof Tipo_pointer) {
            return 1;
        }
        if (tipo instanceof Tipo_array) {
            Tipo_array a = (Tipo_array) tipo;
            int dim = 0;
            try {
                dim = Integer.parseInt(a.dim);
            } catch (NumberFormatException ignored) {
            }
            return dim * tamTipo(a.tipo, visitados);
        }
        if (tipo instanceof Tipo_record) {
            return tamLista(((Tipo_record) tipo).lista, visitados);
        }
        if (tipo instanceof Tipo_id) {
            Tipo_id tid = (Tipo_id) tipo;
            if (!visitados.add(tid)) {
                return 0;
            }
            Nodo vinculacion = info.vinculoDe(tid);
            if (vinculacion instanceof Dec_tipo) {
                return tamTipo(((Dec_tipo) vinculacion).tipo, visitados);
            }
            return 0;
        }
        return 0;
    }

    private int tamLista(ListaRecord lista, Set<Tipo_id> visitados) {
        if (lista instanceof Muchos_camposrecord) {
            Muchos_camposrecord m = (Muchos_camposrecord) lista;
            return tamLista(m.lista, visitados) + tamTipo(m.campo.tipo, visitados);
        }
        if (lista instanceof Un_camporecord) {
            return tamTipo(((Un_camporecord) lista).campo.tipo, visitados);
        }
        return 0;
    }
}
