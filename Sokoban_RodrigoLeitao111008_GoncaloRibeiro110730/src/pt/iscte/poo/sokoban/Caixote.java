package pt.iscte.poo.sokoban;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends Movable {
	private boolean onTarget = false;

	public Caixote(Point2D point2D) {
		super(point2D, 2);
	}

	public boolean isOnTarget() {
		return onTarget;
	}

	public void setOnTarget(boolean onTarget) {
		this.onTarget = onTarget;
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "Bola";
		return super.getName() + (onTarget ? "_no_alvo" : "");
	}

	@Override
	public boolean move(Direction dir) {
		GameEngine engine = GameEngine.getInstance();
		if (engine.hasEasterEgg()) {
			Alvo target = engine.getTargets().stream().filter(t -> !t.isCovered()).findAny().get();
			setPosition(target.getPosition());
			target.interact(this);
			ImageMatrixGUI.getInstance().setMessage("Ronaldo chuta e marca! GOOOOOOLOOOOOOO");
			return true;
		}
		return super.move(dir);
	}

	@Override
	public void interactWithHole(Buraco hole) {
		GameEngine.getInstance().remove(this);
	}
}
