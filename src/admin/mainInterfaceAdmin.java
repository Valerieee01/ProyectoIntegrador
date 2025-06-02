package admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	    verUsuariosPane verUsuariosPane = new verUsuariosPane();

	    // Crear panel de bienvenida
	    JPanel panelBienvenida = new JPanel();
	    panelBienvenida.setBackground(new Color(255, 255, 206));
	    JLabel bienvenidolabel = new JLabel("BIENVENIDO ADMINISTRADOR");
	    bienvenidolabel.setHorizontalAlignment(SwingConstants.CENTER);
	    bienvenidolabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
	    panelBienvenida.add(bienvenidolabel);

	    // Añadirlos al contenedor con una clave
	    panelContenedor.add(panelBienvenida, "panelBienvenida");
	    panelContenedor.add(verPrestamosPane, "namePaneVerPrestamos");
	    panelContenedor.add(crearSalasPane, "namePaneCrearSalas");
	    panelContenedor.add(crearEquiposPane, "namePaneCrearEquipos");
	    panelContenedor.add(verUsuariosPane, "namePaneVerUsuarios");

	    // Mostrar el panel de bienvenida al inicio
	    cardLayout.show(panelContenedor, "panelBienvenida");
	    
		Color colorNormal = new Color(199, 235, 252); // Color inicial
		Color colorHover = new Color(220, 220, 220);  // Color al pasar el mouse

	    
	    JPanel panelMenuBar = new JPanel();
	    panelMenuBar.setLayout(null);
	    panelMenuBar.setBackground(new Color(199, 235, 252));
	    panelMenuBar.setBounds(0, 0, 843, 64);
	    contentPane.add(panelMenuBar);
	    
	    JLabel labelCrearEquipos = new JLabel("Equipos");
	    labelCrearEquipos.setBackground(new Color(199, 235, 252));
	    labelCrearEquipos.setHorizontalAlignment(SwingConstants.CENTER);
	    labelCrearEquipos.setOpaque(true); // ¡Esto es clave!
	    labelCrearEquipos.setBounds(42, 25, 134, 28);
	    panelMenuBar.add(labelCrearEquipos);
	    
	    JLabel labelCrearSalas = new JLabel("Salas");
	    labelCrearSalas.setBackground(new Color(199, 235, 252));
	    labelCrearSalas.setHorizontalAlignment(SwingConstants.CENTER);
	    labelCrearSalas.setBounds(431, 25, 169, 28);
	    labelCrearSalas.setOpaque(true); // ¡Esto es clave! 
	    panelMenuBar.add(labelCrearSalas);
	    
	    JLabel labelPrestamos = new JLabel("Prestamos");
	    labelPrestamos.setBackground(new Color(199, 235, 252));
	    labelPrestamos.setHorizontalAlignment(SwingConstants.CENTER);
	    labelPrestamos.setBounds(633, 25, 134, 28);
	    labelPrestamos.setOpaque(true); // ¡Esto es clave!
	    panelMenuBar.add(labelPrestamos);
	    
	    JLabel labelUsuarios = new JLabel("Usuarios");
	    labelUsuarios.setBackground(new Color(199, 235, 252));
	    labelUsuarios.setHorizontalAlignment(SwingConstants.CENTER);
	    labelUsuarios.setBounds(267, 25, 134, 28);
	    labelUsuarios.setOpaque(true); // ¡Esto es clave!
	    panelMenuBar.add(labelUsuarios);
	    
	   // Acciones de los label 
	    labelCrearSalas.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneCrearSalas");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelCrearSalas.setBackground(colorHover);
	    		labelCrearSalas.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelCrearSalas.setBackground(colorNormal);
	    	}
	    });
	    
	    labelPrestamos.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneVerPrestamos");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelPrestamos.setBackground(colorHover);
	    		labelPrestamos.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelPrestamos.setBackground(colorNormal);
	    	}
	    });
	    
	    
	    labelUsuarios.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneVerUsuarios");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelUsuarios.setBackground(colorHover);
	    		labelUsuarios.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelUsuarios.setBackground(colorNormal);
	    	}
	    });
	    
	    labelCrearEquipos.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		cardLayout.show(panelContenedor, "namePaneCrearEquipos");
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {
	    		labelCrearEquipos.setBackground(colorHover);
	    		labelCrearEquipos.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    	}
	    	@Override
	    	public void mouseExited(MouseEvent e) {
	    		labelCrearEquipos.setBackground(colorNormal);
	    	}
	    });
	}
}
