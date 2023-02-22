package application;

import java.util.Stack;

public class King extends Piece {
	public King(int x, int y, boolean color) {
		xPos = x;
		yPos = y;
		letter = 'K';
		value = 9000;
		isWhite = color;
	}
	
	public Stack<Move> getMoves(Piece[][] board) {
		Stack<Move> moves = new Stack<Move>();
		
		boolean canCastleRight = false;
		
		// move right
		if (xPos+1 <= 7 && (board[xPos+1][yPos] == null || !sameColor(board[xPos+1][yPos]))) {
			moves.push(new Move(xPos+1, yPos, this));
			
			// check for castling
			if (!hasMoved) {
				for (int i = 5; i <= 7; i++) {
					if (i == 7 && board[7][yPos] != null && board[7][yPos].isRook() && sameColor(board[7][yPos]) && !board[7][yPos].hasMoved) {
						if (!castleCheck(xPos, true, board) && !castleCheck(xPos+1, true, board)) {
							Move m = new Move(xPos+2, yPos, this);
							m.castle = true;
							moves.push(m);
							canCastleRight = true;
						}
					} else if (board[i][yPos] != null) {
						i = 1000; // exit for loop
					}
				}
			}
		}
		
		// move left
		if (xPos-1 >= 0 && (board[xPos-1][yPos] == null || !sameColor(board[xPos-1][yPos]))) {
			moves.push(new Move(xPos-1, yPos, this));
			
			// check for castling
			if (!hasMoved) {
				for (int i = 3; i >= 0; i--) {
					if (i == 0 && board[0][yPos] != null && board[0][yPos].isRook() && sameColor(board[0][yPos]) && !board[0][yPos].hasMoved) {
						if (!castleCheck(xPos-1, false, board) && (canCastleRight || !castleCheck(xPos, false, board))) {
							Move m = new Move(xPos-2, yPos, this);
							m.castle = true;
							moves.push(m);
						}
					} else if (board[i][yPos] != null) {
						i = -1000; // exit for loop
					}
				}
			}
		}
		
		// move up
		if (yPos+1 <= 7 && (board[xPos][yPos+1] == null || !sameColor(board[xPos][yPos+1]))) {
			moves.push(new Move(xPos, yPos+1, this));
		}
		
		// move down
		if (yPos-1 >= 0 && (board[xPos][yPos-1] == null || !sameColor(board[xPos][yPos-1]))) {
			moves.push(new Move(xPos, yPos-1, this));
		}
		
		// move up right
		if (xPos+1 <= 7 && yPos+1 <= 7 && (board[xPos+1][yPos+1] == null || !sameColor(board[xPos+1][yPos+1]))) {
			moves.push(new Move(xPos+1, yPos+1, this));
		}
		
		// move up left
		if (xPos-1 >= 0 && yPos+1 <= 7 && (board[xPos-1][yPos+1] == null || !sameColor(board[xPos-1][yPos+1]))) {
			moves.push(new Move(xPos-1, yPos+1, this));
		}
		
		// move down right
		if (xPos+1 <= 7 && yPos-1 >= 0 && (board[xPos+1][yPos-1] == null || !sameColor(board[xPos+1][yPos-1]))) {
			moves.push(new Move(xPos+1, yPos-1, this));
		}
		
		// move down left
		if (xPos-1 >= 0 && yPos-1 >= 0 && (board[xPos-1][yPos-1] == null || !sameColor(board[xPos-1][yPos-1]))) {
			moves.push(new Move(xPos-1, yPos-1, this));
		}
		
		return moves;
	}
	
	
	// returns false if the space is not in check for castles
	// if rightCastle is true, we are castling right; else, we are castling left
	boolean castleCheck(int xCheck, boolean rightCastle, Piece[][] board) {
		// move right
		if (xCheck == xPos && !rightCastle) {
			for (int x = xPos+1; x <= 7; x++) {
				if (board[x][yPos] != null) {
					if (!sameColor(board[x][yPos])) {
						if (board[x][yPos].isQueen() || board[x][yPos].isRook()) {
							return true;
						} else if (Math.abs(x - xPos) == 1 && board[x][yPos].isKing()) {
							return true;
						}
					}
					x = 1000; // exit for loop
				}
			}
		}
		
		// move left
		if (xCheck == xPos && rightCastle) {
			for (int x = xPos-1; x >= 0; x--) {
				if (board[x][yPos] != null) {
					if (!sameColor(board[x][yPos])) {
						if (board[x][yPos].isQueen() || board[x][yPos].isRook()) {
							return true;
						} else if (Math.abs(x - xPos) == 1 && board[x][yPos].isKing()) {
							return true;
						}
					}
					x = -1000; // exit for loop
				}
			}
		}
		
		// move up
		for (int y = yPos+1; y <= 7; y++) {
			if (board[xCheck][y] != null) {
				if (!sameColor(board[xCheck][y])) {
					if (board[xCheck][y].isQueen() || board[xCheck][y].isRook()) {
						return true;
					} else if (Math.abs(y - yPos) == 1 && board[xCheck][y].isKing()) {
						return true;
					}
				}
				y = 1000;
			}
		}
		
		// move down
		for (int y = yPos-1; y >= 0; y--) {
			if (board[xCheck][y] != null) {
				if (!sameColor(board[xCheck][y])) {
					if (board[xCheck][y].isQueen() || board[xCheck][y].isRook()) {
						return true;
					} else if (Math.abs(y - yPos) == 1 && board[xCheck][y].isKing()) {
						return true;
					}
				}
				y = -1000;
			}
		}
		
		// move up right
		for (int i = 1; i < 8; i++) {
			if (xCheck+i <= 7 && yPos+i <= 7) {
				if (board[xCheck+i][yPos+i] != null) {
					if (!sameColor(board[xCheck+i][yPos+i])) {
						if (board[xCheck+i][yPos+i].isQueen() || board[xCheck+i][yPos+i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[xCheck+i][yPos+i].isKing()) {
								return true;
							} else if (isWhite && board[xCheck+i][yPos+i].isPawn()) {
								return true;
							}
						}
					}
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// move down right
		for (int i = 1; i < 8; i++) {
			if (xCheck+i <= 7 && yPos-i >= 0) {
				if (board[xCheck+i][yPos-i] != null) {
					if (!sameColor(board[xCheck+i][yPos-i])) {
						if (board[xCheck+i][yPos-i].isQueen() || board[xCheck+i][yPos-i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[xCheck+i][yPos-i].isKing()) {
								return true;
							} else if (!isWhite && board[xCheck+i][yPos-i].isPawn()) {
								return true;
							}
						}
					}
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// move up left
		for (int i = 1; i < 8; i++) {
			if (xCheck-i >= 0 && yPos+i <= 7) {
				if (board[xCheck-i][yPos+i] != null) {
					if (!sameColor(board[xCheck-i][yPos+i])) {
						if (board[xCheck-i][yPos+i].isQueen() || board[xCheck-i][yPos+i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[xCheck-i][yPos+i].isKing()) {
								return true;
							} else if (isWhite && board[xCheck-i][yPos+i].isPawn()) {
								return true;
							}
						}
					}
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// move down left
		for (int i = 1; i < 8; i++) {
			if (xCheck-i >= 0 && yPos-i >= 0) {
				if (board[xCheck-i][yPos-i] != null) {
					if (!sameColor(board[xCheck-i][yPos-i])) {
						if (board[xCheck-i][yPos-i].isQueen() || board[xCheck-i][yPos-i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[xCheck-i][yPos-i].isKing()) {
								return true;
							} else if (!isWhite && board[xCheck-i][yPos-i].isPawn()) {
								return true;
							}
						}
					}
					i = 1000;
				}
			} else {
				i = 1000;
			}
		}
		
		// knight moves
		if (xCheck < 6 && yPos < 7) {
			Piece square = board[xCheck+2][yPos+1];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck < 6 && yPos > 0) {
			Piece square = board[xCheck+2][yPos-1];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck < 7 && yPos < 6) {
			Piece square = board[xCheck+1][yPos+2];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck > 0 && yPos < 6) {
			Piece square = board[xCheck-1][yPos+2];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck > 1 && yPos < 7) {
			Piece square = board[xCheck-2][yPos+1];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck > 1 && yPos > 0) {
			Piece square = board[xCheck-2][yPos-1];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck < 7 && yPos > 1) {
			Piece square = board[xCheck+1][yPos-2];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (xCheck > 0 && yPos > 1) {
			Piece square = board[xCheck-1][yPos-2];
			if (square != null && !sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		
		return false;
	}
}
