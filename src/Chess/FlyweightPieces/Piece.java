package Chess.FlyweightPieces;

import javax.swing.*;
import java.awt.*;

public class Piece implements IPiece{
    public static final int TILESIZE = 32;
    private static Image image = new ImageIcon("pieces4.png").getImage();

    private int index;
    Piece(int idx)	{ index = idx;}
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(image, x, y, x+1, y+1,
                index*TILESIZE, 0, (index+1)*TILESIZE, TILESIZE, null);
    }
}