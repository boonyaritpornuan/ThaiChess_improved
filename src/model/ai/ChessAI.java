package model.ai;

import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับ AI ในหมากรุกไทย
 * รองรับหลายระดับความยาก
 */
public class ChessAI {
    public enum Difficulty {
        EASY(2),
        MEDIUM(3),
        HARD(4),
        EXPERT(5);
        
        private final int depth;
        
        Difficulty(int depth) {
            this.depth = depth;
        }
        
        public int getDepth() {
            return depth;
        }
    }
    
    private AlphaBeta alphaBeta;
    private Difficulty difficulty;
    
    /**
     * สร้าง ChessAI ใหม่ด้วยระดับความยากปานกลาง
     */
    public ChessAI() {
        this(Difficulty.MEDIUM);
    }
    
    /**
     * สร้าง ChessAI ใหม่ด้วยระดับความยากที่กำหนด
     * @param difficulty ระดับความยาก
     */
    public ChessAI(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.alphaBeta = new AlphaBeta(difficulty.getDepth());
    }
    
    /**
     * ตั้งค่าระดับความยาก
     * @param difficulty ระดับความยาก
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.alphaBeta.setMaxDepth(difficulty.getDepth());
    }
    
    /**
     * รับระดับความยากปัจจุบัน
     * @return ระดับความยากปัจจุบัน
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * หาการเคลื่อนที่ที่ดีที่สุด
     * @param board กระดานปัจจุบัน
     * @return การเคลื่อนที่ที่ดีที่สุด หรือ null ถ้าไม่มีการเคลื่อนที่ที่ถูกต้อง
     */
    public Move findBestMove(Board board) {
        // ปรับเวลาในการคิดตามระดับความยาก
        switch (difficulty) {
            case EASY:
                alphaBeta.setTimeLimit(1000); // 1 วินาที
                break;
            case MEDIUM:
                alphaBeta.setTimeLimit(3000); // 3 วินาที
                break;
            case HARD:
                alphaBeta.setTimeLimit(5000); // 5 วินาที
                break;
            case EXPERT:
                alphaBeta.setTimeLimit(10000); // 10 วินาที
                break;
        }
        
        return alphaBeta.findBestMove(board);
    }
    
    /**
     * เลือกการเคลื่อนที่แบบสุ่ม (สำหรับระดับความยากง่าย)
     * @param board กระดานปัจจุบัน
     * @return การเคลื่อนที่ที่สุ่ม หรือ null ถ้าไม่มีการเคลื่อนที่ที่ถูกต้อง
     */
    public Move findRandomMove(Board board) {
        List<Move> legalMoves = board.generateLegalMoves();
        if (legalMoves.isEmpty()) {
            return null;
        }
        
        // สุ่มการเคลื่อนที่
        int randomIndex = (int) (Math.random() * legalMoves.size());
        return legalMoves.get(randomIndex);
    }
}
