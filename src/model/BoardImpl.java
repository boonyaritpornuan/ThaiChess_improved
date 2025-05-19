package model;

/**
 * คลาสสำหรับแก้ไขปัญหาการ import ใน Board.java
 * เนื่องจากมีการใช้ abstract Board ในคลาสอื่น
 */
public class BoardImpl extends BoardAbstract {
    private model.pieces.Piece[][] board; // กระดาน 8x8
    
    /**
     * ตั้งค่าหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @param piece หมากที่จะตั้งค่า
     */
    @Override
    public void setPiece(int row, int col, model.pieces.Piece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกรุกหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกรุก, false ถ้าไม่ถูกรุก
     */
    @Override
    public boolean isInCheck(boolean isWhite) {
        return new model.rules.CheckDetector(this).isInCheck(isWhite);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกจนหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกจน, false ถ้าไม่ถูกจน
     */
    @Override
    public boolean isCheckmate(boolean isWhite) {
        return new model.rules.CheckDetector(this).isCheckmate(isWhite);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดอับหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าอับ, false ถ้าไม่อับ
     */
    @Override
    public boolean isStalemate(boolean isWhite) {
        return new model.rules.CheckDetector(this).isStalemate(isWhite);
    }
}
