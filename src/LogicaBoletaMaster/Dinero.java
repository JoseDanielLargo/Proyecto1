package LogicaBoletaMaster;

public final class Dinero {
	
	private final int pesos;

    public Dinero(int pesos) { 
    	this.pesos = pesos; 
    }
    public int getPesos() { 
    	return pesos; 
    }

    public static Dinero cero() {
    	return new Dinero(0); 
    }

    public Dinero sumar(Dinero otro) {
        int sum = this.pesos + (otro == null ? 0 : otro.pesos);
        return new Dinero(sum);
    }
    public Dinero restar(Dinero otro) {
        int res = this.pesos - (otro == null ? 0 : otro.pesos);
        return new Dinero(res);
    }
    public Dinero multiplicarPor(double factor) {
        int r = (int) Math.floor(this.pesos * factor + 0.5);
        return new Dinero(r);
    }
    public boolean esNegativo() { 
    	return pesos < 0; 
    }

    @Override 
    public String toString() { 
    	return "$" + pesos; 
    }
    @Override 
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dinero)) return false;
        return this.pesos == ((Dinero) o).pesos;
    }
    @Override 
    public int hashCode() {
    	return Integer.hashCode(pesos); 
    }

}
