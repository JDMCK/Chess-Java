import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite, "Pawn");
    }

    @Override
    public char getUnicode() {
        return isWhite() ? '♘' : '♞';
    }

    @Override
    public char getFEN() {
        return isWhite() ? 'N' : 'n';
    }

    @Override
    public ArrayList<Move> getBasicMoves(int rank, int file, BoardMoveValidator validator) {
        ArrayList<Move> moves = new ArrayList<>();
        Move move = new Move(rank - 2, file + 1);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank - 2, file - 1);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank - 1, file + 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank - 1, file - 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank + 2, file + 1);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank + 2, file - 1);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank + 1, file + 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);
        move = new Move(rank + 1, file - 2);
        if (validator.validate(move) == MoveStatus.VALID) moves.add(move);

        return moves;
    }
}