import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn (boolean isWhite) {
        super(isWhite, "pawn", 1);
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
        Move move;
        if (isWhite()) {
            move = new Move(rank - 1, file);
            if (validator.validate(isWhite(), move) == MoveStatus.VALID) {
                moves.add(move);
                move = new Move(rank - 2, file);
                if (validator.validate(isWhite(), move) == MoveStatus.VALID && rank == 6) moves.add(move);
            }
            move = new Move(rank - 1, file + 1);
            if (validator.validate(isWhite(), move) != MoveStatus.INVALID) moves.add(move);
            move = new Move(rank - 1, file - 1);
        } else {
            move = new Move(rank + 1, file);
            if (validator.validate(isWhite(), move) == MoveStatus.VALID) {
                moves.add(move);
                move = new Move(rank + 2, file);
                if (validator.validate(isWhite(), move) == MoveStatus.VALID && rank == 1) moves.add(move);
            }
            move = new Move(rank + 1, file + 1);
            if (validator.validate(isWhite(), move) != MoveStatus.INVALID) moves.add(move);
            move = new Move(rank + 1, file - 1);
        }
        if (validator.validate(isWhite(), move) != MoveStatus.INVALID) moves.add(move);
        return moves;
    }
}