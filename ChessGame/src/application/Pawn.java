package application;

import java.util.Stack;

public class Pawn extends Piece {
	public Pawn(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'p';
		value = 10;
		isWhite = color;
	}
	
	public Stack<Move> getMoves(Piece[][] board) {
		Stack<Move> moves = new Stack<Move>();
		
		if (isWhite) {
			if (yPos < 7 && board[xPos][yPos+1] == null) {
				if (yPos == 6) {
					Move q = new Move(xPos, yPos+1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos, yPos+1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos, yPos+1, this)); // move one space
					if (yPos == 1 && board[xPos][yPos+2] == null) {
						moves.push(new Move(xPos, yPos+2, this)); // move two spaces
					}
				}
			}
			if (yPos < 7 && xPos < 7 && board[xPos+1][yPos+1] != null && !sameColor(board[xPos+1][yPos+1])) {
				if (yPos == 6) {
					Move q = new Move(xPos+1, yPos+1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos+1, yPos+1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos+1, yPos+1, this)); // capture diagonally
				}
			}
			if (yPos < 7 && xPos > 0 && board[xPos-1][yPos+1] != null && !sameColor(board[xPos-1][yPos+1])) {
				if (yPos == 6) {
					Move q = new Move(xPos-1, yPos+1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos-1, yPos+1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos-1, yPos+1, this)); // capture diagonally
				}
			}
			if ( yPos == 4 && xPos < 7 && board[xPos+1][4] != null && board[xPos+1][4].enPessantable) {
				Move m = new Move(xPos+1, 5, this);
				m.enPessant = true;
				moves.push(m); // en pessant
			}
			if ( yPos == 4 && xPos > 0 && board[xPos-1][4] != null && board[xPos-1][4].enPessantable) {
				Move m = new Move(xPos-1, 5, this);
				m.enPessant = true;
				moves.push(m); // en pessant
			}
		} else {
			if (yPos > 0 && board[xPos][yPos-1] == null) {
				if (yPos == 1) {
					Move q = new Move(xPos, yPos-1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos, yPos-1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos, yPos-1, this)); // move one space
					if (yPos == 6 && board[xPos][yPos-2] == null) {
						moves.push(new Move(xPos, yPos-2, this)); // move two spaces
					}
				}
			}
			if (yPos > 0 && xPos < 7 && board[xPos+1][yPos-1] != null && !sameColor(board[xPos+1][yPos-1])) {
				if (yPos == 1) {
					Move q = new Move(xPos+1, yPos-1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos+1, yPos-1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos+1, yPos-1, this)); // capture diagonally
				}
			}
			if (yPos > 0 && xPos > 0 && board[xPos-1][yPos-1] != null && !sameColor(board[xPos-1][yPos-1])) {
				if (yPos == 1) {
					Move q = new Move(xPos-1, yPos-1, this);
					q.promoteQueen = true;
					moves.push(q); // promote to queen
					Move n = new Move(xPos-1, yPos-1, this);
					n.promoteKnight = true;
					moves.push(n); // promote to knight
				} else {
					moves.push(new Move(xPos-1, yPos-1, this)); // capture diagonally
				}
			}
			if ( yPos == 3 && xPos < 7 && board[xPos+1][3] != null && board[xPos+1][3].enPessantable) {
				Move m = new Move(xPos+1, 2, this);
				m.enPessant = true;
				moves.push(m); // en pessant
			}
			if ( yPos == 3 && xPos > 0 && board[xPos-1][3] != null && board[xPos-1][3].enPessantable) {
				Move m = new Move(xPos-1, 2, this);
				m.enPessant = true;
				moves.push(m); // en pessant
			}
		}
		
		return moves;
	}
}
