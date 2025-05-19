package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากม้า (Knight) ในหมากรุกไทย
 */
public class Knight extends Piece {
    private static final int[][] MOVES = {
        {-2, -1}, {-2, 1},
        {-1, -2}, {-1, 2},
        {1, -2},  {1, 2},
        {2, -1},  {2, 1}
    };
    
    /**
     * สร้างหมากม้าใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Knight(boolean isWhite, int row, int col) {
        super(isWhite ? 'n' : 'N', isWhite, row, col);
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        
        for (int[] move : MOVES) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            
            if (board.isValidPosition(newRow, newCol)) {
                if (board.isEmpty(newRow, newCol) || board.isEnemy(newRow, newCol, isWhite)) {
                    moves.add(new Move(row, col, newRow, newCol));
                }
            }
        }
        
        return moves;
    }
    
    @Override
    public int getValue() {
        return 3; // ค่าของม้าในหมากรุกไทย
    }
    
    @Override
    public int getPositionValue() {
        // ม้าควรอยู่ในตำแหน่งที่สามารถควบคุมพื้นที่ได้มาก
        int[][] positionValues;
        
        if (isWhite) {
            positionValues = new int[][] {
                {-50, -40, -30, -30, -30, -30, -40, -50},
                {-40, -20,   0,   0,   0,   0, -20, -40},
                {-30,   0,  10,  15,  15,  10,   0, -30},
                {-30,   5,  15,  20,  20,  15,   5, -30},
                {-30,   0,  15,  20,  20,  15,   0, -30},
                {-30,   5,  10,  15,  15,  10,   5, -30},
                {-40, -20,   0,   5,   5,   0, -20, -40},
                {-50, -40, -30, -30, -30, -30, -40, -50}
            };
        } else {
            positionValues = new int[][] {
                {-50, -40, -30, -30, -30, -30, -40, -50},
                {-40, -20,   0,   5,   5,   0, -20, -40},
                {-30,   5,  10,  15,  15,  10,   5, -30},
                {-30,   0,  15,  20,  20,  15,   0, -30},
                {-30,   5,  15,  20,  20,  15,   5, -30},
                {-30,   0,  10,  15,  15,  10,   0, -30},
                {-40, -20,   0,   0,   0,   0, -20, -40},
                {-50, -40, -30, -30, -30, -30, -40, -50}
            };
        }
        
        return positionValues[row][col];
    }
}
