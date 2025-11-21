package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComprarSimpleTest extends BaseTest {
	
	@Test
    void compraTiqueteSimpleDebitaSaldoYMarcaVendido() {
        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A3");

        Dinero precioBase = platea.precioPublicoAhora();
        Dinero esperado = precioBase
                .sumar(precioBase.multiplicarPor(admin.getPorcentajeServicio()))
                .sumar(admin.getTarifaEmision());

        Dinero saldoAntes = juan.getSaldo();
        Compra compra = sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);

        assertNotNull(compra.getId());
        assertTrue(t.esVendido());
        assertEquals(juan.getLogin(), t.getDueno().getLogin());
        assertEquals(saldoAntes.getPesos() - esperado.getPesos(), juan.getSaldo().getPesos());
    }

}
