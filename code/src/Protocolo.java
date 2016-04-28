
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Catalano.Core.IntRange;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Concurrent.Filters.ColorFiltering;
import Catalano.Imaging.Concurrent.Filters.Grayscale;
import Catalano.Imaging.Concurrent.Filters.Threshold;
import Catalano.Imaging.Filters.BlobsFiltering;
import Catalano.Imaging.Tools.Blob;
import Catalano.Imaging.Tools.BlobDetection;

public class Protocolo {
	
	private static List<IntRange[]> colors = new ArrayList<IntRange[]>();
	
	public static void add(IntRange[] color) {
		colors.add(color);
	}

	public static String protocolo(FastBitmap bmp) {
		
		List<Blob> blobs = detectBlobs(bmp);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.valueOf(bmp.getWidth()) + ":" + String.valueOf(bmp.getHeight()) + "/");
		
		for (int i = 0; i < blobs.size(); i++) {
			String R = String.valueOf(colors.get(i)[0].getMax());
			String G = String.valueOf(colors.get(i)[1].getMax());
			String B = String.valueOf(colors.get(i)[2].getMax());
			String[] POS = { String.valueOf(blobs.get(i).getCenter().x), String.valueOf(blobs.get(i).getCenter().y) };
			
			if (i != blobs.size() - 1) {
				String partialProtocol = R + "," + G + "," + B + "," + POS[0] + ":" + POS[1] + ";";
				builder.append(partialProtocol);
			} else {
				String partialProtocol = R + "," + G + "," + B + "," + POS[0] + ":" + POS[1];
				builder.append(partialProtocol);
			}
		}
		
		String protocolo = builder.toString();
				
		return protocolo;
	}
	
	private static List<Blob> detectBlobs (FastBitmap bmp) {
		
		List<Blob> blobs = new ArrayList<Blob>();
		
		FastBitmap bp = new FastBitmap(bmp);		
		
		for (IntRange[] i : colors) {
			
			FastBitmap fb = new FastBitmap(bmp);
			//JOptionPane.showMessageDialog(null, fb.toIcon());

			ColorFiltering cf = new ColorFiltering(i[0], i[1], i[2]);
			cf.applyInPlace(fb);
			//JOptionPane.showMessageDialog(null, fb.toIcon());

			Grayscale gs = new Grayscale(); 
			gs.applyInPlace(fb); 
			// JOptionPane.showMessageDialog(null, fb.toIcon()); 
			
			Threshold t = new Threshold(50); 
			t.applyInPlace(fb);
			//JOptionPane.showMessageDialog(null, fb.toIcon());
		
			BlobsFiltering bf = new BlobsFiltering(1, 20); 
			bf.applyInPlace(fb);
			// JOptionPane.showMessageDialog(null, fb.toIcon());

			BlobDetection bd = new BlobDetection(); 
			
			if (bd.ProcessImage(fb).size() != 0)
				blobs.add((bd.ProcessImage(fb)).get(0));
		}
		
		return blobs;
	}
}