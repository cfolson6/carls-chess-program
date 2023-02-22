package application;

import java.util.Stack;

public abstract class Piece {
	int xPos;
	int yPos;
	char letter; // p = pawn, N = knight, B = bishop, R = rook, Q = queen, K = king
	boolean isWhite; // white = true, black = false
	int value; // pawn = 10; knight, bishop = 30; rook = 50; queen = 90; king = 9000
	boolean hasMoved = false;
	boolean captured = false;
	boolean enPessantable = false; // true if this piece can be captured by en pessant
	
	public abstract Stack<Move> getMoves(Piece[][] board); // returns a stack of pseudo-legal moves
	
	void setX(int x) {
		xPos = x;
	}
	
	void setY(int y) {
		yPos = y;
	}
	
	void setXY(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	void setWhite() {
		isWhite = true;
	}
	
	void setBlack() {
		isWhite = false;
	}
	
	boolean sameColor(Piece compare) {
		return isWhite == compare.isWhite;
	}
	
	boolean isPawn() {
		return letter == 'p';
	}
	
	boolean isKnight() {
		return letter == 'N';
	}
	
	boolean isBishop() {
		return letter == 'B';
	}
	
	boolean isRook() {
		return letter == 'R';
	}
	
	boolean isQueen() {
		return letter == 'Q';
	}
	
	boolean isKing() {
		return letter == 'K';
	}
	
	Piece deepCopy() {
		Piece piece = null;
		
		switch (letter) {
		case 'p' : piece = new Pawn(xPos, yPos, isWhite);
		break;
		case 'N' : piece = new Knight(xPos, yPos, isWhite);
		break;
		case 'B' : piece = new Bishop(xPos, yPos, isWhite);
		break;
		case 'R' : piece = new Rook(xPos, yPos, isWhite);
		break;
		case 'Q' : piece = new Queen(xPos, yPos, isWhite);
		break;
		case 'K' : piece = new King(xPos, yPos, isWhite);
		break;
		}
		
		piece.hasMoved = hasMoved;
		piece.captured = captured;
		piece.enPessantable = enPessantable;
		
		return piece;
	}
}
