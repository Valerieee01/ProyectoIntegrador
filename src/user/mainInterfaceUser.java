package user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JList;

public class mainInterfaceUser extends JFrame {

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
					mainInterfaceUser frame = new mainInterfaceUser();
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
	public mainInterfaceUser() {
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
	    verPrestamosPane verPrestamosPane = new verPrestamosPane();
	    paneHistorialPrestamos panelHistorialPrestamos = new paneHistorialPrestamos();

	    // Crear panel de bienvenida
	    JPanel panelBienvenida = new JPanel();
	    panelBienvenida.setBackground(new Color(255, 255, 206));
	    JLabel bienvenidolabel = new JLabel("BIENVENIDO USUARIO");
	    bienvenidolabel.setHorizontalAlignment(SwingConstants.CENTER);
	    bienvenidolabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
	    panelBienvenida.add(bienvenidolabel);

	    // Añadirlos al contenedor con una clave
	    panelContenedor.add(panelBienvenida, "panelBienvenida");
	    panelContenedor.add(verPrestamosPane, "namePaneSalas");
	    panelContenedor.add(panelHistorialPrestamos, "namePaneHistorial");

	    // Mostrar el panel de bienvenida al inicio
	    cardLayout.show(panelContenedor, "panelBienvenida");

	    // Resto del código...
	    // barra de navegacion entre paneles
	    JPanel panelMenuBar = new JPanel();
	    panelMenuBar.setBackground(new Color(199, 235, 252));
	    panelMenuBar.setBounds(0, 0, 843, 64);
	    contentPane.add(panelMenuBar);
	    panelMenuBar.setLayout(null);
		
	    JLabel labelSolicitarPrestamos = new JLabel("SolicitarPrestamos");
	    labelSolicitarPrestamos.setBackground(new Color(199, 235, 252));
	    labelSolicitarPrestamos.setHorizontalAlignment(SwingConstants.CENTER);
	    labelSolicitarPrestamos.setBounds(562, 25, 134, 28);
	    labelSolicitarPrestamos.setOpaque(true); // ¡Esto es clave!
	    panelMenuBar.add(labelSolicitarPrestamos);
	    
		
		JLabel labelHistorialPrestamos = new JLabel("Historial Prestamos");
		labelHistorialPrestamos.setOpaque(true); // ¡Esto es clave!
		labelHistorialPrestamos.setBounds(122, 25, 134, 28);
		labelHistorialPrestamos.setBackground(new Color(199, 235, 252));
		labelHistorialPrestamos.setHorizontalAlignment(SwingConstants.CENTER);
		panelMenuBar.add(labelHistorialPrestamos);
		
		Color colorNormal = new Color(199, 235, 252); // Color inicial
		Color colorHover = new Color(220, 220, 220);  // Color al pasar el mouse
		
		

		// Acciones para label menu
		
		  labelSolicitarPrestamos.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
		    		cardLayout.show(panelContenedor, "namePaneSalas");
		    	}
		    	@Override
		    	public void mouseEntered(MouseEvent e) {
		    		labelSolicitarPrestamos.setBackground(colorHover);
		    		labelSolicitarPrestamos.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    	}
		    	@Override
		    	public void mouseExited(MouseEvent e) {
		    		labelSolicitarPrestamos.setBackground(colorNormal);
		    	}
		    });

		labelHistorialPrestamos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        cardLayout.show(panelContenedor, "namePaneHistorial");
		    }
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        labelHistorialPrestamos.setBackground(colorHover);
		        labelHistorialPrestamos.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        labelHistorialPrestamos.setBackground(colorNormal);
		    }
		});
	}
	

	public void cargarImagenEnLabel(JLabel label, String rutaInterna, int ancho, int alto) {
	    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaInterna));
	    Image imagenOriginal = iconoOriginal.getImage();
	    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	    ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
	    label.setIcon(iconoEscalado);
	}
}
