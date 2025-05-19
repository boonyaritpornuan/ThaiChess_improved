package model.events;

/**
 * คลาสสำหรับเหตุการณ์ในเกมหมากรุกไทย
 */
public class GameEvent {
    public enum EventType {
        BOARD_CHANGED,
        CHECK,
        CHECKMATE,
        STALEMATE,
        COUNTING_RULE_APPLIED,
        GAME_OVER
    }
    
    private EventType type;
    private boolean isWhiteAffected;
    
    /**
     * สร้างเหตุการณ์ใหม่
     * @param type ประเภทของเหตุการณ์
     */
    public GameEvent(EventType type) {
        this.type = type;
        this.isWhiteAffected = false;
    }
    
    /**
     * สร้างเหตุการณ์ใหม่พร้อมระบุฝ่ายที่ได้รับผลกระทบ
     * @param type ประเภทของเหตุการณ์
     * @param isWhiteAffected true ถ้าฝ่ายขาวได้รับผลกระทบ, false ถ้าฝ่ายดำได้รับผลกระทบ
     */
    public GameEvent(EventType type, boolean isWhiteAffected) {
        this.type = type;
        this.isWhiteAffected = isWhiteAffected;
    }
    
    /**
     * รับประเภทของเหตุการณ์
     * @return ประเภทของเหตุการณ์
     */
    public EventType getType() {
        return type;
    }
    
    /**
     * ตรวจสอบว่าฝ่ายขาวได้รับผลกระทบหรือไม่
     * @return true ถ้าฝ่ายขาวได้รับผลกระทบ, false ถ้าฝ่ายดำได้รับผลกระทบ
     */
    public boolean isWhiteAffected() {
        return isWhiteAffected;
    }
}
