package uk.sliske.graphics.rendering.software;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends Renderable{

	public final Point start,end;

	
	public Line(Point start, Point end, Color c){
		super(c);
		this.start = start;
		this.end=end;

	}


	@Override
	protected void draw(Graphics g) {
		g.drawLine(start.x, start.y, end.x, end.y);
	}
}
