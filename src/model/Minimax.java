package model; // เพิ่มแพ็กเกจให้ตรงกับโฟลเดอร์ src/model

import java.util.List; // นำเข้า List

public class Minimax {
    private static final int MAX_DEPTH = 3; // กำหนด MAX_DEPTH

    public Move findBestMove(Board board) {
        List<Move> legalMoves = board.generateMoves();
        if (legalMoves.isEmpty()) {
            System.out.println("Minimax: No legal moves available.");
            return null;
        }

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Move move : legalMoves) {
            char captured = board.getBoard()[move.toRow][move.toCol];
            board.makeMove(move);
            int value = minimax(board, MAX_DEPTH - 1, false);
            board.undoMove(move, captured);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }
        System.out.println("Minimax: Best move selected: " + bestMove + " with value: " + bestValue);
        return bestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        if (depth == 0 || board.isGameOver()) {
            return board.evaluate();
        }

        List<Move> legalMoves = board.generateMoves();
        if (legalMoves.isEmpty()) {
            return board.evaluate();
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                char captured = board.getBoard()[move.toRow][move.toCol];
                board.makeMove(move);
                int eval = minimax(board, depth - 1, false);
                board.undoMove(move, captured);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                char captured = board.getBoard()[move.toRow][move.toCol];
                board.makeMove(move);
                int eval = minimax(board, depth - 1, true);
                board.undoMove(move, captured);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }
}