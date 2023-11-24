package pt.iscte.poo.sokoban;

import java.util.ArrayList;
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

// Note que esta classe e' um exemplo - nao pretende ser o inicio do projeto, 
// embora tambem possa ser usada para isso.
//
// No seu projeto e' suposto haver metodos diferentes.
// 
// As coisas que comuns com o projeto, e que se pretendem ilustrar aqui, sao:
// - GameEngine implementa Observer - para  ter o metodo update(...)  
// - Configurar a janela do interface grafico (GUI):
//        + definir as dimensoes
//        + registar o objeto GameEngine ativo como observador da GUI
//        + lancar a GUI
// - O metodo update(...) e' invocado automaticamente sempre que se carrega numa tecla
//
// Tudo o mais podera' ser diferente!

public class GameEngine implements Observer {

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	public static final String STATS_FILE = "stats.txt";

	private static GameEngine INSTANCE;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();;
	private String username;
	private Level level;
	private int moves, restarts;

	private GameEngine() {
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	// Inicio
	public void start() {

		// Setup inicial da janela que faz a interface com o utilizador
		// algumas coisas poderiam ser feitas no main, mas estes passos tem sempre que
		// ser feitos!

		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI

		while (username == null) {
			username = gui.askUser("Insira o seu nome");
		}

		// Criar o cenario de jogo
		level = new Level(0);

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(
				"Level: " + level.getLevel() + " - Player: " + username + " - Moves: " + moves + " - Energy: "
						+ level.getBobcat().getEnergy());
		gui.update();
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega
	// numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado
	// (neste caso a GUI)
	@Override
	public void update(Observed source) {

		int key = gui.keyPressed(); // obtem o codigo da tecla pressionada

		try {
			Empilhadora bobcat = level.getBobcat();
			if (key == KeyEvent.VK_SPACE) {
				restart();
			} else {
				if (bobcat.move(Direction.directionFor(key)))
					moves++;
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
		gui.setStatusMessage(
				"Level: " + level.getLevel() + " - Player: " + username + " - Moves: " + moves + " - Energy: "
						+ level.getBobcat().getEnergy());
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
					.filter(em -> (em instanceof Interactable && !(em instanceof Buraco)))
					.findFirst()
					.orElse(null);
			Interactable newInteractable = (Interactable) newList.stream()
					.filter(em -> (em instanceof Interactable && !(em instanceof Buraco)))
					.findFirst()
					.orElse(null);

			if (oldInteractable != null) {
				if (oldInteractable instanceof Alvo && e instanceof Caixote) {
					oldInteractable.setCovered(false);
					((Caixote) e).setOnTarget(false);
				} else if (!(oldInteractable instanceof Alvo))
					oldInteractable.setCovered(false);
			}
			if (newInteractable != null) {
				if (newInteractable instanceof Alvo && e instanceof Caixote) {
					newInteractable.setCovered(true);
					((Caixote) e).setOnTarget(true);
				} else if (!(newInteractable instanceof Alvo))
					newInteractable.setCovered(true);
			}

		}
	}

	public ArrayList<GameElement> getElementsIn(Point2D position) {
		return level.getElementMap().get(position);
	}

	public Teleporte getTeleportPair(Teleporte tp) {
		return level.getTeleportes().stream().filter(t -> t != tp).findFirst().get();
	}

	/**
	 * Returns the first special element in the given position (an element with
	 * special interactions)
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
		gui.setMessage("Nível " + oldLevel + " concluído!");
		try {
			level = new Level(oldLevel + 1);
			updateStatusBar();
		} catch (IllegalArgumentException e) {
			// if creating the level errors it means there are no more levels, thus we won
			int score = computeScore();
			int position = updateLeaderboard(score);
			gui.setMessage((position == -1 ? "Terminaste o jogo! Infelizmente não ficaste no top 3"
					: "Parabéns! Ficaste em " + (position + 1) + "º lugar!") + "\nScore: " + score);
			gui.clearImages();
			System.exit(0);
		}
		return true;
	}

	private int computeScore() {
		// Mean amount of moves to complete the game. A lower amount is possible (293)
		// getting much more than this will lead to a negative score
		int moveThreshold = 350;
		return level.getBobcat().getEnergy() * 5 - (moves - moveThreshold) * 2 - restarts * 50;
	}

	private int updateLeaderboard(int score) {
		ArrayList<String[]> leaderboard = readLearderboard();
		String[] newEntry = { username, String.valueOf(score) };
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
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine())
				leaderboard.add(s.nextLine().split(" - "));

			s.close();
		} catch (FileNotFoundException err) {
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
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
			// TODO: handle exception
		}
	}

	public void restart(String s) {
		gui.update();
		gui.setMessage(s);
		// TODO fix message rendering blank when losing
		// This causes the game to go unplayable and requires a restart
		// doesn't happen if the next line is commented out
		restart();
	}

	public void restart() {
		restarts++;
		level = new Level(level.getLevel());
	}
}
