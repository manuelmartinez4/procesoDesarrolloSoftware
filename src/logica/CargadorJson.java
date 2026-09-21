// objetivo de este archivo: sacarle responsabilidades a menuprincicpal()
package logica;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.Set;

public class CargadorJson implements CargadorDatos {
    private final String rutaInventario;
    private final String rutaDepositos;

    public CargadorJson() {
        this("src/logica/inventario.json", "src/logica/depositos.json");
    }

    public CargadorJson(String rutaInventario, String rutaDepositos) {
        this.rutaInventario = rutaInventario;
        this.rutaDepositos = rutaDepositos;
    }

    @Override
    public void cargarInventario(Set<String> idsUsados, CentroDistribucion centro) {
        try (FileReader reader = new FileReader(rutaInventario)) {
            Gson gson = new Gson();
            Paquete[] lista = gson.fromJson(reader, Paquete[].class);

            if (lista != null) {
                RegistroDatos.registrarInventario(lista, idsUsados, centro);

                System.out.println("Inventario cargado exitosamente.");
            }

        } catch (Exception e) {
            System.out.println("Error al cargar JSON: " + e.getMessage());
        }
    }

    @Override
    public void cargarDepositos(ABB arbolDepositos, RedDepositos redDepositos) {
        try (FileReader reader = new FileReader(rutaDepositos)) {
            Gson gson = new Gson();
            DepositosWrapper wrapper = gson.fromJson(reader, DepositosWrapper.class);

            if (wrapper != null && wrapper.depositos != null) {
                RegistroDatos.registrarDepositos(wrapper.depositos, arbolDepositos, redDepositos);

                System.out.println("Depósitos cargados exitosamente.");
            }

        } catch (Exception e) {
            System.out.println("Error al cargar depósitos: " + e.getMessage());
        }
    }
}

// Esto saca la lectura de archivos JSON de MenuPrincipal, que actualmente concentra esa lógica además del menú y la coordinación del sistema.
