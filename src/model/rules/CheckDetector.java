package model.rules;

import java.util.List;
import model.Board;
import model.Move;
import model.Piece;

/**
 * คลาสสำหรับตรวจสอบการรุก การจน และการอับในหมากรุกไทย
 */
public class CheckDetector {
    private Board board;
    private MoveValidator moveValidator;
    
    /**
     * สร้าง CheckDetector ใหม่
     * @param board กระดานที่จะตรวจสอบ
     */
    public CheckDetector(Board board) {
        this.board = board;
        this.moveValidator = new MoveValidator(board);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกรุกหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกรุก, false ถ้าไม่ถูกรุก
     */
    public boolean isInCheck(boolean isWhite) {
        return moveValidator.isKingInCheck(isWhite);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดถูกจนหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าถูกจน, false ถ้าไม่ถูกจน
     */
    public boolean isCheckmate(boolean isWhite) {
        // ถ้าไม่ได้ถูกรุก ก็ไม่ใช่การจน
        if (!isInCheck(isWhite)) {
            return false;
        }
        
        // ตรวจสอบว่ามีการเคลื่อนที่ที่ถูกต้องหรือไม่
        return !hasLegalMoves(isWhite);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดอับหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้าอับ, false ถ้าไม่อับ
     */
    public boolean isStalemate(boolean isWhite) {
        // ถ้าถูกรุก ก็ไม่ใช่การอับ
        if (isInCheck(isWhite)) {
            return false;
        }
        
        // ตรวจสอบว่ามีการเคลื่อนที่ที่ถูกต้องหรือไม่
        return !hasLegalMoves(isWhite);
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่กำหนดมีการเคลื่อนที่ที่ถูกต้องหรือไม่
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return true ถ้ามีการเคลื่อนที่ที่ถูกต้อง, false ถ้าไม่มี
     */
    private boolean hasLegalMoves(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhite) {
                    List<Move> legalMoves = moveValidator.getLegalMoves(i, j);
                    if (!legalMoves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
