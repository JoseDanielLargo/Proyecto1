package LogicaBoletaMaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TiqueteMultiple {
	
    private final String idPaquete;
    private final Evento eventoPrincipal; 
    private final List<Tiquete> componentes = new ArrayList<>();
    private boolean vendido = false;
    private Cliente dueno; 
    private final Dinero precioPaquete;
    private final boolean transferible = false; 

    public TiqueteMultiple(String idPaquete, Evento eventoPrincipal, Dinero precioPaquete) {
        this.idPaquete = idPaquete;
        this.eventoPrincipal = eventoPrincipal;
        this.precioPaquete = precioPaquete;
    }

    public String getIdPaquete() { 
    	return idPaquete; 
    }
    
    public Evento getEventoPrincipal() { 
    	return eventoPrincipal;
    }
    
    public Dinero getPrecioPaquete() {
    	return precioPaquete; 
    }
    
    public boolean esTransferible() { 
    	return transferible; 
   	}
    
    public boolean esVendido() {
    	return vendido;
    }
    
    public Cliente getDueno() { 
    	return dueno; 
    }

    public List<Tiquete> getComponentes() {
    	return Collections.unmodifiableList(componentes); 
    }
    
    public void agregarComponente(Tiquete t) { 
    	componentes.add(t); 
    }

    public int totalEntradas() { 
    	return componentes.size(); 
    }

    public void venderA(Cliente c) {
        if (vendido) throw new IllegalStateException("Paquete ya vendido");
        this.vendido = true; this.dueno = c;
        for (Tiquete t : componentes) {
            if (!t.esVendido()) t.venderA(c, t.precioBaseAhora());
        }
    }

    public void cancelarVenta() {
        if (!vendido) return;
        this.vendido = false; this.dueno = null;
        for (Tiquete t : componentes) t.cancelarVenta();
    }

}
