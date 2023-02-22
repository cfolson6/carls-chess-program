package application;

import java.util.Stack;

public class Knight extends Piece {
	public Knight(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'N';
		value = 30;
		isWhite = color;
	}
	
	public Stack<Move> getMoves(Piece[][] board) {
		Stack<Move> moves = new Stack<Move>();
		
		if (xPos < 6 && yPos < 7 && (board[xPos+2][yPos+1] == null || !sameColor(board[xPos+2][yPos+1]))) {
			moves.push(new Move(xPos+2, yPos+1, this));
		}
		if (xPos < 6 && yPos > 0 && (board[xPos+2][yPos-1] == null || !sameColor(board[xPos+2][yPos-1]))) {
			moves.push(new Move(xPos+2, yPos-1, this));
		}
		if (xPos < 7 && yPos < 6 && (board[xPos+1][yPos+2] == null || !sameColor(board[xPos+1][yPos+2]))) {
			moves.push(new Move(xPos+1, yPos+2, this));
		}
		if (xPos > 0 && yPos < 6 && (board[xPos-1][yPos+2] == null || !sameColor(board[xPos-1][yPos+2]))) {
			moves.push(new Move(xPos-1, yPos+2, this));
		}
		if (xPos > 1 && yPos < 7 && (board[xPos-2][yPos+1] == null || !sameColor(board[xPos-2][yPos+1]))) {
			moves.push(new Move(xPos-2, yPos+1, this));
		}
		if (xPos > 1 && yPos > 0 && (board[xPos-2][yPos-1] == null || !sameColor(board[xPos-2][yPos-1]))) {
			moves.push(new Move(xPos-2, yPos-1, this));
		}
		if (xPos < 7 && yPos > 1 && (board[xPos+1][yPos-2] == null || !sameColor(board[xPos+1][yPos-2]))) {
			moves.push(new Move(xPos+1, yPos-2, this));
		}
		if (xPos > 0 && yPos > 1 && (board[xPos-1][yPos-2] == null || !sameColor(board[xPos-1][yPos-2]))) {
			moves.push(new Move(xPos-1, yPos-2, this));
		}
		
		return moves;
	}
}
