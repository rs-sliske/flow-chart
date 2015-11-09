package uk.sliske.util.flowchart;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import uk.sliske.graphics.rendering.software.Frame;
import uk.sliske.graphics.rendering.software.GraphicsMGR;
import uk.sliske.util.Constants;
import uk.sliske.util.flowchart.nodes.Node;
import uk.sliske.util.flowchart.nodes.Node.TYPE;
import uk.sliske.util.io.FileSaver;

public class Main implements KeyListener {

	private Node					root;
	private Frame					frame;
	private long					startTime;
	private long					timeOffset;
	private boolean					paused	= false;
	private HashMap<String, Node>	nodes;

	private Main(final String title) {
		nodes = new HashMap<>();
		root = new Node("root", title, Node.TYPE.TITLE, null, false);
		nodes.put("root", root);
		String filename = title + " - plans.txt";

		load(filename);
		
		if(nodes.size() <= 1){
			createDgStack();
		}
	

		GraphicsMGR mgr = new GraphicsMGR(1280, 960);

		frame = new Frame(title+" - flow chart", mgr.getCanvas());
		mgr.getCanvas().addKeyListener(this);

		startTime = System.currentTimeMillis();
		timeOffset = 0;

		mgr.getContext().clear(0x00);

		while (loop(mgr) && frame.frame.isActive() && !paused) {
			if (!frame.frame.isVisible()) {
				break;
			}
		}
		frame.frame.dispose();

		save(filename);
	}

	private void load(String filename) {
		String path = Constants.USER_PATH + "//script plans//" + filename;

		File file = new File(path);

		Scanner scanner;
		try {
			scanner = new Scanner(file);

			while (scanner.hasNextLine()) {

				String[] tokens = scanner.nextLine().split("  ");
				if (tokens[0] == "-n" && tokens.length == 6) {
					TYPE type;
					switch (tokens[3].toLowerCase()) {
						case "action":
							type = TYPE.ACTION;
							break;
						case "condition":
							type = TYPE.CONDITION;
							break;
						case "title":
							type = TYPE.TITLE;
							break;
						default:
							type = TYPE.DEFAULT;
							break;
					}
					boolean b = tokens[5].equalsIgnoreCase("true") ? true : false;
					createNode(tokens[1], tokens[2], type, tokens[4], b);

				}

			}
			scanner.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void save(String filename) {
		FileSaver file = new FileSaver(filename, "script plans\\");
		for (Entry<String, Node> e : nodes.entrySet()) {
			Node n = e.getValue();
			file.appendf("-n  %s  %s  %s  %s  %s\n", e.getKey(), n.getText(), n.getType().name(), n
					.getParent() == null ? "null" : n.getParent().key, n.yes());
		}
		file.close();
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

		// System.out.println(true);
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

	private void createNode(final String key, final String text, final TYPE type, final String parent, final boolean condition) {
		Node parentNode = nodes.containsKey(parent) ? nodes.get(parent) : null;
		nodes.put(key, new Node(key, text, type, parentNode, condition));
	}

	private void createDgStack() {
		createNode("inside", "in a dungeon?", TYPE.CONDITION, "root", true);
		createNode("waitingArea", "in waiting area?", TYPE.CONDITION, "inside", false);
		createNode("do floor", "do floor", TYPE.ACTION, "inside", true);
		createNode("inParty", "in party?", TYPE.CONDITION, "waitingArea", true);
		createNode("teamFull", "team full?", TYPE.CONDITION, "inParty", true);
		createNode("leader", "leader?", TYPE.CONDITION, "teamFull", true);
		createNode("floorSet", "floor set?", TYPE.CONDITION, "leader", true);
		createNode("difficultySet", "difficulty set?", TYPE.CONDITION, "floorSet", true);
		createNode("startFloor", "start floor", TYPE.ACTION, "difficultySet", true);
		createNode("soloing", "soloing?", TYPE.CONDITION, "inParty", false);
		createNode("createTeam", "create team", TYPE.ACTION, "soloing", true);
		nodes.get("leader").move(150, 0, true);
		createNode("waitForTeam", "wait", TYPE.ACTION, "leader", false);
		createNode("setFloor", "set floor", TYPE.ACTION, "floorSet", false);
		createNode("setDifficulty", "set difficulty", TYPE.ACTION, "difficultySet", false);

	}

}
