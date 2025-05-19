package model;

/**
 * คลาสสำหรับการเคลื่อนที่ในหมากรุกไทย
 */
public class Move {
    public int fromRow, fromCol, toRow, toCol;
    private boolean promotion;
    private Piece capturedPiece;
    
    /**
     * สร้างการเคลื่อนที่ใหม่
     * @param fromRow แถวเริ่มต้น
     * @param fromCol คอลัมน์เริ่มต้น
     * @param toRow แถวปลายทาง
     * @param toCol คอลัมน์ปลายทาง
     */
    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.promotion = false;
        this.capturedPiece = null;
    }
    
    /**
     * สร้างการเคลื่อนที่ใหม่พร้อมระบุการเลื่อนขั้น
     * @param fromRow แถวเริ่มต้น
     * @param fromCol คอลัมน์เริ่มต้น
     * @param toRow แถวปลายทาง
     * @param toCol คอลัมน์ปลายทาง
     * @param promotion เป็นการเลื่อนขั้นหรือไม่
     */
    public Move(int fromRow, int fromCol, int toRow, int toCol, boolean promotion) {
        this(fromRow, fromCol, toRow, toCol);
        this.promotion = promotion;
    }
    
    /**
     * ตรวจสอบว่าเป็นการเลื่อนขั้นหรือไม่
     * @return true ถ้าเป็นการเลื่อนขั้น, false ถ้าไม่ใช่
     */
    public boolean isPromotion() {
        return promotion;
    }
    
    /**
     * ตั้งค่าหมากที่ถูกกิน
     * @param capturedPiece หมากที่ถูกกิน
     */
    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    
    /**
     * รับหมากที่ถูกกิน
     * @return หมากที่ถูกกิน หรือ null ถ้าไม่มีหมากถูกกิน
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    }
    
    @Override
    public String toString() {
        return "(" + fromRow + "," + fromCol + ") -> (" + toRow + "," + toCol + ")" +
               (promotion ? " (เลื่อนขั้น)" : "") +
               (capturedPiece != null ? " (กิน)" : "");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return fromRow == move.fromRow && fromCol == move.fromCol &&
               toRow == move.toRow && toCol == move.toCol;
    }
    
    @Override
    public int hashCode() {
        return 31 * (fromRow + fromCol + toRow + toCol);
    }
}
