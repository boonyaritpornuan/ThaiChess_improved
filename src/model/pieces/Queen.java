package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากเรือ (Queen) ในหมากรุกไทย
 * เรือในหมากรุกไทยเคลื่อนที่ได้ 1 ช่องในแนวทแยง
 */
public class Queen extends Piece {
    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 1},
        {1, -1},  {1, 1}
    };
    
    /**
     * สร้างหมากเรือใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Queen(boolean isWhite, int row, int col) {
        super(isWhite ? 'q' : 'Q', isWhite, row, col);
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        addMovesInDirections(board, moves, DIRECTIONS, false); // เรือเคลื่อนที่ได้ 1 ช่องในแนวทแยง
        return moves;
    }
    
    @Override
    public int getValue() {
        return 5; // ค่าของเรือในหมากรุกไทย
    }
    
    @Override
    public int getPositionValue() {
        // เรือควรอยู่ในตำแหน่งที่สามารถควบคุมพื้นที่ได้มาก
        int[][] positionValues;
        
        if (isWhite) {
            positionValues = new int[][] {
                {-20, -10, -10, -5, -5, -10, -10, -20},
                {-10,   0,   0,  0,  0,   0,   0, -10},
                {-10,   0,   5,  5,  5,   5,   0, -10},
                { -5,   0,   5, 10, 10,   5,   0,  -5},
                {  0,   0,   5, 10, 10,   5,   0,  -5},
                {-10,   5,   5,  5,  5,   5,   0, -10},
                {-10,   0,   5,  0,  0,   0,   0, -10},
                {-20, -10, -10, -5, -5, -10, -10, -20}
            };
        } else {
            positionValues = new int[][] {
                {-20, -10, -10, -5, -5, -10, -10, -20},
                {-10,   0,   5,  0,  0,   0,   0, -10},
                {-10,   5,   5,  5,  5,   5,   0, -10},
                {  0,   0,   5, 10, 10,   5,   0,  -5},
                { -5,   0,   5, 10, 10,   5,   0,  -5},
                {-10,   0,   5,  5,  5,   5,   0, -10},
                {-10,   0,   0,  0,  0,   0,   0, -10},
                {-20, -10, -10, -5, -5, -10, -10, -20}
            };
        }
        
        return positionValues[row][col];
    }
}
