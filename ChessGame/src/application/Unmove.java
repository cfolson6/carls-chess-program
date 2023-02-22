package application;

// This class is a move that can be unmade.
// it contains things such as castling rights, en pessant rights, and the captured piece
public class Unmove {
	char capturedPieceLetter;
	int endX;
	int endY;
	int startX;
	int startY;
	boolean castle = false;
	boolean enPessant = false;
	boolean promote = false;
	
	// en pessant rights
	boolean[] whiteEnPessant;
	boolean[] blackEnPessant;
	
	// castling rights
	boolean whiteLeftRook = false;
	boolean whiteRightRook = false;
	boolean whiteKing = false;
	boolean blackLeftRook = false;
	boolean blackRightRook = false;
	boolean blackKing = false;
	
	// the board, whitePieces, and blackPieces should be from before the move was made
	public Unmove(Move move, Piece[][] board, Piece[] whitePieces, Piece[] blackPieces) {
		endX = move.endX;
		endY = move.endY;
		startX = move.startX;
		startY = move.startY;
		castle = move.castle;
		enPessant = move.enPessant;
		if (move.promoteQueen || move.promoteKnight || move.promoteRook || move.promoteBishop) {
			promote = true;
		}
		
		if (enPessant) {
			capturedPieceLetter = 'p';
		} else if (board[endX][endY] != null) {
			capturedPieceLetter = board[endX][endY].letter;
		} else {
			capturedPieceLetter = 'x'; // this means there is no captured piece
		}
		
		// en pessant stuff
		whiteEnPessant = new boolean[8];
		blackEnPessant = new boolean[8];
		for (int i = 0; i < 8; i++) {
			whiteEnPessant[i] = whitePieces[i].enPessantable;
			blackEnPessant[i] = blackPieces[i].enPessantable;
		}
		
		// castling stuff
		whiteLeftRook = whitePieces[10].hasMoved;
		whiteRightRook = whitePieces[11].hasMoved;
		whiteKing = whitePieces[15].hasMoved;
		blackLeftRook = blackPieces[10].hasMoved;
		blackRightRook = blackPieces[11].hasMoved;
		blackKing = blackPieces[15].hasMoved;
	}
}
