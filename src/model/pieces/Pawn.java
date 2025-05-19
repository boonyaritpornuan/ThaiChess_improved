package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากเบี้ย (Pawn) ในหมากรุกไทย
 */
public class Pawn extends Piece {
    private boolean isPromoted = false;
    
    /**
     * สร้างหมากเบี้ยใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite ? 'p' : 'P', isWhite, row, col);
    }
    
    /**
     * ตรวจสอบว่าเบี้ยได้รับการเลื่อนขั้นหรือไม่
     * @return true ถ้าเบี้ยได้รับการเลื่อนขั้น, false ถ้าไม่ได้รับการเลื่อนขั้น
     */
    public boolean isPromoted() {
        return isPromoted;
    }
    
    /**
     * เลื่อนขั้นเบี้ย
     */
    public void promote() {
        isPromoted = true;
        // เปลี่ยนสัญลักษณ์เมื่อเลื่อนขั้น
        symbol = isWhite ? 'u' : 'U'; // u = เบี้ยหงาย (promoted pawn)
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int direction = isWhite ? -1 : 1; // ขาวเดินขึ้น (ลด row), ดำเดินลง (เพิ่ม row)
        
        if (isPromoted) {
            // เบี้ยหงายเคลื่อนที่ได้ 1 ช่องในแนวทแยง
            int[][] promotedDirections = {
                {-1, -1}, {-1, 1},
                {1, -1},  {1, 1}
            };
            
            for (int[] dir : promotedDirections) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (board.isValidPosition(newRow, newCol)) {
                    if (board.isEmpty(newRow, newCol) || board.isEnemy(newRow, newCol, isWhite)) {
                        moves.add(new Move(row, col, newRow, newCol, true));
                    }
                }
            }
        } else {
            // เบี้ยปกติเคลื่อนที่ได้ 1 ช่องในแนวตั้ง
            int newRow = row + direction;
            
            if (board.isValidPosition(newRow, col) && board.isEmpty(newRow, col)) {
                // ตรวจสอบการเลื่อนขั้น
                boolean willPromote = (isWhite && newRow <= 2) || (!isWhite && newRow >= 5);
                moves.add(new Move(row, col, newRow, col, willPromote));
            }
            
            // เบี้ยกินในแนวทแยง 1 ช่อง
            int[][] captureDirections = {
                {direction, -1}, {direction, 1}
            };
            
            for (int[] dir : captureDirections) {
                int captureRow = row + dir[0];
                int captureCol = col + dir[1];
                
                if (board.isValidPosition(captureRow, captureCol) && board.isEnemy(captureRow, captureCol, isWhite)) {
                    // ตรวจสอบการเลื่อนขั้น
                    boolean willPromote = (isWhite && captureRow <= 2) || (!isWhite && captureRow >= 5);
                    moves.add(new Move(row, col, captureRow, captureCol, willPromote));
                }
            }
        }
        
        return moves;
    }
    
    @Override
    public int getValue() {
        return isPromoted ? 2 : 1; // ค่าของเบี้ยในหมากรุกไทย (เบี้ยหงายมีค่ามากกว่า)
    }
    
    @Override
    public int getPositionValue() {
        // เบี้ยควรเคลื่อนที่ไปข้างหน้าและควบคุมศูนย์กลาง
        int[][] positionValues;
        
        if (isPromoted) {
            // เบี้ยหงายมีค่าตำแหน่งเหมือนเรือ
            if (isWhite) {
                positionValues = new int[][] {
                    {-20, -10, -10, -5, -5, -10, -10, -20},
                    {-10,   0,   0,  0,  0,   0,   0, -10},
                    {-10,   0,   5,  5,  5,   5,   0, -10},
                    { -5,   0,   5, 10, 10,   5,   0,  -5},
                    {  0,   0,   5, 10, 10,   5,   0,  -5},
                    {-10,   5,   5,  5,  5,   5,   0, -10},
                    {-10,   0,   5,  0,  0,   0,   0, -10},
                    {-20, -10, -10, -5, -5, -10, -10, -20}
                };
            } else {
                positionValues = new int[][] {
                    {-20, -10, -10, -5, -5, -10, -10, -20},
                    {-10,   0,   5,  0,  0,   0,   0, -10},
                    {-10,   5,   5,  5,  5,   5,   0, -10},
                    {  0,   0,   5, 10, 10,   5,   0,  -5},
                    { -5,   0,   5, 10, 10,   5,   0,  -5},
                    {-10,   0,   5,  5,  5,   5,   0, -10},
                    {-10,   0,   0,  0,  0,   0,   0, -10},
                    {-20, -10, -10, -5, -5, -10, -10, -20}
                };
            }
        } else {
            // เบี้ยปกติ
            if (isWhite) {
                positionValues = new int[][] {
                    { 0,  0,  0,  0,  0,  0,  0,  0},
                    {50, 50, 50, 50, 50, 50, 50, 50},
                    {10, 10, 20, 30, 30, 20, 10, 10},
                    { 5,  5, 10, 25, 25, 10,  5,  5},
                    { 0,  0,  0, 20, 20,  0,  0,  0},
                    { 5, -5,-10,  0,  0,-10, -5,  5},
                    { 5, 10, 10,-20,-20, 10, 10,  5},
                    { 0,  0,  0,  0,  0,  0,  0,  0}
                };
            } else {
                positionValues = new int[][] {
                    { 0,  0,  0,  0,  0,  0,  0,  0},
                    { 5, 10, 10,-20,-20, 10, 10,  5},
                    { 5, -5,-10,  0,  0,-10, -5,  5},
                    { 0,  0,  0, 20, 20,  0,  0,  0},
                    { 5,  5, 10, 25, 25, 10,  5,  5},
                    {10, 10, 20, 30, 30, 20, 10, 10},
                    {50, 50, 50, 50, 50, 50, 50, 50},
                    { 0,  0,  0,  0,  0,  0,  0,  0}
                };
            }
        }
        
        return positionValues[row][col];
    }
}
