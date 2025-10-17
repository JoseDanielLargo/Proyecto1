package LogicaBoletaMaster;

import java.util.*;

public class SistemaBoletaMaster {
	
	private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<String, Venue> venues = new HashMap<>();
    private final Map<String, Evento> eventos = new HashMap<>();
    private final Map<String, Localidad> localidades = new HashMap<>();
    private final Map<String, Tiquete> tiquetes = new HashMap<>();
    private final Map<String, TiqueteMultiple> paquetes = new HashMap<>();
    private final Map<String, Compra> compras = new HashMap<>();

    private String genId(String pref) { return pref + "-" + UUID.randomUUID(); }

    // --- Registro básico ---
    public void registrarUsuario(Usuario u) {
        if (u == null) throw new IllegalArgumentException("usuario inválido");
        if (usuarios.containsKey(u.getLogin())) throw new IllegalArgumentException("login duplicado");
        usuarios.put(u.getLogin(), u);
    }
    public Usuario getUsuario(String login) { return usuarios.get(login); }

    public void registrarVenue(Venue v) {
        if (v == null) throw new IllegalArgumentException("venue inválido");
        if (venues.containsKey(v.getId())) throw new IllegalArgumentException("venue duplicado");
        venues.put(v.getId(), v);
    }
    public Venue getVenue(String id) { return venues.get(id); }

    public void registrarEvento(Evento e) {
        if (e == null) throw new IllegalArgumentException("evento inválido");
        for (Evento otro : eventos.values()) {
            if (otro.getVenue().getId().equals(e.getVenue().getId())
                && otro.getFechaHora().equals(e.getFechaHora())
                && "PROGRAMADO".equals(otro.getEstado())) {
                throw new IllegalStateException("Ya existe evento en ese venue y fecha");
            }
        }
        eventos.put(e.getId(), e);
    }
    public Evento getEvento(String id) { return eventos.get(id); }

    public void registrarLocalidad(Localidad l) {
        if (l == null) throw new IllegalArgumentException("localidad inválida");
        if (localidades.containsKey(l.getId())) throw new IllegalArgumentException("localidad duplicada");
        localidades.put(l.getId(), l);
    }
    public Localidad getLocalidad(String id) { return localidades.get(id); }

    // --- Asientos util ---
    public void crearAsientos(Localidad loc, List<String> codigos) {
        if (loc == null || !loc.esNumerada()) throw new IllegalArgumentException("localidad no numerada");
        for (String c : codigos) loc.agregarAsiento(c);
    }

    // --- Inventario ---
    public Tiquete crearTiqueteSimple(Evento e, Localidad loc, String codigoAsiento) {
        if (e == null || loc == null) throw new IllegalArgumentException("datos inválidos");
        if (loc.esNumerada()) {
            if (codigoAsiento == null || !loc.asientoExiste(codigoAsiento)) throw new IllegalArgumentException("asiento inexistente");
            if (!loc.asientoDisponible(codigoAsiento)) throw new IllegalStateException("asiento no disponible");
        } else {
            boolean ok = loc.consumirCupo(1);
            if (!ok) throw new IllegalStateException("no hay cupo disponible");
        }
        Tiquete t = new Tiquete(genId("T"), e, loc, loc.esNumerada() ? codigoAsiento : null);
        tiquetes.put(t.getIdUnico(), t);
        return t;
    }

    public TiqueteMultiple crearPaqueteLocal(Evento e, Localidad loc, int cantidad, Dinero precioPaquete) {
        if (e == null || loc == null) throw new IllegalArgumentException("datos inválidos");
        if (cantidad <= 0) throw new IllegalArgumentException("cantidad inválida");
        TiqueteMultiple p = new TiqueteMultiple(genId("P"), e, precioPaquete);
        for (int i = 0; i < cantidad; i++) {
            if (loc.esNumerada()) throw new IllegalStateException("Paquete no soporta numerada sin asientos");
            Tiquete t = crearTiqueteSimple(e, loc, null);
            p.agregarComponente(t);
        }
        paquetes.put(p.getIdPaquete(), p);
        return p;
    }

    public PaqueteDeluxe crearPaqueteDeluxe(Evento e, Localidad loc, int cantidad, Dinero precioPaquete) {
        if (e == null || loc == null) throw new IllegalArgumentException("datos inválidos");
        if (cantidad <= 0) throw new IllegalArgumentException("cantidad inválida");
        PaqueteDeluxe d = new PaqueteDeluxe(genId("D"), e, precioPaquete);
        for (int i = 0; i < cantidad; i++) {
            if (loc.esNumerada()) throw new IllegalStateException("Deluxe no soporta numerada sin asientos");
            Tiquete t = crearTiqueteSimple(e, loc, null);
            d.agregarComponente(t);
        }
        paquetes.put(d.getIdPaquete(), d);
        return d;
    }

    // --- Compras ---
    public Compra comprarTiquetes(Cliente c, Administrador admin,
                                  List<Tiquete> simples,
                                  List<TiqueteMultiple> packs) {
        if (c == null || admin == null) throw new IllegalArgumentException("datos inválidos");

        Compra compra = new Compra(genId("C"), c);
        if (simples != null) for (Tiquete t : simples) compra.agregarTiquete(t);
        if (packs != null)   for (TiqueteMultiple p : packs) compra.agregarPaquete(p);

        // Validar tope por transacción (si el evento define alguno):
        // Nota: si hay múltiples eventos/paquetes, podrías validar por evento,
        // aquí hacemos una validación simple a nivel agregado si lo necesitas.

        // Calcular total
        Dinero total = compra.subtotalBase().sumar(compra.totalServicio(admin)).sumar(compra.totalEmision(admin));

        if (!c.tieneSaldoSuficiente(total)) throw new IllegalStateException("Saldo insuficiente para comprar");
        c.debitarSaldo(total);

        // Marcar vendidos y dueños
        if (simples != null) for (Tiquete t : simples) t.venderA(c, t.precioBaseAhora());
        if (packs != null)   for (TiqueteMultiple p : packs) p.venderA(c);

        compras.put(compra.getId(), compra);
        c.registrarCompra(compra);
        return compra;
    }

    // --- Transferencias (solo tiquete simple) ---
    public void transferirTiquete(Cliente origen, String password, Cliente destino, String idTiquete) {
        if (origen == null || destino == null) throw new IllegalArgumentException("usuarios inválidos");
        if (!origen.validarPassword(password)) throw new IllegalArgumentException("credenciales inválidas");
        Tiquete t = tiquetes.get(idTiquete);
        if (t == null) throw new IllegalArgumentException("tiquete no existe");
        if (!t.esVendido()) throw new IllegalStateException("tiquete no vendido");
        if (t.getDueno() == null || !t.getDueno().getLogin().equals(origen.getLogin()))
            throw new IllegalStateException("origen no es dueño");
        if (!t.esTransferible()) throw new IllegalStateException("tiquete no transferible");

        // Transferir: liberar y volver a vender al destino
        Dinero precio = t.precioBaseAhora();
        t.cancelarVenta();
        t.venderA(destino, precio);
    }

    // --- Cancelación y reembolso ---
    public void cancelarTiqueteYReembolsar(Administrador admin, String idTiquete) {
        if (admin == null) throw new IllegalArgumentException("admin requerido");
        Tiquete t = tiquetes.get(idTiquete);
        if (t == null) throw new IllegalArgumentException("tiquete no existe");
        if (!t.esVendido()) return;

        Cliente dueno = t.getDueno();
        // Reembolso simple: precio pagado + servicio aplicado + emisión (ajusta si tu profe pide otra fórmula)
        Dinero monto = t.getPrecioPagado()
                .sumar(t.getPrecioPagado().multiplicarPor(admin.getPorcentajeServicio()))
                .sumar(admin.getTarifaEmision());

        dueno.acreditarSaldo(monto);
        t.cancelarVenta();
    }

    // --- Lectura para pruebas ---
    public Collection<Tiquete> getTiquetes() { return tiquetes.values(); }
    public Collection<TiqueteMultiple> getPaquetes() { return paquetes.values(); }
    public Collection<Compra> getCompras() { return compras.values(); }
    
    public java.util.Map<String, Evento> getEventoMap() { return eventos; }

}
