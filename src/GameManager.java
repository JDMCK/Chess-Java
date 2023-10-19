import java.util.ArrayList;

public class GameManager {

    Board board;
    private boolean isWhiteToPlay;
    private int whiteMaterialScore;
    private int blackMaterialScore;
    private String castle;
    private String enPassant;
    private int halfMoves;
    private int fullMoves;

    /**
     * Constructor for new GameManager instantiates a new board and sets it do the default chess starting position.
     */

    public GameManager() {
        board = new Board(this::handleClick);
        setFENState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        whiteMaterialScore = 0;
        blackMaterialScore = 0;
    }

    /**
     * Sets current state of board (position and other game states) using a FEN String.
     * @param fenString A board state represented as a FEN String
     */
    public final void setFENState(String fenString) {
        int i = 0;
        int j = 0;
        String[] fenFields = fenString.split(" ");
        if (fenFields.length != 6)
            throw new IllegalArgumentException("Illegal FEN String given.");
        char[] fenArray = fenFields[0].toCharArray();
        for (char c : fenArray) {
            switch (c) {
                case 'r' -> {
                    board.setPieceAtPosition(i, j, new Rook(false));
                    j++;
                }
                case 'n' -> {
                    board.setPieceAtPosition(i, j, new Knight(false));
                    j++;
                }
                case 'b' -> {
                    board.setPieceAtPosition(i, j, new Bishop(false));
                    j++;
                }
                case 'q' -> {
                    board.setPieceAtPosition(i, j, new Queen(false));
                    j++;
                }
                case 'k' -> {
                    board.setPieceAtPosition(i, j, new King(false));
                    j++;
                }
                case 'p' -> {
                    board.setPieceAtPosition(i, j, new Pawn(false));
                    j++;
                }
                case 'R' -> {
                    board.setPieceAtPosition(i, j, new Rook(true));
                    j++;
                }
                case 'N' -> {
                    board.setPieceAtPosition(i, j, new Knight(true));
                    j++;
                }
                case 'B' -> {
                    board.setPieceAtPosition(i, j, new Bishop(true));
                    j++;
                }
                case 'Q' -> {
                    board.setPieceAtPosition(i, j, new Queen(true));
                    j++;
                }
                case 'K' -> {
                    board.setPieceAtPosition(i, j, new King(true));
                    j++;
                }
                case 'P' -> {
                    board.setPieceAtPosition(i, j, new Pawn(true));
                    j++;
                }
                case '/' -> {
                    i++;
                    j = 0;
                }
                default -> {
                    j += (c - '0');
                }
            }
        }
        isWhiteToPlay = fenFields[1].equals("w");
        castle = fenFields[2];
        enPassant = fenFields[3];
        halfMoves = Integer.parseInt(fenFields[4]);
        fullMoves = Integer.parseInt(fenFields[5]);
    }

    /**
     * Draws board on the console using UNICODE.
     */
    public void consoleDraw() {
        board.consoleDraw(isWhiteToPlay);
    }

    /**
     * Gets the board of this game manager.
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Validates board suggested moves based on game rules.
     * @param piece selected to move
     * @param rank of piece
     * @param file of piece
     * @param move to validate
     * @return VALID if valid, INVALID if not
     */
    private MoveStatus validate(Piece piece, int rank, int file, Move move) {
        if (piece.isWhite() != isWhiteToPlay) return MoveStatus.INVALID;
        switch (piece.getName()) {
            case "pawn": return validatePawn(piece.isWhite(), rank, file, move);
            case "king": return validateKing(piece.isWhite(), rank, file, move);
            default: return MoveStatus.VALID;
        }
    }

    private MoveStatus validatePawn(boolean isWhite, int rank, int file, Move move) {
        if (move.file == file) {
            return MoveStatus.VALID;
        }
        if ((isWhite ? rank == 3 : rank == board.BOARD_SIZE - 4) && indToAlg(move.rank, move.file).equals(enPassant)) {
            return MoveStatus.VALID;
        }
        Piece targetPiece = board.getPieceAtPosition(move.rank, move.file);
        if (targetPiece != null && targetPiece.isWhite() != isWhite) {
            return MoveStatus.VALID;
        }
        return MoveStatus.INVALID;
    }

    /**
     * Validates all moves (including castling) as an option for a potential king move.
     * @param isWhite kings color
     * @param rank of king
     * @param file of king
     * @param move potential move to consider
     * @return Move status based on the move to consider
     */
    private MoveStatus validateKing(boolean isWhite, int rank, int file, Move move) {
        if (Math.abs(rank - move.rank) <= 1 && Math.abs(file - move.file) <= 1) {
            return MoveStatus.VALID;
        }
        if (move.file > file) {
            if ((isWhite && !castle.contains("K")) || (!isWhite && !castle.contains("k"))) {
                return MoveStatus.INVALID;
            }
            for (int i = file + 1; i < board.BOARD_SIZE - 1; i++) {
                if (board.getPieceAtPosition(rank, i) != null) return MoveStatus.INVALID;
            }
        } else {
            if ((isWhite && !castle.contains("Q")) || (!isWhite && !castle.contains("q"))) {
                return MoveStatus.INVALID;
            }
            for (int i = file - 1; i >= 1; i--) {
                if (board.getPieceAtPosition(rank, i) != null) return MoveStatus.INVALID;
            }
        }
            return MoveStatus.VALID;
    }

    /**
     * Handles special cases like en passant, castling, and pawn promoting.
     * @param rank of currently selected piece
     * @param file of currently selected piece
     * @param move where the piece is going
     * @return true if given move is a special case, false if not
     */
    private boolean handleSpecialCase(int rank, int file, Move move) {
        Piece piece = board.getPieceAtPosition(rank, file);
        switch (piece.getName()) {
            case "pawn": {
                if (enPassant.equals("-")) return false;
                Move enPassantAsMove = getEnPassantAsMove();
                if (enPassantAsMove.equals(move)) {
                    handleEnPassant(rank, file);
                    return true;
                }
                break;
            }
            case "king": {
                if (Math.abs(move.file - file) == 2) {
                    handleCastle(rank, file, move.file - file == 2);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Moves pawn in an en passant pattern.
     * @param rank of pawn moving
     * @param file of pawn moving
     */
    private void handleEnPassant(int rank, int file) {
        Move move = getEnPassantAsMove();
        Piece pawn = board.getPieceAtPosition(rank, file);
        if (rank == 3) {
            board.setPieceAtPosition(3, move.file, null);
        } else {
            board.setPieceAtPosition(board.BOARD_SIZE - 4, move.file, null);
        }
        updateMaterialScore(true, pawn.getMaterialScore());
        executeMove(board.getTileAt(move.rank, move.file), board.getTileAt(rank, file));
    }

    /**
     * Moves king and rook in a castling pattern.
     * @param rank of king
     * @param file of king
     * @param isKingSide is castling on king or queen side
     */
    private void handleCastle(int rank, int file, boolean isKingSide) {
        int boardSize = board.BOARD_SIZE;
        int rookFile;
        int newKingFile;
        int newRookFile;
        if (isKingSide) {
            rookFile = boardSize - 1;
            newKingFile = file + 2;
            newRookFile = file + 1;
        } else {
            rookFile = 0;
            newKingFile = file - 2;
            newRookFile = file - 1;
        }
        Piece rook = board.getPieceAtPosition(rank, rookFile);
        board.setPieceAtPosition(rank, newRookFile, rook);
        board.setPieceAtPosition(rank, rookFile, null);
        executeMove(board.getTileAt(rank, newKingFile), board.getTileAt(rank, file));
    }

    /**
     * Alters game states en passant, move count, or castling possibility upon moving a piece.
     * @param rank of piece
     * @param file of piece
     * @param move where the piece is going
     * @param isCapture whether this move is a capture or not
     */
    private void handleGameStateChange(int rank, int file, Move move, boolean isCapture) {
        Piece piece = board.getPieceAtPosition(move.rank, move.file);
        enPassant = "-";
        switch (piece.getName()) {
            case "pawn": {
                halfMoves = -1;
                if (Math.abs(rank - move.rank) == 2) enPassant = indToAlg(rank == 1 ? 2 : board.BOARD_SIZE - 3, file);
                break;
            }
            case "king": {
                if (piece.isWhite()) {
                    editCastleState("KQ");
                } else {
                    editCastleState("kq");
                }
            }
            case "rook": {
                if (move.file == 3) {
                    if (piece.isWhite()) {
                        editCastleState("Q");
                    } else {
                        editCastleState("q");
                    }
                }
                else {
                    if (piece.isWhite()) {
                        editCastleState("K");
                    } else {
                        editCastleState("k");
                    }
                }
            }
        }
        if (isCapture) halfMoves = -1;
        halfMoves++;
        if (!piece.isWhite()) fullMoves++;
        isWhiteToPlay = !isWhiteToPlay;
    }

    private void editCastleState(String castleOptionsToRemove) {
        for (int i = 0; i < castleOptionsToRemove.length(); i++) {
            char charToRemove = castleOptionsToRemove.charAt(i);
            castle = castle.replace(Character.toString(charToRemove), "");
        }
    }

    /**
     * Converts array indices to algorithmic (chess coordinates) notation.
     * @param rank of tile
     * @param file of tile
     * @return string in algorithmic notation
     */
    private String indToAlg(int rank, int file) {
        char num = (char) ('8' - rank);
        char letter = (char) (file + 'a');
        return new String(new char[] {letter, num});
    }

    /**
     * Gets enPassant String and converts it to a move object with correct rank and file.
     * @return enPassant as a move
     */
    private Move getEnPassantAsMove() {
        int rank = '8' - enPassant.charAt(1);
        int file = enPassant.charAt(0) - 'a';
        return new Move(rank, file);
    }

    /**
     * Handles tile click behavior such as capturing, moving, selecting, and deselecting.
     * @param clickedTile clicked on
     * @param rank of tile clicked on
     * @param file of tile clicked on
     */
    private void handleClick(Tile clickedTile, int rank, int file) {
        Piece clickedPiece = clickedTile.getPiece();
        Move selectedPosition = board.getSelectedPosition();
        Tile selectedTile = selectedPosition == null ? null : board.getTileAt(selectedPosition);
        Piece selectedPiece = selectedTile == null ? null : selectedTile.getPiece();

        if (clickedPiece != null) {
            if (selectedPiece != null && board.highlightedTilesContains(clickedTile) && clickedPiece.isWhite() != selectedPiece.isWhite()) {
                handleCaptureMove(clickedTile, selectedTile, selectedPiece, clickedPiece);
                handleGameStateChange(selectedPosition.rank, selectedPosition.file, new Move(rank, file), true);
            } else if (clickedTile == selectedTile) {
                handleDeselectTile();
            } else {
                handleSelectTile(clickedTile, rank, file);
            }
        } else if (board.highlightedTilesContains(clickedTile)) {
            if (!handleSpecialCase(selectedPosition.rank, selectedPosition.file, new Move(rank, file))) handleMove(clickedTile, selectedTile);
            handleGameStateChange(selectedPosition.rank, selectedPosition.file, new Move(rank, file), false);
        }
    }

    private void handleCaptureMove(Tile clickedTile, Tile selectedTile, Piece selectedPiece, Piece clickedPiece) {
        updateMaterialScore(selectedPiece.isWhite(), clickedPiece.getMaterialScore());
        executeMove(clickedTile, selectedTile);
    }

    private void handleDeselectTile() {
        board.setSelectedPosition(null);
        board.toggleHighlightedTiles();
        board.clearHighlightedTiles();
    }

    private void handleSelectTile(Tile clickedTile, int rank, int file) {
        board.toggleHighlightedTiles();
        board.clearHighlightedTiles();

        ArrayList<Move> moves = board.getBoardValidMoves(rank, file);
        moves.removeIf((move) -> validate(clickedTile.getPiece(), rank, file, move) == MoveStatus.INVALID);

        for (Move move : moves) {
            board.addTileToHighlighted(board.getTileAt(move.rank, move.file));
        }
        board.setSelectedPosition(new Move(rank, file));
        board.addTileToHighlighted(clickedTile);
        board.toggleHighlightedTiles();
    }

    private void handleMove(Tile clickedTile, Tile selectedTile) {
        executeMove(clickedTile, selectedTile);
    }

    private void executeMove(Tile clickedTile, Tile selectedTile) {
        clickedTile.setPiece(selectedTile.getPiece());
        selectedTile.setPiece(null);
        board.toggleHighlightedTiles();
        board.clearHighlightedTiles();
        board.setSelectedPosition(null);
        board.rotateBoard();
    }

    private void updateMaterialScore(boolean isWhite, int score) {
        if (isWhite) {
            whiteMaterialScore += score;
        } else {
            blackMaterialScore += score;
        }
    }
}
