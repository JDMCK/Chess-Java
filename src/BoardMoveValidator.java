@FunctionalInterface
public interface BoardMoveValidator {
    /**
     * Used by pieces to ask their board if a move is valid.
     * @param move is move to check
     * @return Validity status of given move
     */
    public MoveStatus validate(Move move);
}
