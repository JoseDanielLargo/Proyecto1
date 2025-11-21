package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransferenciaTest extends BaseTest {
	
	@Test
    void transferenciaDeTiqueteSimpleConPasswordValida() {
        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A4");
        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);

        assertEquals(juan.getLogin(), t.getDueno().getLogin());
        sistema.transferirTiquete(juan, "123", maria, t.getIdUnico());
        assertEquals(maria.getLogin(), t.getDueno().getLogin());
    }

    @Test
    void transferenciaFallaConPasswordIncorrecta() {
        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A1");
        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sistema.transferirTiquete(juan, "mala", maria, t.getIdUnico()));
        assertTrue(ex.getMessage().toLowerCase().contains("credenciales"));
    }

    @Test
    void transferenciaFallaSiNoEsDueno() {
        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A2");
        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> sistema.transferirTiquete(maria, "456", juan, t.getIdUnico()));
        assertTrue(ex.getMessage().toLowerCase().contains("no es dueño"));
    }


}
