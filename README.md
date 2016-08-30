#Decorator

In the final of description of the problem you will find a skeleton of a chess game. It allows displaying pieces, places on the chessboard in the main function, and also moving them with the mouse (including taking other figures). The chess moving rules are not enforced - the pieces can be moved in any way.

The problem is following: the pieces uses coordinates of the chessboard (0,1,...,7) while to display them on the screen one need coordinates in pixels.

Solve this problem embedding the pieces with a Decorator. It should change the functioning of the draw method by manipulating the graphic context Graphics2D passed as the parameter. Use the AffineTransformation class transforming the context coordinates:

```
AffineTransform tr=new AffineTransform();
tr.translate(ZEROX,ZEROY);
tr.scale(TILESIZE,TILESIZE);
```
Such the transformation set on the graphic context object (transform method - use this method, not setTransform, to apply the transformations) in the draw method will assure correct displaying of the pieces. Remember about restoring the original transformation (getTransform/setTransform methods). After it you should also change the original draw method in the Piece class.
```
g.drawImage(image, x, y, x+TILESIZE, y+TILESIZE, ...
```
->
```
g.drawImage(image, x, y, x+1, y+1, ...
```
Besides the static displaying of figures, there is also implemented (and not yet working) mechanism of moving them. For the time of moving pieces (drag-and-drop) they should be embedded with another decorator (in the mousePressed method) with a affine transform continuously modified (in the mouseDragged method, with its setToTranslation method). This decorator adds the additional translation equal to a vector from the first click point to the current mouse position. After releasing the mouse button the piece being moved has to be freed from this second decorator.

#Flyweight

Objects representing the same pieces can shared according to the Flyweight design pattern. Of course they are not identical - they differ with the position (x,y). So make this position the external state: remove it from the class and pass it only when it is used (as parameters of draw method). Leave in this class the piece index (as internal state). Then many objects will get identical and able to be shared.

The external state (piece position) don't need to be stored outside nor calculated - the pieces have been already stored in the map with this position as their keys. The draw method is called in a loop iterating through the whole map - so you have access to it and can pass it as the parameter.

Only while dragging the piece the original position should be stored in temporary external variables.

Remember to block the possibility to create piece objects freely: make a pool of them (similarly to the the Singleton/Multiton pattern).

```
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class Piece {
	public static final int TILESIZE = 32;
	private static Image image = new ImageIcon("pieces4.png").getImage();

	private int index, x, y;
	public Piece(int idx, int xx, int yy)	{ index = idx; x=xx; y=yy;  }
	public void draw(Graphics2D g) {
		g.drawImage(image, x, y, x+TILESIZE, y+TILESIZE,
			index*TILESIZE, 0, (index+1)*TILESIZE, TILESIZE, null);
	}
	public int getX() { return x; }
	public int getY() {return y; }
	public void moveTo(int xx, int yy) { x=xx; y=yy; }
}


public class Chessboard extends JPanel {
	public static final int ZEROX = 23;
	static final int ZEROY = 7;

	private HashMap<Point, Piece> board = new HashMap<Point, Piece>();
	public void drop(Piece p, int x, int y)	{
		repaint();
		p.moveTo(x,y);
		board.put(new Point(x, y), p); // if there is any piece on this coordinates - it is replaced and disappears
						//(HashMap doesn't allow entries with the same key)
	}
	public Piece take(int x, int y)	{
		repaint();
		return board.remove(new Point(x, y));
	}

	private Image image;
	private Piece dragged = null;
	private Point mouse = null;
	public void paint(Graphics g)	{
		g.drawImage(image, 0, 0, null);
		for(Map.Entry<Point, Piece> e : board.entrySet()) {
			Point pt = e.getKey();
			Piece pc = e.getValue();
			pc.draw((Graphics2D)g);
		}
		if(mouse != null && dragged != null) {
			dragged.draw((Graphics2D)g);
		}
	}

	Chessboard()	{
		board.put(new Point(0,2), new Piece(11,0,2));
		board.put(new Point(0,6), new Piece(0,0,6));
		board.put(new Point(1,4), new Piece(6,1,4));
		board.put(new Point(1,5), new Piece(5,1,5));
		board.put(new Point(3,7), new Piece(1,3,7));
		board.put(new Point(4,3), new Piece(6,4,3));
		board.put(new Point(4,4), new Piece(7,4,4));
		board.put(new Point(5,4), new Piece(6,5,4));
		board.put(new Point(5,6), new Piece(0,5,6));
		board.put(new Point(6,5), new Piece(0,6,5));
		board.put(new Point(7,4), new Piece(0,7,4));


		image = new ImageIcon("board3.png").getImage();
		setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent ev) {
				dragged = take((ev.getX()-ZEROX)/Piece.TILESIZE, (ev.getY()-ZEROY)/Piece.TILESIZE);
				mouse = ev.getPoint();
			}
			public void mouseReleased(MouseEvent ev) {
				drop(dragged, (ev.getX()-ZEROX)/Piece.TILESIZE, (ev.getY()-ZEROY)/Piece.TILESIZE);
				dragged = null;
				undo.setEnabled(true);
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent ev)	{
				mouse = ev.getPoint();
				repaint();
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
```