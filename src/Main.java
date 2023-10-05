public class Main {
    public static void main(String[] args) throws BoardIndexOutOfBoundsException {
        GameManager gm = new GameManager();
        gm.consoleDraw();
        for (Move move : gm.getValidMoves(3, 4)) {
            System.out.println(move.rank + ", " + move.file);
        }
    }
}