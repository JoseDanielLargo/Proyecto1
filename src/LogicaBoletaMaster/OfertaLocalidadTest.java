package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OfertaLocalidadTest extends BaseTest {
	
	 @Test
	    void precioConOfertaAbsolutaVigente() {
	        
	        assertEquals(25_000, general.precioPublicoAhora().getPesos());
	    }

}
