package LogicaBoletaMaster;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.Arrays;

public class BaseTest {
	
	protected SistemaBoletaMaster sistema;
    protected Administrador admin;
    protected Organizador org;
    protected Cliente juan;
    protected Cliente maria;
    protected Venue venue;
    protected Evento evento;
    protected Localidad platea;  
    protected Localidad general;

    @BeforeEach
    void baseSetUp() {
        sistema = new SistemaBoletaMaster();


        admin = new Administrador("admin", "admin123");
        admin.fijarTarifaEmision(new Dinero(2000)); 
        admin.fijarPorcentajeServicio(0.10);        
        sistema.registrarUsuario(admin);

        
        org = new Organizador("promotor", "promotor123");
        sistema.registrarUsuario(org);

        juan = new Cliente("juan", "123");
        maria = new Cliente("maria", "456");
        sistema.registrarUsuario(juan);
        sistema.registrarUsuario(maria);

       
        juan.acreditarSaldo(new Dinero(200_000));
        maria.acreditarSaldo(new Dinero(80_000));

        
        venue = new Venue("VEN-001", "Bogotá", 20000);
        sistema.registrarVenue(venue);

        LocalDateTime fecha = LocalDateTime.now().plusDays(7).withHour(20).withMinute(0);
        evento = new Evento("EV-001", "Rock Fest", "MUSICAL", org, venue, fecha);
        sistema.registrarEvento(evento);

       
        platea = new Localidad("LOC-PLATEA", evento, "Platea", true, new Dinero(50_000), 0);
        general = new Localidad("LOC-GRAL", evento, "General", false, new Dinero(30_000), 150);
        sistema.registrarLocalidad(platea);
        sistema.registrarLocalidad(general);

      
        sistema.crearAsientos(platea, Arrays.asList("A1", "A2", "A3", "A4"));

        
        general.definirOferta(new Dinero(5000),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2));
    }
	

}
