package LogicaBoletaMaster;

public abstract class Usuario {
	
    private String login;
    private String password;
    private Dinero saldo = Dinero.cero();

    public Usuario(String login, String password) {
        if (login == null || login.isEmpty()) throw new IllegalArgumentException("login obligatorio");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("password obligatorio");
        this.login = login;
        this.password = password;
    }

    public String getLogin() { 
    	return login; 
    }
    public Dinero getSaldo() {
    	return saldo; 
    }
    public void setPassword(String nPassword) {
    	this.password = nPassword;
    }
    public void setLogin(String nLogin) { 
    	this.login = nLogin; 
    }

    public boolean validarPassword(String passwordPlano) {
        if (passwordPlano == null) return false;
        return this.password.equals(passwordPlano);
    }

    public void cambiarPassword(String actualPlano, String nuevaPlano) {
        if (!validarPassword(actualPlano)) throw new IllegalArgumentException("Contraseña actual incorrecta");
        if (nuevaPlano == null || nuevaPlano.isEmpty()) throw new IllegalArgumentException("La nueva contraseña no puede ser vacía");
        this.password = nuevaPlano;
    }

    public void acreditarSaldo(Dinero monto) {
        if (monto == null || monto.esNegativo() || monto.getPesos() == 0) throw new IllegalArgumentException("Monto a acreditar inválido");
        this.saldo = this.saldo.sumar(monto);
    }

    public void debitarSaldo(Dinero monto) {
        if (monto == null || monto.esNegativo() || monto.getPesos() == 0) throw new IllegalArgumentException("Monto a debitar inválido");
        if (this.saldo.getPesos() < monto.getPesos()) throw new IllegalStateException("Saldo insuficiente");
        this.saldo = this.saldo.restar(monto);
    }

    public boolean tieneSaldoSuficiente(Dinero monto) {
        if (monto == null) return false;
        return this.saldo.getPesos() >= monto.getPesos();
    }
}
