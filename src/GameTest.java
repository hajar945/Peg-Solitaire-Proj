import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void testInitialBoardSetup() {
        Game game = new Game();
        
        // test the center hole
        assertEquals(Game.EMPTY, game.pieceAt(3, 3), "The exact center of a 7x7 board should start EMPTY.");
        
        // test a corner
        assertEquals(Game.INVALID, game.pieceAt(0, 0), "The top-left corner should be marked INVALID.");
        
        // test a standard peg spot
        assertEquals(Game.PEG, game.pieceAt(3, 1), "The middle-left arm should contain a PEG.");
    }

    @Test
    public void testMakeMoveRemovesPegs() {
        Game game = new Game();
        
        // first standard move in Peg Solitaire which is jumping into the center
        // jump the peg from (row 3, col 1) over (row 3, col 2) into the center (row 3, col 3)
        game.makeMove(3, 1, 3, 3);

        // verify the landing spot now has a peg
        assertEquals(Game.PEG, game.pieceAt(3, 3), "The center hole should now contain a peg.");
        
        // verify the starting spot is now empty
        assertEquals(Game.EMPTY, game.pieceAt(3, 1), "The starting hole should now be empty.");
        
        // verify the jumped peg was deleted
        assertEquals(Game.EMPTY, game.pieceAt(3, 2), "The middle hole should be empty because the peg was jumped.");
    }

    @Test
    public void testSetBoardSizeUpdatesInternalTracker() {
        Game game = new Game();
        
        // the default size should be 7
        assertEquals(7, game.getBoardSize(), "Default board size should be 7.");
        
        // update the size to 11
        game.setBoardSize(11);
        
        // verify the internal getter returns the new size correctly
        assertEquals(11, game.getBoardSize(), "The board size tracker should update to 11.");
    }
}