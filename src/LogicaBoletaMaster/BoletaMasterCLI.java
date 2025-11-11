package LogicaBoletaMaster;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Function;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class BoletaMasterCLI {

    private final Scanner sc = new Scanner(System.in);
    private final SistemaBoletaMaster sistema;


    private Administrador admin;
    private Organizador organizador;
    private Map<String, Cliente> clientes = new HashMap<>();
    
    private static final String RUTA_EVENTOS = "data/eventos.json";
    private static final String RUTA_USUARIOS = "data/usuarios.json";

    public BoletaMasterCLI(SistemaBoletaMaster sistema) {
        this.sistema = sistema;
    }
    
    public void seedData() {
    	Type tipoUsuarios = new TypeToken<List<Cliente>>(){}.getType();
    	List<Cliente> listaClientes = PersistenciaJSON.cargarLista(RUTA_USUARIOS, tipoUsuarios);
    	if (listaClientes != null && !listaClientes.isEmpty()) {
    	    clientes = listaClientes.stream()
    	                .collect(Collectors.toMap(Cliente::getLogin, Function.identity()));
    	} else {
    	    Cliente juan = new Cliente("juan", "123");
    	    juan.acreditarSaldo(new Dinero(200_000));
    	    clientes.put(juan.getLogin(), juan);

    	    Cliente maria = new Cliente("maria", "456");
    	    maria.acreditarSaldo(new Dinero(80_000));
    	    clientes.put(maria.getLogin(), maria);
    	}
    	Type tipoEventos = new TypeToken<List<Evento>>(){}.getType();
    	List<Evento> listaEventos = PersistenciaJSON.cargarLista(RUTA_EVENTOS, tipoEventos);
    	Venue v1 = new Venue("VEN-001", "Bogotá", 20000);
    	sistema.registrarVenue(v1);
    	Venue v2 = new Venue("VEN-002", "Medellín", 15000);
    	sistema.registrarVenue(v2);
    	for (Evento e : listaEventos) {
    	    Venue venueAsociado = sistema.getVenue(e.getVenueId());
    	    if (venueAsociado == null) {
    	        venueAsociado = v1;
    	    }
    	    e.setVenue(venueAsociado);
    	    sistema.registrarEvento(e);
    	}
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PersistenciaJSON.guardar(RUTA_USUARIOS, clientes.values());
        }));
    }


    public void run() {
        System.out.println("=== BoletaMaster (CLI) ===");
        seedData();

        Cliente sesion = loginCliente();
        if (sesion == null) {
            System.out.println("No se pudo iniciar sesión. Saliendo.");
            return;
        }

        int op;
        do {
            mostrarMenu(sesion);
            op = leerEntero("Elige una opción: ");
            try {
                switch (op) {
                    case 1: verSaldo(sesion); break;
                    case 2: recargarSaldo(sesion); break;
                    case 3: listarEventosYLocalidades(); break;
                    case 4: comprarTiqueteSimple(sesion); break;
                    case 5: comprarPaquete(sesion); break;
                    case 6: transferirTiquete(sesion); break;
                    case 7: cancelarTiqueteConReembolso(); break;
                    case 8: verCompras(sesion); break;
                    case 0: System.out.println("¡Hasta luego!"); break;
                    default: System.out.println("Opción inválida");
                }
            } catch (Exception ex) {
                System.out.println("️Error: " + ex.getMessage());
            }
        } while (op != 0);
    }


    private Cliente loginCliente() {
    	/*
        System.out.println("\nInicia sesión como cliente. Disponibles: " + clientes.keySet());
        System.out.print("Login: ");
        String login = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass = sc.nextLine().trim();

        Cliente c = clientes.get(login);
        if (c == null || !c.validarPassword(pass)) {
            System.out.println("Credenciales inválidas.");
            return null;
        }
        System.out.println("Bienvenido, " + c.getLogin() + "!");
        return c;
        */
    	System.out.println("\nInicia sesión o crea un nuevo cliente.");
        System.out.println("Clientes existentes: " + clientes.keySet());

        System.out.print("Login: ");
        String login = sc.nextLine().trim();

        Cliente c = clientes.get(login);

        if (c == null) {
            System.out.print("El cliente no existe. ¿Deseas crear una nueva cuenta? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s")) {
                System.out.print("Crea una contraseña: ");
                String pass = sc.nextLine().trim();
                c = new Cliente(login, pass);
                sistema.registrarUsuario(c);
                clientes.put(login, c);
                System.out.println("Cliente '" + login + "' creado exitosamente.");
            } else {
                System.out.println("No se creó el cliente. Saliendo...");
                return null;
            }
        } else {
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();
            if (!c.validarPassword(pass)) {
                System.out.println("Contraseña incorrecta.");
                return null;
            }
            System.out.println("Bienvenido, " + c.getLogin() + "!");
        }

        return c;
    }

    private void mostrarMenu(Cliente c) {
        System.out.println("\n--- Menú --- (cliente: " + c.getLogin() + ")");
        System.out.println("1) Ver saldo");
        System.out.println("2) Recargar saldo");
        System.out.println("3) Listar eventos y localidades");
        System.out.println("4) Comprar TIQUETE simple");
        System.out.println("5) Comprar PAQUETE (localidad no numerada)");
        System.out.println("6) Transferir TIQUETE simple");
        System.out.println("7) Cancelar TIQUETE y reembolso");
        System.out.println("8) Ver mis compras");
        System.out.println("0) Salir");
    }

    private int leerEntero(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) { System.out.print("Ingresa un número: "); sc.next(); }
        int v = sc.nextInt(); sc.nextLine(); // consumir salto
        return v;
    }
    private String leerLinea(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private void verSaldo(Cliente c) {
        System.out.println("Saldo: " + c.getSaldo());
    }

    private void recargarSaldo(Cliente c) {
        int monto = leerEntero("Monto a acreditar ($): ");
        c.acreditarSaldo(new Dinero(monto));
        System.out.println("Nuevo saldo: " + c.getSaldo());
    }

    private void listarEventosYLocalidades() {
        System.out.println("\nEventos:");
        for (Evento e : new ArrayList<>(sistema.getEventoMap().values())) {
            //System.out.println("- " + e.getId() + " | " + e.getNombre() + " | " + e.getTipo() + " | " + e.getFechaHora());
        	System.out.println(String.format("- %-8s | %-20s | %-10s | %s",e.getId(), e.getNombre(), e.getTipo(),e.getFechaHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

            for (Localidad l : e.getLocalidades()) {
                String extra = l.esNumerada() ? "Numerada" : ("No numerada, cupo=" + l.getCupoDisponible());
                System.out.println("   · " + l.getId() + " | " + l.getNombre() + " | precio ahora=" + l.precioPublicoAhora() + " | " + extra);
            }
        }
    }

    private void comprarTiqueteSimple(Cliente c) {
        String idEvento = leerLinea("ID Evento: ");
        Evento e = sistema.getEvento(idEvento);
        if (e == null) throw new IllegalArgumentException("Evento no existe");

        String idLoc = leerLinea("ID Localidad: ");
        Localidad loc = sistema.getLocalidad(idLoc);
        if (loc == null) throw new IllegalArgumentException("Localidad no existe");
        if (!loc.getEvento().getId().equals(e.getId())) throw new IllegalArgumentException("La localidad no pertenece al evento");

        String asiento = null;
        if (loc.esNumerada()) {
            asiento = leerLinea("Código de asiento (ej. A1): ");
        }
        Tiquete t = sistema.crearTiqueteSimple(e, loc, asiento);

        
        Compra compra = sistema.comprarTiquetes(c, admin, Collections.singletonList(t), null);
        System.out.println("Compra OK. ID: " + compra.getId() + ". Nuevo saldo: " + c.getSaldo());
        System.out.println("Tiquete: " + t.getIdUnico() + " vendido=" + t.esVendido());
    }

    private void comprarPaquete(Cliente c) {
        String idEvento = leerLinea("ID Evento: ");
        Evento e = sistema.getEvento(idEvento);
        if (e == null) throw new IllegalArgumentException("Evento no existe");

        String idLoc = leerLinea("ID Localidad (NO numerada): ");
        Localidad loc = sistema.getLocalidad(idLoc);
        if (loc == null) throw new IllegalArgumentException("Localidad no existe");
        if (!loc.getEvento().getId().equals(e.getId())) throw new IllegalArgumentException("La localidad no pertenece al evento");
        if (loc.esNumerada()) throw new IllegalArgumentException("Para paquetes use localidades NO numeradas");

        int cantidad = leerEntero("Cantidad de entradas dentro del paquete: ");
        int precioPack = leerEntero("Precio del paquete ($): ");

        TiqueteMultiple pack = sistema.crearPaqueteLocal(e, loc, cantidad, new Dinero(precioPack));
        Compra compra = sistema.comprarTiquetes(c, admin, null, Collections.singletonList(pack));

        System.out.println("Compra OK. ID: " + compra.getId() + ". Nuevo saldo: " + c.getSaldo());
        System.out.println("Paquete: " + pack.getIdPaquete() + " (entradas: " + pack.totalEntradas() + ")");
    }

    private void transferirTiquete(Cliente origen) {
        String idT = leerLinea("ID del tiquete a transferir: ");
        String loginDestino = leerLinea("Login del destino: ");
        Usuario u = sistema.getUsuario(loginDestino);
        if (!(u instanceof Cliente)) throw new IllegalArgumentException("Destino debe ser cliente existente");
        Cliente destino = (Cliente) u;
        String pass = leerLinea("Tu contraseña: ");

        sistema.transferirTiquete(origen, pass, destino, idT);
        System.out.println("Transferencia OK al usuario " + destino.getLogin());
    }

    private void cancelarTiqueteConReembolso() {
        String idT = leerLinea("ID del tiquete a cancelar: ");
        sistema.cancelarTiqueteYReembolsar(admin, idT);
        System.out.println("Tiquete cancelado y reembolsado (si estaba vendido).");
    }

    private void verCompras(Cliente c) {
        System.out.println("Compras de " + c.getLogin() + ":");
        for (Compra comp : c.getCompras()) {
            System.out.println("- " + comp.getId() + " | " + comp.getFecha()
                    + " | simples=" + comp.getTiquetes().size()
                    + " | paquetes=" + comp.getPaquetes().size());
            for (Tiquete t : comp.getTiquetes()) {
                System.out.println("   · T " + t.getIdUnico() + " | " + t.getEvento().getNombre()
                        + " | " + t.getLocalidad().getNombre()
                        + (t.getCodigoAsiento() != null ? " | asiento=" + t.getCodigoAsiento() : ""));
            }
            for (TiqueteMultiple p : comp.getPaquetes()) {
                System.out.println("   · P " + p.getIdPaquete() + " | entradas=" + p.totalEntradas());
            }
        }
    }
    public SistemaBoletaMaster getSistema() { 
    	return sistema; 
    	}
	
}
