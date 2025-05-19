package model.pieces;

/**
 * คลาสสำหรับการสร้างหมากแต่ละประเภทในหมากรุกไทย
 * ใช้ Factory Pattern เพื่อให้การสร้างหมากเป็นระบบและง่ายต่อการขยาย
 */
public class PieceFactory {
    
    /**
     * สร้างหมากจากสัญลักษณ์
     * @param symbol สัญลักษณ์ของหมาก
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     * @return หมากที่สร้างขึ้น
     */
    public static Piece createPiece(char symbol, int row, int col) {
        boolean isWhite = Character.isLowerCase(symbol);
        char lowerSymbol = Character.toLowerCase(symbol);
        
        switch (lowerSymbol) {
            case 'k': return new King(isWhite, row, col);
            case 'q': return new Queen(isWhite, row, col);
            case 'b': return new Bishop(isWhite, row, col);
            case 'n': return new Knight(isWhite, row, col);
            case 'r': return new Rook(isWhite, row, col);
            case 'p': return new Pawn(isWhite, row, col);
            case 'u': // เบี้ยหงาย (promoted pawn)
                Pawn pawn = new Pawn(isWhite, row, col);
                pawn.promote();
                return pawn;
            default: return null;
        }
    }
}
