package LogicaBoletaMaster;

public abstract class Usuario {
	
    private final String login;
    private String password;           
    private Dinero saldo = Dinero.cero();

    
    public Usuario(String login, String passwordPlano) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("login obligatorio");
        }
        if (passwordPlano == null || passwordPlano.isEmpty()) {
            throw new IllegalArgumentException("password obligatorio");
        }
        this.login = login;
        this.password = passwordPlano;
    }


    public String getLogin() { return login; }
    public Dinero getSaldo() { return saldo; }


    public boolean validarPassword(String passwordPlano) {
        if (passwordPlano == null) return false;
        return this.password.equals(passwordPlano);
    }

    public void cambiarPassword(String actualPlano, String nuevaPlano) {
        if (!validarPassword(actualPlano)) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }
        if (nuevaPlano == null || nuevaPlano.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser vacía");
        }
        this.password = nuevaPlano;
    }


    public void acreditarSaldo(Dinero monto) {
        if (monto == null || monto.esNegativo() || monto.getPesos() == 0) {
            throw new IllegalArgumentException("Monto a acreditar inválido");
        }
        this.saldo = this.saldo.sumar(monto);
    }

    public void debitarSaldo(Dinero monto) {
        if (monto == null || monto.esNegativo() || monto.getPesos() == 0) {
            throw new IllegalArgumentException("Monto a debitar inválido");
        }
        if (!tieneSaldoSuficiente(monto)) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        this.saldo = this.saldo.restar(monto);
    }

    public boolean tieneSaldoSuficiente(Dinero monto) {
        if (monto == null) return false;
        return this.saldo.getPesos() >= monto.getPesos();
    }


    public abstract boolean puedeComprar();
    public abstract boolean puedeCancelarEventos();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario u = (Usuario) o;
        return this.login.equals(u.login);
    }

    @Override
    public int hashCode() { return login.hashCode(); }

    @Override
    public String toString() {
        return "Usuario{login='" + login + "', saldo=" + saldo + "}";
    }
}
