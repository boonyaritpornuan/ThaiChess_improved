package model;

/**
 * คลาสหลักสำหรับเริ่มต้นเกมหมากรุกไทย
 */
public class Main {
    
    /**
     * เมธอดหลักสำหรับเริ่มต้นเกม
     * @param args อาร์กิวเมนต์จากคอมมานด์ไลน์
     */
    public static void main(String[] args) {
        // สร้างกระดาน
        Board board = new Board();
        
        // สร้าง AI
        model.ai.ChessAI ai = new model.ai.ChessAI(model.ai.ChessAI.Difficulty.MEDIUM);
        
        // สร้างตัวควบคุม
        controller.GameController controller = new controller.GameController(board, ai);
        
        // สร้าง view
        view.GameView view = new view.GameView(controller, board, ai);
        
        // ตั้งค่า view ให้กับตัวควบคุม
        controller.setView(view);
        
        System.out.println("เริ่มเกมหมากรุกไทย (Makruk)");
    }
}
