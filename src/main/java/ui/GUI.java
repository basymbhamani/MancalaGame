package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import mancala.AyoRules;
import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import mancala.KalahRules;
import mancala.MancalaGame;
import mancala.Player;
import mancala.Saver;
import mancala.UserProfile;

public class GUI extends JFrame {
    private JButton[][] buttons;
    private JLabel storeLabelPlayer1;
    private JLabel storeLabelPlayer2;
    private JLabel currentPlayerLabel;
    private MancalaGame mancalaGame = new MancalaGame();
    private UserProfile profile1;
    private UserProfile profile2;
    private JPanel profilePanel;
    public GUI() {
        initializeGame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mancala Game");
        setLayout(new BorderLayout());

        
        profile1 = new UserProfile(mancalaGame.getPlayers().get(0).getName());
        profile2 = new UserProfile(mancalaGame.getPlayers().get(1).getName());

        JPanel buttonPanel = makeButtonGrid(6, 2);
        add(buttonPanel, BorderLayout.CENTER);

        profilePanel = new JPanel();
        profilePanel.setLayout(new GridLayout(2, 1));
        add(profilePanel, BorderLayout.EAST);

        displayProfiles();

        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit Game");



        saveButton.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog("Enter the filename to save the game:");
            if (filename != null && !filename.isEmpty()) {
                saveGame(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Game not saved.");
            }

            filename = JOptionPane.showInputDialog("Enter the filename to save profile 1");
            if (filename != null && !filename.isEmpty()) {
                saveUserProfile1(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 1 not saved.");
            }

            filename = JOptionPane.showInputDialog("Enter the filename to save profile 2");
            if (filename != null && !filename.isEmpty()) {
                saveUserProfile2(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 2 not saved.");
            }

            displayProfiles();
        });

        loadButton.addActionListener(e -> {
            String filename = JOptionPane.showInputDialog("Enter the filename to load the game:");
            if (filename != null && !filename.isEmpty()) {
                loadGame(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Game not loaded.");
            }

            filename = JOptionPane.showInputDialog("Enter the filename to load profile 1");
            if (filename != null && !filename.isEmpty()) {
                loadUserProfile1(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 1 not loaded.");
            }

            filename = JOptionPane.showInputDialog("Enter the filename to load profile 2");
            if (filename != null && !filename.isEmpty()) {
                loadUserProfile2(filename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 2 not loaded.");
            }

            displayProfiles();
        });

        quitButton.addActionListener(e -> quitGame());

        storeLabelPlayer1 = new JLabel();
        storeLabelPlayer2 = new JLabel();
        currentPlayerLabel = new JLabel("Current Player: " + mancalaGame.getCurrentPlayer().getName());
        

        JPanel controlPanel = new JPanel();
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(quitButton);
        controlPanel.add(profilePanel);
        
        add(controlPanel, BorderLayout.SOUTH);
        add(currentPlayerLabel, BorderLayout.NORTH);
        add(storeLabelPlayer1, BorderLayout.EAST);
        add(storeLabelPlayer2, BorderLayout.WEST);
        //add(profilePanel, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null); // Center the frame
        refreshGUI();
        setVisible(true);
        refreshGUI();

        
    }

    private void displayProfiles() {
        // Clear existing profiles in the profilePanel

        if (profilePanel != null) {
            profilePanel.removeAll();
        }
    
        // Add new labels for user profiles
        JLabel profileLabel1 = new JLabel("Player 1 - Name: " + profile1.getUserName() +
                ", Kalah Games Won: " + profile1.getKalahGamesWon() +
                ", Ayo Games Won: " + profile1.getAyoGamesWon());
        JLabel profileLabel2 = new JLabel("Player 2 - Name: " + profile2.getUserName() +
                ", Kalah Games Won: " + profile2.getKalahGamesWon() +
                ", Ayo Games Won: " + profile2.getAyoGamesWon());

        // Add labels to the profilePanel
        profilePanel.add(profileLabel1);
        profilePanel.add(profileLabel2);

        // Update the GUI
        profilePanel.revalidate();
        profilePanel.repaint();
    }

    private void initializeGame() {

        // Set up players and board
        Player p1 = new Player();
        Player p2 = new Player();

        int loadProfilesOption = JOptionPane.showConfirmDialog(
                this,
                "Do you want to load user profiles?",
                "Load Profiles",
                JOptionPane.YES_NO_OPTION
        );

        if (loadProfilesOption == JOptionPane.YES_OPTION) {
            String loadFilename = JOptionPane.showInputDialog("Enter the filename to load user profile 1:");
            if (loadFilename != null && !loadFilename.isEmpty()) {
                loadUserProfile1(loadFilename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename.");
    
                String playerName1 = JOptionPane.showInputDialog("Enter Player 1's name:");
                p1.setName(playerName1);

                profile1 = new UserProfile(playerName1);

            }

            loadFilename = JOptionPane.showInputDialog("Enter the filename to load user profile 2:");
            if (loadFilename != null && !loadFilename.isEmpty()) {
                loadUserProfile2(loadFilename);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Invalid filename.");
    

                String playerName2 = JOptionPane.showInputDialog("Enter Player 2's name:");
                p2.setName(playerName2);
                profile2 = new UserProfile(playerName2);
            }
        } else {
            // Initialize default profiles if the user chooses not to load
            String playerName1 = JOptionPane.showInputDialog("Enter Player 1's name:");
            p1.setName(playerName1);

            String playerName2 = JOptionPane.showInputDialog("Enter Player 2's name:");
            p2.setName(playerName2);

            profile1 = new UserProfile(playerName1);
            profile2 = new UserProfile(playerName2);
            
        }

        // Prompt user for player names
        mancalaGame.setPlayers(p1, p2);
       
        // Set up board based on user's choice
        Object[] options = {"Kalah", "Ayo"};
        int boardChoice = JOptionPane.showOptionDialog(null, "Choose a game variant:",
                "Game Variant", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (boardChoice == 0) {
            mancalaGame.setBoard(new KalahRules());
        } else {
            mancalaGame.setBoard(new AyoRules());
        }
    }

    private void loadUserProfile1(String filename) {
        try {
            UserProfile loadedProfile1 = (UserProfile) Saver.loadObject(filename);
            if (loadedProfile1 != null) {
                profile1 = loadedProfile1;
            } else {
                JOptionPane.showMessageDialog(this, "Error loading user profiles.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading user profiles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserProfile2(String filename) {
        try {
            UserProfile loadedProfile2 = (UserProfile) Saver.loadObject(filename);
            if (loadedProfile2 != null) {
                profile2 = loadedProfile2;
            } else {
                JOptionPane.showMessageDialog(this, "Error loading user profiles.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading user profiles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUserProfile1(String filename) {
        try {
            Saver.saveObject(profile1, filename);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveUserProfile2(String filename) {
        try {
            Saver.saveObject(profile2, filename);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving user profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitGame() {
        int option = JOptionPane.showOptionDialog(
                this,
                "Do you want to save the game before quitting?",
                "Quit Game",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Save and Quit", "Quit without Saving", "Cancel"},
                "Save and Quit"
        );
    
        switch (option) {
            case JOptionPane.YES_OPTION:
                // Save the game and then return to the main menu
                String filename = JOptionPane.showInputDialog("Enter the filename to save the game:");
                if (filename != null && !filename.isEmpty()) {
                    saveGame(filename);
                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Game not saved.");
                }
                filename = JOptionPane.showInputDialog("Enter the filename to save profile 1");
                if (filename != null && !filename.isEmpty()) {
                    saveUserProfile1(filename);;
                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 1 not saved.");
                }
                
                filename = JOptionPane.showInputDialog("Enter the filename to save profile 2");
                if (filename != null && !filename.isEmpty()) {
                    saveUserProfile2(filename);
                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid filename. Profile 2 not saved.");
                }

                mancalaGame.getBoard().resetBoard();
                refreshGUI();
                initializeGame();
                displayProfiles();
                refreshGUI();
                break;   
            case JOptionPane.NO_OPTION:
                // Return to the main menu without saving
                mancalaGame.getBoard().resetBoard();
                refreshGUI();
                initializeGame();
                displayProfiles();
                refreshGUI();
                break;
            // If the user selects cancel or closes the dialog, do nothing
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                break;
        }
    }
    

    private JPanel makeButtonGrid(int wide, int tall) {
        JPanel panel = new JPanel();
        buttons = new JButton[wide][tall];
        panel.setLayout(new GridLayout(tall, wide));

        for (int x = 0; x < wide; x++) {
            buttons[x][0] = new JButton();
            buttons[x][0].setPreferredSize(new Dimension(50, 50)); // Adjust size as needed
            buttons[x][0].addActionListener(new ButtonClickListener(0, x).getActionListener());
            panel.add(buttons[x][0]);
        }

        for (int x = 0; x < wide; x++) {
            buttons[wide-x-1][1] = new JButton();
            buttons[wide-x-1][1].setPreferredSize(new Dimension(50, 50)); // Adjust size as needed
            buttons[wide-x-1][1].addActionListener(new ButtonClickListener(1, wide-x-1).getActionListener());
            panel.add(buttons[wide-x-1][1]);
        }
        
        // for (int y = 0; y < tall; y++) {
        //     for (int x = 0; x < wide; x++) {
        //         buttons[x][y] = new JButton();
        //         buttons[x][y].setPreferredSize(new Dimension(50, 50)); // Adjust size as needed
        //         buttons[x][y].addActionListener(new ButtonClickListener(y, x));
        //         panel.add(buttons[x][y]);
        //     }
        // }
        return panel;
    }

    private class ButtonClickListener {
        private int row;
        private int col;
    
        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
    
        // Using lambda notation for actionPerformed
        public ActionListener getActionListener() {
            return (ActionEvent e) -> {
                int pitNumber = row * 6 + col + 1; // Assuming each button corresponds to a pit
    
                try {
                    mancalaGame.move(pitNumber);
                    refreshGUI();
                } catch (InvalidMoveException ex) {
                    JOptionPane.showMessageDialog(GUI.this, "Invalid move. Try again.");
                }
    
                if (mancalaGame.isGameOver()) {
                    displayWinner();
                }
                refreshGUI();
            };
        }
    }


    private void displayWinner() {
        int gameRules;
        if (mancalaGame.getBoard() instanceof KalahRules) {
            gameRules = 1;
            profile1.incrementKalahGamesPlayed();
            profile2.incrementKalahGamesPlayed();
        } else {
            gameRules = 2;
            profile1.incrementAyoGamesPlayed();
            profile2.incrementAyoGamesPlayed();
        }
        
        

        try {
            Player winner = mancalaGame.getWinner();
            String message;
            if (winner == null) {
                message = "Game over! It's a tie!";
            } else {
                message = "Game over! The winner is: " + winner.getName();
            }

            if (winner.getName().equals(profile1.getUserName())) {
                if (gameRules == 1) {
                    profile1.incrementKalahGamesWon();
                } else {
                    profile1.incrementAyoGamesWon();
                }
            } else if (winner.getName().equals(profile2.getUserName())) {
                if (gameRules == 1) {
                    profile2.incrementKalahGamesWon();
                } else {
                    profile2.incrementAyoGamesWon();
                }
            }
    
            int option = JOptionPane.showOptionDialog(
                    this,
                    message + "\nDo you want to play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Play Again", "Return to Main Screen"},
                    "Play Again"
            );
    
            if (option == JOptionPane.YES_OPTION) {
                // Reset the game and start a new one
                mancalaGame.getBoard().resetBoard();
                refreshGUI();
            } else {
                mancalaGame.getBoard().resetBoard();
                refreshGUI();
                initializeGame();
                displayProfiles();
                refreshGUI();

                // Return to the main screen or exit the application
                // Add your logic here
            }
        } catch (GameNotOverException e) {
            JOptionPane.showMessageDialog(this, "The game is not over yet.");
        }
    }
    


    private void saveGame(String filename) {
        try {
            Saver.saveObject(mancalaGame, filename);
            JOptionPane.showMessageDialog(this, "Game saved successfully.");
            refreshGUI();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame(String filename) {
        try {
            MancalaGame loadedGame = (MancalaGame) Saver.loadObject(filename);
            mancalaGame = loadedGame;
            JOptionPane.showMessageDialog(this, "Game loaded successfully.");
            refreshGUI();
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshGUI() {
        for (int i = 1; i <= 12; i++) {
            int stoneCount = mancalaGame.getBoard().getNumStones(i);
            buttons[(i - 1) % 6][(i - 1) / 6].setText(Integer.toString(stoneCount)); // Update button label with stone count
        }

        int storeCountPlayer1 = mancalaGame.getBoard().getDataStructure().getStoreCount(1);
        storeLabelPlayer1.setText("Store P1: " + storeCountPlayer1);

        // Update store labels for player 2
        int storeCountPlayer2 = mancalaGame.getBoard().getDataStructure().getStoreCount(2);
        storeLabelPlayer2.setText("Store P2: " + storeCountPlayer2);

        currentPlayerLabel.setText("Current Player: " + mancalaGame.getCurrentPlayer().getName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
