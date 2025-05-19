package model.commands;

import model.Board;
import model.Move;
import model.Piece;

/**
 * คลาสสำหรับคำสั่งการเคลื่อนที่ในหมากรุกไทย
 * ใช้ Command Pattern เพื่อให้สามารถย้อนกลับและทำซ้ำได้
 */
public class MoveCommand {
    private Board board;
    private Move move;
    private boolean executed;
    
    /**
     * สร้างคำสั่งการเคลื่อนที่ใหม่
     * @param board กระดานที่จะทำการเคลื่อนที่
     * @param move การเคลื่อนที่
     */
    public MoveCommand(Board board, Move move) {
        this.board = board;
        this.move = move;
        this.executed = false;
    }
    
    /**
     * ดำเนินการคำสั่ง
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean execute() {
        if (executed) {
            return false;
        }
        
        boolean success = board.makeMove(move);
        if (success) {
            executed = true;
        }
        
        return success;
    }
    
    /**
     * ยกเลิกคำสั่ง
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean undo() {
        if (!executed) {
            return false;
        }
        
        boolean success = board.undoLastMove();
        if (success) {
            executed = false;
        }
        
        return success;
    }
    
    /**
     * รับการเคลื่อนที่
     * @return การเคลื่อนที่
     */
    public Move getMove() {
        return move;
    }
    
    /**
     * ตรวจสอบว่าคำสั่งถูกดำเนินการแล้วหรือไม่
     * @return true ถ้าถูกดำเนินการแล้ว, false ถ้ายังไม่ถูกดำเนินการ
     */
    public boolean isExecuted() {
        return executed;
    }
}
