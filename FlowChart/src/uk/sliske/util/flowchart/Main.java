package uk.sliske.util.flowchart;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import uk.sliske.graphics.rendering.software.Frame;
import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.util.flowchart.nodes.Node;
import uk.sliske.util.flowchart.nodes.Node.TYPE;

public class Main implements KeyListener {

	private Node	root;
	private Frame	frame;
	private long	startTime;
	private long	timeOffset;
	private boolean paused = false;

	private Main(final String title) {
		root = new Node(title, Node.TYPE.TITLE, null, false);

		createDgStack();

		GraphicsMGR mgr = new GraphicsMGR(1280, 960);

		frame = new Frame("dg flow chart", mgr.getCanvas());
		mgr.getCanvas().addKeyListener(this);

		startTime = System.currentTimeMillis();
		timeOffset = 0;

		mgr.getContext().clear(0x00);

		while (loop(mgr) && frame.frame.isActive() && !paused) {

		}
	}

	private boolean loop(GraphicsMGR mgr) {
		if (!frame.frame.isVisible()) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		long delta = currentTime - startTime;

		update(mgr);
		if (delta > timeOffset) {
			render(mgr);
			timeOffset += 1000;
		}

		return frame.frame.isVisible();
	}

	private void update(GraphicsMGR mgr) {

	}

	private void render(GraphicsMGR mgr) {

		root.render(mgr);
		mgr.update();
	}

	public static void main(String[] args) {
		new Main("DG");
		
		//System.out.println(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				root.move(-1, 0, true);
				break;
			case KeyEvent.VK_RIGHT:
				root.move(1, 0, true);
				break;
			case KeyEvent.VK_SPACE:
				paused = paused ? false : true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void createDgStack() {
		Node inside = new Node("in a dungeon?", TYPE.CONDITION, root, true);
		Node waitingArea = new Node("in waiting area?", TYPE.CONDITION, inside, false);
		new Node("do floor", Node.TYPE.ACTION, inside, true);
		Node inParty = new Node("in party?", TYPE.CONDITION, waitingArea, true);
		Node teamFull = new Node("team full?", TYPE.CONDITION, inParty, true);
		Node leader = new Node("leader?", TYPE.CONDITION, teamFull, true);
		Node floorSet = new Node("floor set?", TYPE.CONDITION, leader, true);
		Node difficultySet = new Node("difficulty set?", TYPE.CONDITION, floorSet, true);
		Node startFloor = new Node("start floor", TYPE.ACTION, difficultySet, true);
		Node soloing = new Node("soloing?", TYPE.CONDITION, inParty, false);
		Node createTeam = new Node("create team", TYPE.ACTION, soloing, true);
		leader.move(150, 0, true);
		//teamFull.move(100, 0, true);
		
		Node waitForTeam = new Node("wait", TYPE.ACTION, leader, false);
		Node setFloor = new Node("set floor", TYPE.ACTION, floorSet, false);
		Node setDifficulty = new Node("set difficulty", TYPE.ACTION, difficultySet, false);
		
		

	}

}
