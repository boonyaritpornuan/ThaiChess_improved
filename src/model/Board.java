package model;

import java.util.ArrayList;
import java.util.List;
import model.pieces.Piece;
import model.pieces.PieceFactory;
import model.rules.MoveValidator;
import model.rules.CheckDetector;
import model.events.GameEvent;
import model.events.GameListener;

/**
 * คลาสสำหรับกระดานหมากรุกไทย
 * จัดการสถานะของกระดาน การเคลื่อนที่ของหมาก และกฎต่างๆ
 */
public class Board extends BoardAbstract {
    private Piece[][] board; // กระดาน 8x8
    private boolean isWhiteTurn; // ขาว = ผู้เล่น, ดำ = คอมพิวเตอร์
    private MoveValidator moveValidator;
    private CheckDetector checkDetector;
    private List<GameListener> listeners;
    private List<Move> moveHistory;
    private int movesSinceCapture; // จำนวนตาที่ไม่มีการกินหมาก (สำหรับกฎการนับครบ)
    
    /**
     * สร้างกระดานใหม่
     */
    public Board() {
        board = new Piece[8][8];
        isWhiteTurn = true;
        moveValidator = new MoveValidator(this);
        checkDetector = new CheckDetector(this);
        listeners = new ArrayList<>();
        moveHistory = new ArrayList<>();
        movesSinceCapture = 0;
        initializeBoard();
    }
    
    /**
     * เริ่มต้นกระดาน
     */
    private void initializeBoard() {
        // ล้างกระดาน
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        
        // ฝั่งขาว (ผู้เล่น) อยู่ด้านล่าง (แถว 7-5)
        board[7][0] = PieceFactory.createPiece('r', 7, 0); // เม็ด
        board[7][1] = PieceFactory.createPiece('n', 7, 1); // ม้า
        board[7][2] = PieceFactory.createPiece('b', 7, 2); // โคน
        board[7][3] = PieceFactory.createPiece('q', 7, 3); // เรือ
        board[7][4] = PieceFactory.createPiece('k', 7, 4); // ขุน
        board[7][5] = PieceFactory.createPiece('b', 7, 5); // โคน
        board[7][6] = PieceFactory.createPiece('n', 7, 6); // ม้า
        board[7][7] = PieceFactory.createPiece('r', 7, 7); // เม็ด
        
        for (int i = 0; i < 8; i++) {
            board[6][i] = PieceFactory.createPiece('p', 6, i); // เบี้ย
        }
        
        // ฝั่งดำ (คอมพิวเตอร์) อยู่ด้านบน (แถว 0-2)
        board[0][0] = PieceFactory.createPiece('R', 0, 0); // เม็ด
        board[0][1] = PieceFactory.createPiece('N', 0, 1); // ม้า
        board[0][2] = PieceFactory.createPiece('B', 0, 2); // โคน
        board[0][3] = PieceFactory.createPiece('Q', 0, 3); // เรือ
        board[0][4] = PieceFactory.createPiece('K', 0, 4); // ขุน
        board[0][5] = PieceFactory.createPiece('B', 0, 5); // โคน
        board[0][6] = PieceFactory.createPiece('N', 0, 6); // ม้า
        board[0][7] = PieceFactory.createPiece('R', 0, 7); // เม็ด
        
        for (int i = 0; i < 8; i++) {
            board[1][i] = PieceFactory.createPiece('P', 1, i); // เบี้ย
        }
        
        // รีเซ็ตตัวแปรอื่นๆ
        isWhiteTurn = true;
        moveHistory.clear();
        movesSinceCapture = 0;
        
        // แจ้งเตือนผู้ฟัง
        notifyBoardChanged();
    }
    
    /**
     * รับหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @return หมากที่ตำแหน่งที่กำหนด หรือ null ถ้าไม่มีหมาก
     */
    public Piece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }
    
    /**
     * ตั้งค่าหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @param piece หมากที่จะตั้งค่า
     */
    @Override
    public void setPiece(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    /**
     * ตรวจสอบว่าตำแหน่งว่างหรือไม่
     * @param row แถว
     * @param col คอลัมน์
     * @return true ถ้าตำแหน่งว่าง, false ถ้าไม่ว่าง
     */
    public boolean isEmpty(int row, int col) {
        return getPiece(row, col) == null;
    }
    
    /**
     * ตรวจสอบว่าตำแหน่งมีหมากของฝ่ายตรงข้ามหรือไม่
     * @param row แถว
     * @param col คอลัมน์
     * @param isWhite เป็นฝ่ายขาวหรือไม่
     * @return true ถ้าตำแหน่งมีหมากของฝ่ายตรงข้าม, false ถ้าไม่มี
     */
    public boolean isEnemy(int row, int col, boolean isWhite) {
        Piece piece = getPiece(row, col);
        return piece != null && piece.isWhite() != isWhite;
    }
    
    /**
     * ตรวจสอบว่าตำแหน่งอยู่ในกระดานหรือไม่
     * @param row แถว
     * @param col คอลัมน์
     * @return true ถ้าตำแหน่งอยู่ในกระดาน, false ถ้าไม่อยู่
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    /**
     * ตรวจสอบว่าเป็นตาของฝ่ายขาวหรือไม่
     * @return true ถ้าเป็นตาของฝ่ายขาว, false ถ้าเป็นตาของฝ่ายดำ
     */
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
    
    /**
     * ทำการเคลื่อนที่
     * @param move การเคลื่อนที่
     * @return true ถ้าการเคลื่อนที่สำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean makeMove(Move move) {
        if (!moveValidator.isValidMove(move)) {
            return false;
        }
        
        // บันทึกหมากที่ถูกกิน (ถ้ามี)
        Piece capturedPiece = getPiece(move.toRow, move.toCol);
        move.setCapturedPiece(capturedPiece);
        
        // ย้ายหมาก
        Piece piece = getPiece(move.fromRow, move.fromCol);
        board[move.toRow][move.toCol] = piece;
        board[move.fromRow][move.fromCol] = null;
        
        // อัปเดตตำแหน่งของหมาก
        piece.setPosition(move.toRow, move.toCol);
        
        // ตรวจสอบการเลื่อนขั้นของเบี้ย
        if (move.isPromotion() && piece instanceof model.pieces.Pawn) {
            ((model.pieces.Pawn) piece).promote();
        }
        
        // อัปเดตจำนวนตาที่ไม่มีการกินหมาก
        if (capturedPiece != null) {
            movesSinceCapture = 0;
        } else {
            movesSinceCapture++;
        }
        
        // บันทึกการเคลื่อนที่
        moveHistory.add(move);
        
        // สลับตา
        isWhiteTurn = !isWhiteTurn;
        
        // แจ้งเตือนผู้ฟัง
        notifyBoardChanged();
        
        // ตรวจสอบการรุก
        if (checkDetector.isInCheck(isWhiteTurn)) {
            notifyCheck();
        }
        
        // ตรวจสอบการจน
        if (checkDetector.isCheckmate(isWhiteTurn)) {
            notifyCheckmate();
        }
        
        // ตรวจสอบการอับ
        if (checkDetector.isStalemate(isWhiteTurn)) {
            notifyStalemate();
        }
        
        return true;
    }
    
    /**
     * ยกเลิกการเคลื่อนที่ล่าสุด
     * @return true ถ้าการยกเลิกสำเร็จ, false ถ้าไม่มีการเคลื่อนที่ให้ยกเลิก
     */
    public boolean undoLastMove() {
        if (moveHistory.isEmpty()) {
            return false;
        }
        
        Move lastMove = moveHistory.remove(moveHistory.size() - 1);
        
        // ย้ายหมากกลับ
        Piece piece = getPiece(lastMove.toRow, lastMove.toCol);
        board[lastMove.fromRow][lastMove.fromCol] = piece;
        board[lastMove.toRow][lastMove.toCol] = lastMove.getCapturedPiece();
        
        // อัปเดตตำแหน่งของหมาก
        piece.setPosition(lastMove.fromRow, lastMove.fromCol);
        
        // ยกเลิกการเลื่อนขั้นของเบี้ย (ถ้ามี)
        if (lastMove.isPromotion() && piece instanceof model.pieces.Pawn) {
            // ต้องสร้างเบี้ยใหม่เพราะไม่สามารถยกเลิกการเลื่อนขั้นได้
            board[lastMove.fromRow][lastMove.fromCol] = PieceFactory.createPiece(
                piece.isWhite() ? 'p' : 'P',
                lastMove.fromRow,
                lastMove.fromCol
            );
        }
        
        // อัปเดตจำนวนตาที่ไม่มีการกินหมาก
        if (lastMove.getCapturedPiece() != null) {
            movesSinceCapture = 0;
            for (int i = moveHistory.size() - 1; i >= 0; i--) {
                if (moveHistory.get(i).getCapturedPiece() != null) {
                    break;
                }
                movesSinceCapture++;
            }
        } else {
            movesSinceCapture--;
        }
        
        // สลับตา
        isWhiteTurn = !isWhiteTurn;
        
        // แจ้งเตือนผู้ฟัง
        notifyBoardChanged();
        
        return true;
    }
    
    /**
     * สร้างรายการการเคลื่อนที่ที่ถูกต้องทั้งหมดสำหรับฝ่ายปัจจุบัน
     * @return รายการการเคลื่อนที่ที่ถูกต้อง
     */
    public List<Move> generateLegalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhiteTurn) {
                    legalMoves.addAll(moveValidator.getLegalMoves(i, j));
                }
            }
        }
        
        return legalMoves;
    }
    
    /**
     * รับรายการการเคลื่อนที่ที่ถูกต้องสำหรับหมากที่ตำแหน่งที่กำหนด
     * @param row แถว
     * @param col คอลัมน์
     * @return รายการการเคลื่อนที่ที่ถูกต้อง
     */
    public List<Move> getLegalMoves(int row, int col) {
        return moveValidator.getLegalMoves(row, col);
    }
    
    /**
     * ประเมินสถานะของกระดาน
     * @return ค่าของกระดาน (บวกถ้าฝ่ายขาวได้เปรียบ, ลบถ้าฝ่ายดำได้เปรียบ)
     */
    public int evaluate() {
        int score = 0;
        
        // ประเมินจากหมากที่เหลือ
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(i, j);
                if (piece != null) {
                    int value = piece.getValue() + piece.getPositionValue();
                    score += piece.isWhite() ? value : -value;
                }
            }
        }
        
        // ประเมินจากการรุก
        if (checkDetector.isInCheck(!isWhiteTurn)) {
            score += isWhiteTurn ? 50 : -50;
        }
        
        // ประเมินจากการจน
        if (checkDetector.isCheckmate(!isWhiteTurn)) {
            score += isWhiteTurn ? 10000 : -10000;
        }
        
        return score;
    }
    
    /**
     * ตรวจสอบว่าเกมจบหรือไม่
     * @return true ถ้าเกมจบ, false ถ้าเกมยังไม่จบ
     */
    public boolean isGameOver() {
        return checkDetector.isCheckmate(isWhiteTurn) || 
               checkDetector.isStalemate(isWhiteTurn) ||
               isCountingRuleApplied();
    }
    
    /**
     * ตรวจสอบว่ากฎการนับครบถูกใช้หรือไม่
     * @return true ถ้ากฎการนับครบถูกใช้, false ถ้าไม่ถูกใช้
     */
    public boolean isCountingRuleApplied() {
        return movesSinceCapture >= 64; // นับครบ 64 ตา
    }
    
    /**
     * เพิ่มผู้ฟังการเปลี่ยนแปลงของกระดาน
     * @param listener ผู้ฟัง
     */
    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * ลบผู้ฟังการเปลี่ยนแปลงของกระดาน
     * @param listener ผู้ฟัง
     */
    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * แจ้งเตือนผู้ฟังว่ากระดานเปลี่ยนแปลง
     */
    private void notifyBoardChanged() {
        for (GameListener listener : listeners) {
            listener.onBoardChanged();
        }
    }
    
    /**
     * แจ้งเตือนผู้ฟังว่ามีการรุก
     */
    private void notifyCheck() {
        for (GameListener listener : listeners) {
            listener.onCheck(!isWhiteTurn);
        }
    }
    
    /**
     * แจ้งเตือนผู้ฟังว่ามีการจน
     */
    private void notifyCheckmate() {
        for (GameListener listener : listeners) {
            listener.onCheckmate(!isWhiteTurn);
        }
    }
    
    /**
     * แจ้งเตือนผู้ฟังว่ามีการอับ
     */
    private void notifyStalemate() {
        for (GameListener listener : listeners) {
            listener.onStalemate();
        }
    }
    
    /**
     * รับจำนวนตาที่ไม่มีการกินหมาก
     * @return จำนวนตาที่ไม่มีการกินหมาก
     */
    public int getMovesSinceCapture() {
        return movesSinceCapture;
    }
    
    /**
     * รับประวัติการเคลื่อนที่
     * @return ประวัติการเคลื่อนที่
     */
    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
}
