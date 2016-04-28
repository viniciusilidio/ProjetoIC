

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Catalano.Core.IntRange;
import Catalano.Imaging.Color;
import Catalano.Imaging.FastBitmap;

public class Calibragem {
	
	private static Point[] pressed = new Point[2];
	private static int count = 0;
	public static float pixelsPerCm;
	
	public static void calibragemCor (int intervalo, FastBitmap file) { 
		
		FastBitmap fb = file;
		
		JFrame frame = new JFrame("Calibragem Cores");
		frame.setBounds(0, 0, fb.getWidth(), fb.getHeight());
		
		JPanel panel = new JPanel();
		JLabel label = new JLabel(fb.toIcon());
				
		panel.addMouseListener(new MouseAdapter() {
			
			@Override
		     public void mousePressed(MouseEvent e) {
		        int[] posTemp = new int[2];
		        
		        posTemp[0] = e.getY();
		        posTemp[1] = e.getX();
		      
		        
		        addColorToIndex(posTemp, intervalo, fb);
		     }
		});
		
		panel.add(label);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	
	private static void addColorToIndex(int[] pos, int intervalo, FastBitmap fb) {
		
		IntRange[] color = new IntRange[4];
		
		int[] rgb = fb.getRGB(pos[0], pos[1]);
										
		if ((rgb[0] - intervalo) >= 0) { 
			if (rgb[0] + intervalo <= 255) {
				color[0] = new IntRange(rgb[0] - intervalo, rgb[0] + intervalo);
			} else {
				color[0] = new IntRange(rgb[0] - 2 * intervalo, 255);
			}
		} else {
			color[0] = new IntRange(0, rgb[0] + 2 * intervalo);
		}
		
		if ((rgb[1] - intervalo) >= 0) { 
			if (rgb[1] + intervalo <= 255) {
				color[1] = new IntRange(rgb[1] - intervalo, rgb[1] + intervalo);
			} else {
				color[1] = new IntRange(rgb[1] - 2 * intervalo, 255);
			}
		} else {
			color[1] = new IntRange(0, rgb[1] + 2 * intervalo);
		}
		
		if ((rgb[2] - intervalo) >= 0) { 
			if (rgb[2] + intervalo <= 255) {
				color[2] = new IntRange(rgb[2] - intervalo, rgb[2] + intervalo);
			} else {
				color[2] = new IntRange(rgb[2] - 2 * intervalo, 255);
			}
		} else {
			color[2] = new IntRange(0, rgb[2] + 2 * intervalo);
		}
		
		color[3] = new IntRange(pos[0], pos[1]);
		
		Tela.logMessage((new Color(color[0].getMax(), color[1].getMax(), color[2].getMax())).toString());

		Protocolo.add(color);
	}
	
	public static void calibDist (FastBitmap bmp) {

        FastBitmap fb = bmp;
		
		JFrame frame = new JFrame("Calibragem Distância");
		frame.setBounds(0, 0, fb.getWidth(), fb.getHeight());
		
		JPanel panel = new JPanel();
		JLabel label = new JLabel(fb.toIcon());
				
		panel.addMouseListener(new MouseAdapter() {
			@Override
		     public void mousePressed(MouseEvent e) {
				pressed[count] = e.getPoint();
				count++;
				if (count == 2) {
					Tela.logMessage(pressed[0] + " " + pressed[1]);
					if (pressed[1].x > pressed[0].x) {
						float temp = Math.abs(pressed[1].x - pressed[0].x);
						pixelsPerCm = temp / Tela.distCalib;
						Tela.logMessage(String.valueOf(pixelsPerCm));
						Tela.pixelsPerCm = pixelsPerCm;
					} else {
						float temp = Math.abs(pressed[1].y - pressed[0].y);
						pixelsPerCm = temp / Tela.distCalib;
						Tela.logMessage(String.valueOf(pixelsPerCm));
						Tela.pixelsPerCm = pixelsPerCm;
					}
				}
		     }
		});
		
		panel.add(label);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
}