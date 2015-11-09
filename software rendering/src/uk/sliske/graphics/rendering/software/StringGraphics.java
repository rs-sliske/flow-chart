package uk.sliske.graphics.rendering.software;

import java.awt.Color;
import java.awt.Graphics;

public class StringGraphics extends Renderable {

	public final String	text;
	public final Offset	position;

	public StringGraphics(String text, Color color, Offset position) {
		super(color);
		this.text = text;
		this.position = position;
	}

	@Override
	protected void draw(Graphics g) {
		g.drawString(text, position.x, position.y);
	}

}
