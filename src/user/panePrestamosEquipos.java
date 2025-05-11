package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class panePrestamosEquipos extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tableDispositivos;
    private JTable tableReferencias;
    private JButton btnReservar;
    private Map<String, List<Referencia>> referenciasPorDispositivo;

    public panePrestamosEquipos() {
        setBackground(new Color(255, 255, 198));
        setLayout(null);
        
        // Inicializar datos de dispositivos y referencias
        inicializarDatos();
        
        JLabel labelDispositivo = new JLabel("SISTEMA DE PRÉSTAMO DE EQUIPOS");
        labelDispositivo.setHorizontalAlignment(SwingConstants.CENTER);
        labelDispositivo.setFont(new Font("Tahoma", Font.BOLD, 16));
        labelDispositivo.setBounds(10, 10, 580, 30);
        add(labelDispositivo);
        
        JLabel labelSeleccion = new JLabel("Seleccione un dispositivo:");
        labelSeleccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
        labelSeleccion.setBounds(81, 50, 150, 20);
        add(labelSeleccion);
        
        // Combo box para selección de dispositivos
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Selecciona un dispositivo..."}));
        comboBox.setBounds(81, 75, 238, 30);
        add(comboBox);
        
        // Imagen del dispositivo
        JLabel labelImgDispositivo = new JLabel("");
        labelImgDispositivo.setHorizontalAlignment(SwingConstants.CENTER);
        labelImgDispositivo.setBounds(81, 115, 238, 150);
        labelImgDispositivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(labelImgDispositivo);
        
        // Tabla de dispositivos
        tableDispositivos = new JTable();
        tableDispositivos.setModel(new DefaultTableModel(
            new Object[][] {
                {"1", "Computador Portátil", "PC"},
                {"2", "Proyector", "PY"},
                {"3", "Cámara", "CAM"},
                {"4", "Televisor", "TV"}
            },
            new String[] {"Código", "Nombre", "Abreviación"}
        ));
        JScrollPane scrollDispositivos = new JScrollPane(tableDispositivos);
        scrollDispositivos.setBounds(350, 75, 250, 120);
        add(scrollDispositivos);
        
        // Tabla para mostrar referencias
        tableReferencias = new JTable();
        tableReferencias.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Referencia", "Modelo", "Estado", "Disponibilidad"}
        ));
        JScrollPane scrollReferencias = new JScrollPane(tableReferencias);
        scrollReferencias.setBounds(81, 280, 520, 150);
        add(scrollReferencias);
        
        // Botón de reserva
        btnReservar = new JButton("RESERVAR EQUIPO");
        btnReservar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnReservar.setBounds(543, 214, 250, 40);
        btnReservar.setEnabled(false);
        add(btnReservar);
        
        // Llenar el comboBox con los nombres de dispositivos
        DefaultTableModel modelDispositivos = (DefaultTableModel) tableDispositivos.getModel();
        for (int i = 0; i < modelDispositivos.getRowCount(); i++) {
            String nombre = modelDispositivos.getValueAt(i, 1).toString();
            comboBox.addItem(nombre);
        }
        
        // ActionListener para el comboBox
        comboBox.addActionListener(e -> {
            String dispositivoSeleccionado = (String) comboBox.getSelectedItem();
            if (dispositivoSeleccionado != null && !dispositivoSeleccionado.equals("Selecciona un dispositivo...")) {
                String abreviatura = obtenerAbreviatura(dispositivoSeleccionado);
                cargarImagenDispositivo(abreviatura, labelImgDispositivo);
                mostrarReferenciasDispositivo(abreviatura);
                btnReservar.setEnabled(false);
            }
        });
        
        // Listener para selección en tabla de referencias
        tableReferencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnReservar.setEnabled(tableReferencias.getSelectedRow() >= 0);
            }
        });
        
        // ActionListener para el botón de reserva
        btnReservar.addActionListener(e -> realizarReserva());
    }
    
    private void realizarReserva() {
        int filaSeleccionada = tableReferencias.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String referencia = (String) tableReferencias.getValueAt(filaSeleccionada, 0);
            String modelo = (String) tableReferencias.getValueAt(filaSeleccionada, 1);
            String disponibilidad = (String) tableReferencias.getValueAt(filaSeleccionada, 3);
            
            if ("Disponible".equals(disponibilidad)) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Confirmar reserva para:\n" + modelo + " (" + referencia + ")?",
                    "Confirmar Reserva",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, "Reserva realizada con éxito");
                    // Aquí iría la lógica para registrar la reserva
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Este equipo no está disponible para préstamo",
                    "No Disponible", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void inicializarDatos() {
        referenciasPorDispositivo = new HashMap<>();
        
        referenciasPorDispositivo.put("PC", Arrays.asList(
            new Referencia("PC-001", "Dell XPS 15", "Excelente", "Disponible"),
            new Referencia("PC-002", "MacBook Pro M1", "Bueno", "Prestado"),
            new Referencia("PC-003", "HP EliteBook", "Regular", "Disponible")
        ));
        
        referenciasPorDispositivo.put("PY", Arrays.asList(
            new Referencia("PY-101", "Epson EB-1781W", "Excelente", "Disponible"),
            new Referencia("PY-102", "BenQ MH535", "Bueno", "Disponible")
        ));
        
        referenciasPorDispositivo.put("CAM", Arrays.asList(
            new Referencia("CAM-201", "Canon EOS R5", "Excelente", "En mantenimiento"),
            new Referencia("CAM-202", "Sony A7 III", "Bueno", "Disponible")
        ));
        
        referenciasPorDispositivo.put("TV", Arrays.asList(
            new Referencia("TV-301", "Samsung QLED 55\"", "Excelente", "Disponible"),
            new Referencia("TV-302", "LG OLED 65\"", "Bueno", "Prestado")
        ));
    }
    
    private String obtenerAbreviatura(String nombreDispositivo) {
        DefaultTableModel model = (DefaultTableModel) tableDispositivos.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(nombreDispositivo)) {
                return model.getValueAt(i, 2).toString();
            }
        }
        return "";
    }
    
    private void cargarImagenDispositivo(String abreviatura, JLabel label) {
        String rutaImagen = "";
        switch (abreviatura) {
            case "PC": rutaImagen = "/img/pc.jpg"; break;
            case "PY": rutaImagen = "/img/proyector.jpg"; break;
            case "CAM": rutaImagen = "/img/camara.jpg"; break;
            case "TV": rutaImagen = "/img/tv.jpg"; break;
            default: rutaImagen = "/img/default.jpg";
        }
        cargarImagenEnLabel(label, rutaImagen, 238, 150);
    }
    
    private void mostrarReferenciasDispositivo(String abreviaturaDispositivo) {
        DefaultTableModel model = (DefaultTableModel) tableReferencias.getModel();
        model.setRowCount(0);
        
        List<Referencia> referencias = referenciasPorDispositivo.get(abreviaturaDispositivo);
        if (referencias != null) {
            for (Referencia ref : referencias) {
                model.addRow(new Object[]{
                    ref.getCodigo(),
                    ref.getModelo(),
                    ref.getEstado(),
                    ref.getDisponibilidad()
                });
            }
        }
    }
    
    public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
            if (iconoOriginal.getImage() != null) {
                Image imagenOriginal = iconoOriginal.getImage();
                Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imagenEscalada));
            }
        } catch (Exception e) {
            label.setIcon(null);
            label.setText("Imagen no disponible");
        }
    }
    
    class Referencia {
        private String codigo;
        private String modelo;
        private String estado;
        private String disponibilidad;
        
        public Referencia(String codigo, String modelo, String estado, String disponibilidad) {
            this.codigo = codigo;
            this.modelo = modelo;
            this.estado = estado;
            this.disponibilidad = disponibilidad;
        }
        
        public String getCodigo() { return codigo; }
        public String getModelo() { return modelo; }
        public String getEstado() { return estado; }
        public String getDisponibilidad() { return disponibilidad; }
    }
}