package pt.iscte.poo.sokoban;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.utils.Point2D;

public class Level {
	private HashMap<Point2D, ArrayList<GameElement>> elementMap = new HashMap<>(100);
	private int level = 0;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
	private ArrayList<Alvo> targets = new ArrayList<>(2);
	private ArrayList<Teleporte> teleportes;
	private final static int[] LEVEL_SCORES = new int[] { 20, 50, 80, 80, 45, 60, 20 };
	private Empilhadora bobcat;
	private int boxCount, moves;

	public Level(int level) {
		this.level = level;
		for (int x = 0; x < GameEngine.GRID_WIDTH; x++)
			for (int y = 0; y < GameEngine.GRID_HEIGHT; y++)
				elementMap.put(new Point2D(x, y), new ArrayList<>(2));
		readLevelData(level);
	}

	private void readLevelData(int level) {
		File f = new File("levels/level" + level + ".txt");
		try {
			Scanner s = new Scanner(f);
			gui.clearImages();
			int y = 0;
			while (s.hasNext()) {
				String str = s.nextLine();
				System.out.println(str);
				for (int x = 0; x < GameEngine.GRID_WIDTH; x++) {
					char c = str.charAt(x);
					System.out.println(c);
					Point2D point = new Point2D(x, y);
					System.out.println(point);
					GameElement newElement = GameElement.create(c, point);
					add(newElement);
					// If it is layer 2 it will need a background
					if (newElement.getLayer() > 0)
						add(new Chao(point));
					if (newElement instanceof Empilhadora)
						bobcat = (Empilhadora) newElement;
					else if (newElement instanceof Alvo)
						targets.add((Alvo) newElement);
					else if (newElement instanceof Caixote)
						boxCount++;
					else if (newElement instanceof Teleporte) {
						if (teleportes == null)
							teleportes = new ArrayList<>(2);
						teleportes.add((Teleporte) newElement);
					}
				}
				y++;
			}
			System.out.println("Fim");
			s.close();
			if (teleportes != null && teleportes.size() != 2)
				throw new IllegalArgumentException("There must be exactly 2 teleporters");
			gui.update();
		} catch (FileNotFoundException error) {
			System.out.println("Erro");
			throw new IllegalArgumentException("Couldn't find the file for level " + level);
		}
	}

	public void add(GameElement e) {
		gui.addImage(e);
		elementMap.get(e.getPosition()).add(e);
	}

	public int getLevel() {
		return level;
	}

	public Empilhadora getBobcat() {
		return bobcat;
	}

	public HashMap<Point2D, ArrayList<GameElement>> getElementMap() {
		return elementMap;
	}

	public ArrayList<Alvo> getTargets() {
		return targets;
	}

	public ArrayList<Teleporte> getTeleportes() {
		return teleportes;
	}

	public int getBoxCount() {
		return boxCount;
	}

	public void destroyBox() {
		boxCount--;
	}

	public void moves() {
		moves++;
	}

	public boolean checkEnd() {
		return targets.stream().allMatch(t -> t.isCovered());
	}

	public int computeScore() {
		return getBobcat().getEnergy() * 2 - (moves - LEVEL_SCORES[level]) * 2;
	}
}
