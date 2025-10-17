package LogicaBoletaMaster;

public class Main {
	
    public static void main(String[] args) {
        SistemaBoletaMaster sistema = new SistemaBoletaMaster();
        BoletaMasterCLI cli = new BoletaMasterCLI(sistema);
        cli.run();
    }

}
