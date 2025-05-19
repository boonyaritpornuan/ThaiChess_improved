package controller;

import model.Board;
import model.Move;
import model.ai.ChessAI;
import model.commands.CommandHistory;
import model.commands.MoveCommand;
import view.GameView;

/**
 * คลาสสำหรับควบคุมเกมหมากรุกไทย
 */
public class GameController {
    private Board board;
    private ChessAI ai;
    private CommandHistory commandHistory;
    private GameView view;
    
    /**
     * สร้าง GameController ใหม่
     * @param board กระดาน
     * @param ai AI
     */
    public GameController(Board board, ChessAI ai) {
        this.board = board;
        this.ai = ai;
        this.commandHistory = new CommandHistory();
    }
    
    /**
     * ตั้งค่า view
     * @param view view
     */
    public void setView(GameView view) {
        this.view = view;
    }
    
    /**
     * เริ่มเกมใหม่
     */
    public void newGame() {
        board = new Board();
        commandHistory.clear();
    }
    
    /**
     * ทำการเคลื่อนที่
     * @param fromRow แถวเริ่มต้น
     * @param fromCol คอลัมน์เริ่มต้น
     * @param toRow แถวปลายทาง
     * @param toCol คอลัมน์ปลายทาง
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        Move move = new Move(fromRow, fromCol, toRow, toCol);
        MoveCommand command = new MoveCommand(board, move);
        return commandHistory.executeCommand(command);
    }
    
    /**
     * ให้ AI ทำการเคลื่อนที่
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean makeAIMove() {
        Move move = ai.findBestMove(board);
        if (move == null) {
            return false;
        }
        
        MoveCommand command = new MoveCommand(board, move);
        return commandHistory.executeCommand(command);
    }
    
    /**
     * ย้อนกลับการเคลื่อนที่
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean undoMove() {
        return commandHistory.undo();
    }
    
    /**
     * ทำซ้ำการเคลื่อนที่
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean redoMove() {
        return commandHistory.redo();
    }
}
