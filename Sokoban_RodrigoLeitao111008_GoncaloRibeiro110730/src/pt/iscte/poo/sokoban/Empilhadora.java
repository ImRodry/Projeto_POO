package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends Movable {
	private int energy = 100;
	private boolean hammer = false;
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

	public void consumeBattery() {
		energy += 50;
	}

	public void consumeHammer() {
		hammer = true;
	}

	public boolean getHammer() {
		return hammer;
	}

	public boolean move(Direction dir) {
		boolean didMove = false;
		if (energy <= 0)
			return didMove;
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = getPosition().plus(dir.asVector());
		GameEngine ge = GameEngine.getInstance();
		interactWithHole(newPosition);
		Movable m = ge.getMovableIn(newPosition);
		Consumable c = ge.getConsumableIn(newPosition);
		if ((ge.isWithinBounds(newPosition) && canMoveTo(dir)) || (m != null && m.canMoveTo(dir)) || c != null) {
			if (m != null && !m.isTransposable()) {
				m.interactWithHole(newPosition.plus(dir.asVector()));
				// TODO move element after bobcat? Fixes ghost move issue but dupes code
				m.move(dir);
				energy = Math.max(0, energy - 2);
			} else {
				if (c != null && !c.consume(this))
					return didMove;
				energy--;
			}
			setPosition(newPosition);
			didMove = true;
		}
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
		return didMove;
	}

	@Override
	public void interactWithHole(Point2D newPosition) {
		if (isHole(newPosition) != null) {
			GameEngine.getInstance().lose("Caiu num buraco!");
		}
	}
}
