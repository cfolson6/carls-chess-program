package application;

import java.util.Stack;

public class Bishop extends Piece {
	public Bishop(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'B';
		value = 30;
		isWhite = color;
	}
	
	public Stack<Move> getMoves(Piece[][] board) {
		Stack<Move> moves = new Stack<Move>();
		
		// move up right
		for (int i = 1; i < 8; i++) {
			if (xPos+i <= 7 && yPos+i <= 7 && (board[xPos+i][yPos+i] == null || !sameColor(board[xPos+i][yPos+i]))) {
				moves.push(new Move(xPos+i, yPos+i, this));
				if (board[xPos+i][yPos+i] != null) {
					i = 1000; // if this move was a capture, end for loop
				}
			} else {
				i = 1000; // if we just ran into a piece, end for loop
			}
		}
		
		// move up left
		for (int i = 1; i < 8; i++) {
			if (xPos-i >= 0 && yPos+i <= 7 && (board[xPos-i][yPos+i] == null || !sameColor(board[xPos-i][yPos+i]))) {
				moves.push(new Move(xPos-i, yPos+i, this));
				if (board[xPos-i][yPos+i] != null) {
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// move down right
		for (int i = 1; i < 8; i++) {
			if (xPos+i <= 7 && yPos-i >= 0 && (board[xPos+i][yPos-i] == null || !sameColor(board[xPos+i][yPos-i]))) {
				moves.push(new Move(xPos+i, yPos-i, this));
				if (board[xPos+i][yPos-i] != null) {
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// move down left
		for (int i = 1; i < 8; i++) {
			if (xPos-i >= 0 && yPos-i >= 0 && (board[xPos-i][yPos-i] == null || !sameColor(board[xPos-i][yPos-i]))) {
				moves.push(new Move(xPos-i, yPos-i, this));
				if (board[xPos-i][yPos-i] != null) {
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		return moves;
	}
}
