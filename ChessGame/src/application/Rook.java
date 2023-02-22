package application;

import java.util.Stack;

public class Rook extends Piece {
	public Rook(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'R';
		value = 50;
		isWhite = color;
	}
	
	public Stack<Move> getMoves(Piece[][] board) {
		Stack<Move> moves = new Stack<Move>();
		
		// move right
		for (int x = xPos+1; x <= 7; x++) {
			if (board[x][yPos] == null || !sameColor(board[x][yPos])) {
				moves.push(new Move(x, yPos, this));
				if (board[x][yPos] != null) {
					x = 1000; // if this move was a capture, end for loop
				}
			} else {
				x = 1000; // if we just ran into a piece, end for loop
			}
		}
		
		// move left
		for (int x = xPos-1; x >= 0; x--) {
			if (board[x][yPos] == null || !sameColor(board[x][yPos])) {
				moves.push(new Move(x, yPos, this));
				if (board[x][yPos] != null) {
					x = -1000; // if this move was a capture, end for loop
				}
			} else {
				x = -1000; // if we just ran into a piece, end for loop
			}
		}
		
		// move up
		for (int y = yPos+1; y <= 7; y++) {
			if (board[xPos][y] == null || !sameColor(board[xPos][y])) {
				moves.push(new Move(xPos, y, this));
				if (board[xPos][y] != null) {
					y = 1000; // if this move was a capture, end for loop
				}
			} else {
				y = 1000; // if we just ran into a piece, end for loop
			}
		}
		
		// move down
		for (int y = yPos-1; y >= 0; y--) {
			if (board[xPos][y] == null || !sameColor(board[xPos][y])) {
				moves.push(new Move(xPos, y, this));
				if (board[xPos][y] != null) {
					y = -1000; // if this move was a capture, end for loop
				}
			} else {
				y = -1000; // if we just ran into a piece, end for loop
			}
		}
		
		return moves;
	}
}
