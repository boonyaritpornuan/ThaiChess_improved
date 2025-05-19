package model.events;

/**
 * อินเตอร์เฟซสำหรับผู้ฟังเหตุการณ์ในเกมหมากรุกไทย
 */
public interface GameListener {
    
    /**
     * เรียกเมื่อกระดานเปลี่ยนแปลง
     */
    void onBoardChanged();
    
    /**
     * เรียกเมื่อมีการรุก
     * @param isWhiteInCheck true ถ้าฝ่ายขาวถูกรุก, false ถ้าฝ่ายดำถูกรุก
     */
    void onCheck(boolean isWhiteInCheck);
    
    /**
     * เรียกเมื่อมีการจน
     * @param isWhiteCheckmated true ถ้าฝ่ายขาวถูกจน, false ถ้าฝ่ายดำถูกจน
     */
    void onCheckmate(boolean isWhiteCheckmated);
    
    /**
     * เรียกเมื่อมีการอับ
     */
    void onStalemate();
    
    /**
     * เรียกเมื่อกฎการนับครบถูกใช้
     */
    void onCountingRuleApplied();
}
