package pt.iscte.poo.sokoban;

import java.util.ArrayList;

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

	private static GameEngine INSTANCE;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();;
	private String username;
	private Level level;
	private int moves = 0;

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

		username = gui.askUser("Insira o seu nome");
		// Criar o cenario de jogo
		level = new Level(1);

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
			if (level.getBobcat().move(Direction.directionFor(key)))
				moves++;
			gui.setStatusMessage("Level: " + 0 + " - Player: " + username + " - Moves: " + moves + " - Energy: "
					+ level.getBobcat().getEnergy());
		} catch (IllegalArgumentException error) {
			System.err.println("Tecla desconhecida");
		}
		gui.update(); // redesenha a lista de ImageTiles na GUI,
		// tendo em conta as novas posicoes dos objetos
	}

	public boolean isWithinBounds(Point2D point) {
		return gui.isWithinBounds(point);
	}

	public void remove(GameElement e) {
		gui.removeImage(e);
		level.getElementMap().get(e.getPosition()).remove(e);
	}

	public void updatePosition(GameElement e, Point2D newPosition) {
		ArrayList<GameElement> oldList = level.getElementMap().get(e.getPosition());
		ArrayList<GameElement> newList = level.getElementMap().get(newPosition);
		oldList.remove(e);
		newList.add(e);
		if (e instanceof Caixote) {
			Alvo oldAlvo = (Alvo) oldList.stream().filter(em -> (em instanceof Alvo)).findFirst()
					.orElse(null);
			Alvo newAlvo = (Alvo) newList.stream().filter(em -> (em instanceof Alvo)).findFirst()
					.orElse(null);

			if (oldAlvo != null) {
				oldAlvo.setFilled(false);
			}
			if (newAlvo != null) {
				newAlvo.setFilled(true);
			}

		}
	}

	public ArrayList<GameElement> getElementsIn(Point2D position) {
		return level.getElementMap().get(position);
	}

	public Movable getMovableIn(Point2D p) {
		for (GameElement e : getElementsIn(p))
			if (e instanceof Movable)
				return (Movable) e;
		return null;
	}

	public Consumable getConsumableIn(Point2D p) {
		for (GameElement e : getElementsIn(p))
			if (e instanceof Consumable)
				return (Consumable) e;
		return null;
	}

	public boolean checkEnd() {
		if (!level.checkEnd())
			return false;
		int oldLevel = level.getLevel();
		gui.setMessage("Nível " + oldLevel + " concluído!");
		gui.clearImages();
		try {
			level = new Level(oldLevel + 1);
		} catch (IllegalArgumentException e) {
			// if creating the level errors it means there are no more levels, thus we won
			gui.setMessage("Congratulations, you won!");
			gui.clearImages();
			System.exit(0);
		}
		return true;
	}
}
