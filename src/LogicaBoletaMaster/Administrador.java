package LogicaBoletaMaster;



public class Administrador extends Usuario{
	
    private Dinero tarifaEmision = Dinero.cero(); 
    private double porcentajeServicio = 0.0;      

    public Administrador(String login, String password) {
        super(login, password);
    }

    public void fijarTarifaEmision(Dinero valor) {
        if (valor == null || valor.esNegativo()) throw new IllegalArgumentException("Tarifa inválida");
        this.tarifaEmision = valor;
    }
    public Dinero getTarifaEmision() { return tarifaEmision; }

    public void fijarPorcentajeServicio(double pct) {
        if (pct < 0 || pct > 1) throw new IllegalArgumentException("Porcentaje fuera de rango");
        this.porcentajeServicio = pct;
    }
    public double getPorcentajeServicio() { return porcentajeServicio; }

}
