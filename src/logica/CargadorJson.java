// objetivo de este archivo: sacarle responsabilidades a menuprincicpal()
package logica;

import com.google.gson.Gson;
import java.io.FileReader;
import java.util.Set;

public class CargadorJson implements CargadorDatos {

    private final Gson gson = new Gson();
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
    public void cargarInventario(
            Set<String> idsUsados,
            CentroDistribucion centro) {

        try (FileReader reader = new FileReader(rutaInventario)) {

            Paquete[] lista = gson.fromJson(reader, Paquete[].class);

            if (lista == null) {
                throw new IllegalStateException(
                        "El archivo no contiene una lista de paquetes.");
            }

            RegistroDatos.registrarInventario(
                    lista, idsUsados, centro);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error al cargar inventario: " + e.getMessage(), e);
        }
    }

    @Override
    public void cargarDepositos(
            ABB arbolDepositos,
            RedDepositos redDepositos) {

        try (FileReader reader = new FileReader(rutaDepositos)) {

            DepositosWrapper wrapper =
                    gson.fromJson(reader, DepositosWrapper.class);

            if (wrapper == null || wrapper.depositos == null) {
                throw new IllegalStateException(
                        "El archivo no contiene una lista de depósitos.");
            }

            RegistroDatos.registrarDepositos(
                    wrapper.depositos, arbolDepositos, redDepositos);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error al cargar depósitos: " + e.getMessage(), e);
        }
    }
}
// Esto saca la lectura de archivos JSON de MenuPrincipal, que actualmente concentra esa lógica además del menú y la coordinación del sistema.
