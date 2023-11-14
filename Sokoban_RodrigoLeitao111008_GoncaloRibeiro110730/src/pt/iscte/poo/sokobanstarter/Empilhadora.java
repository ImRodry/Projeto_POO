package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends Movable {
	private int energy = 100;
	private Direction lastDirection = Direction.DOWN;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition, 2);
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

	public int getEnergy() {
		return energy;
	}

	public void pickupBattery() {
		energy += 50;
	}

	public boolean move(Direction dir) {
		boolean didMove = false;
		if (energy <= 0)
			return didMove;
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = getPosition().plus(dir.asVector());
		GameEngine ge = GameEngine.getInstance();
		Movable m = ge.getMovableIn(newPosition);
		if ((ge.isWithinBounds(newPosition) && canMoveTo(dir)) || (m != null && m.canMoveTo(dir))) {
			if (m != null) {
				setPosition(newPosition);
				m.move(dir);
				energy -= 2;
			} else {
				setPosition(newPosition);
				energy--;
			}
			didMove = true;
		}
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
		return didMove;
	}
}
