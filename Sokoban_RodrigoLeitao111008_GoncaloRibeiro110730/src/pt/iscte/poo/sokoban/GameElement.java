package pt.iscte.poo.sokoban;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

abstract class GameElement implements ImageTile {
	private Point2D position;
	private int layer;
	private boolean transposable;

	public GameElement(Point2D position, int layer, boolean transposable) {
		this.position = position;
		this.layer = layer;
		this.transposable = transposable;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		GameEngine.getInstance().updatePosition(this, position);
		this.position = position;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public boolean isTransposable() {
		return transposable;
	}

	public void setTransposable(boolean transposable) {
		this.transposable = transposable;
	}

	public static GameElement create(char c, Point2D point) {
		switch (c) {
		case 'E':
			return new Empilhadora(point);
		case 'C':
			return new Caixote(point);
		case 'X':
			return new Alvo(point);
		case 'B':
			return new Bateria(point);
		case '#':
			return new Parede(point);
		case ' ':
			return new Chao(point);
		case '=':
			return new Vazio(point);
		case 'O':
			return new Buraco(point);
		case 'P':
			return new Palete(point);
		case 'M':
			return new Martelo(point);
		case '%':
			return new ParedeRachada(point);
		case 'T':
			return new Teleporte(point);
		default:
			throw new IllegalArgumentException("Unknown symbol");
		}
	}
}