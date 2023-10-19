import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    private Piece piece = null;
    final private boolean isLight;
    final private double tileSize;
    final private Rectangle highlight;
    final private Rectangle background;
    private boolean isHighlighted;

    /**
     * Constructor for an empty tile.
     * @param isLight the tile is light or dark
     */
    public Tile(boolean isLight, double tileSize) {
        this.isLight = isLight;
        this.tileSize = tileSize;
        isHighlighted = false;

        Rectangle background = new Rectangle();
        background.setWidth(tileSize);
        background.setHeight(tileSize);
        background.setFill(isLight ? Color.rgb(238,238,210) : Color.rgb(118,150,86));
        this.background = background;

        Rectangle highlight = new Rectangle();
        highlight.setWidth(tileSize);
        highlight.setHeight(tileSize);
        highlight.setFill(Color.rgb	(186,202,68));
        highlight.setOpacity(0.75);
        highlight.setVisible(isHighlighted);
        this.highlight = highlight;

        getChildren().addAll(background, highlight);
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
        if (piece == null) {
            getChildren().remove(this.piece.getGraphic());
        } else {
            if (this.piece != null) {
                getChildren().remove(this.piece.getGraphic());
            }
            ImageView pieceGraphic = piece.getGraphic();
            pieceGraphic.setPreserveRatio(true);
            pieceGraphic.setFitHeight(tileSize);
            getChildren().add(pieceGraphic);
        }
        this.piece = piece;
    }

    /**
     * Gets if this tile is light or dark.
     * @return true if light, dark if not
     */
    public boolean isLight() {
        return isLight;
    }

    /**
     * Toggles this tiles highlight.
     */
    public void toggleHighlight() {
        isHighlighted = !isHighlighted;
        highlight.setVisible(isHighlighted);
    }
}
