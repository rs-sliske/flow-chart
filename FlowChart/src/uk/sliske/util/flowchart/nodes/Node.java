package uk.sliske.util.flowchart.nodes;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;

import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.graphics.rendering.software.Line;
import uk.sliske.graphics.rendering.software.Offset;
import uk.sliske.util.flowchart.graphics.NodeGraphics;

public class Node {

	protected HashMap<String, Node>	children;

	protected String				text;
	protected TYPE					type;
	protected NodeGraphics			graphics;

	public Node(final String text, final TYPE type, final Node parent) {
		this.text = text;
		this.children = new HashMap<String, Node>();
		this.type = type;
		
		Offset pos = new Offset(590, 100);		
		if (parent != null) {
			parent.children.put(this.toString(), this);
			switch(parent.children.size()){
				case 1:
					pos = parent.graphics.getPos().offset(0, 100);
					break;
				case 2:
					for(Node n:parent.children.values()){
						if(n == this){
							pos = parent.graphics.getPos().offset(75, 100);
						} else {
							n.graphics.getPos().set(parent.graphics.getPos().offset(-75, 100));
						}
						
					}
					break;
			}
		}
		
		
		
		
		graphics = new NodeGraphics(this, pos);
	}

	public Node(final String text) {
		this(text, TYPE.DEFAULT, null);
	}
	
	public void addChild(String key, Node child){
		children.put(key, child);
	}
	
	public void setParent(String key, Node parent){
		parent.addChild(key, this);
	}
	
	public void move(int xAmt, int yAmt, boolean moveChildren){
		graphics.getPos().set(graphics.getPos().offset(xAmt, yAmt));
		if(moveChildren){
			for(Node n:children.values()){
				n.move(xAmt, yAmt, moveChildren);
			}
		}
	}

	public void render(GraphicsMGR mgr) {
		graphics.render(mgr);
		Color c = new Color(0x00);
		for (Node n : children.values()) {
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
