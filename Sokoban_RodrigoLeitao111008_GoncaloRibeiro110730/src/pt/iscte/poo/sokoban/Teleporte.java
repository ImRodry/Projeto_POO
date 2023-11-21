package pt.iscte.poo.sokoban;

import java.util.ArrayList;

import pt.iscte.poo.utils.Point2D;

public class Teleporte extends Interactable {
	
	public Teleporte(Point2D point2D){
		super(point2D, 1);
	}

	public Point2D getTeleportPair(ArrayList<Teleporte> teleportes) {
		for (Teleporte t : teleportes) {
			if (!(t.getPosition().equals(this.getPosition()))) {
				if (t.isCovered())
					return null;
				t.setCovered(true);
				return t.getPosition();
			}
		}
		throw new IllegalStateException("Not possible to have only one teleport");
	}
}
