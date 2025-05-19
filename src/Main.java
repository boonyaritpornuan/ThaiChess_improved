import model.Board;
import view.GameView;
import controller.GameController;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        GameView view = new GameView(board);
        new GameController(view, board);
    }
}