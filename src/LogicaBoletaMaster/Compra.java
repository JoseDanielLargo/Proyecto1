package LogicaBoletaMaster;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Compra {
	
	private final String id;
    private final Cliente comprador;
    private final LocalDateTime fecha;

    private final List<Tiquete> tiquetes = new ArrayList<>();
    private final List<TiqueteMultiple> paquetes = new ArrayList<>();

    public Compra(String id, Cliente comprador) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id obligatorio");
        if (comprador == null) throw new IllegalArgumentException("comprador obligatorio");
        this.id = id; this.comprador = comprador; this.fecha = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Cliente getComprador() { return comprador; }
    public LocalDateTime getFecha() { return fecha; }
    public List<Tiquete> getTiquetes() { return Collections.unmodifiableList(tiquetes); }
    public List<TiqueteMultiple> getPaquetes() { return Collections.unmodifiableList(paquetes); }

    public void agregarTiquete(Tiquete t) {
        if (t == null) throw new IllegalArgumentException("tiquete inválido");
        tiquetes.add(t);
    }
    public void agregarPaquete(TiqueteMultiple p) {
        if (p == null) throw new IllegalArgumentException("paquete inválido");
        paquetes.add(p);
    }

    public Dinero subtotalBase() {
        Dinero total = Dinero.cero();
        for (Tiquete t : tiquetes) total = total.sumar(t.precioBaseAhora());
        for (TiqueteMultiple p : paquetes) total = total.sumar(p.getPrecioPaquete());
        return total;
    }

    public Dinero totalServicio(Administrador admin) {
        if (admin == null) throw new IllegalArgumentException("admin requerido");
        double pct = admin.getPorcentajeServicio();
        if (pct <= 0) return Dinero.cero();
        Dinero total = Dinero.cero();
        for (Tiquete t : tiquetes) total = total.sumar(t.precioBaseAhora().multiplicarPor(pct));
        for (TiqueteMultiple p : paquetes) total = total.sumar(p.getPrecioPaquete().multiplicarPor(pct));
        return total;
    }

    public Dinero totalEmision(Administrador admin) {
        if (admin == null) throw new IllegalArgumentException("admin requerido");
        Dinero tarifa = admin.getTarifaEmision();
        if (tarifa == null) tarifa = Dinero.cero();
        int items = tiquetes.size() + paquetes.size();
        Dinero total = Dinero.cero();
        for (int i = 0; i < items; i++) total = total.sumar(tarifa);
        return total;
    }

    public Dinero totalAPagar(Administrador admin) {
        return subtotalBase().sumar(totalServicio(admin)).sumar(totalEmision(admin));
    }

    public int contarParaTope() {
        return tiquetes.size() + paquetes.size();
    }
    

}
