package application;

import java.util.Stack;

public class Queen extends Piece {
	public Queen(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'Q';
		value = 90;
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
