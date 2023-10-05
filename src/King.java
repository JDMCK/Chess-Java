import java.util.ArrayList;

public class King extends Piece {

    public King (boolean isWhite) {
        super(isWhite, "King");
    }

    @Override
    public char getUnicode() {
        return isWhite() ? '♔' : '♚';
    }
    @Override
    public char getFEN() {
        return isWhite() ? 'K' : 'k';
    }
    @Override
    public ArrayList<Move> getBasicMoves(int rank, int file, BoardMoveValidator validator) {
        ArrayList<Move> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < directions.length; i++) {
            Move move = new Move(directions[i][0] + rank, directions[i][1] + file);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        }
        Move move = new Move(rank, file + 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank, file - 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);

        return moves;
    }
}