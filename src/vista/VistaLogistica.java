package vista;

import logica.Deposito;
import logica.Paquete;
import logica.envios.ControladorEnvios;
import logica.depositos.ControladorDepositos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VistaLogistica extends JPanel {
    private final ControladorEnvios envios;
    private final ControladorDepositos depositos;

    private final JTextField id = new JTextField();
    private final JTextField peso = new JTextField();
    private final JTextField destino = new JTextField();
    private final JTextField contenido = new JTextField();
    private final JCheckBox urgente = new JCheckBox("Es urgente");

    private final JLabel estado = new JLabel(
            "Listo. Cargá un inventario o registrá un paquete.");

    private final DefaultTableModel pendientes = modelo(
            "ID", "Kg", "Destino", "Prioritario", "Contenido");

    private final DefaultTableModel camion = modelo(
            "ID", "Kg", "Destino", "Prioritario", "Contenido");

    private final DefaultTableModel listaDepositos = modelo(
            "ID", "Auditado", "Última auditoría");

    private final JSpinner nivel = new JSpinner(
            new SpinnerNumberModel(0, 0, 100, 1));

    private Integer depositoBuscado;

    public VistaLogistica(
            ControladorEnvios envios,
            ControladorDepositos depositos) {

        super(new BorderLayout(10, 10));

        this.envios = envios;
        this.depositos = depositos;

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Paquetes y camión", panelEnvios());
        pestanas.addTab("Depósitos y rutas", panelDepositos());

        add(pestanas, BorderLayout.CENTER);
        add(estado, BorderLayout.SOUTH);

        refrescar();
    }

    private JPanel panelEnvios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 6));

        formulario.setBorder(
                BorderFactory.createTitledBorder("Registrar paquete"));

        formulario.add(new JLabel("ID"));
        formulario.add(id);

        formulario.add(new JLabel("Peso (kg)"));
        formulario.add(peso);

        formulario.add(new JLabel("Destino"));
        formulario.add(destino);

        formulario.add(new JLabel("Contenido"));
        formulario.add(contenido);

        formulario.add(urgente);

        formulario.add(boton("Registrar paquete", () -> {
            double kilos;

            try {
                kilos = Double.parseDouble(
                        peso.getText().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Ingresá un peso numérico, por ejemplo 12,5.");
            }

            boolean registrado = envios.cargarPaquete(
                    id.getText(),
                    kilos,
                    destino.getText(),
                    urgente.isSelected(),
                    contenido.getText());

            if (!registrado) {
                throw new IllegalArgumentException(
                        "Ya existe un paquete con ese ID.");
            }

            id.setText("");
            peso.setText("");
            destino.setText("");
            contenido.setText("");
            urgente.setSelected(false);
            id.requestFocusInWindow();

            return "Paquete registrado en el centro.";
        }));

        panel.add(formulario, BorderLayout.NORTH);

        JSplitPane tablas = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tabla("Centro: orden de despacho", pendientes),
                tabla("Camión: el último cargado sale primero", camion));

        tablas.setResizeWeight(0.5);
        panel.add(tablas, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridLayout(1, 0, 6, 6));

        acciones.add(boton("Cargar inventario", () -> {
            envios.cargarInventario();
            return "Inventario cargado.";
        }));

        acciones.add(boton("Enviar al camión", () -> {
            Paquete paquete = envios.despacharHaciaCamion();

            return paquete == null
                    ? "No hay paquetes pendientes."
                    : "Enviado al camión: " + paquete.getId();
        }));

        acciones.add(boton("Deshacer carga", () -> {
            Paquete paquete = envios.deshacerCarga();

            return paquete == null
                    ? "El camión está vacío."
                    : "Devuelto al centro: " + paquete.getId();
        }));

        acciones.add(boton("Descargar camión", () -> {
            Paquete paquete = envios.descargarCamion();

            return paquete == null
                    ? "El camión está vacío."
                    : "Paquete descargado: " + paquete.getId();
        }));

        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelDepositos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel controles = new JPanel(new GridLayout(0, 1, 6, 6));

        JPanel carga = new JPanel(new FlowLayout(FlowLayout.LEFT));

        carga.add(boton("Cargar depósitos", () -> {
            depositos.cargarDepositos();
            depositoBuscado = null;

            return "Depósitos cargados. Se muestra el nivel seleccionado.";
        }));

        carga.add(boton("Auditar", () -> {
            depositos.auditarDepositos();
            return "Auditoría completada.";
        }));

        carga.add(new JLabel("Nivel:"));
        carga.add(nivel);

        carga.add(boton("Ver nivel", () -> {
            nivel.commitEdit();
            depositoBuscado = null;

            return "Mostrando depósitos del nivel " + nivel.getValue() + ".";
        }));

        controles.add(carga);

        JTextField idDeposito = new JTextField(8);
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        busqueda.add(new JLabel("ID del depósito:"));
        busqueda.add(idDeposito);

        busqueda.add(boton("Buscar", () -> {
            int numero = entero(idDeposito);
            depositoBuscado = numero;

            return depositos.buscarDeposito(numero) == null
                    ? "No existe ese depósito."
                    : "Depósito encontrado: " + numero;
        }));

        busqueda.add(boton("Agregar depósito", () -> {
            int numero = entero(idDeposito);
            depositos.insertarDeposito(numero);
            depositoBuscado = numero;

            return "Depósito agregado sin conexiones. Las rutas se cargan desde el JSON.";
        }));

        controles.add(busqueda);

        JTextField origen = new JTextField(6);
        JTextField destinoRuta = new JTextField(6);

        JPanel rutas = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rutas.add(new JLabel("Origen:"));
        rutas.add(origen);

        rutas.add(new JLabel("Destino:"));
        rutas.add(destinoRuta);

        rutas.add(boton("Calcular saltos", () -> {
            int saltos = depositos.calcularRuta(
                    entero(origen), entero(destinoRuta));

            return saltos < 0
                    ? "No hay ruta entre esos depósitos."
                    : "Cantidad de saltos: " + saltos;
        }));

        controles.add(rutas);

        panel.add(controles, BorderLayout.NORTH);
        panel.add(
                tabla("Resultado de búsqueda o consulta por nivel", listaDepositos),
                BorderLayout.CENTER);

        return panel;
    }

    private int entero(JTextField campo) {
        try {
            int valor = Integer.parseInt(campo.getText().trim());

            if (valor >= 0) {
                return valor;
            }
        } catch (NumberFormatException ignored) {
        }

        throw new IllegalArgumentException(
                "Ingresá un ID entero mayor o igual que cero.");
    }

    private JButton boton(String texto, Accion accion) {
        JButton boton = new JButton(texto);

        boton.addActionListener(evento -> {
            try {
                estado.setText(accion.ejecutar());
            } catch (IllegalArgumentException | IllegalStateException e) {
                estado.setText("Error: " + e.getMessage());
            } catch (java.text.ParseException e) {
                estado.setText("Error: ingresá un nivel válido.");
            } finally {
                refrescar();
            }
        });

        return boton;
    }

    private interface Accion {
        String ejecutar() throws java.text.ParseException;
    }

    private DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private JScrollPane tabla(String titulo, DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);

        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setColumnHeaderView(tabla.getTableHeader());
        scroll.setBorder(BorderFactory.createTitledBorder(titulo));
        scroll.setPreferredSize(new Dimension(410, 240));

        return scroll;
    }

    private void mostrarPaquetes(
            DefaultTableModel modelo,
            List<Paquete> paquetes) {

        modelo.setRowCount(0);

        for (Paquete paquete : paquetes) {
            modelo.addRow(new Object[]{
                    paquete.getId(),
                    paquete.getPeso(),
                    paquete.getDestino(),
                    paquete.esPrioritario() ? "Sí" : "No",
                    paquete.getContenido()
            });
        }
    }

    private void mostrarDeposito(Deposito deposito) {
        if (deposito != null) {
            listaDepositos.addRow(new Object[]{
                    deposito.getId(),
                    deposito.isVisitado() ? "Sí" : "No",
                    deposito.getFechaUltimaAuditoria().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            });
        }
    }

    private void refrescar() {
        mostrarPaquetes(pendientes, envios.obtenerPendientes());
        mostrarPaquetes(camion, envios.obtenerPaquetes());

        listaDepositos.setRowCount(0);

        if (depositoBuscado != null) {
            mostrarDeposito(depositos.buscarDeposito(depositoBuscado));
        } else {
            for (Deposito deposito : depositos.obtenerDepositosPorNivel(
                    (Integer) nivel.getValue())) {

                mostrarDeposito(deposito);
            }
        }
    }
}
