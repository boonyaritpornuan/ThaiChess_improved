package model.rules;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;
import model.Piece;

/**
 * คลาสสำหรับตรวจสอบความถูกต้องของการเคลื่อนที่ในหมากรุกไทย
 */
public class MoveValidator {
    private Board board;
    
    /**
     * สร้าง MoveValidator ใหม่
     * @param board กระดานที่จะตรวจสอบ
     */
    public MoveValidator(Board board) {
        this.board = board;
    }
    
    /**
     * ตรวจสอบว่าการเคลื่อนที่ถูกต้องหรือไม่
     * @param move การเคลื่อนที่ที่จะตรวจสอบ
     * @return true ถ้าการเคลื่อนที่ถูกต้อง, false ถ้าไม่ถูกต้อง
     */
    public boolean isValidMove(Move move) {
        // ตรวจสอบว่าตำแหน่งเริ่มต้นและปลายทางอยู่ในกระดาน
        if (!board.isValidPosition(move.fromRow, move.fromCol) || 
            !board.isValidPosition(move.toRow, move.toCol)) {
            return false;
        }
        
        // ตรวจสอบว่ามีหมากที่ตำแหน่งเริ่มต้น
        Piece piece = board.getPiece(move.fromRow, move.fromCol);
        if (piece == null) {
            return false;
        }
        
        // ตรวจสอบว่าเป็นตาของฝ่ายที่ถูกต้อง
        if (piece.isWhite() != board.isWhiteTurn()) {
            return false;
        }
        
        // ตรวจสอบว่าการเคลื่อนที่อยู่ในรายการการเคลื่อนที่ที่ถูกต้องของหมาก
        List<Move> legalMoves = piece.getLegalMoves(board);
        for (Move legalMove : legalMoves) {
            if (legalMove.fromRow == move.fromRow && 
                legalMove.fromCol == move.fromCol && 
                legalMove.toRow == move.toRow && 
                legalMove.toCol == move.toCol) {
                
                // ตรวจสอบว่าการเคลื่อนที่ไม่ทำให้ขุนของตัวเองถูกรุก
                if (!wouldKingBeInCheck(move)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * ตรวจสอบว่าการเคลื่อนที่จะทำให้ขุนของตัวเองถูกรุกหรือไม่
     * @param move การเคลื่อนที่ที่จะตรวจสอบ
     * @return true ถ้าการเคลื่อนที่จะทำให้ขุนของตัวเองถูกรุก, false ถ้าไม่ทำให้ถูกรุก
     */
    private boolean wouldKingBeInCheck(Move move) {
        // จำลองการเคลื่อนที่
        Piece piece = board.getPiece(move.fromRow, move.fromCol);
        Piece capturedPiece = board.getPiece(move.toRow, move.toCol);
        
        // ทำการเคลื่อนที่ชั่วคราว
        board.getPiece(move.fromRow, move.fromCol).setPosition(move.toRow, move.toCol);
        board.setPiece(move.toRow, move.toCol, piece);
        board.setPiece(move.fromRow, move.fromCol, null);
        
        // ตรวจสอบว่าขุนของตัวเองถูกรุกหรือไม่
        boolean kingInCheck = isKingInCheck(piece.isWhite());
        
        // ย้อนกลับการเคลื่อนที่
        board.getPiece(move.toRow, move.toCol).setPosition(move.fromRow, move.fromCol);
        board.setPiece(move.fromRow, move.fromCol, piece);
        board.setPiece(move.toRow, move.toCol, capturedPiece);
        
        return kingInCheck;
    }
    
    /**
     * ตรวจสอบว่าขุนของฝ่ายที่กำหนดถูกรุกหรือไม่
     * @param isWhiteKing true ถ้าเป็นขุนขาว, false ถ้าเป็นขุนดำ
     * @return true ถ้าขุนถูกรุก, false ถ้าไม่ถูกรุก
     */
    public boolean isKingInCheck(boolean isWhiteKing) {
        // หาตำแหน่งของขุน
        int kingRow = -1, kingCol = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece instanceof model.pieces.King && piece.isWhite() == isWhiteKing) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        // ตรวจสอบว่ามีหมากของฝ่ายตรงข้ามที่สามารถเคลื่อนที่มาที่ตำแหน่งของขุนได้หรือไม่
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null && piece.isWhite() != isWhiteKing) {
                    List<Move> moves = piece.getLegalMoves(board);
                    for (Move move : moves) {
                        if (move.toRow == kingRow && move.toCol == kingCol) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * รับรายการการเคลื่อนที่ที่ถูกต้องสำหรับหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @return รายการการเคลื่อนที่ที่ถูกต้อง
     */
    public List<Move> getLegalMoves(int row, int col) {
        List<Move> legalMoves = new ArrayList<>();
        
        Piece piece = board.getPiece(row, col);
        if (piece == null || piece.isWhite() != board.isWhiteTurn()) {
            return legalMoves;
        }
        
        List<Move> candidateMoves = piece.getLegalMoves(board);
        for (Move move : candidateMoves) {
            if (!wouldKingBeInCheck(move)) {
                legalMoves.add(move);
            }
        }
        
        return legalMoves;
    }
}
