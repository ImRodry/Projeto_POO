package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends Movable {
	private int energy;
	private boolean hammer = false;
	private Direction lastDirection = Direction.DOWN;
	private boolean hasUsedEasterEgg = false;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition, 2);
		// Must be defined here to ensure the easter egg is correctly calculated
		energy = GameEngine.getInstance().hasEasterEgg() ? 10000 : 100;
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "Ronaldo";
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
		// We intentionally save the last direction even if there was no movement
		// to represent the attempt of a movement
		lastDirection = dir;
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
			} else if (special instanceof Interactable) {
				((Interactable) special).interact(this);
			}
			didMove = true;
		} else if ((engine.isWithinBounds(newPosition) && canMoveTo(dir))) {
			setPosition(newPosition);
			didMove = true;
			energy--;
		}
		return didMove;
	}

	public boolean easterEgg() {
		if (hasUsedEasterEgg)
			return false;
		energy += 500;
		hasUsedEasterEgg = true;
		return true;
	}

	@Override
	public void interactWithHole(Buraco hole) {
		if (hole.isCovered())
			return;
		GameEngine engine = GameEngine.getInstance();
		engine.remove(this);
		engine.restart("Caiu num buraco!");
	}
}
