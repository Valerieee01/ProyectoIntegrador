package admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class verPrestamosPane extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtIdPrestamo, txtCodigo, txtTipo;
    private JTextField txtEspecialidad, txtInicio, txtFin, txtSolicitante, txtAdmin;
    private JButton btnModificar, btnEliminar, btnRegistrar;
    private JLabel lbltituloPrestamos;
    private JComboBox<String> comboObjeto;
    private JComboBox<String> comboSolicitante, comboAdmin;
    private Map<String, Integer> mapaUsuarios = new HashMap<>();
    private Map<String, Integer> mapaAdmins = new HashMap<>();


    public verPrestamosPane() {
        setLayout(null);
        setBackground(new Color(255, 255, 206));

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 20, 50, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(80, 20, 100, 25);
        add(txtTipo);

        JLabel lblIdPrestamo = new JLabel("ID Prestamo:");
        lblIdPrestamo.setBounds(200, 20, 90, 25);
        add(lblIdPrestamo);

        txtIdPrestamo = new JTextField();
        txtIdPrestamo.setBounds(300, 20, 100, 25);
        add(txtIdPrestamo);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(420, 20, 60, 25);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(490, 20, 100, 25);
        add(txtCodigo);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(610, 20, 100, 25);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(720, 20, 100, 25);
        add(btnEliminar);

        tableModel = new DefaultTableModel(new String[]{"ID", "Objeto", "Especialidad", "Inicio", "Fin", "Solicitante", "Admin"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 60, 800, 300);
        add(scrollPane);

        lbltituloPrestamos = new JLabel("Prestamos de Salas  y Equipos");
        lbltituloPrestamos.setFont(new Font("Tahoma", Font.BOLD, 28));
        lbltituloPrestamos.setBounds(30, 490, 438, 40);
        add(lbltituloPrestamos);
        
        
        // resgistro del prestamo
        JLabel lblObjeto = new JLabel("Objeto:");
        lblObjeto.setBounds(20, 370, 60, 25);
        add(lblObjeto);

        comboObjeto = new JComboBox<>();
        comboObjeto.setBounds(71, 371, 250, 25);  // Más ancho para mostrar nombre y código
        add(comboObjeto);
;

        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setBounds(350, 371, 100, 25);
        add(lblEsp);
        txtEspecialidad = new JTextField();
        txtEspecialidad.setBounds(430, 370, 100, 25);
        add(txtEspecialidad);

        JLabel lblInicio = new JLabel("Inicio:");
        lblInicio.setBounds(556, 370, 60, 25);
        add(lblInicio);
        txtInicio = new JTextField("YYYY-MM-DD HH:MM:SS");
        txtInicio.setBounds(610, 371, 150, 25);
        add(txtInicio);

        JLabel lblFin = new JLabel("Fin:");
        lblFin.setBounds(20, 410, 50, 25);
        add(lblFin);
        txtFin = new JTextField("YYYY-MM-DD HH:MM:SS");
        txtFin.setBounds(80, 410, 150, 25);
        add(txtFin);

        JLabel lblSolicitante = new JLabel("ID Sol:");
        lblSolicitante.setBounds(250, 410, 100, 25);
        add(lblSolicitante);
    
        // Reemplaza txtAdmin
        comboAdmin = new JComboBox<>();
        comboAdmin.setBounds(490, 410, 100, 25);
        add(comboAdmin);

        JLabel lblAdmin = new JLabel("ID Admin:");
        lblAdmin.setBounds(420, 410, 100, 25);
        add(lblAdmin);
        
        // Reemplaza txtSolicitante
        comboSolicitante = new JComboBox<>();
        comboSolicitante.setBounds(300, 410, 100, 25);
        add(comboSolicitante);


        btnRegistrar = new JButton("Registrar Préstamo");
        btnRegistrar.setBounds(610, 410, 150, 25);
        add(btnRegistrar);

        cargarPrestamos();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    txtTipo.setText(tableModel.getValueAt(fila, 3).toString());
                    
                }
            }
        });

        btnModificar.addActionListener(e -> modificarPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        btnRegistrar.addActionListener(e -> registrarPrestamo());
        
        cargarOpcionesObjeto();
        
        String seleccionado = (String) comboObjeto.getSelectedItem(); 
        String[] partes = seleccionado.split("[-:]");
        String tipo = partes[0].trim(); 
        int codigo = Integer.parseInt(partes[1].trim()); 
        
        cargarUsuarios();
        cargarAdministradores();
    }
    
    private void cargarUsuarios() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            PreparedStatement ps = con.prepareStatement("SELECT identificacion , nombre FROM solicitante");
            ResultSet rs = ps.executeQuery();
            mapaUsuarios.clear();
            comboSolicitante.removeAllItems();

            while (rs.next()) {
                int id = rs.getInt("identificacion");
                String nombre = rs.getString("nombre");
                String entrada = id + " - " + nombre;
                comboSolicitante.addItem(entrada);
                mapaUsuarios.put(entrada, id);
            }

            rs.close(); ps.close(); con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void cargarAdministradores() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            PreparedStatement ps = con.prepareStatement("SELECT identificacion , nombre FROM AdministradorSoftware");
            ResultSet rs = ps.executeQuery();
            mapaAdmins.clear();
            comboAdmin.removeAllItems();

            while (rs.next()) {
                int id = rs.getInt("identificacion");
                String nombre = rs.getString("nombre");
                String entrada = id + " - " + nombre;
                comboAdmin.addItem(entrada);
                mapaAdmins.put(entrada, id);
            }

            rs.close(); ps.close(); con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar administradores: " + e.getMessage());
        }
    }

    private void cargarOpcionesObjeto() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            comboObjeto.removeAllItems();

            // Salas Informáticas
            PreparedStatement psSala = con.prepareStatement("SELECT codigo, observaciones FROM sala_informatica");
            ResultSet rsSala = psSala.executeQuery();
            while (rsSala.next()) {
                int codigo = rsSala.getInt("codigo");
                String descripcion = rsSala.getString("observaciones");
                comboObjeto.addItem("Sala-" + codigo + ": " + descripcion);
            }
            rsSala.close(); psSala.close();

            // Equipos Audiovisuales
            PreparedStatement psEq = con.prepareStatement("SELECT codigo, nombre FROM equipo_audiovisual");
            ResultSet rsEq = psEq.executeQuery();
            while (rsEq.next()) {
                int codigo = rsEq.getInt("codigo");
                String nombre = rsEq.getString("nombre");
                comboObjeto.addItem("Equipo-" + codigo + ": " + nombre);
            }
            rsEq.close(); psEq.close();

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar objetos: " + e.getMessage());
        }
    }

    private void cargarPrestamos() {
        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            tableModel.setRowCount(0); // Limpiar tabla

            String sql = "SELECT rp.id, rp.objetoSolicita, rp.especialidad, rp.FechaHoraInicio, rp.FechaHoraFin, " +
                         "s.nombre AS nombreSolicitante, a.nombre AS nombreAdmin " +
                         "FROM RegistroPrestamo rp " +
                         "JOIN Solicitante s ON rp.idSolicitante = s.identificacion " +
                         "JOIN AdministradorSoftware a ON rp.idAdmin = a.identificacion";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[]{
                    rs.getInt("id"),
                    rs.getString("objetoSolicita"),
                    rs.getString("especialidad"),
                    rs.getTimestamp("FechaHoraInicio"),
                    rs.getTimestamp("FechaHoraFin"),
                    rs.getString("nombreSolicitante"),
                    rs.getString("nombreAdmin")
                };
                tableModel.addRow(fila);
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
        }
    }


    private void modificarPrestamo() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para modificar.");
            return;
        }
        int id = (int) tableModel.getValueAt(fila, 0);
        String tipo = txtTipo.getText();
        int nuevoIdPrestamo = Integer.parseInt(txtIdPrestamo.getText());
        int nuevoCodigo = Integer.parseInt(txtCodigo.getText());

        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();
            String sql = tipo.equalsIgnoreCase("Sala") ?
                         "UPDATE PrestamoSala SET idPrestamo = ?, codigoSala = ? WHERE id = ?" :
                         "UPDATE PrestamoEquipo SET idPrestamo = ?, codigoEquipo = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoIdPrestamo);
            ps.setInt(2, nuevoCodigo);
            ps.setInt(3, id);
            ps.executeUpdate();
            ps.close();
            con.close();
            JOptionPane.showMessageDialog(this, "Préstamo modificado.");
            cargarPrestamos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarPrestamo() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para eliminar.");
            return;
        }
        int id = (int) tableModel.getValueAt(fila, 0);
        String tipo = tableModel.getValueAt(fila, 3).toString();

        try {
            Connection con = util.ConexionBD.obtenerConexionAdmin();

            // Primero eliminar dependencias
            String sql1 = tipo.equalsIgnoreCase("Sala") ? 
                "DELETE FROM PrestamoSala WHERE idPrestamo = ?" : 
                "DELETE FROM PrestamoEquipo WHERE idPrestamo = ?";

            PreparedStatement ps = con.prepareStatement(sql1);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // Luego eliminar el registro principal
            String sql2 = "DELETE FROM RegistroPrestamo WHERE id = ?";
            PreparedStatement ps1 = con.prepareStatement(sql2);
            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            con.close();

            JOptionPane.showMessageDialog(this, "Préstamo eliminado.");
            cargarPrestamos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void registrarPrestamo() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = util.ConexionBD.obtenerConexionAdmin();

            // 1. Obtener ID del siguiente préstamo (usando RETURNING INTO con Oracle)
            String getIdSql = "SELECT SEQ_REGISTROPRESTAMO.NEXTVAL FROM dual";
            ps = con.prepareStatement(getIdSql);
            rs = ps.executeQuery();
            int nuevoIdPrestamo = -1;
            if (rs.next()) {
                nuevoIdPrestamo = rs.getInt(1);
            }
            rs.close();
            ps.close();

            if (nuevoIdPrestamo == -1) throw new SQLException("No se pudo obtener el ID del préstamo.");
            
            int idSolicitante = mapaUsuarios.get((Integer) comboSolicitante.getSelectedItem());
            int idAdmin = mapaAdmins.get((Integer) comboAdmin.getSelectedItem());

            // 2. Insertar en RegistroPrestamo
            String insertRegSql = "INSERT INTO RegistroPrestamo (id, objetoSolicita, especialidad, FechaHoraInicio, FechaHoraFin, idSolicitante, idAdmin) " +
                                  "VALUES (SEQ_REGISTROPRESTAMO.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?)";
            ps = con.prepareStatement(insertRegSql);
            ps.setString(1, (String) comboObjeto.getSelectedItem());
            ps.setString(2, txtEspecialidad.getText());
            ps.setString(3, txtInicio.getText());
            ps.setString(4, txtFin.getText());
            ps.setInt(5, idSolicitante);
            ps.setInt(6, idAdmin);
            ps.executeUpdate();
            ps.close();

            // 3. Determinar tipo y código del objeto seleccionado
            String seleccionado = (String) comboObjeto.getSelectedItem(); // Ej: "Sala-101: Lab redes"
            String[] partes = seleccionado.split("[-:]");
            String tipo = partes[0].trim(); // "Sala" o "Equipo"
            int codigo = Integer.parseInt(partes[1].trim());

            // 4. Insertar en tabla correspondiente
            if (tipo.equalsIgnoreCase("Sala")) {
                String insertSala = "INSERT INTO PrestamoSala (id, idPrestamo, codigoSala) VALUES (SEQ_PRESTAMOSALA.NEXTVAL, ?, ?)";
                ps = con.prepareStatement(insertSala);
                ps.setInt(1, nuevoIdPrestamo);
                ps.setInt(2, codigo);
            } else {
                String insertEquipo = "INSERT INTO PrestamoEquipo (id, idPrestamo, codigoEquipo) VALUES (SEQ_PRESTAMOEQUPO.NEXTVAL, ?, ?)";
                ps = con.prepareStatement(insertEquipo);
                ps.setInt(1, nuevoIdPrestamo);
                ps.setInt(2, codigo);
            }

            ps.executeUpdate();
            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente.");
            cargarPrestamos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Ignorado
            }
        }
    }

}
