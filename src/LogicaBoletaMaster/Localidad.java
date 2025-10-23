package LogicaBoletaMaster;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Localidad {
	
    private final String id;
    private final Evento evento;
    private final String nombre;
    private final boolean numerada;
    private Dinero precioBase;

    // Numerada
    private final Map<String, Asiento> asientos = new HashMap<>();
    private final Set<String> asientosVendidos = new HashSet<>();

    // No numerada
    private int cupoDisponible;

    // Oferta simple (descuento absoluto por ventana)
    private Dinero ofertaDescuentoAbs = Dinero.cero();
    private LocalDateTime ofertaInicio;
    private LocalDateTime ofertaFin;

    public Localidad(String id, Evento evento, String nombre, boolean numerada, Dinero precioBase, int capacidad) {
        this.id = id;
        this.evento = evento;
        this.nombre = nombre;
        this.numerada = numerada;
        this.precioBase = precioBase;
        this.cupoDisponible = numerada ? 0 : capacidad;
        evento.agregarLocalidad(this);
    }

    public String getId() {
    	return id; 
    }
    public Evento getEvento() { 
    	return evento; 
    }
    public String getNombre() {
    	return nombre; 
    }
    public boolean esNumerada() { 
    	return numerada; 
    }
    public int getCupoDisponible() { 
    	return cupoDisponible; 
    }

    public void definirOferta(Dinero descuentoAbs, LocalDateTime inicio, LocalDateTime fin) {
        this.ofertaDescuentoAbs = (descuentoAbs == null) ? Dinero.cero() : descuentoAbs;
        this.ofertaInicio = inicio; this.ofertaFin = fin;
    }

    public Dinero precioPublicoAhora() {
        Dinero base = precioBase;
        LocalDateTime ahora = LocalDateTime.now();
        if (ofertaInicio != null && ofertaFin != null && !ahora.isBefore(ofertaInicio) && !ahora.isAfter(ofertaFin)) {
            base = base.restar(ofertaDescuentoAbs);
            if (base.esNegativo()) base = Dinero.cero();
        }
        return base;
    }

    // ---- Asientos numerados ----
    public void agregarAsiento(String codigo) {
        if (!numerada) throw new IllegalStateException("Localidad no numerada");
        if (codigo == null || codigo.isEmpty()) throw new IllegalArgumentException("Código de asiento inválido");
        if (asientos.containsKey(codigo)) throw new IllegalArgumentException("Asiento duplicado");
        asientos.put(codigo, new Asiento(codigo));
    }
    public boolean asientoExiste(String codigo) { 
    	return asientos.containsKey(codigo); 
    }
    
    public boolean asientoDisponible(String codigo) { 
    	return asientos.containsKey(codigo) && !asientosVendidos.contains(codigo); 
    }
    
    
    void marcarAsientoVendido(String codigo) { 
    	asientosVendidos.add(codigo);
    }
    
    void liberarAsiento(String codigo) { 
    	asientosVendidos.remove(codigo); 
    }

    // ---- Cupo no numerado ----
    boolean consumirCupo(int n) {
        if (numerada) throw new IllegalStateException("Localidad numerada");
        if (n <= 0) return false;
        if (cupoDisponible < n) return false;
        cupoDisponible -= n;
        return true;
    }
    void devolverCupo(int n) {
    	if (!numerada) cupoDisponible += n; 
    }

}
