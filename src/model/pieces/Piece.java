package model.pieces;

import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสนามธรรมสำหรับหมากทุกประเภทในหมากรุกไทย
 */
public abstract class Piece {
    protected char symbol;
    protected boolean isWhite;
    protected int row;
    protected int col;
    
    /**
     * สร้างหมากใหม่
     * @param symbol สัญลักษณ์ของหมาก
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Piece(char symbol, boolean isWhite, int row, int col) {
        this.symbol = symbol;
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
    }
    
    /**
     * รับสัญลักษณ์ของหมาก
     * @return สัญลักษณ์ของหมาก
     */
    public char getSymbol() {
        return symbol;
    }
    
    /**
     * ตรวจสอบว่าเป็นหมากขาวหรือไม่
     * @return true ถ้าเป็นหมากขาว, false ถ้าเป็นหมากดำ
     */
    public boolean isWhite() {
        return isWhite;
    }
    
    /**
     * รับแถวปัจจุบันของหมาก
     * @return แถวปัจจุบัน
     */
    public int getRow() {
        return row;
    }
    
    /**
     * รับคอลัมน์ปัจจุบันของหมาก
     * @return คอลัมน์ปัจจุบัน
     */
    public int getCol() {
        return col;
    }
    
    /**
     * ตั้งค่าตำแหน่งใหม่ของหมาก
     * @param row แถวใหม่
     * @param col คอลัมน์ใหม่
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * สร้างรายการการเคลื่อนที่ที่ถูกต้องสำหรับหมากนี้
     * @param board กระดานปัจจุบัน
     * @return รายการการเคลื่อนที่ที่ถูกต้อง
     */
    public abstract List<Move> getLegalMoves(Board board);
    
    /**
     * รับค่าของหมากสำหรับการประเมิน
     * @return ค่าของหมาก
     */
    public abstract int getValue();
    
    /**
     * รับค่าตำแหน่งของหมากสำหรับการประเมิน
     * @return ค่าตำแหน่งของหมาก
     */
    public abstract int getPositionValue();
    
    /**
     * สร้างการเคลื่อนที่ในทิศทางที่กำหนด
     * @param board กระดานปัจจุบัน
     * @param moves รายการการเคลื่อนที่ที่จะเพิ่ม
     * @param directions ทิศทางที่จะตรวจสอบ
     * @param continuous สามารถเคลื่อนที่ต่อเนื่องในทิศทางเดียวกันได้หรือไม่
     */
    protected void addMovesInDirections(Board board, List<Move> moves, int[][] directions, boolean continuous) {
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            while (board.isValidPosition(r, c)) {
                if (board.isEmpty(r, c)) {
                    moves.add(new Move(row, col, r, c));
                } else if (board.isEnemy(r, c, isWhite)) {
                    moves.add(new Move(row, col, r, c));
                    break;
                } else {
                    break;
                }
                
                if (!continuous) break;
                
                r += dir[0];
                c += dir[1];
            }
        }
    }
}
