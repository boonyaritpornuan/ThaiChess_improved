package model.ai;

import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับอัลกอริทึม Minimax ที่ปรับปรุงด้วย Alpha-Beta Pruning
 * ใช้สำหรับ AI ในหมากรุกไทย
 */
public class AlphaBeta {
    private static final int DEFAULT_DEPTH = 4;
    private int maxDepth;
    private int nodesExplored;
    private long startTime;
    private long timeLimit;
    
    /**
     * สร้าง AlphaBeta ใหม่ด้วยความลึกเริ่มต้น
     */
    public AlphaBeta() {
        this(DEFAULT_DEPTH);
    }
    
    /**
     * สร้าง AlphaBeta ใหม่ด้วยความลึกที่กำหนด
     * @param maxDepth ความลึกสูงสุดในการค้นหา
     */
    public AlphaBeta(int maxDepth) {
        this.maxDepth = maxDepth;
        this.timeLimit = 5000; // 5 วินาที
    }
    
    /**
     * ตั้งค่าความลึกสูงสุดในการค้นหา
     * @param maxDepth ความลึกสูงสุดในการค้นหา
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
    
    /**
     * ตั้งค่าเวลาสูงสุดในการค้นหา (มิลลิวินาที)
     * @param timeLimit เวลาสูงสุดในการค้นหา (มิลลิวินาที)
     */
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    /**
     * หาการเคลื่อนที่ที่ดีที่สุด
     * @param board กระดานปัจจุบัน
     * @return การเคลื่อนที่ที่ดีที่สุด หรือ null ถ้าไม่มีการเคลื่อนที่ที่ถูกต้อง
     */
    public Move findBestMove(Board board) {
        List<Move> legalMoves = board.generateLegalMoves();
        if (legalMoves.isEmpty()) {
            System.out.println("AlphaBeta: ไม่มีการเคลื่อนที่ที่ถูกต้อง");
            return null;
        }
        
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        nodesExplored = 0;
        startTime = System.currentTimeMillis();
        
        for (Move move : legalMoves) {
            board.makeMove(move);
            int value = alphaBeta(board, maxDepth - 1, alpha, beta, false);
            board.undoLastMove();
            
            nodesExplored++;
            
            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
            
            alpha = Math.max(alpha, bestValue);
            
            // ตรวจสอบเวลา
            if (System.currentTimeMillis() - startTime > timeLimit) {
                System.out.println("AlphaBeta: เกินเวลาที่กำหนด");
                break;
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("AlphaBeta: ค้นหา " + nodesExplored + " โหนด ใช้เวลา " + (endTime - startTime) + " มิลลิวินาที");
        System.out.println("AlphaBeta: เลือกการเคลื่อนที่ที่ดีที่สุด: " + bestMove + " ด้วยค่า: " + bestValue);
        
        return bestMove;
    }
    
    /**
     * อัลกอริทึม Alpha-Beta Pruning
     * @param board กระดานปัจจุบัน
     * @param depth ความลึกที่เหลือ
     * @param alpha ค่า alpha
     * @param beta ค่า beta
     * @param isMaximizing true ถ้าเป็นผู้เล่นที่ต้องการค่าสูงสุด, false ถ้าเป็นผู้เล่นที่ต้องการค่าต่ำสุด
     * @return ค่าของกระดาน
     */
    private int alphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizing) {
        nodesExplored++;
        
        // ตรวจสอบเงื่อนไขการหยุด
        if (depth == 0 || board.isGameOver()) {
            return evaluate(board, depth);
        }
        
        // ตรวจสอบเวลา
        if (System.currentTimeMillis() - startTime > timeLimit) {
            return evaluate(board, depth);
        }
        
        List<Move> legalMoves = board.generateLegalMoves();
        if (legalMoves.isEmpty()) {
            return evaluate(board, depth);
        }
        
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                board.makeMove(move);
                int eval = alphaBeta(board, depth - 1, alpha, beta, false);
                board.undoLastMove();
                
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                board.makeMove(move);
                int eval = alphaBeta(board, depth - 1, alpha, beta, true);
                board.undoLastMove();
                
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                
                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }
            return minEval;
        }
    }
    
    /**
     * ประเมินสถานะของกระดาน
     * @param board กระดานที่จะประเมิน
     * @param depth ความลึกที่เหลือ
     * @return ค่าของกระดาน
     */
    private int evaluate(Board board, int depth) {
        // ถ้าเกมจบแล้ว
        if (board.isGameOver()) {
            // ถ้าเป็นการจน
            if (board.isWhiteTurn()) {
                return -10000 - depth; // ฝ่ายขาวแพ้ (ค่าลบ)
            } else {
                return 10000 + depth; // ฝ่ายขาวชนะ (ค่าบวก)
            }
        }
        
        // ใช้การประเมินปกติของกระดาน
        return board.evaluate();
    }
}
