package LogicaBoletaMaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente extends Usuario {
	
	private final List<Compra> compras = new ArrayList<>();

    public Cliente(String login, String password) {
        super(login, password);
    }

    public void registrarCompra(Compra compra) {
        if (compra == null) throw new IllegalArgumentException("Compra inválida");
        this.compras.add(compra);
    }

    public List<Compra> getCompras() {
        return Collections.unmodifiableList(compras);
    }

}
