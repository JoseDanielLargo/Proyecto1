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
    private final LocalDateTime fechaHora;
    private String estado; 

    private final List<Localidad> localidades = new ArrayList<>();

    public Evento(String id, String nombre, String tipo, Organizador organizador, Venue venue, LocalDateTime fechaHora) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id del evento obligatorio");
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("nombre del evento obligatorio");
        if (tipo == null || tipo.isEmpty()) throw new IllegalArgumentException("tipo del evento obligatorio");
        if (organizador == null) throw new IllegalArgumentException("organizador obligatorio");
        if (venue == null) throw new IllegalArgumentException("venue obligatorio");
        if (fechaHora == null) throw new IllegalArgumentException("fecha y hora obligatoria");

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.organizador = organizador;
        this.venue = venue;
        this.fechaHora = fechaHora;
        this.estado = "PROGRAMADO";
    }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public Organizador getOrganizador() { return organizador; }
    public Venue getVenue() { return venue; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }


    public void agregarLocalidad(Localidad l) {
        if (l == null) throw new IllegalArgumentException("localidad inválida");
        localidades.add(l);
    }
    public List<Localidad> getLocalidades() {
        return Collections.unmodifiableList(localidades);
    }

    public void cancelarEvento() {
        if ("CANCELADO".equals(estado)) return;
        this.estado = "CANCELADO";
    }

    public void finalizarEvento() {
        if ("FINALIZADO".equals(estado)) return;
        this.estado = "FINALIZADO";
    }

    public boolean estaActivo() {
        return "PROGRAMADO".equals(estado);
    }
    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", organizador='" + organizador.getLogin() + '\'' +
                ", venue='" + venue.getId() + '\'' +
                ", fecha=" + fechaHora +
                ", estado=" + estado +
                ", localidades=" + localidades.size() +
                '}';
    }
}
