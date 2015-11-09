package uk.sliske.graphics.rendering.software;

import java.awt.Graphics;
import java.util.ArrayList;

public class GraphicsMGR {
	private static GraphicsMGR	current;

	public static GraphicsMGR getCurrent() {
		if (current == null) {
			// TODO: handle if current has not been set
		}
		return current;
	}

	private Display					display;
	private final RenderContext		target;
	private ArrayList<Renderable>	renderables;
	private ArrayList<Renderable>	strings;

	public Display getCanvas() {
		return display;
	}

	public Bitmap getContext() {
		return target;
	}

	public GraphicsMGR(int width, int height) {
		renderables = new ArrayList<>();
		strings = new ArrayList<>();
		display = new Display(width, height);
		target = display.getFrameBuffer();

		current = this;
	}

	public boolean drawRenderable(final Renderable renderable) {
		if(renderable instanceof StringGraphics){
			return strings.add(renderable);

		}
		return renderables.add(renderable);
	}

	public void update() {
		display.swapBuffers();
		Graphics g = display.getGraphics();

		for (Renderable r : renderables) {
			r.render(g);
		}
		for (Renderable s : strings) {
			s.render(g);
		}

		target.clear(0xff);
		renderables.clear();
		strings.clear();
	}

}