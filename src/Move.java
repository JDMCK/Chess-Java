public class Move {
    int file;
    int rank;

    public Move (int rank, int file) {
        this.file = file;
        this.rank = rank;
    }

    public boolean equals(Move move) {
        return move.rank == rank && move.file == file;
    }
}
