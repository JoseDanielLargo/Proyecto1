package LogicaBoletaMaster;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Evento {
	
    private final String id;
    private final String nombre;
    private final String tipo; 
    private final Organizador organizador;
    private final Venue venue;
    private LocalDateTime fechaHora;
    private String estado = "PROGRAMADO";
    private final List<Localidad> localidades = new ArrayList<>();
    private int maxTiquetesPorTransaccion = 0; 

    public Evento(String id, String nombre, String tipo, Organizador organizador, Venue venue, LocalDateTime fechaHora) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id obligatorio");
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.organizador = organizador;
        this.venue = venue;
        this.fechaHora = fechaHora;
        organizador.agregarEvento(this);
    }

    public String getId() { 
    	return id; 
    }
    
    public String getNombre() { 
    	return nombre; 
    }
    
    public String getTipo() { 
    	return tipo; 
    }
    
    public Organizador getOrganizador() {
    	return organizador; 
    }
    
    public Venue getVenue() {
    	return venue; 
    }
    
    public LocalDateTime getFechaHora() {
    	return fechaHora; 
    }
    
    public void setFechaHora(LocalDateTime fh) { 
    	this.fechaHora = fh; 
    }
    
    public String getEstado() {
    	return estado; 
    }
    
    public void setEstado(String e) { 
    	this.estado = e; 
    }

    public List<Localidad> getLocalidades() { 
    	return Collections.unmodifiableList(localidades); 
    }
    
    void agregarLocalidad(Localidad l) {
    	localidades.add(l); 
    }

    public int getMaxTiquetesPorTransaccion() {
    	return maxTiquetesPorTransaccion; 
    }
    
    public void setMaxTiquetesPorTransaccion(int max) {
    	this.maxTiquetesPorTransaccion = max; 
    }

    public boolean estaProgramado() {
    	return "PROGRAMADO".equals(estado); 
    }

}
