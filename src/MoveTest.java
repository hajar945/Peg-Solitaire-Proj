import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveTest{
    
    @Test
    public void testValidHorizontalJump() {
        // jumping from Column 1 to Column 3 on Row 3
        Move move = new Move(3, 1, 3, 3);
        assertTrue(move.isJump(), "A horizontal move of 2 spaces should be considered a jump.");
    }

    @Test
    public void testValidVerticalJump() {
        // jumping from Row 5 to Row 3 on Column 3
        Move move = new Move(5, 3, 3, 3);
        assertTrue(move.isJump(), "A vertical move of 2 spaces should be considered a jump.");
    }

    @Test
    public void testInvalidDiagonalMove() {
        // moving 1 space diagonally
        Move move = new Move(2, 2, 3, 3);
        assertFalse(move.isJump(), "Diagonal moves should fail the jump test in Peg Solitaire.");
    }

    @Test
    public void testZeroDistanceMoveIsNotAJump() {
        // simulating a scenario where the start and end coordinates are exactly the same
        Move move = new Move(4, 4, 4, 4);
        
        assertFalse(move.isJump(), "A move with zero distance should mathematically fail the jump test.");
    }
}