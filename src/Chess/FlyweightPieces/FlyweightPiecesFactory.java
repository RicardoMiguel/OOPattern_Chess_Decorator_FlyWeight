package Chess.FlyweightPieces;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ricardo on 20/06/2016.
 */
public class FlyweightPiecesFactory {

    private static FlyweightPiecesFactory instance;

    private Map<Integer, IPiece> pieces;

    private FlyweightPiecesFactory(){
        pieces = new HashMap<>();
    }

    public static FlyweightPiecesFactory getInstance(){
        if(instance == null){
            instance = new FlyweightPiecesFactory();
        }
        return instance;
    }

    public IPiece getPiece(int index) throws InvalidPieceException {
        IPiece piece = pieces.get(index);
        if(piece == null){
            piece = createPiece(index);
            pieces.put(index,piece);
        }
        return piece;
    }

    private IPiece createPiece(int index) throws InvalidPieceException{
        if(index == 0){
            return new PieceDecorator(new Piece(0));
        } else if(index == 1){
            return new PieceDecorator(new Piece(1));
        } else if(index == 5){
            return new PieceDecorator(new Piece(5));
        } else if(index == 6){
            return new PieceDecorator(new Piece(6));
        } else if(index == 7){
            return new PieceDecorator(new Piece(7));
        } else if(index == 11){
            return new PieceDecorator(new Piece(11));
        }

        throw new InvalidPieceException();
    }
}
