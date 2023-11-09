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
}
