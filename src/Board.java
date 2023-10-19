import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board extends GridPane {

    public final int BOARD_SIZE = 8;
    //private GameManager gm;
    private final int BOARD_SIZE_PIXELS = 700;
    private final Tile[][] tiles;
    private Move selectedPosition;
    private final ArrayList<Tile> highlightedTiles = new ArrayList<>();

    public Board(GMOnClickHandler clickHandler) {
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile tile = new Tile((i + j) % 2 == 0, (double) BOARD_SIZE_PIXELS / BOARD_SIZE);
                int finalI = i;
                int finalJ = j;
                tile.setOnMouseClicked((event) -> {
                    clickHandler.handleClick(tile, finalJ, finalI);
                });
                tiles[j][i] = tile;
                this.add(tile, i, j);
            }
        }
    }

    /**
     * Visually rotates board 180 degrees.
     */
    public void rotateBoard() {
        // Create an array to temporarily store the children
        Node[][] nodes = new Node[8][8];

        // Fetch the children into the temporary array
        for (Node node : this.getChildren()) {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            nodes[row][col] = node;
        }

        // Remove all children from the GridPane
        this.getChildren().clear();

        // Add them back in the reverse order
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Node node = nodes[row][col];
                this.add(node, BOARD_SIZE - 1 - col, BOARD_SIZE - 1 - row);
            }
        }
    }

    /**
     * Gets valid moves from a given piece, including checking game rules via the game manager.
     * @param rank of piece to get moves from
     * @param file  of piece to get moves from
     * @return valid moves
     */
    public ArrayList<Move> getBoardValidMoves(int rank, int file) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece piece = tiles[rank][file].getPiece();
        if (piece == null) return moves;
        moves = piece.getBasicMoves(rank, file, this::boardValidate);
        return moves;
    }

    private MoveStatus boardValidate(boolean isWhite, Move move) {
        if (!this.isValidPosition(move.rank, move.file)) {
            return MoveStatus.INVALID;
        }
        if (this.isEmptyPosition(move.rank, move.file)) {
            return MoveStatus.VALID;
        }
        if (this.isOccupiedByFriendly(isWhite, move.rank, move.file)) {
            return MoveStatus.INVALID;
        }
        if (this.isOccupiedByEnemy(isWhite, move.rank, move.file)) {
            return MoveStatus.VALID_STOP;
        }
        return MoveStatus.VALID;
    }

    /**
     * Sets selected position.
     * @param selectedPosition to select
     */
    public void setSelectedPosition(Move selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    /**
     * Gets selected position.
     * @return selected position
     */
    public Move getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Gets active tiles.
     * @return active tiles
     */
    public ArrayList<Tile> getHighlightedTiles() {
        return highlightedTiles;
    }

    /**
     * Adds a tile to highlightedTiles.
     * @param tile to add to highlightedTiles
     */
    public void addTileToHighlighted(Tile tile) {
        highlightedTiles.add(tile);
    }

    public void clearHighlightedTiles() {
        highlightedTiles.clear();
    }

    public boolean highlightedTilesContains(Tile tile) {
        return highlightedTiles.contains(tile);
    }

    /**
     * Toggles highlighted tiles.
     */
    public void toggleHighlightedTiles() {
        for (Tile tile : highlightedTiles) {
            tile.toggleHighlight();
        }
    }

    /**
     * Gets tile from tiles.
     * @param rank of tile
     * @param file of tile
     * @return tile
     */
    public Tile getTileAt(int rank, int file) {
        return tiles[rank][file];
    }

    public Tile getTileAt(Move move) {
        return tiles[move.rank][move.file];
    }

    /**
     * Sets a given position with a piece. Can be null value.
     * @param file to place piece
     * @param rank to place piece
     * @param piece to place
     */
    protected void setPieceAtPosition(int rank, int file, Piece piece) {
        tiles[rank][file].setPiece(piece);
    }

    /**
     * Gets Piece from given position.
     * @param rank of position
     * @param file of position
     * @return Piece or null if none exist on given position
     */
    protected Piece getPieceAtPosition(int rank, int file) {
        return tiles[rank][file].getPiece();
    }

    /**
     * Evaluates if a given postion is occupied by any piece.
     * @param rank of position to check
     * @param file of position to check
     * @return true if empty, false if not
     */
    private boolean isEmptyPosition(int rank, int file) {
        return tiles[rank][file].getPiece() == null;
    }

    /**
     * Evaluates if a given position is occupied by an enemy piece.
     * @param isWhite color of piece inquiring
     * @param rank of piece to check
     * @param file of piece to check
     * @return true if enemy, false if not
     */
    private boolean isOccupiedByEnemy(boolean isWhite, int rank, int file){
        return tiles[rank][file].getPiece().isWhite() != isWhite;
    }

    /**
     * Evaluates if a given position is occupied by a friendly piece.
     * @param isWhite color of piece inquiring
     * @param rank of piece to check
     * @param file of piece to check
     * @return true if friendly, false if not
     */
    private boolean isOccupiedByFriendly(boolean isWhite, int rank, int file){
        return tiles[rank][file].getPiece().isWhite() == isWhite;
    }

    /**
     * Evaluates if a given position is valid on this board (i.e. not out of bounds).
     * @param rank to check
     * @param file to check
     * @return true if valid, false if not
     */
    private boolean isValidPosition(int rank, int file) {
        return rank < BOARD_SIZE && rank >= 0 && file < BOARD_SIZE && file >= 0;
    }

    /**
     * Draws the board using UNICODE characters.
     */
    protected void consoleDraw(boolean isWhiteToPlay) {
        System.out.println("    -------------------------------");
        if (isWhiteToPlay) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                System.out.printf(" %d ", BOARD_SIZE - i);
                for (int j = 0; j < BOARD_SIZE; j++) {
                    Piece piece = tiles[i][j].getPiece();
                    System.out.printf("| %c ", piece != null ? piece.getUnicode() : ' ');
                }
                System.out.print("|\n");
                System.out.println(i == BOARD_SIZE - 1 ? "    -------------------------------" : "   |---+---+---+---+---+---+---+---|");
            }
        } else {
            for (int i = BOARD_SIZE - 1; i >= 0; i--) {
                System.out.printf(" %d ", BOARD_SIZE - i);
                for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                    Piece piece = tiles[i][j].getPiece();
                    System.out.printf("| %c ", piece != null ? piece.getUnicode() : ' ');
                }
                System.out.print("|\n");
                System.out.println(i == BOARD_SIZE - 1 ? "    -------------------------------" : "   |---+---+---+---+---+---+---+---|");
            }
        }
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("   %c", isWhiteToPlay ? 'a' + i : 'h' - i);
        }
        System.out.println();
    }
}
