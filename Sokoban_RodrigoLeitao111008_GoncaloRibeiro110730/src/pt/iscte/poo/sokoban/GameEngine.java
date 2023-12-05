package pt.iscte.poo.sokoban;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer {
	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	public static final String STATS_FILE = "stats.txt";
	private static GameEngine INSTANCE;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();;
	private String username;
	private Level level;
	private int moves, restarts, score;

	private GameEngine() {}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	// Inicio
	public void start() {
		// Setup inicial da janela que faz a interface com o utilizador algumas coisas poderiam ser feitas no main, mas
		// estes passos tem sempre que ser feitos!
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI
		while (username == null || username.equals("")) {
			username = gui.askUser("Insira o seu nome");
		}
		// Criar o cenario de jogo
		level = new Level(0);
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Level: " + level.getLevel() + " - Player: " + username + " - Moves: " + moves
				+ " - Energy: " + level.getBobcat().getEnergy());
		gui.update();
		if (hasEasterEgg())
			gui.setMessage("SIUUUU");
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado (neste caso a GUI)
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed(); // obtem o codigo da tecla pressionada
		try {
			Empilhadora bobcat = level.getBobcat();
			if (key == KeyEvent.VK_SPACE) {
				restart();
			} else {
				if (key == 17) {
					if (bobcat.easterEgg())
						gui.setMessage("Uma entidade passou por ti e deixou cair 10 baterias, aproveita!");
					else
						gui.setMessage("Ouves o vento a soprar ao fundo mas nada acontece...");
				} else if (bobcat.move(Direction.directionFor(key))) {
					level.countMove();
				}
				gui.update();
			}
			updateStatusBar();
			if (bobcat.getEnergy() <= 0)
				restart("Ficou sem energia!");
			else if (level.getBoxCount() < level.getTargets().size())
				restart("Ficou sem caixotes suficientes!");
			checkEnd();
		} catch (IllegalArgumentException error) {
			System.err.println("Tecla desconhecida");
		}
	}

	public void updateStatusBar() {
		gui.setStatusMessage("Level: " + level.getLevel() + " - Player: " + username + " - Moves: "
				+ (moves + level.getMoves()) + " - Energy: " + level.getBobcat().getEnergy());
	}

	public boolean isWithinBounds(Point2D point) {
		return gui.isWithinBounds(point);
	}

	public void remove(GameElement e) {
		gui.removeImage(e);
		level.getElementMap().get(e.getPosition()).remove(e);
	}

	public void updatePosition(GameElement e, Point2D newPosition) {
		ArrayList<GameElement> oldList = getElementsIn(e.getPosition());
		ArrayList<GameElement> newList = getElementsIn(newPosition);
		oldList.remove(e);
		newList.add(e);
		if (e instanceof Movable) {
			Interactable oldInteractable = (Interactable) oldList.stream()
					.filter(em -> (em instanceof Interactable && !(em instanceof Buraco))).findAny().orElse(null);
			if (oldInteractable != null) {
				oldInteractable.setCovered(false);
				if (e instanceof Caixote)
					((Caixote) e).setOnTarget(false);
			}
		}
	}

	public ArrayList<GameElement> getElementsIn(Point2D position) {
		return level.getElementMap().get(position);
	}

	public Teleporte getTeleportPair(Teleporte tp) {
		// We intentionally want to use the inequality operator instead of .equals since the intances are equal
		// We also use findAny instead of findFirst since this is more performant and there's only 1 element anyway
		return level.getTeleportes().stream().filter(t -> t != tp).findAny().get();
	}

	public ArrayList<Alvo> getTargets() {
		return level.getTargets();
	}

	public void destroyBox() {
		level.destroyBox();
	}

	public boolean hasEasterEgg() {
		// hmmm I wonder what this could be
		return username.equalsIgnoreCase(new String(Base64.getDecoder().decode("cm9uYWxkbw==")));
	}

	/**
	 * Returns the first special element in the given position (an element with special interactions)
	 * 
	 * @param p The position to check
	 * @return An instance of a class that extends GameElement and has extra methods
	 */
	public GameElement getSpecialIn(Point2D p) {
		ArrayList<GameElement> elements = getElementsIn(p);
		Collections.reverse(elements);
		elements.sort((a, b) -> b.getLayer() - a.getLayer());
		for (GameElement e : elements)
			if (e.isSpecial())
				return e;
		return null;
	}

	public boolean checkEnd() {
		if (!level.checkEnd())
			return false;
		int oldLevel = level.getLevel();
		int levelScore = level.computeScore(restarts);
		// We only keep the restarts in the GameEngine because the level class may be recreated
		restarts = 0;
		score += levelScore;
		moves += level.getMoves();
		gui.setMessage("Nível " + oldLevel + " concluído!\nPontuação: " + levelScore);
		try {
			level = new Level(oldLevel + 1);
			updateStatusBar();
		} catch (IllegalArgumentException e) {
			// if creating the level errors it means there are no more levels, thus the game is over
			int position = updateLeaderboard();
			score += restarts * 50;
			gui.setMessage((position == -1 ? "Terminaste o jogo! Infelizmente não ficaste no top 3"
					: "Parabéns! Ficaste em " + (position + 1) + "º lugar!") + "\nPontuação total: " + score);
			gui.clearImages();
			System.exit(0);
		}
		return true;
	}

	private int updateLeaderboard() {
		ArrayList<String[]> leaderboard = readLearderboard();
		String[] newEntry = { username, String.valueOf(score + restarts * 50) };
		leaderboard.add(newEntry);
		leaderboard.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
		if (leaderboard.size() > 3)
			leaderboard.remove(3);
		writeLeaderboard(leaderboard);
		return leaderboard.indexOf(newEntry);
	}

	private ArrayList<String[]> readLearderboard() {
		ArrayList<String[]> leaderboard = new ArrayList<>(5);
		File f = new File(STATS_FILE);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine())
				leaderboard.add(s.nextLine().split(" - "));
			s.close();
		} catch (FileNotFoundException err) {
			// We should never reach this point because we create the file if it doesn't exist
		}
		return leaderboard;
	}

	private void writeLeaderboard(ArrayList<String[]> leaderboard) {
		File f = new File(STATS_FILE);
		try {
			PrintWriter pw = new PrintWriter(f);
			for (String[] entry : leaderboard)
				pw.println(String.join(" - ", entry));
			pw.close();
		} catch (FileNotFoundException e) {
		}
	}

	public void restart(String s) {
		gui.update();
		gui.setMessage(s);
		restart();
	}

	public void restart() {
		restarts++;
		level = new Level(level.getLevel());
		updateStatusBar();
	}
}
