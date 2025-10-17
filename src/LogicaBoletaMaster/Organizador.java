package LogicaBoletaMaster;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Organizador extends Usuario{
	
	private final List<Evento> eventos = new ArrayList<>();

    public Organizador(String login, String password) {
        super(login, password);
    }

    void agregarEvento(Evento e) {
        eventos.add(e);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

}
