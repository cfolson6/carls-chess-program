package application;

import java.util.Stack;

public class Engine {
	Game game;
	
	public Engine(Game g) {
		game = g;
	}
	
	Move getMove() {
		return rootAlphaBeta(6);
	}
	
	Move rootAlphaBeta(int depth) {
		int alpha = -999999999;
		int beta = 999999999;
		Move returnMove = null;
		
		Piece[] pieces;
		if (game.whiteToMove()) {
			pieces = game.whitePieces;
		} else {
			pieces = game.blackPieces;
		}
		
		for (int i = 0; i < 16; i++) {
			if (!pieces[i].captured) {
				Stack<Move> moves = game.getLegalMoves(pieces[i]);
				
				while (!moves.isEmpty()) {
					Move m = moves.pop();
					game.makeMove(m);
					int score = -alphaBeta(-beta, -alpha, depth-1);
					game.unmakeMove();
					if (score > alpha) {
						alpha = score;
						returnMove = m;
					}
				}
			}
		}
		
		return returnMove;
	}
	
	int alphaBeta(int alpha, int beta, int depth) {
		if (depth == 0) {
			return evaluate();
		}
		
		Piece[] pieces;
		if (game.whiteToMove()) {
			pieces = game.whitePieces;
		} else {
			pieces = game.blackPieces;
		}
		
		boolean hasMoves = false;
		
		for (int i = 0; i < 16; i++) {
			if (!pieces[i].captured) {
				Stack<Move> moves = game.getLegalMoves(pieces[i]);
				
				if (!moves.isEmpty()) {
					hasMoves = true;
				}
				
				while (!moves.isEmpty()) {
					game.makeMove(moves.pop());
					int score = -alphaBeta(-beta, -alpha, depth-1);
					game.unmakeMove();
					if (score >= beta) {
						return beta; // fail beta cutoff
					} else if (score > alpha) {
						alpha = score;
					}
				}
			}
		}
		
		// if we don't have any moves, it's either checkmate or stalemate
		if (!hasMoves) {
			if (game.inCheck(false)) {
				return -90000; // checkmate
			} else {
				return 0; // stalemate
			}
		}
		
		return alpha; // alpha is like max in regular minimax
	}
	
	int quiesce(int alpha, int beta) {
		int eval = evaluate();
		if (eval >= beta) {
			return beta;
		} else if (eval > alpha) {
			alpha = eval;
		}
		
		Piece[] pieces;
		if (game.whiteToMove()) {
			pieces = game.whitePieces;
		} else {
			pieces = game.blackPieces;
		}
		
		for (int i = 0; i < 16; i++) {
			if (!pieces[i].captured) {
				Stack<Move> moves = pieces[i].getMoves(game.board);
				
				while (!moves.isEmpty()) {
					Move m = moves.pop();
					
					//
					if (m.enPessant || game.board[m.endX][m.endY] != null) {
						game.makeMove(m);
						int score = -quiesce(-beta, -alpha);
						game.unmakeMove();
						if (score >= beta) {
							return beta;
						} else if (score > alpha) {
							alpha = score;
						}
					}
				}
			}
		}
		
		return alpha;
	}
	
	int evaluate() {
		int score = 0;
		
		for (int i = 0; i < 16; i++) {
			if (!game.whitePieces[i].captured) {
				score += game.whitePieces[i].value;
			}
			if (!game.blackPieces[i].captured) {
				score -= game.blackPieces[i].value;
			}
		}
		
		if (!game.whiteToMove()) {
			score *= -1;
		}
		
		return score;
	}
	
	void setGame(Game g) {
		game = g;
	}
}
