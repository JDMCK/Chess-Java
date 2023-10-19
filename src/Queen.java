import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen (boolean isWhite) {
        super(isWhite, "queen", 9);
    }
    @Override
    public char getUnicode() {
        return isWhite() ? '♕' : '♛';
    }
    @Override
    public char getFEN() {
        return isWhite() ? 'Q' : 'q';
    }
    @Override
    public ArrayList<Move> getBasicMoves(int rank, int file, BoardMoveValidator validator) {
        ArrayList<Move> moves = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < directions.length; i++) {
            int distance = 1;
            while (true) {
                Move move = new Move(directions[i][0] * distance + rank, directions[i][1] * distance + file);
                if (validator.validate(isWhite(), move) == MoveStatus.VALID_STOP) {
                    moves.add(move);
                    break;
                }
                if (validator.validate(isWhite(), move) == MoveStatus.INVALID) {
                    break;
                }
                moves.add(move);
                distance++;
            }
        }

        return moves;
    }
}