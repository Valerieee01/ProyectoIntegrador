package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class panePrestmosSalas extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableSalas;
    private JComboBox<String> comboBox;
    private JLabel labelImgEdificio;
    private JButton btnReservar;
    private Map<String, Sala[]> salasPorEdificio;
    private Map<String, String> imagenesEdificios;

    public panePrestmosSalas() {
        setBackground(new Color(255, 255, 198));
        setLayout(null);
        
        // Inicializar datos
        inicializarDatos();
        
        JLabel labelEdificio = new JLabel("Elige un edificio");
        labelEdificio.setFont(new Font("Tahoma", Font.ITALIC, 15));
        labelEdificio.setHorizontalAlignment(SwingConstants.CENTER);
        labelEdificio.setBounds(104, 31, 160, 30);
        add(labelEdificio);
        
        labelImgEdificio = new JLabel("");
        labelImgEdificio.setBounds(124, 72, 140, 150);
        add(labelImgEdificio);
        
        // ComboBox para selección de edificios
        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Selecciona un edificio ..."}));
        comboBox.setBounds(74, 233, 238, 36);
        add(comboBox);
        
        // Tabla de salas
        tableSalas = new JTable();
        tableSalas.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Código Sala", "Nombre", "Capacidad", "Estado", "Reservar"}
        ) {
            Class[] columnTypes = new Class[] {String.class, String.class, String.class, String.class, Boolean.class};
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        JScrollPane scrollSalas = new JScrollPane(tableSalas);
        scrollSalas.setBounds(451, 40, 350, 200);
        add(scrollSalas);
        
        // Botón de reserva
        btnReservar = new JButton("CONFIRMAR RESERVA");
        btnReservar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReservar.setBounds(451, 251, 350, 40);
        btnReservar.setEnabled(false);
        add(btnReservar);
        
        // Llenar el comboBox con los nombres de edificios
        for (String edificio : salasPorEdificio.keySet()) {
            comboBox.addItem(edificio);
        }
        
        // Listener para ComboBox
        comboBox.addActionListener(e -> {
            String seleccionado = (String) comboBox.getSelectedItem();
            if (seleccionado != null && !seleccionado.equals("Selecciona un edificio ...")) {
                // Cargar imagen del edificio
                cargarImagenEdificio(seleccionado);
                // Cargar salas del edificio
                cargarSalasEdificio(seleccionado);
            }
        });
        
        // Listener para selección de sala
        tableSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnReservar.setEnabled(tableSalas.getSelectedRow() >= 0);
            }
        });
        
        // Listener para botón de reserva
        btnReservar.addActionListener(e -> realizarReserva());
    }
    
    private void inicializarDatos() {
        salasPorEdificio = new HashMap<>();
        imagenesEdificios = new HashMap<>();
        
        // Datos para Jorge Luis Borges
        salasPorEdificio.put("JORGE LUIS BORGES", new Sala[] {
            new Sala("JLB-101", "Sala de Conferencias", "50 personas", "Disponible"),
            new Sala("JLB-102", "Laboratorio de Computación", "30 equipos", "Disponible")
        });
        imagenesEdificios.put("JORGE LUIS BORGES", "/img/1.jpg");
        
        // Datos para Enrico Fermi
        salasPorEdificio.put("ENRICO FERMI", new Sala[] {
            new Sala("EF-201", "Aula Magna", "100 personas", "Disponible"),
            new Sala("EF-202", "Sala de Proyecciones", "80 personas", "En mantenimiento")
        });
        imagenesEdificios.put("ENRICO FERMI", "/img/2.jpg");
        
        // Datos para Simón Bolívar
        salasPorEdificio.put("SIMÓN BOLÍVAR", new Sala[] {
            new Sala("SB-301", "Sala de Juntas", "20 personas", "Disponible"),
            new Sala("SB-302", "Laboratorio de Física", "25 equipos", "Ocupada")
        });
        imagenesEdificios.put("SIMÓN BOLÍVAR", "/img/3.png");
    }
    
    private void cargarImagenEdificio(String edificio) {
        String rutaImagen = imagenesEdificios.get(edificio);
        if (rutaImagen != null) {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
            if (icono.getImage() != null) {
                Image imagen = icono.getImage().getScaledInstance(140, 150, Image.SCALE_SMOOTH);
                labelImgEdificio.setIcon(new ImageIcon(imagen));
            }
        } else {
            labelImgEdificio.setIcon(null);
            labelImgEdificio.setText("Imagen no disponible");
        }
    }
    
    private void cargarSalasEdificio(String edificio) {
        DefaultTableModel model = (DefaultTableModel) tableSalas.getModel();
        model.setRowCount(0); // Limpiar tabla
        
        Sala[] salas = salasPorEdificio.get(edificio);
        if (salas != null) {
            for (Sala sala : salas) {
                model.addRow(new Object[] {
                    sala.codigo,
                    sala.nombre,
                    sala.capacidad,
                    sala.estado,
                    false // Checkbox para reserva
                });
            }
        }
    }
    
    private void realizarReserva() {
        int row = tableSalas.getSelectedRow();
        if (row >= 0) {
            String codigoSala = (String) tableSalas.getValueAt(row, 0);
            String nombreSala = (String) tableSalas.getValueAt(row, 1);
            String estado = (String) tableSalas.getValueAt(row, 3);
            
            if ("Disponible".equals(estado)) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Confirmar reserva para la sala:\n" + nombreSala + " (" + codigoSala + ")?",
                    "Confirmar Reserva",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Reserva confirmada con éxito");
                    // Aquí iría la lógica para guardar la reserva
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "La sala seleccionada no está disponible para reserva",
                    "Error", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    // Clase para representar una sala
    class Sala {
        String codigo;
        String nombre;
        String capacidad;
        String estado;
        
        public Sala(String codigo, String nombre, String capacidad, String estado) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.capacidad = capacidad;
            this.estado = estado;
        }
    }
}