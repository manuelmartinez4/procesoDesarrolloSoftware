package vista;

import logica.Deposito;
import logica.Paquete;
import logica.CargadorDatos;
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
    private final CargadorDatos cargadorJson;
    private final CargadorDatos cargadorMemoria;

    private final JTextField id = new JTextField();
    private final JTextField peso = new JTextField();
    private final JTextField destino = new JTextField();
    private final JTextField contenido = new JTextField();
    private final JCheckBox urgente = new JCheckBox("Es urgente");
    private final JComboBox<String> zona = new JComboBox<>(
            new String[]{"Automática", "Norte", "Sur", "Centro", "Otro"});
    private final JLabel resumen = new JLabel();

    private final JLabel estado = new JLabel(
            "Listo. Cargá un inventario o registrá un paquete.");

    private final DefaultTableModel pendientes = modelo(
            "ID", "Kg", "Destino", "Zona", "Prioritario", "Contenido");

    private final DefaultTableModel camion = modelo(
            "ID", "Kg", "Destino", "Zona", "Prioritario", "Contenido");

    private final DefaultTableModel listaDepositos = modelo(
            "ID", "Nombre", "Auditado", "Última auditoría", "Conexiones");

    private final JSpinner nivel = new JSpinner(
            new SpinnerNumberModel(0, 0, 100, 1));

    private Integer depositoBuscado;
    private boolean mostrarTodos = true;

    public VistaLogistica(
            ControladorEnvios envios,
            ControladorDepositos depositos) {
        this(envios, depositos, null, null);
    }

    public VistaLogistica(ControladorEnvios envios, ControladorDepositos depositos,
                          CargadorDatos cargadorJson, CargadorDatos cargadorMemoria) {

        super(new BorderLayout(10, 10));

        this.envios = envios;
        this.depositos = depositos;
        this.cargadorJson = cargadorJson;
        this.cargadorMemoria = cargadorMemoria;

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Paquetes y camión", panelEnvios());
        pestanas.addTab("Depósitos y rutas", panelDepositos());

        add(pestanas, BorderLayout.CENTER);
        if (cargadorJson != null && cargadorMemoria != null) {
            add(panelFuente(), BorderLayout.NORTH);
        }
        JPanel pie = new JPanel(new GridLayout(0, 1, 0, 5));
        pie.add(resumen);
        pie.add(estado);
        add(pie, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(1100, 650));

        refrescar();
    }

    private JPanel panelFuente() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> fuente = new JComboBox<>(
                new String[]{"Archivos JSON", "Datos de demostración"});
        fuente.setName("fuenteDatos");
        fuente.addActionListener(evento -> {
            CargadorDatos elegido = fuente.getSelectedIndex() == 0
                    ? cargadorJson : cargadorMemoria;
            envios.cambiarCargador(elegido);
            depositos.cambiarCargador(elegido);
            estado.setText("Fuente seleccionada. Usá Cargar inventario o Cargar depósitos; se conservan los datos actuales.");
        });
        panel.add(new JLabel("Cargar datos desde:"));
        panel.add(fuente);
        panel.add(new JLabel("Los cambios de esta sesión se mantienen hasta cerrar el programa."));
        return panel;
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

        formulario.add(new JLabel("Zona"));
        formulario.add(zona);

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
                    contenido.getText(),
                    zona.getSelectedIndex() == 0 ? null
                            : Paquete.ZonaDestino.desdeTexto((String) zona.getSelectedItem()));

            if (!registrado) {
                throw new IllegalArgumentException(
                        "Ya existe un paquete con ese ID.");
            }

            id.setText("");
            peso.setText("");
            destino.setText("");
            contenido.setText("");
            urgente.setSelected(false);
            zona.setSelectedIndex(0);
            id.requestFocusInWindow();

            return "Paquete registrado en el centro.";
        }));

        panel.add(formulario, BorderLayout.NORTH);

        JSplitPane tablas = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tabla("Centro: orden de despacho", pendientes),
                tabla("Camión: la primera fila se descarga primero", camion));

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
            mostrarTodos = true;

            return "Depósitos cargados. Se muestran todos.";
        }));

        carga.add(boton("Auditar", () -> {
            if (depositos.contarDepositos() == 0) return "No hay depósitos para auditar.";
            depositos.auditarDepositos();
            return "Auditoría completada.";
        }));

        carga.add(new JLabel("Nivel:"));
        carga.add(nivel);

        carga.add(boton("Ver nivel", () -> {
            nivel.commitEdit();
            depositoBuscado = null;
            mostrarTodos = false;

            return "Mostrando depósitos del nivel " + nivel.getValue() + ".";
        }));

        carga.add(boton("Ver todos", () -> {
            depositoBuscado = null;
            mostrarTodos = true;
            return "Mostrando todos los depósitos.";
        }));

        controles.add(carga);

        JTextField idDeposito = new JTextField(8);
        idDeposito.setName("idDeposito");
        JTextField nombreDeposito = new JTextField(20);
        nombreDeposito.setName("nombreDeposito");
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
            depositos.insertarDeposito(numero, nombreDeposito.getText());
            depositoBuscado = numero;

            return "Depósito agregado. Podés conectarlo desde los campos Origen y Destino.";
        }));

        busqueda.add(new JLabel("Nombre (opcional):"));
        busqueda.add(nombreDeposito);

        controles.add(busqueda);

        JTextField origen = new JTextField(6);
        JTextField destinoRuta = new JTextField(6);
        origen.setName("origenRuta");
        destinoRuta.setName("destinoRuta");

        JPanel rutas = new JPanel(new FlowLayout(FlowLayout.LEFT));

        rutas.add(new JLabel("Origen:"));
        rutas.add(origen);

        rutas.add(new JLabel("Destino:"));
        rutas.add(destinoRuta);

        rutas.add(boton("Conectar depósitos", () -> {
            depositos.conectarDepositos(entero(origen), entero(destinoRuta));
            return "Conexión creada en ambos sentidos.";
        }));

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
                tabla("Depósitos: listado, búsqueda y consulta por nivel", listaDepositos),
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
                    paquete.getZonaDestino(),
                    paquete.esPrioritario() ? "Sí" : "No",
                    paquete.getContenido()
            });
        }
    }

    private void mostrarDeposito(Deposito deposito) {
        if (deposito != null) {
            listaDepositos.addRow(new Object[]{
                    deposito.getId(),
                    deposito.getNombre(),
                    deposito.isVisitado() ? "Sí" : "No",
                    deposito.isVisitado() ? deposito.getFechaUltimaAuditoria().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Pendiente",
                    depositos.obtenerConexiones(deposito.getId())
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
            List<Deposito> resultado = mostrarTodos ? depositos.obtenerTodos()
                    : depositos.obtenerDepositosPorNivel((Integer) nivel.getValue());
            for (Deposito deposito : resultado) {

                mostrarDeposito(deposito);
            }
        }
        resumen.setText("Pendientes: " + pendientes.getRowCount()
                + "   |   En camión: " + camion.getRowCount()
                + "   |   Depósitos: " + depositos.contarDepositos());
    }
}
