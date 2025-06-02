package admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import user.paneHistorialPrestamos;
import user.panePrestamosEquipos;
import user.panePrestmosSalas;

public class mainInterfaceAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private JPanel contentPane;
	JPanel panelContenedor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainInterfaceAdmin frame = new mainInterfaceAdmin();
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
	public mainInterfaceAdmin() {
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    setBounds(100, 100, 861, 551);
		    setLocationRelativeTo(null);

		    cardLayout = new CardLayout();
		    contentPane = new JPanel();
		    contentPane.setLayout(null); 
		    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		    setContentPane(contentPane); 
		    
		    panelContenedor = new JPanel();
		    panelContenedor.setBackground(new Color(255, 255, 206));
		    panelContenedor.setBounds(0, 63, 843, 449);
		    panelContenedor.setLayout(cardLayout);
		    contentPane.add(panelContenedor);
		    
		    // Instanciar los paneles
		    crearEquiposPane crearEquiposPane = new crearEquiposPane();
		    crearSalasPane crearSalasPane = new crearSalasPane();
		    verPrestamosPane verPrestamosPane = new verPrestamosPane();

		    // Crear panel de bienvenida
		    JPanel panelBienvenida = new JPanel();
		    panelBienvenida.setBackground(new Color(255, 255, 206));
		    JLabel bienvenidolabel = new JLabel("BIENVENIDO USUARIO");
		    bienvenidolabel.setHorizontalAlignment(SwingConstants.CENTER);
		    bienvenidolabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
		    panelBienvenida.add(bienvenidolabel);

		    // Añadirlos al contenedor con una clave
		    panelContenedor.add(panelBienvenida, "panelBienvenida");
		    panelContenedor.add(verPrestamosPane, "namePaneVerPrestamos");
		    panelContenedor.add(crearSalasPane, "namePaneCrearSalas");
		    panelContenedor.add(crearEquiposPane, "namePaneCrearEquipos");

		    // Mostrar el panel de bienvenida al inicio
		    cardLayout.show(panelContenedor, "panelBienvenida");
	}

}
