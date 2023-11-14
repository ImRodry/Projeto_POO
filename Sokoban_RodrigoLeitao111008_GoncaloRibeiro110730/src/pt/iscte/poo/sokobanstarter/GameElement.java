package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

abstract class GameElement implements ImageTile {
	private Point2D position;
	private int layer;

	public GameElement(Point2D position, int layer) {
		this.position = position;
		this.layer = layer;
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
		this.position = position;
	}

	@Override
	public int getLayer() {
		return layer;
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
			return new Parede(point);
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
