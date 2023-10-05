import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn (boolean isWhite) {
        super(isWhite, "Pawn");
    }
    @Override
    public char getUnicode() {
        return isWhite() ? '♙' : '♟';
    }
    @Override
    public char getFEN() {
        return isWhite() ? 'P' : 'p';
    }
    @Override
    public ArrayList<Move> getBasicMoves(int rank, int file, BoardMoveValidator validator) {
        ArrayList<Move> moves = new ArrayList<>();
        if (isWhite()) {
            Move move = new Move(rank - 1, file);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank - 2, file);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank - 1, file + 1);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank - 1, file - 1);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        } else {
            Move move = new Move(rank + 1, file);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank + 2, file);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank + 1, file + 1);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
            move = new Move(rank + 1, file - 1);
            if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        }
        return moves;
    }
}