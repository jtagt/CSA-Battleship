import java.util.Arrays;

public class Battleship {

    public static void main(String[] args) {
        Board board = new Board(10);

       Submarine submarine = new Submarine();
       System.out.println(board.getBoardOutput(new Ship[]{submarine}));
    }
}
