package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Alvo extends Interactable {
	
	public Alvo(Point2D point2D){
		super(point2D, 1);
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "Baliza";
		return super.getName();
	}

	@Override
	public void interact(Movable m) {
		if (m instanceof Caixote)
			setCovered(true);
	}
}
