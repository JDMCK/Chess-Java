import javafx.scene.layout.StackPane;

public class Tile extends StackPane {

    private Piece piece;
    final private boolean isLight;
    /**
     * Constructor for an occupied tile.
     * @param isLight the tile is light or dark
     * @param piece is the piece you want to set
     */
    public Tile(boolean isLight, Piece piece, double tileSize) {
        this.piece = piece;
        this.isLight = isLight;

        setPrefSize(tileSize, tileSize);
    }

    /**
     * Constructor for an empty tile.
     * @param isLight the tile is light or dark
     */
    public Tile(boolean isLight, double tileSize) {
        this.isLight = isLight;

        setPrefSize(tileSize, tileSize);
    }

    /**
     * Gets piece.
     * @return piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Sets piece.
     * @param piece to set
     */
    public void setPiece(Piece piece) {
        this.piece = piece;

        getChildren().add(piece);
    }

    /**
     * Gets if this tile is light or dark.
     * @return true if light, dark if not
     */
    public boolean isLight() {
        return isLight;
    }
}
