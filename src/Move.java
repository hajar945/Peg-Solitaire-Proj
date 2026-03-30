public class Move {
    // where the peg is starting
    public int fromRow, fromCol;
    // where the peg is trying to go
    public int toRow, toCol;

    // constructor that takes in fromRow, fromCol, toRow, and toCol
    public Move(int r1, int c1, int r2, int c2) {
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    // checks to see if a move is a jump
    public boolean isJump() {
        // checks if the pegs travels exactly 2 spaces vertically or horizontally
        // if so, then yes, it's a jump
        return (Math.abs(fromRow - toRow) == 2 || Math.abs(fromCol - toCol) == 2);
    }
}