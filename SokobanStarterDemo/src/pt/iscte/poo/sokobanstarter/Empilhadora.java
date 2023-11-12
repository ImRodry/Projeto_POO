package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement {

	private Point2D position;
	private int energy = 100;
	private Direction lastDirection = Direction.DOWN;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition);
		position = initialPosition;
	}

	@Override
	public String getName() {
		String imagePrefix = "Empilhadora_";
		switch (lastDirection) {
			case UP:
				return imagePrefix + "U";
			default:
			case DOWN:
				return imagePrefix + "D";
			case LEFT:
				return imagePrefix + "L";
			case RIGHT:
				return imagePrefix + "R";
		}
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public int getEnergy() {
		return energy;
	}

	public void move(Direction dir) {
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = position.plus(dir.asVector());
		if (GameEngine.getInstance().isWithinBounds(newPosition)) {
			position = newPosition;
			energy--;
		}
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
	}
}