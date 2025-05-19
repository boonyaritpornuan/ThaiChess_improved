package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากขุน (King) ในหมากรุกไทย
 */
public class King extends Piece {
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},           {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };
    
    /**
     * สร้างหมากขุนใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public King(boolean isWhite, int row, int col) {
        super(isWhite ? 'k' : 'K', isWhite, row, col);
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        addMovesInDirections(board, moves, DIRECTIONS, false);
        return moves;
    }
    
    @Override
    public int getValue() {
        return 10000; // ค่าของขุนสูงมาก เพราะการเสียขุนคือการแพ้
    }
    
    @Override
    public int getPositionValue() {
        // ขุนควรอยู่ในตำแหน่งที่ปลอดภัย
        // ในช่วงต้นเกม ขุนควรอยู่ด้านหลัง
        // ในช่วงท้ายเกม ขุนควรมีส่วนร่วมมากขึ้น
        
        int[][] positionValues;
        
        if (isWhite) {
            positionValues = new int[][] {
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-20, -30, -30, -40, -40, -30, -30, -20},
                {-10, -20, -20, -20, -20, -20, -20, -10},
                { 20,   0,   0,   0,   0,   0,   0,  20},
                { 20,  30,  10,   0,   0,  10,  30,  20}
            };
        } else {
            positionValues = new int[][] {
                { 20,  30,  10,   0,   0,  10,  30,  20},
                { 20,   0,   0,   0,   0,   0,   0,  20},
                {-10, -20, -20, -20, -20, -20, -20, -10},
                {-20, -30, -30, -40, -40, -30, -30, -20},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30}
            };
        }
        
        return positionValues[row][col];
    }
}
