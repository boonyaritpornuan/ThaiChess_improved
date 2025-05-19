package model.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * คลาสสำหรับจัดการประวัติคำสั่งในหมากรุกไทย
 * ใช้ Command Pattern เพื่อให้สามารถย้อนกลับและทำซ้ำได้
 */
public class CommandHistory {
    private List<MoveCommand> history;
    private int currentIndex;
    
    /**
     * สร้างประวัติคำสั่งใหม่
     */
    public CommandHistory() {
        history = new ArrayList<>();
        currentIndex = -1;
    }
    
    /**
     * เพิ่มคำสั่งใหม่และดำเนินการ
     * @param command คำสั่งที่จะเพิ่ม
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean executeCommand(MoveCommand command) {
        // ลบคำสั่งที่อยู่หลังตำแหน่งปัจจุบัน (ถ้ามี)
        if (currentIndex < history.size() - 1) {
            history = new ArrayList<>(history.subList(0, currentIndex + 1));
        }
        
        // ดำเนินการคำสั่ง
        boolean success = command.execute();
        if (success) {
            history.add(command);
            currentIndex = history.size() - 1;
        }
        
        return success;
    }
    
    /**
     * ย้อนกลับคำสั่งล่าสุด
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean undo() {
        if (currentIndex < 0) {
            return false;
        }
        
        boolean success = history.get(currentIndex).undo();
        if (success) {
            currentIndex--;
        }
        
        return success;
    }
    
    /**
     * ทำซ้ำคำสั่งที่ถูกย้อนกลับ
     * @return true ถ้าสำเร็จ, false ถ้าไม่สำเร็จ
     */
    public boolean redo() {
        if (currentIndex >= history.size() - 1) {
            return false;
        }
        
        boolean success = history.get(currentIndex + 1).execute();
        if (success) {
            currentIndex++;
        }
        
        return success;
    }
    
    /**
     * ล้างประวัติคำสั่ง
     */
    public void clear() {
        history.clear();
        currentIndex = -1;
    }
    
    /**
     * ตรวจสอบว่าสามารถย้อนกลับได้หรือไม่
     * @return true ถ้าสามารถย้อนกลับได้, false ถ้าไม่สามารถย้อนกลับได้
     */
    public boolean canUndo() {
        return currentIndex >= 0;
    }
    
    /**
     * ตรวจสอบว่าสามารถทำซ้ำได้หรือไม่
     * @return true ถ้าสามารถทำซ้ำได้, false ถ้าไม่สามารถทำซ้ำได้
     */
    public boolean canRedo() {
        return currentIndex < history.size() - 1;
    }
    
    /**
     * รับรายการคำสั่งทั้งหมด
     * @return รายการคำสั่งทั้งหมด
     */
    public List<MoveCommand> getHistory() {
        return new ArrayList<>(history);
    }
}
