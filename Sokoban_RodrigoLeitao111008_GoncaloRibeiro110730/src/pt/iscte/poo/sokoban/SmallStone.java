package pt.iscte.poo.sokoban;

import pt.iscte.poo.utils.Point2D;

public class SmallStone extends Movable {
    public SmallStone(Point2D position) {
        super(position, 0);
    }

    @Override
    public void interactWithHole(Buraco hole) {
        GameEngine.getInstance().remove(this);
    }
}
