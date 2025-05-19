package model;

import model.pieces.Piece;

/**
 * คลาสสำหรับการเพิ่มเติมเมธอดที่จำเป็นใน Board
 * เพื่อให้สามารถทำงานร่วมกับ MoveValidator ได้
 */
public abstract class Board {
    
    /**
     * ตั้งค่าหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @param piece หมากที่จะตั้งค่า
     */
    public void setPiece(int row, int col, Piece piece) {
        // ต้องถูก Override โดยคลาสลูก
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกรุกหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกรุก, false ถ้าไม่ถูกรุก
     */
    public boolean isInCheck(boolean isWhite) {
        // ต้องถูก Override โดยคลาสลูก
        return false;
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกจนหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกจน, false ถ้าไม่ถูกจน
     */
    public boolean isCheckmate(boolean isWhite) {
        // ต้องถูก Override โดยคลาสลูก
        return false;
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดอับหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าอับ, false ถ้าไม่อับ
     */
    public boolean isStalemate(boolean isWhite) {
        // ต้องถูก Override โดยคลาสลูก
        return false;
    }
}
