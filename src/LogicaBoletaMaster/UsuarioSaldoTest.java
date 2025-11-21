package LogicaBoletaMaster;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UsuarioSaldoTest extends BaseTest {
	
	@Test
    void loginYPasswordValidos() {
        Usuario u = sistema.getUsuario("juan");
        assertTrue(u.validarPassword("123"));
        assertFalse(u.validarPassword("zzz"));
    }

    @Test
    void acreditarYDebitarSaldo() {
        Cliente c = (Cliente) sistema.getUsuario("juan");
        Dinero antes = c.getSaldo();
        c.acreditarSaldo(new Dinero(5000));
        assertEquals(antes.getPesos() + 5000, c.getSaldo().getPesos());

        c.debitarSaldo(new Dinero(3000));
        assertEquals(antes.getPesos() + 2000, c.getSaldo().getPesos());
    }

    @Test
    void cambiarPassword() {
        Usuario u = sistema.getUsuario("juan");
        u.cambiarPassword("123", "abc");
        assertTrue(u.validarPassword("abc"));
    }

}
