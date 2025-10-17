package LogicaBoletaMaster;

public class Tiquete {
	
    private final String idUnico;
    private final Evento evento;
    private final Localidad localidad;
    private final String codigoAsiento; // null si no numerada
    private boolean vendido = false;
    private Cliente dueno;             // null hasta que se venda
    private boolean transferible = true;
    private Dinero precioPagado = Dinero.cero();

    public Tiquete(String idUnico, Evento evento, Localidad localidad, String codigoAsiento) {
        this.idUnico = idUnico;
        this.evento = evento;
        this.localidad = localidad;
        this.codigoAsiento = codigoAsiento;
    }

    public String getIdUnico() {
    	return idUnico; 
    	}
    
    public Evento getEvento() { 
    	return evento; 
    }
    
    public Localidad getLocalidad() {
    	return localidad; 
    	}
    
    public String getCodigoAsiento() {
    	return codigoAsiento; 
    }
    
    public boolean esVendido() { 
    	return vendido; 
    }
    
    public Cliente getDueno() { 
    	return dueno; 
    }
    
    public Dinero getPrecioPagado() { 
    	return precioPagado; 
    }

    public Dinero precioBaseAhora() { 
    	return localidad.precioPublicoAhora(); 
    }

    public boolean esTransferible() {
    	return transferible; 
    }
    
    public void setTransferible(boolean t) {
    	this.transferible = t; 
    }

    public void venderA(Cliente c, Dinero precioPagado) {
        if (vendido) throw new IllegalStateException("Tiquete ya vendido");
        this.vendido = true; this.dueno = c; this.precioPagado = precioPagado;
        if (codigoAsiento != null) localidad.marcarAsientoVendido(codigoAsiento);
        else localidad.consumirCupo(1); 
    }

    public void cancelarVenta() {
        if (!vendido) return;
        this.vendido = false;
        this.dueno = null;
        this.precioPagado = Dinero.cero();
        if (codigoAsiento != null) localidad.liberarAsiento(codigoAsiento);
        else localidad.devolverCupo(1);
    }

}
