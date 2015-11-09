package uk.sliske.util.flowchart.nodes;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.graphics.rendering.software.Line;
import uk.sliske.graphics.rendering.software.Offset;
import uk.sliske.util.flowchart.graphics.NodeGraphics;

public class Node {

	private static final int		Y_OFFSET	= 60;

	protected HashMap<String, Node>	children;

	protected String				text;
	protected TYPE					type;
	protected NodeGraphics			graphics;
	private boolean					yes;
	protected Node					parent;
	public final String key;

	public Node(final String key, final String text, final TYPE type, final Node parent, boolean yes) {
		this.text = text;
		this.children = new HashMap<String, Node>();
		this.type = type;
		this.yes = yes;
		this.parent = parent;
		this.key = key;

		Offset pos = new Offset(590, Y_OFFSET);
		if (parent != null) {
			parent.children.put(Boolean.toString(yes), this);
			switch (parent.children.size()) {
				case 1:
					pos = parent.graphics.getPos().offset(0, Y_OFFSET);
					break;
				case 2:
					for (Node n : parent.children.values()) {
						if (n.yes) {
							pos = parent.graphics.getPos().offset(75, Y_OFFSET);
						} else {
							pos = parent.graphics.getPos().offset(-75, Y_OFFSET);
						}
						if (n != this) {
							n.graphics.getPos().set(pos);
						}
					}
					break;
			}
		}

		graphics = new NodeGraphics(this, pos);
	}

	public void addChild(String key, Node child) {
		children.put(key, child);
	}

	public void setParent(String key, Node parent) {
		parent.addChild(key, this);
		this.parent = parent;
	}

	public Node getParent(){
		return parent;
	}
	
	public boolean yes(){
		return yes;
	}
	
	public void move(int xAmt, int yAmt, boolean moveChildren) {
		graphics.getPos().set(graphics.getPos().offset(xAmt, yAmt));
		if (moveChildren) {
			for (Node n : children.values()) {
				n.move(xAmt, yAmt, moveChildren);
			}
		}
	}

	public void render(GraphicsMGR mgr) {
		graphics.render(mgr);

		for (Entry<String, Node> e : children.entrySet()) {
			Color c = e.getKey().toString() == "true" ? Color.GREEN : Color.RED;
			Node n = e.getValue();
			n.render(mgr);
			mgr.drawRenderable(new Line(graphics.getCentre(), n.graphics.getCentre(), c));
		}
	}

	public enum TYPE {
		CONDITION,
		ACTION,
		TITLE,
		DEFAULT
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

}
