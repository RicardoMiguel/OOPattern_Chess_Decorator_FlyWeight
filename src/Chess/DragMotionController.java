package Chess;


import Chess.FlyweightPieces.DragDecorator;
import Chess.FlyweightPieces.IPiece;

import java.awt.*;

/**
 * Created by ricar on 31/08/2016.
 */
public class DragMotionController {

    //Variables for dragged
    private DragDecorator dragged = null;
    private Point mouse = null;
    private Point draggedOriginalPoint = null;

    DragMotionController(IPiece piece, Point originalPiecePoint, Point currentMousePoint){
        dragged = new DragDecorator(piece);
        mouse = currentMousePoint;
        draggedOriginalPoint = originalPiecePoint;
    }

    public void paintDragMotion(Graphics g){
        if(mouse != null && dragged != null) {
            dragged.draw((Graphics2D)g, draggedOriginalPoint.x, draggedOriginalPoint.y);
        }
    }

    public DragDecorator getDragged(){
        return dragged;
    }

    public void calcAlphaX(int currentX){
        dragged.setAlphaX(currentX - mouse.x);
    }

    public void calcAlphaY(int currentY){
        dragged.setAlphaY(currentY - mouse.y);
    }
}
