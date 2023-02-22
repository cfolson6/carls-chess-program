package application;

public class Move {
	int endX;
	int endY;
	int startX;
	int startY;
	boolean castle = false;
	boolean enPessant = false;
	boolean promoteQueen = false;
	boolean promoteKnight = false;
	boolean promoteBishop = false;
	boolean promoteRook = false;
	
	public Move(int x, int y, Piece p) {
		endX = x;
		endY = y;
		startX = p.xPos;
		startY = p.yPos;
	}
}
