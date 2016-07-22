package Chess.FlyweightPieces;

import Chess.Chessboard;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Ricardo on 01/06/2016.
 */
public class PieceDecorator implements IPiece{
    private IPiece decorated;

    public PieceDecorator(IPiece toDecorate) {
        this.decorated = toDecorate;
    }

    public void draw(Graphics2D g, int x, int y) {
        AffineTransform old=g.getTransform();
        AffineTransform tr=new AffineTransform();
        tr.translate(Chessboard.ZEROX, Chessboard.ZEROY);
        tr.scale(Piece.TILESIZE,Piece.TILESIZE);
        g.transform(tr);
        decorated.draw(g, x, y);
        g.setTransform(old);
    }

    public IPiece getDecorated(){
        return decorated;
    }
}
