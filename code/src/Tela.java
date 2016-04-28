
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Catalano.Imaging.FastBitmap;

public class Tela {

	
	private JFrame frame;
	private JPanel visualization;
	private JPanel options;
	private JPanel log;
	private FastBitmap arquivoCalib;
	
	private static JTextArea logText;
	
	private final static int WIDTH = 600;
	private final static int HEIGHT = 600;
	private final static int COLORRANGE = 0;
	public final static float distCalib = 8.4f;
	
	private static List<Circle> circles = new ArrayList<Circle>();
	public static float pixelsPerCm;
	
	public Tela () {
		loadCalibImage();
		
		Calibragem.calibDist(arquivoCalib);
		
		frame = new JFrame("Servidor");
		setFrame();
		setVisualization();
		setOptions();
		setLog();
		
		frame.setVisible(true);
	}
	
	private void loadCalibImage () {
		JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            arquivoCalib = new FastBitmap(file.getPath());   
        }
	}
	
	private void setOptions () {
		options = new JPanel();
		options.setBorder(BorderFactory.createBevelBorder(1));
		options.setBounds(520, 10, 70, 500);
		options.setLayout(new GridLayout(2, 1));
		
		JButton buttonCalib = new JButton("<html>C<br>a<br>l<br>i<br>b<br>r<br>a<br>r</html>");
		buttonCalib.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Calibragem.calibragemCor(COLORRANGE, arquivoCalib);
			}
		});
		
		JButton buttonInit = new JButton("<html>I<br>n<br>i<br>t</html>");
		buttonInit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String protocol = Protocolo.protocolo(arquivoCalib);
				System.out.println(protocol);
				updateCircles(protocol);
				drawCirles();
			}
		});
		
		options.add(buttonCalib);
		options.add(buttonInit);
		frame.add(options);
	}
	
	private void setLog () {
		log = new JPanel();
		log.setBounds(10, 520, 580, 70);
		log.setBackground(Color.LIGHT_GRAY);
		log.setBorder(BorderFactory.createEtchedBorder());
		
		logText = new JTextArea();
		logText.setEditable(false);
		
		log.add(logText);
		frame.add(log);
	}
	
	private void setVisualization () {
		visualization = new JPanel(null);
		visualization.setBounds(10, 10, 500, 500);
		visualization.setBorder(BorderFactory.createLoweredBevelBorder());
		frame.add(visualization);
	}
	
	private void setFrame() {
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
	}
	
	public static void logMessage (String message) {
		logText.setText(message);
	}
	
	private void updateCircles (String protocolo) {
		
		circles.clear();
		
		String[] initialSplit = protocolo.split("/");
		String[] size = initialSplit[0].split(":");
		
		int width = Integer.valueOf(size[0]);
		int height = Integer.valueOf(size[1]);
		
		String[] protocoloSplitted = initialSplit[1].split(";");
		
		Tela.logMessage(String.valueOf(protocoloSplitted.length));
		
		for (String str : protocoloSplitted) {
			String[] temp = str.split(",");
			String[] posTemp = temp[3].split(":");
			
			int x = map(Integer.valueOf(posTemp[0]), 0, width, 0, 500);
			int y = map(Integer.valueOf(posTemp[1]), 0, height, 0, 500);
						
			int[] pos = {y, x};
			
			int[] rgb = {Integer.valueOf(temp[0]), Integer.valueOf(temp[1]), Integer.valueOf(temp[2])};
			
			Color cor = new Color(rgb[0], rgb[1], rgb[2]);
			
			circles.add(new Circle(pos[0], pos[1], cor));
		}
	}
	
	private int map(int x, int in_min, int in_max, int out_min, int out_max)
	{
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
		}
	
	private void drawCirles() {
		visualization.removeAll();
		Graphics2D g2 = (Graphics2D) visualization.getGraphics();
		
		for (Circle circle : circles) {
			g2.setColor(circle.getColor());
			g2.setPaint(circle.getColor());
			g2.fillOval(circle.getX(), circle.getY(), circle.getRadius(), circle.getRadius());
			g2.setColor(Color.darkGray);
			g2.drawLine(WIDTH/2, HEIGHT/2, circle.getX() + (circle.getRadius() / 2), circle.getY() + (circle.getRadius() / 2));
			
			int differenceX = circle.getX() - WIDTH/2;
			int differenceY = circle.getY() - HEIGHT/2;
			String dist = String.format("%.2f", calcDistance(differenceX, differenceY));
			g2.drawString(dist, (WIDTH / 2) + (differenceX / 2),
					(HEIGHT/ 2) + (differenceY / 2));
		}
	}
	
	private float calcDistance(int differenceX, int differenceY) {
		double distPixels = 0.0f;
		
		int hipSquare = (int)(Math.pow(differenceX, 2) + Math.pow(differenceY, 2));
		
		distPixels = Math.sqrt(hipSquare);
				
		float distCm = (float)distPixels / pixelsPerCm;
		
		System.out.printf("%.2f\n", distCm);
		
		return distCm;
	}
	
	public static void main (String [] args) {
		new Tela();
	}
}