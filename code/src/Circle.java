
import java.awt.Color;

public class Circle {
	
	private int x, y;
	public final static int radius = 50;
	private Color color;
	
	public Circle (int x, int y, Color color) {
		this.x = x - (radius / 2);
		this.y = y - (radius / 2);
		this.color = color;
	}

	public int getX() {
		return x + (radius / 2);
	}

	public int getY() {
		return y + (radius / 2);
	}

	public int getRadius() {
		return radius;
	}

	public Color getColor() {
		return color;
	}
}