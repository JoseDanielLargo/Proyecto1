package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
class EventoVenueTest extends BaseTest  {
	
	@Test
    void exclusividadPorVenueYFecha() {
        LocalDateTime mismaFecha = evento.getFechaHora();
        Evento choque = new Evento("EV-002", "Otro Fest", "MUSICAL", org, venue, mismaFecha);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> sistema.registrarEvento(choque));
        assertTrue(ex.getMessage().toLowerCase().contains("ya existe evento"));
    }

    @Test
    void organizadorAsociaEventos() {
        assertTrue(org.getEventos().stream().anyMatch(e -> e.getId().equals("EV-001")));
    }



}
