package application;
	
import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Main extends Application implements EventHandler<ActionEvent> {
	Group root;
	Scene scene;
	Stage stage;
	Game game;
	Stack<Move> clickMoves; // moves that can be clicked on after clicking a piece
	Rectangle[][] guiBoard;
	Rectangle[][] greenSquares;
	ImageView[] whiteSprites;
	ImageView[] blackSprites;
	Image spriteSheet;
	Rectangle[][] invisible;
	Text mateText;
	Button undo;
	Button switchSides;
	Engine engine;
	boolean engineIsWhite = false;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		scene = new Scene(root, 1000, 600, Color.rgb(50, 50, 50));
		stage = new Stage();
		
		game = new Game();
		engine = new Engine(game);
		
		Rectangle leftGray = new Rectangle();
		leftGray.setHeight(300);
		leftGray.setWidth(160);
		leftGray.relocate(20, 20);
		leftGray.setFill(Color.rgb(100, 100, 100));
		root.getChildren().add(leftGray);
		
		mateText = new Text(30, 75, "");
		mateText.setFill(Color.WHITE);
		root.getChildren().add(mateText);
		
		
		
		clickMoves = new Stack<Move>(); 
		
		// create the GUI chess board
		guiBoard =  new Rectangle[8][8];
		for (int x = 0; x <= 7; x++) {
			for (int y = 0; y <= 7; y++) {
				Rectangle current = new Rectangle();
				guiBoard[x][y] = current;
				current.setWidth(70);
				current.setHeight(70);
				current.setX(200 + (x * 70));
				current.setY(510 - (y * 70));
				
				if (x % 2 == y % 2) {
					current.setFill(Color.rgb(131, 99, 83)); // color for black squares
				} else {
					current.setFill(Color.rgb(253, 237, 208)); // color for white squares
				}
				
				root.getChildren().add(current); // add the current square to the root
			}
		}
		
		// create the translucent green squares which might appear when a square is clicked
		greenSquares = new Rectangle[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Rectangle current = new Rectangle();
				greenSquares[x][y] = current;
				current.setWidth(70);
				current.setHeight(70);
				current.setX(200 + (x * 70));
				current.setY(510 - (y * 70));
				current.setFill(Color.GREEN);
				current.setOpacity(0);
				root.getChildren().add(current); // add the current square to the root
			}
		}
		
		// create the sprites for the pieces
		spriteSheet = new Image("chess.png");
		whiteSprites = new ImageView[16];
		blackSprites = new ImageView[16];
		for (int i = 0; i < 16; i++) {
			ImageView w = new ImageView(spriteSheet);
			whiteSprites[i] = w;
			w.setFitWidth(60);
			w.setPreserveRatio(true);
			Piece wPiece = game.whitePieces[i];
			if (wPiece.isKing()) {
				Rectangle2D port = new Rectangle2D(0, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isQueen()) {
				Rectangle2D port = new Rectangle2D(200, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isBishop()) {
				Rectangle2D port = new Rectangle2D(400, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isKnight()) {
				Rectangle2D port = new Rectangle2D(600, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isRook()) {
				Rectangle2D port = new Rectangle2D(800, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isPawn()) {
				Rectangle2D port = new Rectangle2D(1000, 0, 200, 200);
				w.setViewport(port);
			}
			w.setX(205 + (wPiece.xPos * 70));
			w.setY(515 - (wPiece.yPos * 70));
			
			ImageView b = new ImageView(spriteSheet);
			blackSprites[i] = b;
			b.setFitWidth(60);
			b.setPreserveRatio(true);
			Piece bPiece = game.blackPieces[i];
			if (bPiece.isKing()) {
				Rectangle2D port = new Rectangle2D(0, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isQueen()) {
				Rectangle2D port = new Rectangle2D(200, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isBishop()) {
				Rectangle2D port = new Rectangle2D(400, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isKnight()) {
				Rectangle2D port = new Rectangle2D(600, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isRook()) {
				Rectangle2D port = new Rectangle2D(800, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isPawn()) {
				Rectangle2D port = new Rectangle2D(1000, 200, 200, 200);
				b.setViewport(port);
			}
			b.setX(205 + (bPiece.xPos * 70));
			b.setY(515 - (bPiece.yPos * 70));
			
			// add the piece sprites to the root
			root.getChildren().add(whiteSprites[i]);
			root.getChildren().add(blackSprites[i]);
		}
		
		// adding the invisible square buttons
		invisible = new Rectangle[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Rectangle current = new Rectangle();
				invisible[x][y] = current;
				current.setWidth(70);
				current.setHeight(70);
				current.setX(200 + (x * 70));
				current.setY(510 - (y * 70));
				current.setOpacity(0);
				
				// this mouse event is what handles the user clicking a square on the chessboard
				int x1 = x; // this is some dumb stuff so the compiler doesn't complain
				int y1 = y; // this too
				EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						clearGreenSquares();
						boolean moveMade = false;
						
						int xClick = x1;
						int yClick = y1;
						if (engineIsWhite) {
							xClick = 7 - x1;
							yClick = 7 - y1;
						}
						
						while (!clickMoves.isEmpty()) {
							Move m = clickMoves.pop();
							
							if (m.endX == xClick && m.endY == yClick) {
								moveMade = true;
								
								// handle promotion
								if (m.promoteQueen || m.promoteKnight || m.promoteRook || m.promoteBishop) {
									char choice = promotionChoice();
									
									m.promoteQueen = false;
									m.promoteKnight = false;
									m.promoteBishop = false;
									m.promoteRook = false;
									
									switch (choice) {
									case 'Q' : m.promoteQueen = true;
									break;
									case 'N' : m.promoteKnight = true;
									break;
									case 'R' : m.promoteRook = true;
									break;
									case 'B' : m.promoteBishop = true;
									break;
									default : m.promoteQueen = true;
									break;
									}
								}
								
								// make the move
								game.makeMove(m);
								placePieces();
								clickMoves.clear();
								
								// if the game isn't over, the engine makes a move
								if (!mateCheck()) {
									// the engine makes a move
									delay(100, () -> engineMove());
								}
							}
						}
						
						if (!moveMade) {
							Piece piece = game.board[xClick][yClick];
							if (piece != null && piece.isWhite == game.whiteToMove()) {
								greenSquares[x1][y1].setOpacity(0.5);
								
								Stack<Move> moves = game.getLegalMoves(piece);
								
								while (!moves.isEmpty()) {
									Move current = moves.pop();
									int endX = current.endX;
									int endY = current.endY;
									if (engineIsWhite) {
										endX = 7 - current.endX;
										endY = 7 - current.endY;
									}
									greenSquares[endX][endY].setOpacity(0.5);
									clickMoves.push(current);
								}
							}
						}
					}
				};
				current.addEventFilter(MouseEvent.MOUSE_CLICKED, clickHandler);
				
				root.getChildren().add(current); // add the current square to the root
			}
		}
		
		// add the undo move button
		undo = new Button("Undo Move");
		undo.relocate(850, 50);
		undo.setOnAction(this);
		root.getChildren().add(undo);
		
		// add switch sides button
		switchSides = new Button("Switch Sides");
		switchSides.relocate(850, 80);
		switchSides.setOnAction(this);
		root.getChildren().add(switchSides);
		
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == undo) {
			if (game.turn > 2) {
				game.unmakeMove();
				game.unmakeMove();
				clickMoves.clear();
				placePieces();
				clearGreenSquares();
				mateCheck();
			}
		} else if (event.getSource() == switchSides) {
			if (!mateCheck()) {
				engineIsWhite = !engineIsWhite;
				clickMoves.clear();
				clearGreenSquares();
				placePieces();
				delay(100, () -> engineMove());
			}
		}
	}
	
	// returns true if the game is over. Also does the check and mate displays
	public boolean mateCheck() {
		// check for check and checkmate/stalemate
		if (game.noLegalMoves()) {
			if (game.inCheck(false)) {
				if (game.whiteToMove()) {
					mateText.setText("CheckMate. Black wins.");
				} else {
					mateText.setText("CheckMate. White wins.");
				}
			} else {
				mateText.setText("Stalemate. Draw.");
			}
			
			return true;
		} else {
			if (game.inCheck(false)) {
				mateText.setText("Check.");
			} else {
				mateText.setText("");
			}
			
			return false;
		}
	}
	
	public void engineMove() {
		engine.setGame(new Game(game));
		game.makeMove(engine.getMove());
		placePieces();
		clickMoves.clear();
		mateCheck();
	}
	
	public void clearGreenSquares() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				greenSquares[x][y].setOpacity(0);
				greenSquares[x][y].setFill(Color.GREEN);
			}
		}
	}
	
	public void placePieces() {
		for (int i = 0; i < 16; i++) {
			ImageView w = whiteSprites[i];
			w.setFitWidth(60);
			w.setPreserveRatio(true);
			w.setOpacity(1);
			Piece wPiece = game.whitePieces[i];
			if (wPiece.captured) {
				w.setOpacity(0);
				w.setFitWidth(1);
			} else if (wPiece.isKing()) {
				Rectangle2D port = new Rectangle2D(0, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isQueen()) {
				Rectangle2D port = new Rectangle2D(200, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isBishop()) {
				Rectangle2D port = new Rectangle2D(400, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isKnight()) {
				Rectangle2D port = new Rectangle2D(600, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isRook()) {
				Rectangle2D port = new Rectangle2D(800, 0, 200, 200);
				w.setViewport(port);
			} else if (wPiece.isPawn()) {
				Rectangle2D port = new Rectangle2D(1000, 0, 200, 200);
				w.setViewport(port);
			}
			int adjustedWX = wPiece.xPos;
			int adjustedWY = wPiece.yPos;
			if (engineIsWhite) {
				adjustedWX = 7 - wPiece.xPos;
				adjustedWY = 7 - wPiece.yPos;
			}
			w.setX(205 + (adjustedWX * 70));
			w.setY(515 - (adjustedWY * 70));
			
			ImageView b = blackSprites[i];
			b.setFitWidth(60);
			b.setPreserveRatio(true);
			b.setOpacity(1);
			Piece bPiece = game.blackPieces[i];
			if (bPiece.captured) {
				b.setOpacity(0);
				b.setFitWidth(1);
			} else if (bPiece.isKing()) {
				Rectangle2D port = new Rectangle2D(0, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isQueen()) {
				Rectangle2D port = new Rectangle2D(200, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isBishop()) {
				Rectangle2D port = new Rectangle2D(400, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isKnight()) {
				Rectangle2D port = new Rectangle2D(600, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isRook()) {
				Rectangle2D port = new Rectangle2D(800, 200, 200, 200);
				b.setViewport(port);
			} else if (bPiece.isPawn()) {
				Rectangle2D port = new Rectangle2D(1000, 200, 200, 200);
				b.setViewport(port);
			}
			int adjustedBX = bPiece.xPos;
			int adjustedBY = bPiece.yPos;
			if (engineIsWhite) {
				adjustedBX = 7 - bPiece.xPos;
				adjustedBY = 7 - bPiece.yPos;
			}
			b.setX(205 + (adjustedBX * 70));
			b.setY(515 - (adjustedBY * 70));
		}
	}
	
	public char promotionChoice() {
		ChoiceDialog<String> d = new ChoiceDialog<String>("Queen", "Queen", "Knight", "Rook", "Bishop");
		
		d.setTitle("Promotion");
		d.setContentText("Which piece would you like to promote into?");
		d.showAndWait();
		String s = d.getSelectedItem();
		
		if (s.contentEquals("Queen")) {
			return 'Q';
		} else if (s.contentEquals("Knight")) {
			return 'N';
		} else if (s.contentEquals("Rook")) {
			return 'R';
		} else if (s.contentEquals("Bishop")) {
			return 'B';
		}
		
		return 'Q';
	}
	
	public static void delay(long millis, Runnable continuation) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try { Thread.sleep(millis); }
	            catch (InterruptedException e) { }
	            return null;
	        }
	    };
	    sleeper.setOnSucceeded(event -> continuation.run());
	    new Thread(sleeper).start();
	}
}
