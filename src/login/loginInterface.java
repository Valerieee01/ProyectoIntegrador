package login;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import admin.mainInterfaceAdmin;

import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

/*Importacion clases**/
import connectionFrames.connectFrames;
import user.mainInterfaceUser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class loginInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldCorreo;
	private JPasswordField textFieldPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginInterface frame = new loginInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loginInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 859, 551);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel titulo = new JLabel("Gestor de Prestamos");
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setFont(new Font("Sylfaen", Font.ITALIC, 19));
		titulo.setBounds(290, 69, 248, 50);
		contentPane.add(titulo);
		
		JLabel labelImg = new JLabel("");
		labelImg.setBounds(364, 130, 95, 80);
		cargarImagenEnLabel(labelImg,"/img/logo.png", 95, 80);
		contentPane.add(labelImg);
		
		textFieldCorreo = new JTextField("Escribe Correo ..." , 20);
		textFieldCorreo.setColumns(10);
		textFieldCorreo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldCorreo.setBounds(335, 277, 151, 32);
		contentPane.add(textFieldCorreo);
		
		textFieldPassword = new JPasswordField();
		textFieldPassword.setColumns(10);
		textFieldPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(217, 244, 253)));
		textFieldPassword.setBounds(335, 372, 151, 32);
		contentPane.add(textFieldPassword);
		
		JButton btnInicioSesion = new JButton("Iniciar Sesion");
		btnInicioSesion.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String correo = textFieldCorreo.getText().trim();
		        String password = new String(textFieldPassword.getPassword()).trim();

		        if (!correo.isEmpty() && !password.isEmpty()) {
		            try (Connection conn = util.ConexionBD.obtenerConexionAdmin()) {

		                // Verificar en AdministradorSoftware
		                String queryAdmin = "SELECT nombre FROM AdministradorSoftware WHERE correo = ? AND contrasenia = ?";
		                try (PreparedStatement pstAdmin = conn.prepareStatement(queryAdmin)) {
		                    pstAdmin.setString(1, correo);
		                    pstAdmin.setString(2, password);
		                    ResultSet rsAdmin = pstAdmin.executeQuery();

		                    if (rsAdmin.next()) {
		                        Long id = rsAdmin.getLong("identificacion");
		                        String nombre = rsAdmin.getString("nombre");
		                        String apellido = rsAdmin.getString("apellido");
		                        String correoUsuario = rsAdmin.getString("correo");

		                        // Guardar usuario en sesión
		                        util.UsuarioSesion.iniciarSesion(id, nombre, apellido, correoUsuario);

		                        JOptionPane.showMessageDialog(null, "Bienvenido Administrador: " + nombre);
		                        
		                        // Abrir ventana de administrador
		                        mainInterfaceAdmin adminFrame = new mainInterfaceAdmin();
		                        adminFrame.setVisible(true);
		                        dispose(); // cerrar login
		                        return;
		                    }
		                }

		                // Verificar en solicitante
		                String querySolicitante = "SELECT identificacion, nombre, apellido, correo FROM solicitante WHERE correo = ? AND contrasenia = ?";
		                try (PreparedStatement pstSolicitante = conn.prepareStatement(querySolicitante)) {
		                    pstSolicitante.setString(1, correo);
		                    pstSolicitante.setString(2, password);
		                    ResultSet rsSolicitante = pstSolicitante.executeQuery();
		                    if (rsSolicitante.next()) {
		                        Long id = rsSolicitante.getLong("identificacion");
		                        String nombre = rsSolicitante.getString("nombre");
		                        String apellido = rsSolicitante.getString("apellido");
		                        String correoUsuario = rsSolicitante.getString("correo");

		                        // Guardar usuario en sesión
		                        util.UsuarioSesion.iniciarSesion(id, nombre, apellido, correoUsuario);

		                        JOptionPane.showMessageDialog(null, "Bienvenido Solicitante: " + nombre);

		                        // Abrir ventana de solicitante
		                        mainInterfaceUser userFrame = new mainInterfaceUser();
		                        userFrame.setVisible(true);
		                        dispose(); // cerrar login
		                        return;
		                    }
		                }

		                // Si no se encontró en ninguna tabla
		                JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos.");

		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(null, "Error de conexión:\n" + ex.getMessage());
		                ex.printStackTrace();
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.");
		        }
		    }
		});

		btnInicioSesion.setBackground(new Color(217, 244, 253));
		btnInicioSesion.setBounds(353, 431, 120, 23);
		contentPane.add(btnInicioSesion);
		
		JLabel registro = new JLabel("No tienes cuenta?");
		registro.setBounds(562, 29, 129, 14);
		contentPane.add(registro);
		
		JButton btnNewButton = new JButton("Registrate aquí");
		btnNewButton.addActionListener(e -> {connectFrames.AbrirFrameSingin(this);});
		btnNewButton.setBounds(669, 25, 151, 23);
		btnNewButton.setBackground(new Color(217, 244, 253));

		contentPane.add(btnNewButton);
		
		JLabel labelConrtasena = new JLabel("Contraseña :");
		labelConrtasena.setBounds(335, 349, 95, 14);
		contentPane.add(labelConrtasena);
		
		JLabel labelnicioSesion = new JLabel("Inicio Sesión");
		labelnicioSesion.setHorizontalAlignment(SwingConstants.CENTER);
		labelnicioSesion.setFont(new Font("Sylfaen", Font.ITALIC, 17));
		labelnicioSesion.setBounds(353, 234, 120, 32);
		contentPane.add(labelnicioSesion);
	}
	
	public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
	    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
	    Image imagenOriginal = iconoOriginal.getImage();
	    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
	    label.setIcon(iconoEscalado);
	}
}
