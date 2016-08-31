package Chess;

import Chess.FlyweightPieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;


public class Chessboard extends JPanel {
    public static final int ZEROX = 23;
    public static final int ZEROY = 7;

    private HashMap<Point, IPiece> board = new HashMap<>();
    public void drop(IPiece p, int x, int y)	{
        repaint();
        board.put(new Point(x, y), p); // if there is any piece on this coordinates - it is replaced and disappears
        //(HashMap doesn't allow entries with the same key)
    }
    public IPiece take(int x, int y)	{
        repaint();
        return board.remove(new Point(x, y));
    }

    private Image image;
    private DragMotionController draggerController;

    public void paint(Graphics g)	{
        g.drawImage(image, 0, 0, null);
        for(Map.Entry<Point, IPiece> e : board.entrySet()) {
            Point pt = e.getKey();
            IPiece pc = e.getValue();
            pc.draw((Graphics2D)g, pt.x, pt.y);
        }

        if(draggerController != null){
            draggerController.paintDragMotion(g);
        }
    }

    Chessboard()	{
        FlyweightPiecesFactory instance = FlyweightPiecesFactory.getInstance();
        try {
            board.put(new Point(0,2), instance.getPiece(11));
            board.put(new Point(0,6), instance.getPiece(0));
            board.put(new Point(1,4), instance.getPiece(6));
            board.put(new Point(1,5), instance.getPiece(5));
            board.put(new Point(3,7), instance.getPiece(1));
            board.put(new Point(4,3), instance.getPiece(6));
            board.put(new Point(4,4), instance.getPiece(7));
            board.put(new Point(5,4), instance.getPiece(6));
            board.put(new Point(5,6), instance.getPiece(0));
            board.put(new Point(6,5), instance.getPiece(0));
            board.put(new Point(7,4), instance.getPiece(0));
        } catch (InvalidPieceException e) {
            e.printStackTrace();
        }

        image = new ImageIcon("board3.png").getImage();
        setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent ev) {
                int x = (ev.getX()-ZEROX)/ Piece.TILESIZE;
                int y = (ev.getY()-ZEROY)/Piece.TILESIZE;
                IPiece piece = take(x, y);

                if(piece != null) {
                    draggerController = new DragMotionController(piece, new Point(x,y), ev.getPoint());
                }
            }

            public void mouseReleased(MouseEvent ev) {
                if(draggerController != null) {
                    drop(draggerController.getDragged().getDecorated(), (ev.getX() - ZEROX) / Piece.TILESIZE, (ev.getY() - ZEROY) / Piece.TILESIZE);
                }
                draggerController = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent ev)	{
                if(draggerController != null) {
                    draggerController.calcAlphaX(ev.getX());
                    draggerController.calcAlphaY(ev.getY());
                    repaint();
                }
            }
        });
    }

    class UndoButton implements ActionListener	{
        public void actionPerformed(ActionEvent ev)
        {
            System.out.println("UNDO");
            redo.setEnabled(true);
        }
    }

    class RedoButton implements ActionListener 	{
        public void actionPerformed(ActionEvent ev) 	{
            System.out.println("REDO");
        }
    }

    private JButton undo, redo;

    public static void main(String[] args)	{
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Chessboard board = new Chessboard();

        JToolBar bar = new JToolBar();
        board.undo = new JButton(new ImageIcon("undo.png"));
        board.redo = new JButton(new ImageIcon("redo.png"));
        board.undo.addActionListener(board.new UndoButton());
        board.redo.addActionListener(board.new RedoButton());
        board.undo.setEnabled(false);
        board.redo.setEnabled(false);
        bar.add(board.undo);
        bar.add(board.redo);

        frame.add(bar, BorderLayout.PAGE_START);
        frame.add(board);

        frame.pack();
        frame.setVisible(true);
    }
}