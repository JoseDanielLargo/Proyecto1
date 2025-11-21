package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LocalidadAsientosTest extends BaseTest {
	
	
	 @Test
	    void asientoUnicoEnLocalidadNumerada() {
	      
	        Tiquete t1 = sistema.crearTiqueteSimple(evento, platea, "A1");
	        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t1), null);
	        assertTrue(t1.esVendido());

	        
	        IllegalStateException ex = assertThrows(IllegalStateException.class,
	                () -> sistema.crearTiqueteSimple(evento, platea, "A1"));
	        assertTrue(ex.getMessage().toLowerCase().contains("no disponible"));
	    }

	    @Test
	    void liberarAsientoTrasCancelacion() {
	        Tiquete t = sistema.crearTiqueteSimple(evento, platea, "A2");
	        sistema.comprarTiquetes(juan, admin, java.util.Collections.singletonList(t), null);
	        assertTrue(t.esVendido());

	        sistema.cancelarTiqueteYReembolsar(admin, t.getIdUnico());
	        assertFalse(t.esVendido());

	       
	        Tiquete t2 = sistema.crearTiqueteSimple(evento, platea, "A2");
	        assertNotNull(t2);
	    }



}
