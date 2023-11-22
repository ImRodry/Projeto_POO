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

	public void consume(Consumable c) {
		if (c instanceof Bateria)
			energy += 50;
		else if (c instanceof Martelo)
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
		GameEngine engine = GameEngine.getInstance();
		GameElement special = engine.getSpecialIn(newPosition);
		if (special != null) {
			if (special instanceof Consumable && !((Consumable) special).canConsume(this)
					|| special instanceof Movable && !special.isTransposable() && !((Movable) special).canMoveTo(dir))
				return didMove;
			setPosition(newPosition);
			if (special instanceof Movable && !special.isTransposable()) {
				((Movable) special).move(dir);
				energy = Math.max(0, energy - 2);
			} else if (special instanceof Consumable) {
				((Consumable) special).consume(this);
				energy--;
			} else if (special instanceof Buraco && !((Buraco) special).isCovered()) {
				interactWithHole((Buraco) special);
			} else if (special instanceof Teleporte) {
				// Whe
				Teleporte p = engine.getTeleportPair((Teleporte) special);
				if (!p.isCovered())
					setPosition(p.getPosition());
			}
			didMove = true;
		} else if ((engine.isWithinBounds(newPosition) && canMoveTo(dir))) {
			setPosition(newPosition);
			didMove = true;
			energy--;
		}
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
		return didMove;
	}

	@Override
	public void interactWithHole(Buraco hole) {
		GameEngine engine = GameEngine.getInstance();
		engine.remove(this);
		engine.restart("Caiu num buraco!");
	}
}
