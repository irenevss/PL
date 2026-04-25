package semantica;

import asint.SintaxisAbstractaTiny;

import java.util.IdentityHashMap;
import java.util.Map;

public class InfoSemantica {
    private final Map<SintaxisAbstractaTiny.Iden, SintaxisAbstractaTiny.Nodo> vinculosIdentificadores = new IdentityHashMap<>();
    private final Map<SintaxisAbstractaTiny.Tipo_id, SintaxisAbstractaTiny.Nodo> vinculosTipos = new IdentityHashMap<>();
    private final Map<SintaxisAbstractaTiny.Instr_invocar, SintaxisAbstractaTiny.Nodo> vinculosInvocaciones = new IdentityHashMap<>();

    public void vincula(SintaxisAbstractaTiny.Iden uso, SintaxisAbstractaTiny.Nodo dec) {
        vinculosIdentificadores.put(uso, dec);
    }

    public void vincula(SintaxisAbstractaTiny.Tipo_id uso, SintaxisAbstractaTiny.Nodo dec) {
        vinculosTipos.put(uso, dec);
    }

    public void vincula(SintaxisAbstractaTiny.Instr_invocar uso, SintaxisAbstractaTiny.Nodo dec) {
        vinculosInvocaciones.put(uso, dec);
    }

    public SintaxisAbstractaTiny.Nodo vinculoDe(SintaxisAbstractaTiny.Iden uso) {
        return vinculosIdentificadores.get(uso);
    }

    public SintaxisAbstractaTiny.Nodo vinculoDe(SintaxisAbstractaTiny.Tipo_id uso) {
        return vinculosTipos.get(uso);
    }

    public SintaxisAbstractaTiny.Nodo vinculoDe(SintaxisAbstractaTiny.Instr_invocar uso) {
        return vinculosInvocaciones.get(uso);
    }
}
