package logica;

public class Main {

    public static void main(String[] args) {
        CargadorJson cargador = new CargadorJson();

        ControladorLogistica controlador =
                new ControladorLogistica(cargador, cargador);

        MenuPrincipal menu = new MenuPrincipal(controlador);
        menu.iniciarMenu();
    }
}
