package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากโคน (Bishop) ในหมากรุกไทย
 */
public class Bishop extends Piece {
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 1},
        {1, -1},  {1, 1}
    };
    
    /**
     * สร้างหมากโคนใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite ? 'b' : 'B', isWhite, row, col);
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        addMovesInDirections(board, moves, DIRECTIONS, true); // โคนเคลื่อนที่ได้หลายช่องในแนวทแยง
        return moves;
    }
    
    @Override
    public int getValue() {
        return 3; // ค่าของโคนในหมากรุกไทย
    }
    
    @Override
    public int getPositionValue() {
        // โคนควรอยู่ในตำแหน่งที่สามารถควบคุมเส้นทแยงได้มาก
        int[][] positionValues;
        
        if (isWhite) {
            positionValues = new int[][] {
                {-20, -10, -10, -10, -10, -10, -10, -20},
                {-10,   0,   0,   0,   0,   0,   0, -10},
                {-10,   0,  10,  10,  10,  10,   0, -10},
                {-10,   5,  10,  10,  10,  10,   5, -10},
                {-10,   0,  10,  10,  10,  10,   0, -10},
                {-10,  10,  10,  10,  10,  10,  10, -10},
                {-10,   5,   0,   0,   0,   0,   5, -10},
                {-20, -10, -10, -10, -10, -10, -10, -20}
            };
        } else {
            positionValues = new int[][] {
                {-20, -10, -10, -10, -10, -10, -10, -20},
                {-10,   5,   0,   0,   0,   0,   5, -10},
                {-10,  10,  10,  10,  10,  10,  10, -10},
                {-10,   0,  10,  10,  10,  10,   0, -10},
                {-10,   5,  10,  10,  10,  10,   5, -10},
                {-10,   0,  10,  10,  10,  10,   0, -10},
                {-10,   0,   0,   0,   0,   0,   0, -10},
                {-20, -10, -10, -10, -10, -10, -10, -20}
            };
        }
        
        return positionValues[row][col];
    }
}
