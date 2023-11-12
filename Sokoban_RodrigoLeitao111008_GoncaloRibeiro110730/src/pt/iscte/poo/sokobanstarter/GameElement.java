package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

abstract class GameElement implements ImageTile {
	private Point2D point2D;

	public GameElement(Point2D point2D) {
		this.point2D = point2D;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public Point2D getPosition() {
		return point2D;
	}

	@Override
	public abstract int getLayer();
	
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
