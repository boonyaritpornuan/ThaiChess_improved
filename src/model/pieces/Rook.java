package model.pieces;

import java.util.ArrayList;
import java.util.List;
import model.Board;
import model.Move;

/**
 * คลาสสำหรับหมากเม็ด (Rook) ในหมากรุกไทย
 */
public class Rook extends Piece {
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0},
        {0, -1}, {0, 1}
    };
    
    /**
     * สร้างหมากเม็ดใหม่
     * @param isWhite เป็นหมากขาวหรือไม่
     * @param row แถวเริ่มต้น
     * @param col คอลัมน์เริ่มต้น
     */
    public Rook(boolean isWhite, int row, int col) {
        super(isWhite ? 'r' : 'R', isWhite, row, col);
    }
    
    @Override
    public List<Move> getLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        addMovesInDirections(board, moves, DIRECTIONS, true); // เม็ดเคลื่อนที่ได้หลายช่องในแนวตั้งและแนวนอน
        return moves;
    }
    
    @Override
    public int getValue() {
        return 5; // ค่าของเม็ดในหมากรุกไทย
    }
    
    @Override
    public int getPositionValue() {
        // เม็ดควรอยู่ในตำแหน่งที่สามารถควบคุมแถวและคอลัมน์ได้มาก
        int[][] positionValues;
        
        if (isWhite) {
            positionValues = new int[][] {
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 5, 10, 10, 10, 10, 10, 10,  5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                { 0,  0,  0,  5,  5,  0,  0,  0}
            };
        } else {
            positionValues = new int[][] {
                { 0,  0,  0,  5,  5,  0,  0,  0},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                {-5,  0,  0,  0,  0,  0,  0, -5},
                { 5, 10, 10, 10, 10, 10, 10,  5},
                { 0,  0,  0,  0,  0,  0,  0,  0}
            };
        }
        
        return positionValues[row][col];
    }
}
