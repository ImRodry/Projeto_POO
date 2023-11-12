package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
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

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui; // Referencia para ImageMatrixGUI (janela de interface com o utilizador)
	private List<ImageTile> tileList; // Lista de imagens
	private Empilhadora bobcat; // Referencia para a empilhadora

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		tileList = new ArrayList<>();
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

		gui = ImageMatrixGUI.getInstance(); // 1. obter instancia ativa de ImageMatrixGUI
		gui.setSize(GRID_HEIGHT, GRID_WIDTH); // 2. configurar as dimensoes
		gui.registerObserver(this); // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go(); // 4. lancar a GUI

		String nome = gui.askUser("Insira o seu nome");
		// Criar o cenario de jogo
		readLevelData(0);
		sendImagesToGUI();

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(
				"Level: " + 0 + " - Player: " + nome + " - Moves: " + 0 + "- Energy: " + bobcat.getEnergy());
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
			bobcat.move(Direction.directionFor(key));

		} catch (IllegalArgumentException error) {

		}
		gui.update(); // redesenha a lista de ImageTiles na GUI,
		// tendo em conta as novas posicoes dos objetos
	}

	private void readLevelData(int level) {
		File f = new File("levels/level" + level + ".txt");
		try {
			Scanner s = new Scanner(f);
			int y = 0;
			while (s.hasNext()) {
				String str = s.nextLine();
				System.out.println(str);
				for (int x = 0; x < GRID_WIDTH; x++) {
					char c = str.charAt(x);
					System.out.println(c);
					Point2D point = new Point2D(x, y);
					System.out.println(point);
					GameElement newElement = GameElement.create(c, point);
					tileList.add(newElement);
					// If it is layer 2 it will need a background
					if (newElement.getLayer() == 2)
						tileList.add(new Chao(point));
					if (newElement instanceof Empilhadora)
						bobcat = (Empilhadora) newElement;
				}
				y++;
			}
			System.out.println("Fim");
			s.close();
		} catch (FileNotFoundException error) {
			System.out.println("Erro");
		}
	}

	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no
	// inicio
	// Nao e' suposto re-enviar os objetos se a unica coisa que muda sao as posicoes
	private void sendImagesToGUI() {
		gui.addImages(tileList);
	}
}
