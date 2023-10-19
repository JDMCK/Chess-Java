@FunctionalInterface
public interface GMOnClickHandler {
    /**
     * Used by board to ask the game manager how to handle clicks.
     * @param tile is the tile that is selected
     * @param rank of piece
     * @param file of piece
     */
    public void handleClick(Tile tile, int rank, int file);
}
