package LogicaBoletaMaster;

public class Venue {
	
	private final String id;
    private final String ubicacion;
    private final int capacidadMaxima;
    private boolean aprobado;

    public Venue(String id, String ubicacion, int capacidadMaxima) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id venue obligatorio");
        this.id = id;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.aprobado = true;
    }

    public String getId() { 
    	return id; 
    }
    
    public String getUbicacion() { 
    	return ubicacion; 
   	}
    
    public int getCapacidadMaxima() { 
    	return capacidadMaxima; 
    }
    
    public boolean isAprobado() { 
    	return aprobado; 
    }
    
    public void setAprobado(boolean aprobado) { 
    	this.aprobado = aprobado;
    }

}
