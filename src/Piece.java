import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public abstract class Piece extends ImageView {

    private final boolean isWhite;
    private final String name;
    private final ImageView graphic;
    private int materialScore;

    public Piece(boolean isWhite, String name, int materialScore) {
        this.isWhite = isWhite;
        this.name = name;
        this.materialScore = materialScore;
        Image image = new Image("images/pieces/" + (isWhite ? "white" : "black") + "-" + name + ".png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        this.graphic = imageView;
    }

    /**
     * Gets the character of this piece as determined in FEN.
     * @return The character of this piece as determined in FEN as char.
     */
    public abstract char getFEN();

    /**
     * Gets the unicode representation of this piece.
     * @return The unicode representation of this piece as a char.
     */
    public abstract char getUnicode();

    /**
     * Returns the pieces display graphic.
     * @return graphic as Image
     */
    public ImageView getGraphic() {
        return graphic;
    }

    /**
     * Gets if piece is white.
     * @return true if white, false if black
     */
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * Gets name of piece.
     * @return name of piece
     */
    public String getName() {
        return name;
    }

    /**
     * Gets material score of piece.
     * @return material score as int
     */
    public int getMaterialScore() {
        return materialScore;
    }

    /**
     * Generates all possible moves from this piece's perspective (some moves may be pruned later).
     * Doesn't evaluate other pieces positions or special rules.
     * @param file File the current piece is on, given as 0-based index.
     * @param rank Rank the current piece is on, given as 0-based index.
     * @return 2D integer array. Each inner array contains [file, rank] as 0-based indices.
     */
    public abstract ArrayList<Move> getBasicMoves(int file, int rank, BoardMoveValidator validator);
}
