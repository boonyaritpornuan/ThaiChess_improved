package model.rules;

import model.Board;

/**
 * คลาสสำหรับจัดการกฎการนับครบในหมากรุกไทย
 */
public class CountingRules {
    private Board board;
    
    /**
     * สร้าง CountingRules ใหม่
     * @param board กระดานที่จะตรวจสอบ
     */
    public CountingRules(Board board) {
        this.board = board;
    }
    
    /**
     * ตรวจสอบว่ากฎการนับครบถูกใช้หรือไม่
     * @return true ถ้ากฎการนับครบถูกใช้, false ถ้าไม่ถูกใช้
     */
    public boolean isCountingRuleApplied() {
        return board.getMovesSinceCapture() >= 64; // นับครบ 64 ตา
    }
    
    /**
     * คำนวณจำนวนตาที่ฝ่ายที่มีหมากมากกว่าต้องชนะ
     * @return จำนวนตาที่ต้องชนะ
     */
    public int getMovesToWin() {
        // คำนวณจำนวนหมากของแต่ละฝ่าย
        int whitePieces = countPieces(true);
        int blackPieces = countPieces(false);
        
        // ถ้าจำนวนหมากเท่ากัน ให้เสมอ
        if (whitePieces == blackPieces) {
            return 0;
        }
        
        // ฝ่ายที่มีหมากมากกว่าต้องชนะภายในจำนวนตาที่กำหนด
        boolean whiteHasMore = whitePieces > blackPieces;
        int difference = Math.abs(whitePieces - blackPieces);
        
        // กำหนดจำนวนตาตามความแตกต่างของหมาก
        if (difference == 1) {
            return 64; // ถ้ามีหมากมากกว่า 1 ตัว ต้องชนะภายใน 64 ตา
        } else if (difference == 2) {
            return 32; // ถ้ามีหมากมากกว่า 2 ตัว ต้องชนะภายใน 32 ตา
        } else {
            return 16; // ถ้ามีหมากมากกว่า 3 ตัวขึ้นไป ต้องชนะภายใน 16 ตา
        }
    }
    
    /**
     * นับจำนวนหมากของฝ่ายที่กำหนด
     * @param isWhite true ถ้าเป็นฝ่ายขาว, false ถ้าเป็นฝ่ายดำ
     * @return จำนวนหมากของฝ่ายที่กำหนด
     */
    private int countPieces(boolean isWhite) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) != null && board.getPiece(i, j).isWhite() == isWhite) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * ตรวจสอบว่าฝ่ายที่มีหมากมากกว่าชนะหรือไม่
     * @return true ถ้าฝ่ายที่มีหมากมากกว่าชนะ, false ถ้าไม่ชนะ
     */
    public boolean hasStrongerSideWon() {
        // ถ้ายังไม่ถึงกฎการนับครบ ให้ยังไม่มีฝ่ายใดชนะ
        if (!isCountingRuleApplied()) {
            return false;
        }
        
        // คำนวณจำนวนหมากของแต่ละฝ่าย
        int whitePieces = countPieces(true);
        int blackPieces = countPieces(false);
        
        // ถ้าจำนวนหมากเท่ากัน ให้เสมอ
        if (whitePieces == blackPieces) {
            return false;
        }
        
        // ฝ่ายที่มีหมากมากกว่าต้องชนะภายในจำนวนตาที่กำหนด
        int movesToWin = getMovesToWin();
        
        // ถ้าเกินจำนวนตาที่กำหนด ให้ฝ่ายที่มีหมากมากกว่าแพ้
        return board.getMovesSinceCapture() <= movesToWin;
    }
}
