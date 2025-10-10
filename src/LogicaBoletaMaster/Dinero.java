package LogicaBoletaMaster;

public class Dinero {
	
	private int pesos;

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
    	return new Dinero(this.pesos + otro.pesos); 
    	}
    public Dinero restar(Dinero otro) {
    	return new Dinero(this.pesos - otro.pesos); 
    	}

    
    public Dinero multiplicarPor(double factor) {
        int resultado = redondear(this.pesos * factor);
        return new Dinero(resultado);
    }

    public boolean esNegativo() {
    	return pesos < 0; 
    	}

    private static int redondear(double valor) {
        return (int) Math.floor(valor + 0.5);
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
