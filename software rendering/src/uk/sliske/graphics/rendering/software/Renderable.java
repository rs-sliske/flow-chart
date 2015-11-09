package uk.sliske.graphics.rendering.software;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Renderable {

	protected final Color color;
	
	protected Renderable(Color c){
		this.color = c;
	}
	
	protected abstract void draw(Graphics g);
	
	public final void render(Graphics g){
		if(g == null){
			return;
		}
		g.setColor(color);
		draw(g);
	}
	
}
