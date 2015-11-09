package uk.sliske.util.flowchart.graphics;

import java.awt.Color;
import java.awt.Point;

import uk.sliske.graphics.rendering.software.Bitmap;
import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.graphics.rendering.software.Offset;
import uk.sliske.graphics.rendering.software.StringGraphics;
import uk.sliske.util.flowchart.nodes.Node;

public class NodeGraphics extends Bitmap {

	private final Node	node;

	private int			backgroundColor, textColor;
	private Offset		position;
	private Color		c;
	private static int	width	= 100;
	private static int	height	= 50;

	public NodeGraphics(Node node, Offset pos) {
		super(width, height);
		this.node = node;
		position = pos;
		setNodeColor(this);

		c = new Color(textColor);

		clear(backgroundColor);

	}
	
	public Offset getPos(){
		return position;
	}

	public Point getCentre() {
		int x = position.x + (width / 2);
		int y = position.y + (height / 2);
		return new Point(x, y);
	}

	private static void setNodeColor(NodeGraphics node) {
		switch (node.node.getType()) {
			case TITLE:
				node.backgroundColor = 0x00;
				node.textColor = 0xffffff;
				break;
			default:
				node.backgroundColor = 0x00;
				node.textColor = 0xffffff;
				break;

		}
	}

	public void render(GraphicsMGR mgr) {
		mgr.getContext().drawBitmap(position.x, position.y, this);
		mgr.drawRenderable(new StringGraphics(node.getText(), c, position.offset(10, 20)));
	}

}
