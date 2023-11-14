package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement {
	private int energy = 100;
	private Direction lastDirection = Direction.DOWN;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition);
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
	public int getLayer() {
		return 2;
	}

	public int getEnergy() {
		return energy;
	}

	public void pickupBattery() {
		energy += 50;
	}

	public void move(Direction dir) {
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = getPosition().plus(dir.asVector());
		if (GameEngine.getInstance().isWithinBounds(newPosition)) {
			setPosition(newPosition);
			energy--;
		}
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
	}
}
