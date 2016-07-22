package Chess.FlyweightPieces;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Ricardo on 08/06/2016.
 */
public class DragDecorator implements IPiece {
    private IPiece decorated;
    private double alphaX;
    private double alphaY;

    public DragDecorator(IPiece decorated) {
        this.decorated = decorated;
    }

    public void setAlphaX(double alphaX) {
        this.alphaX = alphaX;
    }

    public void setAlphaY(double alphaY) {
        this.alphaY = alphaY;
    }

    public IPiece getDecorated() {

        return decorated;
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        AffineTransform old=g.getTransform();
        AffineTransform tr=new AffineTransform();
        tr.translate(alphaX, alphaY);
        g.transform(tr);
        decorated.draw(g, x, y);
        g.setTransform(old);
    }


}
