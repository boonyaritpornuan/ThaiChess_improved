package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.GameController;
import model.Board;
import model.Move;
import model.ai.ChessAI;
import model.events.GameEvent;
import model.events.GameListener;

/**
 * คลาสสำหรับส่วนติดต่อผู้ใช้ของเกมหมากรุกไทย
 */
public class GameView extends JFrame implements GameListener {
    private static final int SQUARE_SIZE = 80;
    private static final Color LIGHT_SQUARE_COLOR = new Color(240, 217, 181);
    private static final Color DARK_SQUARE_COLOR = new Color(181, 136, 99);
    private static final Color SELECTED_SQUARE_COLOR = new Color(106, 168, 79, 150);
    private static final Color LEGAL_MOVE_COLOR = new Color(106, 168, 79, 100);
    private static final Color CHECK_COLOR = new Color(244, 67, 54, 150);
    
    private GameController controller;
    private Board board;
    private ChessAI ai;
    
    private JPanel boardPanel;
    private JPanel infoPanel;
    private JLabel statusLabel;
    private JLabel turnLabel;
    private JLabel countingLabel;
    private JButton newGameButton;
    private JButton undoButton;
    private JComboBox<String> difficultyComboBox;
    
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Move> legalMoves;
    
    private BufferedImage[][] pieceImages;
    
    /**
     * สร้าง GameView ใหม่
     * @param controller ตัวควบคุมเกม
     * @param board กระดาน
     * @param ai AI
     */
    public GameView(GameController controller, Board board, ChessAI ai) {
        this.controller = controller;
        this.board = board;
        this.ai = ai;
        
        // ลงทะเบียนเป็นผู้ฟังเหตุการณ์
        board.addGameListener(this);
        
        // โหลดรูปภาพหมาก
        loadPieceImages();
        
        // ตั้งค่าหน้าต่าง
        setTitle("หมากรุกไทย (Makruk)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // สร้างส่วนประกอบ UI
        createComponents();
        
        // จัดวางส่วนประกอบ
        layoutComponents();
        
        // เพิ่มตัวจัดการเหตุการณ์
        addEventHandlers();
        
        // แสดงหน้าต่าง
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // อัปเดตสถานะ
        updateStatus();
    }
    
    /**
     * โหลดรูปภาพหมาก
     */
    private void loadPieceImages() {
        pieceImages = new BufferedImage[2][6];
        
        try {
            // โหลดรูปภาพหมากขาว
            pieceImages[0][0] = ImageIO.read(new File("resources/images/white_king.png"));
            pieceImages[0][1] = ImageIO.read(new File("resources/images/white_queen.png"));
            pieceImages[0][2] = ImageIO.read(new File("resources/images/white_bishop.png"));
            pieceImages[0][3] = ImageIO.read(new File("resources/images/white_knight.png"));
            pieceImages[0][4] = ImageIO.read(new File("resources/images/white_rook.png"));
            pieceImages[0][5] = ImageIO.read(new File("resources/images/white_pawn.png"));
            
            // โหลดรูปภาพหมากดำ
            pieceImages[1][0] = ImageIO.read(new File("resources/images/black_king.png"));
            pieceImages[1][1] = ImageIO.read(new File("resources/images/black_queen.png"));
            pieceImages[1][2] = ImageIO.read(new File("resources/images/black_bishop.png"));
            pieceImages[1][3] = ImageIO.read(new File("resources/images/black_knight.png"));
            pieceImages[1][4] = ImageIO.read(new File("resources/images/black_rook.png"));
            pieceImages[1][5] = ImageIO.read(new File("resources/images/black_pawn.png"));
        } catch (IOException e) {
            System.err.println("ไม่สามารถโหลดรูปภาพหมาก: " + e.getMessage());
            // ใช้รูปภาพเริ่มต้นแทน
            createDefaultPieceImages();
        }
    }
    
    /**
     * สร้างรูปภาพหมากเริ่มต้น (ใช้เมื่อไม่สามารถโหลดรูปภาพได้)
     */
    private void createDefaultPieceImages() {
        String[] symbols = {"K", "Q", "B", "N", "R", "P"};
        Color[] colors = {Color.WHITE, Color.BLACK};
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                BufferedImage image = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(colors[i]);
                g.setFont(new Font("Serif", Font.BOLD, 48));
                FontMetrics fm = g.getFontMetrics();
                int x = (SQUARE_SIZE - fm.stringWidth(symbols[j])) / 2;
                int y = (SQUARE_SIZE - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(symbols[j], x, y);
                g.dispose();
                
                pieceImages[i][j] = image;
            }
        }
    }
    
    /**
     * สร้างส่วนประกอบ UI
     */
    private void createComponents() {
        // สร้างพาเนลกระดาน
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        boardPanel.setPreferredSize(new Dimension(8 * SQUARE_SIZE, 8 * SQUARE_SIZE));
        
        // สร้างพาเนลข้อมูล
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // สร้างป้ายสถานะ
        statusLabel = new JLabel("เริ่มเกมใหม่");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // สร้างป้ายตา
        turnLabel = new JLabel("ตาของผู้เล่น (ขาว)");
        turnLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // สร้างป้ายการนับ
        countingLabel = new JLabel("การนับ: 0/64");
        countingLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        countingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // สร้างปุ่มเกมใหม่
        newGameButton = new JButton("เกมใหม่");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // สร้างปุ่มย้อนกลับ
        undoButton = new JButton("ย้อนกลับ");
        undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // สร้างคอมโบบ็อกซ์ระดับความยาก
        String[] difficulties = {"ง่าย", "ปานกลาง", "ยาก", "เชี่ยวชาญ"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setSelectedIndex(1); // ปานกลาง
        difficultyComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyComboBox.setMaximumSize(new Dimension(150, 30));
    }
    
    /**
     * จัดวางส่วนประกอบ
     */
    private void layoutComponents() {
        // จัดวางพาเนลข้อมูล
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(turnLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(countingLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(newGameButton);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(undoButton);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(new JLabel("ระดับความยาก:"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(difficultyComboBox);
        
        // จัดวางหน้าต่าง
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(boardPanel, BorderLayout.CENTER);
        contentPane.add(infoPanel, BorderLayout.EAST);
    }
    
    /**
     * เพิ่มตัวจัดการเหตุการณ์
     */
    private void addEventHandlers() {
        // เพิ่มตัวจัดการเหตุการณ์คลิกกระดาน
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
            }
        });
        
        // เพิ่มตัวจัดการเหตุการณ์ปุ่มเกมใหม่
        newGameButton.addActionListener(e -> {
            controller.newGame();
            selectedRow = -1;
            selectedCol = -1;
            legalMoves = null;
            updateStatus();
            boardPanel.repaint();
        });
        
        // เพิ่มตัวจัดการเหตุการณ์ปุ่มย้อนกลับ
        undoButton.addActionListener(e -> {
            // ย้อนกลับการเคลื่อนที่ของผู้เล่นและ AI
            controller.undoMove();
            controller.undoMove();
            selectedRow = -1;
            selectedCol = -1;
            legalMoves = null;
            updateStatus();
            boardPanel.repaint();
        });
        
        // เพิ่มตัวจัดการเหตุการณ์คอมโบบ็อกซ์ระดับความยาก
        difficultyComboBox.addActionListener(e -> {
            int index = difficultyComboBox.getSelectedIndex();
            ChessAI.Difficulty difficulty;
            
            switch (index) {
                case 0:
                    difficulty = ChessAI.Difficulty.EASY;
                    break;
                case 1:
                    difficulty = ChessAI.Difficulty.MEDIUM;
                    break;
                case 2:
                    difficulty = ChessAI.Difficulty.HARD;
                    break;
                case 3:
                    difficulty = ChessAI.Difficulty.EXPERT;
                    break;
                default:
                    difficulty = ChessAI.Difficulty.MEDIUM;
            }
            
            ai.setDifficulty(difficulty);
        });
    }
    
    /**
     * จัดการเหตุการณ์คลิกกระดาน
     * @param x พิกัด x
     * @param y พิกัด y
     */
    private void handleBoardClick(int x, int y) {
        // ถ้าเกมจบแล้ว หรือไม่ใช่ตาของผู้เล่น ไม่ต้องทำอะไร
        if (board.isGameOver() || !board.isWhiteTurn()) {
            return;
        }
        
        // แปลงพิกัดเป็นตำแหน่งบนกระดาน
        int col = x / SQUARE_SIZE;
        int row = y / SQUARE_SIZE;
        
        // ตรวจสอบว่าตำแหน่งอยู่ในกระดาน
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return;
        }
        
        // ถ้ายังไม่ได้เลือกหมาก
        if (selectedRow == -1 && selectedCol == -1) {
            // ตรวจสอบว่ามีหมากของผู้เล่นที่ตำแหน่งที่คลิก
            if (board.getPiece(row, col) != null && board.getPiece(row, col).isWhite()) {
                // เลือกหมาก
                selectedRow = row;
                selectedCol = col;
                legalMoves = board.getLegalMoves(row, col);
                boardPanel.repaint();
            }
        } else {
            // ถ้าคลิกที่หมากของผู้เล่นอีกตัว
            if (board.getPiece(row, col) != null && board.getPiece(row, col).isWhite()) {
                // เลือกหมากใหม่
                selectedRow = row;
                selectedCol = col;
                legalMoves = board.getLegalMoves(row, col);
                boardPanel.repaint();
            } else {
                // ตรวจสอบว่าการเคลื่อนที่ถูกต้อง
                boolean validMove = false;
                for (Move move : legalMoves) {
                    if (move.toRow == row && move.toCol == col) {
                        validMove = true;
                        break;
                    }
                }
                
                if (validMove) {
                    // ทำการเคลื่อนที่
                    controller.makeMove(selectedRow, selectedCol, row, col);
                    
                    // รีเซ็ตการเลือก
                    selectedRow = -1;
                    selectedCol = -1;
                    legalMoves = null;
                    
                    // อัปเดตกระดาน
                    boardPanel.repaint();
                    
                    // ตรวจสอบว่าเกมจบหรือไม่
                    if (!board.isGameOver()) {
                        // ให้ AI เคลื่อนที่
                        SwingUtilities.invokeLater(() -> {
                            controller.makeAIMove();
                        });
                    }
                }
            }
        }
    }
    
    /**
     * วาดกระดาน
     * @param g กราฟิกส์
     */
    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // วาดช่อง
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // กำหนดสีช่อง
                if ((row + col) % 2 == 0) {
                    g2d.setColor(LIGHT_SQUARE_COLOR);
                } else {
                    g2d.setColor(DARK_SQUARE_COLOR);
                }
                
                // วาดช่อง
                g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                
                // ถ้าเป็นช่องที่เลือก
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(SELECTED_SQUARE_COLOR);
                    g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
                
                // ถ้าเป็นช่องที่สามารถเคลื่อนที่ได้
                if (legalMoves != null) {
                    for (Move move : legalMoves) {
                        if (move.toRow == row && move.toCol == col) {
                            g2d.setColor(LEGAL_MOVE_COLOR);
                            g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                            break;
                        }
                    }
                }
                
                // ถ้าเป็นช่องที่มีขุนที่ถูกรุก
                if (board.getPiece(row, col) != null && 
                    board.getPiece(row, col) instanceof model.pieces.King &&
                    ((board.getPiece(row, col).isWhite() && board.isWhiteTurn()) ||
                     (!board.getPiece(row, col).isWhite() && !board.isWhiteTurn()))) {
                    
                    if (board.isInCheck(board.getPiece(row, col).isWhite())) {
                        g2d.setColor(CHECK_COLOR);
                        g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                }
            }
        }
        
        // วาดหมาก
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPiece(row, col) != null) {
                    drawPiece(g2d, row, col);
                }
            }
        }
    }
    
    /**
     * วาดหมาก
     * @param g2d กราฟิกส์ 2D
     * @param row แถว
     * @param col คอลัมน์
     */
    private void drawPiece(Graphics2D g2d, int row, int col) {
        char symbol = board.getPiece(row, col).getSymbol();
        boolean isWhite = board.getPiece(row, col).isWhite();
        
        int colorIndex = isWhite ? 0 : 1;
        int pieceIndex;
        
        // กำหนดดัชนีหมาก
        switch (Character.toLowerCase(symbol)) {
            case 'k': pieceIndex = 0; break; // ขุน
            case 'q': pieceIndex = 1; break; // เรือ
            case 'b': pieceIndex = 2; break; // โคน
            case 'n': pieceIndex = 3; break; // ม้า
            case 'r': pieceIndex = 4; break; // เม็ด
            case 'p': pieceIndex = 5; break; // เบี้ย
            case 'u': pieceIndex = 1; break; // เบี้ยหงาย (ใช้รูปเรือ)
            default: return;
        }
        
        // วาดหมาก
        g2d.drawImage(pieceImages[colorIndex][pieceIndex], 
                      col * SQUARE_SIZE, 
                      row * SQUARE_SIZE, 
                      SQUARE_SIZE, 
                      SQUARE_SIZE, 
                      null);
    }
    
    /**
     * อัปเดตสถานะ
     */
    private void updateStatus() {
        // อัปเดตป้ายตา
        if (board.isWhiteTurn()) {
            turnLabel.setText("ตาของผู้เล่น (ขาว)");
        } else {
            turnLabel.setText("ตาของคอมพิวเตอร์ (ดำ)");
        }
        
        // อัปเดตป้ายการนับ
        countingLabel.setText("การนับ: " + board.getMovesSinceCapture() + "/64");
        
        // อัปเดตป้ายสถานะ
        if (board.isGameOver()) {
            if (board.isCheckmate(true)) {
                statusLabel.setText("คอมพิวเตอร์ชนะ (จน)");
            } else if (board.isCheckmate(false)) {
                statusLabel.setText("ผู้เล่นชนะ (จน)");
            } else if (board.isStalemate(true) || board.isStalemate(false)) {
                statusLabel.setText("เสมอ (อับ)");
            } else if (board.isCountingRuleApplied()) {
                statusLabel.setText("เสมอ (นับครบ)");
            }
        } else {
            if (board.isInCheck(true)) {
                statusLabel.setText("ผู้เล่นถูกรุก");
            } else if (board.isInCheck(false)) {
                statusLabel.setText("คอมพิวเตอร์ถูกรุก");
            } else {
                statusLabel.setText("กำลังเล่น");
            }
        }
    }
    
    // ตัวจัดการเหตุการณ์ GameListener
    
    @Override
    public void onBoardChanged() {
        SwingUtilities.invokeLater(() -> {
            updateStatus();
            boardPanel.repaint();
        });
    }
    
    @Override
    public void onCheck(boolean isWhiteInCheck) {
        SwingUtilities.invokeLater(() -> {
            if (isWhiteInCheck) {
                statusLabel.setText("ผู้เล่นถูกรุก");
            } else {
                statusLabel.setText("คอมพิวเตอร์ถูกรุก");
            }
            boardPanel.repaint();
        });
    }
    
    @Override
    public void onCheckmate(boolean isWhiteCheckmated) {
        SwingUtilities.invokeLater(() -> {
            if (isWhiteCheckmated) {
                statusLabel.setText("คอมพิวเตอร์ชนะ (จน)");
            } else {
                statusLabel.setText("ผู้เล่นชนะ (จน)");
            }
            boardPanel.repaint();
        });
    }
    
    @Override
    public void onStalemate() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("เสมอ (อับ)");
            boardPanel.repaint();
        });
    }
    
    @Override
    public void onCountingRuleApplied() {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("เสมอ (นับครบ)");
            boardPanel.repaint();
        });
    }
}
