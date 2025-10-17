package LogicaBoletaMaster;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaqueteDeluxe extends TiqueteMultiple {
	
	private final List<String> beneficios = new ArrayList<>();
    public PaqueteDeluxe(String idPaquete, Evento evento, Dinero precioPaquete) {
        super(idPaquete, evento, precioPaquete);
    }
    public void agregarBeneficio(String b) { if (b != null && !b.isEmpty()) beneficios.add(b); }
    public List<String> getBeneficios() { return Collections.unmodifiableList(beneficios); }

}
