package application;

import java.util.Stack;

// contains the current state of the game
public class Game {
	Piece[][] board = new Piece[8][8]; // the board is represented as an 8 by 8 array of pieces
	
	Piece[] whitePieces = new Piece[16]; // king is always at 15
	Piece[] blackPieces = new Piece[16]; // king is always at 15
	
	int turn = 1; // the turn number
	
	Stack<Unmove> prevMoves = new Stack<Unmove>(); // the previous moves, which can be unmade
	
	// this constructor creates a game with the default starting position
	public Game() {
		// make the pawns
		for (int i = 0; i < 8; i++) {
			whitePieces[i] = new Pawn(i, 1, true);
			blackPieces[i] = new Pawn(i, 6, false);
		}
		
		// make knights
		whitePieces[8] = new Knight(1, 0, true);
		whitePieces[9] = new Knight(6, 0, true);
		blackPieces[8] = new Knight(1, 7, false);
		blackPieces[9] = new Knight(6, 7, false);
		
		// make rooks
		whitePieces[10] = new Rook(0, 0, true);
		whitePieces[11] = new Rook(7, 0, true);
		blackPieces[10] = new Rook(0, 7, false);
		blackPieces[11] = new Rook(7, 7, false);
		
		// make bishops
		whitePieces[12] = new Bishop(2, 0, true);
		whitePieces[13] = new Bishop(5, 0, true);
		blackPieces[12] = new Bishop(2, 7, false);
		blackPieces[13] = new Bishop(5, 7, false);
		
		// make queens
		whitePieces[14] = new Queen(3, 0, true);
		blackPieces[14] = new Queen(3, 7, false);
		
		// make kings
		whitePieces[15] = new King(4, 0, true);
		blackPieces[15] = new King(4, 7, false);
		
		// attach pieces to board
		for (int i = 0; i < 16; i++) {
			Piece w = whitePieces[i];
			board[w.xPos][w.yPos] = w;
			Piece b = blackPieces[i];
			board[b.xPos][b.yPos] = b;
		}
		
		turn = 1;
	}
	
	// this constructor creates an exact copy of another game, with all new objects (pieces)
	public Game(Game g) {
		// copy pieces and attach them to the board
		for (int i = 0; i < 16; i++) {
			whitePieces[i] = g.whitePieces[i].deepCopy();
			Piece w = whitePieces[i];
			if (!w.captured) {
				board[w.xPos][w.yPos] = w;
			}
			blackPieces[i] = g.blackPieces[i].deepCopy();
			Piece b = blackPieces[i];
			if (!b.captured) {
				board[b.xPos][b.yPos] = b;
			}
		}
		
		// replicate the previous moves stack
		Stack<Unmove> temp = new Stack<Unmove>();
		while (!g.prevMoves.isEmpty()) {
			temp.push(g.prevMoves.pop());
		}
		while (!temp.isEmpty()) {
			Unmove u = temp.pop();
			g.prevMoves.push(u);
			prevMoves.push(u);
		}
		
		turn = g.turn;
	}
	
	public void makeMove(Move move) {
		prevMoves.push(new Unmove(move, board, whitePieces, blackPieces)); // add this move to the previous moves stack
		
		if (board[move.endX][move.endY] != null) {
			board[move.endX][move.endY].captured = true; // if there is a piece here, capture it
		}
		
		Piece piece = board[move.startX][move.startY];
		
		// move piece to the new square
		piece.xPos = move.endX;
		piece.yPos = move.endY;
		board[move.startX][move.startY] = null;
		board[move.endX][move.endY] = piece;
		piece.hasMoved = true;
		
		// if move was en Pessant, capture the en Pessanted piece
		if (move.enPessant) {
			board[move.endX][move.startY].captured = true;
			board[move.endX][move.startY] = null;
		}
		
		// if move was castle, move rook
		if (move.castle) {
			if (move.endX > move.startX) {
				board[7][move.endY].xPos = 5;
				board[5][move.endY] = board[7][move.endY];
				board[7][move.endY] = null;
			} else {
				board[0][move.endY].xPos = 3;
				board[3][move.endY] = board[0][move.endY];
				board[0][move.endY] = null;
			}
		}
			
		// handle promotion
		if (move.promoteQueen || move.promoteKnight || move.promoteRook || move.promoteBishop) {
			// we need to find the piece in the pieces array
			int pieceIndex = 0;
			if (whiteToMove()) {
				for (int i = 0; i <= 15; i++) {
					if (whitePieces[i] == piece) {
						pieceIndex = i;
						i = 1000; // exit for loop
					}
				}
			} else {
				for (int i = 0; i <= 15; i++) {
					if (blackPieces[i] == piece) {
						pieceIndex = i;
						i = 1000; // exit for loop
					}
				}
			}
			
			if (move.promoteQueen) {
				piece = new Queen(piece.xPos, piece.yPos, piece.isWhite);
			} else if (move.promoteKnight) {
				piece = new Knight(piece.xPos, piece.yPos, piece.isWhite);
			} else if (move.promoteRook) {
				piece = new Rook(piece.xPos, piece.yPos, piece.isWhite);
			} else if (move.promoteBishop) {
				piece = new Bishop(piece.xPos, piece.yPos, piece.isWhite);
			}
			
			piece.hasMoved = true;
			board[piece.xPos][piece.yPos] = piece;
			if (whiteToMove()) {
				whitePieces[pieceIndex] = piece;
			} else {
				blackPieces[pieceIndex] = piece;
			}
		}
		
		if (piece.isPawn() && Math.abs(move.startY - move.endY) == 2) {
			piece.enPessantable = true;
		}
		
		turn++; // increase turn count
		
		// unmark any enPessantable pieces on opposing side; this is how we keep track of en Pessant
		if (whiteToMove()) {
			for (int i = 0; i < 16; i++) {
				whitePieces[i].enPessantable = false;
			}
		} else {
			for (int i = 0; i < 16; i++) {
				blackPieces[i].enPessantable = false;
			}
		}
	}
	
	public void unmakeMove() {
		Unmove unmove = prevMoves.pop();
		
		Piece piece = board[unmove.endX][unmove.endY];
		
		// move the piece back to the old square
		piece.xPos = unmove.startX;
		piece.yPos = unmove.startY;
		board[unmove.endX][unmove.endY] = null;
		board[unmove.startX][unmove.startY] = piece;
		
		// if there's a captured piece, move it back to its old square
		if (unmove.capturedPieceLetter != 'x') {
			for (int i = 0; i < 16; i++) {
				Piece p;
				if (whiteToMove()) {
					p = whitePieces[i];
				} else {
					p = blackPieces[i];
				}
				if (p.captured && p.letter == unmove.capturedPieceLetter) {
					if (unmove.enPessant) {
						if (p.xPos == unmove.endX && p.yPos == unmove.startY) {
							p.captured = false;
							board[unmove.endX][unmove.startY] = p;
							break;
						}
					} else {
						if (p.xPos == unmove.endX && p.yPos == unmove.endY) {
							p.captured = false;
							board[unmove.endX][unmove.endY] = p;
							break;
						}
					}
				}
			}
		}
		
		// handle promotion and castling
		if (unmove.promote) {
			// we have to find our piece in the piece array
			int pieceIndex = 0;
			if (!whiteToMove()) {
				for (int i = 0; i <= 15; i++) {
					if (whitePieces[i] == piece) {
						pieceIndex = i;
						break;
					}
				}
			} else {
				for (int i = 0; i <= 15; i++) {
					if (blackPieces[i] == piece) {
						pieceIndex = i;
						break;
					}
				}
			}
			
			piece = new Pawn(piece.xPos, piece.yPos, piece.isWhite);
			board[piece.xPos][piece.yPos] = piece;
			if (!whiteToMove()) {
				whitePieces[pieceIndex] = piece;
			} else {
				blackPieces[pieceIndex] = piece;
			}
		} else if (unmove.castle) {
			if (unmove.endX > unmove.startX) {
				board[5][unmove.endY].xPos = 7;
				board[7][unmove.endY] = board[5][unmove.endY];
				board[5][unmove.endY] = null;
			} else {
				board[3][unmove.endY].xPos = 0;
				board[0][unmove.endY] = board[3][unmove.endY];
				board[3][unmove.endY] = null;
			}
		}
		
		// restore enPessantability
		for (int i = 0; i < 8; i++) {
			whitePieces[i].enPessantable = unmove.whiteEnPessant[i];
			blackPieces[i].enPessantable = unmove.blackEnPessant[i];
		}
		
		// restore castling rights
		whitePieces[10].hasMoved = unmove.whiteLeftRook;
		whitePieces[11].hasMoved = unmove.whiteRightRook;
		whitePieces[15].hasMoved = unmove.whiteKing;
		blackPieces[10].hasMoved = unmove.blackLeftRook;
		blackPieces[11].hasMoved = unmove.blackRightRook;
		blackPieces[15].hasMoved = unmove.blackKing;
		
		turn--;
	}
	
	public Stack<Move> getLegalMoves(Piece p) {
		Stack<Move> moves = p.getMoves(board); // the pseudo-legal moves of a piece
		Stack<Move> legalMoves = new Stack<Move>();
		
		while (!moves.isEmpty()) {
			Move m = moves.pop();
			
			makeMove(m);
			
			if (!inCheck(true)) {
				legalMoves.push(m);
			}
			
			unmakeMove();
		}
		
		return legalMoves;
	}
	
	
	// pseudoLegal is true if we just made a pseudo-legal move and we want to see if it's legal
	// pseudoLegal is false if we're just checking if the side that's about to move is in check
	public boolean inCheck(boolean pseudoLegal) {
		Piece king;
		
		if (pseudoLegal) {
			if (whiteToMove()) {
				king = blackPieces[15];
			} else {
				king = whitePieces[15];
			}
		} else {
			if (whiteToMove()) {
				king = whitePieces[15];
			} else {
				king = blackPieces[15];
			}
		}
		
		// move right
		for (int x = king.xPos+1; x <= 7; x++) {
			if (board[x][king.yPos] != null) {
				if (!king.sameColor(board[x][king.yPos])) {
					if (board[x][king.yPos].isQueen() || board[x][king.yPos].isRook()) {
						return true;
					} else if (Math.abs(x - king.xPos) == 1 && board[x][king.yPos].isKing()) {
						return true;
					}
				}
				x = 1000; // exit for loop
			}
		}
		
		// move left
		for (int x = king.xPos-1; x >= 0; x--) {
			if (board[x][king.yPos] != null) {
				if (!king.sameColor(board[x][king.yPos])) {
					if (board[x][king.yPos].isQueen() || board[x][king.yPos].isRook()) {
						return true;
					} else if (Math.abs(x - king.xPos) == 1 && board[x][king.yPos].isKing()) {
						return true;
					}
				}
				x = -1000; // exit for loop
			}
		}
		
		// move up
		for (int y = king.yPos+1; y <= 7; y++) {
			if (board[king.xPos][y] != null) {
				if (!king.sameColor(board[king.xPos][y])) {
					if (board[king.xPos][y].isQueen() || board[king.xPos][y].isRook()) {
						return true;
					} else if (Math.abs(y - king.yPos) == 1 && board[king.xPos][y].isKing()) {
						return true;
					}
				}
				y = 1000;
			}
		}
		
		// move down
		for (int y = king.yPos-1; y >= 0; y--) {
			if (board[king.xPos][y] != null) {
				if (!king.sameColor(board[king.xPos][y])) {
					if (board[king.xPos][y].isQueen() || board[king.xPos][y].isRook()) {
						return true;
					} else if (Math.abs(y - king.yPos) == 1 && board[king.xPos][y].isKing()) {
						return true;
					}
				}
				y = -1000;
			}
		}
		
		// move up right
		for (int i = 1; i < 8; i++) {
			if (king.xPos+i <= 7 && king.yPos+i <= 7) {
				if (board[king.xPos+i][king.yPos+i] != null) {
					if (!king.sameColor(board[king.xPos+i][king.yPos+i])) {
						if (board[king.xPos+i][king.yPos+i].isQueen() || board[king.xPos+i][king.yPos+i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[king.xPos+i][king.yPos+i].isKing()) {
								return true;
							} else if (king.isWhite && board[king.xPos+i][king.yPos+i].isPawn()) {
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
			if (king.xPos+i <= 7 && king.yPos-i >= 0) {
				if (board[king.xPos+i][king.yPos-i] != null) {
					if (!king.sameColor(board[king.xPos+i][king.yPos-i])) {
						if (board[king.xPos+i][king.yPos-i].isQueen() || board[king.xPos+i][king.yPos-i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[king.xPos+i][king.yPos-i].isKing()) {
								return true;
							} else if (!king.isWhite && board[king.xPos+i][king.yPos-i].isPawn()) {
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
			if (king.xPos-i >= 0 && king.yPos+i <= 7) {
				if (board[king.xPos-i][king.yPos+i] != null) {
					if (!king.sameColor(board[king.xPos-i][king.yPos+i])) {
						if (board[king.xPos-i][king.yPos+i].isQueen() || board[king.xPos-i][king.yPos+i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[king.xPos-i][king.yPos+i].isKing()) {
								return true;
							} else if (king.isWhite && board[king.xPos-i][king.yPos+i].isPawn()) {
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
			if (king.xPos-i >= 0 && king.yPos-i >= 0) {
				if (board[king.xPos-i][king.yPos-i] != null) {
					if (!king.sameColor(board[king.xPos-i][king.yPos-i])) {
						if (board[king.xPos-i][king.yPos-i].isQueen() || board[king.xPos-i][king.yPos-i].isBishop()) {
							return true;
						} else if (i == 1) {
							if (board[king.xPos-i][king.yPos-i].isKing()) {
								return true;
							} else if (!king.isWhite && board[king.xPos-i][king.yPos-i].isPawn()) {
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
		if (king.xPos < 6 && king.yPos < 7) {
			Piece square = board[king.xPos+2][king.yPos+1];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos < 6 && king.yPos > 0) {
			Piece square = board[king.xPos+2][king.yPos-1];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos < 7 && king.yPos < 6) {
			Piece square = board[king.xPos+1][king.yPos+2];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos > 0 && king.yPos < 6) {
			Piece square = board[king.xPos-1][king.yPos+2];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos > 1 && king.yPos < 7) {
			Piece square = board[king.xPos-2][king.yPos+1];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos > 1 && king.yPos > 0) {
			Piece square = board[king.xPos-2][king.yPos-1];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos < 7 && king.yPos > 1) {
			Piece square = board[king.xPos+1][king.yPos-2];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		if (king.xPos > 0 && king.yPos > 1) {
			Piece square = board[king.xPos-1][king.yPos-2];
			if (square != null && !king.sameColor(square) && square.isKnight()) {
				return true;
			}
		}
		
		return false; // we've checked everything and we're not in check
	}
	
	public boolean noLegalMoves() {
		if (whiteToMove()) {
			for (int i = 0; i <= 15; i++) {
				if (!whitePieces[i].captured) {
					Stack<Move> legalMoves = getLegalMoves(whitePieces[i]);
					if (!legalMoves.isEmpty()) {
						return false; // we have legal moves
					}
				}
			}
			return true; // we don't have legal moves
		} else {
			for (int i = 0; i <= 15; i++) {
				if (!blackPieces[i].captured) {
					Stack<Move> legalMoves = getLegalMoves(blackPieces[i]);
					if (!legalMoves.isEmpty()) {
						return false; // we have legal moves
					}
				}
			}
			return true; // we don't have legal moves
		}
	}
	
	public boolean whiteToMove() {
		return turn % 2 == 1;
	}
}
