package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CancelacionReembolsoTest extends BaseTest {
	
	@Test
    void cancelarReembolsaYLiberaDisponibilidad() {
        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A1");
        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);
        assertTrue(t.esVendido());

        Dinero saldoAntes = juan.getSaldo();
        Dinero reembolso = t.getPrecioPagado()
                .sumar(t.getPrecioPagado().multiplicarPor(admin.getPorcentajeServicio()))
                .sumar(admin.getTarifaEmision());

        sistema.cancelarTiqueteYReembolsar(admin, t.getIdUnico());

        assertFalse(t.esVendido());
        assertEquals(saldoAntes.sumar(reembolso).getPesos(), juan.getSaldo().getPesos());

       
        Tiquete t2 = sistema.crearTiqueteSimple(evento, platea, "A1");
        assertNotNull(t2);
    }



}
