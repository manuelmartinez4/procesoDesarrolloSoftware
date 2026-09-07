// objetivo de este archivo: sacarle responsabilidades a menuprincicpal()
package logica;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.Set;

public class CargadorJson {

    public void cargarInventario(Set<String> idsUsados, CentroDistribucion centro) {
        try (FileReader reader = new FileReader("src/logica/inventario.json")) {
            Gson gson = new Gson();
            Paquete[] lista = gson.fromJson(reader, Paquete[].class);

            if (lista != null) {
                for (Paquete paquete : lista) {
                    String idString = String.valueOf(paquete.getId());

                    if (!idsUsados.contains(idString)) {
                        idsUsados.add(idString);

                        Paquete nuevo = new Paquete(
                                idString,
                                paquete.getPeso(),
                                paquete.getDestino(),
                                paquete.isUrgente(),
                                paquete.getContenido()
                        );

                        centro.recibirPaquete(nuevo);
                    }
                }

                System.out.println("Inventario cargado exitosamente.");
            }

        } catch (Exception e) {
            System.out.println("Error al cargar JSON: " + e.getMessage());
        }
    }

    public void cargarDepositos(ABB arbolDepositos, RedDepositos redDepositos) {
        try (FileReader reader = new FileReader("src/logica/depositos.json")) {
            Gson gson = new Gson();
            DepositosWrapper wrapper = gson.fromJson(reader, DepositosWrapper.class);

            if (wrapper != null && wrapper.depositos != null) {
                for (DepositoJson deposito : wrapper.depositos) {
                    arbolDepositos.insertar(deposito.id, deposito.auditado);

                    if (deposito.conexiones != null) {
                        for (int conexion : deposito.conexiones) {
                            redDepositos.agregarRuta(deposito.id, conexion);
                        }
                    }
                }

                System.out.println("Depósitos cargados exitosamente.");
            }

        } catch (Exception e) {
            System.out.println("Error al cargar depósitos: " + e.getMessage());
        }
    }
}

// Esto saca la lectura de archivos JSON de MenuPrincipal, que actualmente concentra esa lógica además del menú y la coordinación del sistema.
