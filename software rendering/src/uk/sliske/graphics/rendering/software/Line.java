package uk.sliske.graphics.rendering.software;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

public class Line extends Renderable{

	public final Point start,end;
	private final Stroke stroke;

	
	public Line(Point start, Point end, Color c){
		super(c);
		this.start = start;
		this.end=end;
		stroke = new BasicStroke(3);

	}


	@Override
	protected void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		g2.drawLine(start.x, start.y, end.x, end.y);
	}
}
