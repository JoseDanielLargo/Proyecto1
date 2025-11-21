package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaqueteTest extends BaseTest {
	
	 @Test
	    void compraPaqueteVendeComponentesYCuentaUnoParaTope() {
	        TiqueteMultiple pack = sistema.crearPaqueteLocal(evento, general, 3, new Dinero(80_000));

	        Dinero subtotal = new Dinero(80_000);
	        Dinero total = subtotal
	                .sumar(subtotal.multiplicarPor(admin.getPorcentajeServicio()))
	                .sumar(admin.getTarifaEmision());

	        Dinero saldoAntes = juan.getSaldo();
	        Compra compra = sistema.comprarTiquetes(juan, admin, null, java.util.Collections.singletonList(pack));

	        assertTrue(pack.esVendido());
	        assertEquals(3, pack.totalEntradas());
	        assertEquals(saldoAntes.getPesos() - total.getPesos(), juan.getSaldo().getPesos());
	        assertEquals(1, compra.contarParaTope());
	        for (Tiquete comp : pack.getComponentes()) {
	            assertTrue(comp.esVendido());
	            assertEquals(juan.getLogin(), comp.getDueno().getLogin());
	        }
	    }

	    @Test
	    void paqueteNoTransferible() {
	        TiqueteMultiple pack = sistema.crearPaqueteLocal(evento, general, 2, new Dinero(60_000));
	        sistema.comprarTiquetes(juan, admin, null, java.util.Collections.singletonList(pack));
	        assertFalse(pack.esTransferible());
	    }
	

}
