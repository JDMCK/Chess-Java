import java.util.ArrayList;

public class GameManager {

    Board board;
    private boolean isWhiteToPlay;
    private String castle;
    private String enPassant;
    private int halfMoves;
    private int fullMoves;

    /**
     * Constructor for new GameManager instantiates a new board and sets it do the default chess starting position.
     */

    public GameManager() throws BoardIndexOutOfBoundsException {
        board = new Board();
        setFENState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public ArrayList<Move> getValidMoves(int rank, int file) throws BoardIndexOutOfBoundsException {
        ArrayList<Move> moves = new ArrayList<>();
        Piece piece = board.getPieceAtPosition(rank, file);
        if (piece == null) return moves;
        if ((isWhiteToPlay != piece.isWhite()))
            return moves;
        moves = board.getValidMoves(rank, file);
        return moves;
    }

    /**
     * Sets current state of board (position and other game states) using a FEN String.
     * @param fenString A board state represented as a FEN String
     */
    public final void setFENState(String fenString) throws BoardIndexOutOfBoundsException {
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

    public void consoleDraw() {
        board.consoleDraw(isWhiteToPlay);
    }
}
