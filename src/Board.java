import java.util.ArrayList;

public class Board {

    private final int BOARD_SIZE = 8;
    private final int BOARD_SIZE_PIXELS = 700;
    private final Tile[][] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                tiles[i][j] = new Tile((i + j) % 2 == 0, (double) BOARD_SIZE_PIXELS / BOARD_SIZE);
            }
        }
    }

    /**
     * Gets valid moves from a given piece. Not necessary legal moves.
     * @param rank of piece to get moves from
     * @param file  of piece to get moves from
     * @return valid moves
     * @throws BoardIndexOutOfBoundsException if out of bounds
     */
    public ArrayList<Move> getValidMoves(int rank, int file) throws BoardIndexOutOfBoundsException {
        if (!isValidPosition(rank, file)) throw new BoardIndexOutOfBoundsException();
        ArrayList<Move> moves = new ArrayList<>();
        Piece piece = tiles[rank][file].getPiece();
        if (piece == null) return moves;
        return piece.getBasicMoves(rank, file, (move) -> {
            if (!this.isValidPosition(move.rank, move.file)) {
                return MoveStatus.INVALID;
            }
            if (this.isEmptyPosition(move.rank, move.file)) {
                return MoveStatus.VALID;
            }
            if (this.isOccupiedByFriendly(piece.isWhite(), move.rank, move.file)) {
                return MoveStatus.INVALID;
            }
            if (this.isOccupiedByEnemy(piece.isWhite(), move.rank, move.file)) {
                return MoveStatus.VALID_STOP;
            }
            return MoveStatus.VALID;
        });
    }

    /**
     * Sets a given position with a piece. Can be null value.
     * @param file to place piece
     * @param rank to place piece
     * @param piece to place
     * @throws BoardIndexOutOfBoundsException if out of bounds
     */
    protected void setPieceAtPosition(int rank, int file, Piece piece) throws BoardIndexOutOfBoundsException {
        if (!isValidPosition(rank, file)) throw new BoardIndexOutOfBoundsException();
        tiles[rank][file].setPiece(piece);
    }

    /**
     * Gets Piece from given position.
     * @param rank of position
     * @param file of position
     * @return Piece or null if none exist on given position
     * @throws BoardIndexOutOfBoundsException if out of bounds
     */
    protected Piece getPieceAtPosition(int rank, int file) throws BoardIndexOutOfBoundsException {
        if (!isValidPosition(rank, file)) throw new BoardIndexOutOfBoundsException();
        return tiles[rank][file].getPiece();
    }

    /**
     * Evaluates if a given postion is occupied by any piece.
     * @param rank of position to check
     * @param file of position to check
     * @return true if empty, false if not
     */
    private boolean isEmptyPosition(int rank, int file) {
        return tiles[rank][file] == null;
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
