package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class Teleporte extends Interactable {
	
	public Teleporte(Point2D point2D){
		super(point2D, 0);
	}

	@Override
	public String getName() {
		if (GameEngine.getInstance().hasEasterEgg())
			return "TeleporteRelva";
		return super.getName();
	}
}
