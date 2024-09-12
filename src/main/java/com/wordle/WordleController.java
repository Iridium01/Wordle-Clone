package com.wordle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

public class WordleController {

    @FXML
    private Button KeyA;

    @FXML
    private Button KeyB;

    @FXML
    private Button KeyBackspace;

    @FXML
    private Button KeyC;

    @FXML
    private Button KeyD;

    @FXML
    private Button KeyE;

    @FXML
    private Button KeyEnter;

    @FXML
    private Button KeyF;

    @FXML
    private Button KeyG;

    @FXML
    private Button KeyH;

    @FXML
    private Button KeyI;

    @FXML
    private Button KeyJ;

    @FXML
    private Button KeyK;

    @FXML
    private Button KeyL;

    @FXML
    private Button KeyM;

    @FXML
    private Button KeyN;

    @FXML
    private Button KeyO;

    @FXML
    private Button KeyP;

    @FXML
    private Button KeyQ;

    @FXML
    private Button KeyR;

    @FXML
    private Button KeyS;

    @FXML
    private Button KeyT;

    @FXML
    private Button KeyU;

    @FXML
    private Button KeyV;

    @FXML
    private Button KeyW;

    @FXML
    private Button KeyX;

    @FXML
    private Button KeyY;

    @FXML
    private Button KeyZ;

    @FXML
    private Button LoadButton;

    @FXML
    private Button ResetButton;

    @FXML
    private Button SaveButton;

    @FXML
    private Label guess_00;

    @FXML
    private Label guess_01;

    @FXML
    private Label guess_02;

    @FXML
    private Label guess_03;

    @FXML
    private Label guess_04;

    @FXML
    private Label guess_10;

    @FXML
    private Label guess_11;

    @FXML
    private Label guess_12;

    @FXML
    private Label guess_13;

    @FXML
    private Label guess_14;

    @FXML
    private Label guess_20;

    @FXML
    private Label guess_21;

    @FXML
    private Label guess_22;

    @FXML
    private Label guess_23;

    @FXML
    private Label guess_24;

    @FXML
    private Label guess_30;

    @FXML
    private Label guess_31;

    @FXML
    private Label guess_32;

    @FXML
    private Label guess_33;

    @FXML
    private Label guess_34;

    @FXML
    private Label guess_40;

    @FXML
    private Label guess_41;

    @FXML
    private Label guess_42;

    @FXML
    private Label guess_43;

    @FXML
    private Label guess_44;

    @FXML
    private Label guess_50;

    @FXML
    private Label guess_51;

    @FXML
    private Label guess_52;

    @FXML
    private Label guess_53;

    @FXML
    private Label guess_54;

    @FXML
    private AnchorPane main_game;

    @FXML
    private Pane stats_screen;

    @FXML
    private Button close_stats;

    @FXML
    private Label curr_streak_label;

    @FXML
    private ProgressBar progress_1;

    @FXML
    private ProgressBar progress_2;

    @FXML
    private ProgressBar progress_3;

    @FXML
    private ProgressBar progress_4;

    @FXML
    private ProgressBar progress_5;

    @FXML
    private ProgressBar progress_6;

    @FXML
    private Label games_played_label;

    @FXML
    private Label max_streak_label;

    @FXML
    private Label win_lose_text;

    @FXML
    private Label win_percent_label;

    // extra vars for game logic

    private int row = 0;
    private int col = 0;

    private ArrayList<ArrayList<Label>> board;

    private HashMap<String, Button> buttons;

    private String hidden_word;
    private List<String> possible_words;

    private boolean correct_guess = false;

    // vars for stats
    private int games_played = 0;
    private int games_won = 0;
    private int max_streak = 0;
    private int curr_streak = 0;
    private ArrayList<Integer> guesses_per_game = new ArrayList<>();

    @FXML
    void LoadFile(ActionEvent event) {
        if (event.getSource() == LoadButton) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File file = fileChooser.showOpenDialog(LoadButton.getScene().getWindow());

            if (file != null) {
                // clear board
                reset();

                // load game
                try {
                    Path p = Paths.get(file.getAbsolutePath());
                    List<String> lines = Files.readAllLines(p);

                    hidden_word = lines.get(0);
                    games_played = Integer.parseInt(lines.get(1));
                    games_won = Integer.parseInt(lines.get(2));
                    max_streak = Integer.parseInt(lines.get(3));
                    curr_streak = Integer.parseInt(lines.get(4));
                    for (int i = 5; i < 11; i++) {
                        String l = lines.get(i);
                        guesses_per_game.set(i - 5, Integer.parseInt(l));
                    }

                    // load board
                    for (int i = 0; i < 6; i++) {
                        String guess = lines.get(i + 11);
                        col = 0;
                        for (int j = 0; j < 5; j++) {
                            Label curr_label = board.get(i).get(j);
                            curr_label.setText(guess.substring(j, j + 1));
                            if (curr_label.getText().equals(" ")) {
                                curr_label.setText("");
                            }
                            else {
                                col++;
                            }

                        }

                        // check if row is filled, evaluate the row
                        if (col == 5) {
                            evaluateGuess();
                        }
                    }

                    // lock letters if game is over
                    if (row == 5) {
                        LockLetters();
                        KeyBackspace.setDisable(true);
                        KeyEnter.setDisable(true);
                        LoadButton.setDisable(true);
                        ResetButton.setDisable(true);
                        SaveButton.setDisable(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void PressedKeyBackspace() {
        if (col == 0) {
            return;
        }

        col--;
        Label curr_label = board.get(row).get(col);
        curr_label.setText("");

    }

    @FXML
    void PressedKeyEnter() {
        if (col != 5) {
            return;
        }

        evaluateGuess();
    }

    void warnings(boolean full) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");

        if (full) {
            alert.setContentText("Guess is full. All words are 5 letters long. Press Enter to evaluate guess.");
        }
        else {
            alert.setContentText("Word is invalid. Please enter a valid word.");
        }

        alert.showAndWait();
    }

    void evaluateGuess() {
        String guess = "";
        for (Label label : board.get(row)) {
            guess += label.getText().toUpperCase();
        }
        // check if guess is an actual word
        int in = possible_words.indexOf(guess);
        if (possible_words.indexOf(guess) == -1) {
            warnings(false);
            return;
        }


        // handle letters
        String temp = hidden_word;
        // loop through labels in the row twice

        // first loop checks for greens
        for (int i = 0; i < 5; i++) {
            Label curr_label = board.get(row).get(i);
            String hidden_letter = hidden_word.substring(i, i + 1);

            // if green
            if (hidden_letter.equals(curr_label.getText())) {
                // set to green #538d4e
                curr_label.setStyle("-fx-background-color: #538d4e");
                buttons.get(curr_label.getText()).setStyle("-fx-background-color: #538d4e;");
                
                // set temp[i] to empty placeholder
                char[] temp_arr = temp.toCharArray();
                temp_arr[i] = '_';
                temp = new String(temp_arr);
            }
        }
        
        // second loop checks grays/yellows
        for (int i = 0; i < 5; i++) {
            Label curr_label = board.get(row).get(i);
            String hidden_letter = hidden_word.substring(i, i + 1);

            // if green (temp.charAt is "_"), then skip
            if (temp.charAt(i) == '_') {
                continue;
            }

            boolean gray = temp.indexOf(curr_label.getText()) == -1;
            boolean yellow = !(hidden_letter.equals(curr_label.getText())) && temp.indexOf(curr_label.getText()) != -1;
            
            // if letter is not in the word
            if (gray) {
                // set background color of label and key to #3a3a3c
                curr_label.setStyle("-fx-background-color: #3a3a3c");
                buttons.get(curr_label.getText()).setStyle("-fx-background-color: #3a3a3c;");

                // disable the button
                buttons.get(curr_label.getText()).setDisable(true);
            }
            // if letter is not in right spot
            else if (yellow) {
                // set label and key color to #b59f3b
                curr_label.setStyle("-fx-background-color: #b59f3b");
                buttons.get(curr_label.getText()).setStyle("-fx-background-color: #b59f3b;");
                temp = temp.replaceFirst(curr_label.getText(), " ");
            }
        }

        // check if end of game
        // check if guess matches word

        if (guess.equals(hidden_word)) {
            // show stats, etc
            System.out.println("correct guess"); // replace this

            // lock all buttons
            LockLetters();
            KeyBackspace.setDisable(true);
            KeyEnter.setDisable(true);
            LoadButton.setDisable(true);
            ResetButton.setDisable(true);
            SaveButton.setDisable(true);

            // update stats
            correct_guess = true;
            games_played++;
            games_won++;
            curr_streak++;
            if (curr_streak > max_streak) {
                max_streak = curr_streak;
            }
            guesses_per_game.set(row + 1, guesses_per_game.get(row + 1) + 1);

            SwitchScreen();
            return;
        }
        else if (row == 5) {
            // game lost
            // show word and stats
            System.out.println("you lost"); // replace this

            // lock all buttons
            LockLetters();
            KeyBackspace.setDisable(true);
            KeyEnter.setDisable(true);
            LoadButton.setDisable(true);
            ResetButton.setDisable(true);
            SaveButton.setDisable(true);

            // update stats
            correct_guess = false;
            games_played++;
            curr_streak = 0;

            SwitchScreen();
            return;
        }

        // update row and col
        row++;
        col = 0;
    }

    @FXML
    void ClickedLetter(ActionEvent event) {
        Button button = (Button) event.getSource();
        System.out.println("Clicked " + button.getText());

        try (FileWriter writer = new FileWriter("buttonPresses.txt", true)) {
            writer.write("Clicked " + button.getId() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (button.getId().equals("KeyBackspace")) {
            PressedKeyBackspace();
        }
        else if (button.getId().equals("KeyEnter")) {
            PressedKeyEnter();
        }
        else {
            if (col < 5) {
                String text = button.getText();
                Label curr_label = board.get(row).get(col);
                curr_label.setText(text);

                col++;
            }
            else {
                warnings(true);
            }
        }

        // if last letter, lock all letter keys

    }

    @FXML
    void KeyPress(KeyEvent event) {
        System.out.println("Pressed " + event.getText().toUpperCase());

        if (event.getCode() == KeyCode.BACK_SPACE) {
            PressedKeyBackspace();
        }
        else if (event.getCode() == KeyCode.ENTER) {
            PressedKeyEnter();
        }

        else if (Character.isLetter(event.getText().charAt(0)) && col < 5) {
            String text = event.getText().toUpperCase();
            Label curr_label = board.get(row).get(col);
            curr_label.setText(text);

            col++;
        }
    }

    void LockLetters() {
        for (Button b : buttons.values()) {
            b.setDisable(true);
        }
    }

    @FXML
    void ResetBoard(ActionEvent event) {
        if (event.getSource() == ResetButton)
            reset();
    }

    void reset() {
        row = 0;
        col = 0;

        for (ArrayList<Label> r : board) {
            for (Label l : r) {
                l.setText("");
                l.setStyle("-fx-border-color: #818384; -fx-border-width: 2px; -fx-border-radius: 8px;");
            }
        }

        for (Button b : buttons.values()) {
            b.setDisable(false);
            b.setStyle("-fx-background-color: #818384;");
        }
        KeyBackspace.setDisable(false);
        KeyEnter.setDisable(false);
        correct_guess = false;

        setHidden();
    }

    @FXML
    void SaveFile(ActionEvent event) {
        if (event.getSource() == SaveButton) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File file = fileChooser.showSaveDialog(SaveButton.getScene().getWindow());

            if (file != null) {
                // save game
                try {
                    Path p = Paths.get(file.getAbsolutePath());
                    List<String> lines = new ArrayList<>();

                    lines.add(hidden_word);
                    lines.add(Integer.toString(games_played));
                    lines.add(Integer.toString(games_won));
                    lines.add(Integer.toString(max_streak));
                    lines.add(Integer.toString(curr_streak));
                    for (int i = 1; i < 7; i++) {
                        lines.add(Integer.toString(guesses_per_game.get(i)));
                    }

                    // add board
                    for (ArrayList<Label> r : board) {
                        String row = "";
                        for (Label l : r) {
                            row += (l.getText() == "") ? " " : l.getText();
                        }
                        lines.add(row);
                    }

                    Files.write(p, lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void switch_screen(ActionEvent event) {
        stats_screen.setVisible(false);
        LoadButton.setDisable(false);
        ResetButton.setDisable(false);
        SaveButton.setDisable(false);
    }

    void SwitchScreen() {
        // set messages
        if (correct_guess) {
            win_lose_text.setText("Congratulations!");
        }
        else {
            win_lose_text.setText("Unfortunate");
        }

        games_played_label.setText(Integer.toString(games_played));
        curr_streak_label.setText(Integer.toString(curr_streak));
        max_streak_label.setText(Integer.toString(max_streak));
        win_percent_label.setText(String.format("%.2f", (double) games_won / games_played * 100) + "%");

        // set progress bars
        progress_1.setProgress((double) guesses_per_game.get(1) / games_won);
        progress_2.setProgress((double) guesses_per_game.get(2) / games_won);
        progress_3.setProgress((double) guesses_per_game.get(3) / games_won);
        progress_4.setProgress((double) guesses_per_game.get(4) / games_won);
        progress_5.setProgress((double) guesses_per_game.get(5) / games_won);
        progress_6.setProgress((double) guesses_per_game.get(6) / games_won);

        // main_game.setVisible(false);
        stats_screen.setVisible(true);
        stats_screen.toFront();
    }

    void initializeBoard() {
        board = new ArrayList<>();
        
        for (int i = 0; i < 6; i++) {
            ArrayList<Label> curr_row = new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                String label_name = "guess_" + i + j;

                // finding labels using label_name and adding to board
                try {
                    Field f = this.getClass().getDeclaredField(label_name);
                    curr_row.add((Label) f.get(this));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            
            board.add(curr_row);
        }
    }

    void readWords() {
        Path p = Paths.get("./src/main/resources/com/wordle/possible_words.txt");
        possible_words = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(p);

            for (String line : lines) {
                String[] words = line.split("\\s+");

                // set words to all caps
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toUpperCase();
                }

                possible_words.addAll(Arrays.asList(words));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setHidden() {
        Random rand = new Random();
        int i = rand.nextInt(possible_words.size());
        hidden_word = possible_words.get(i).toUpperCase();
        System.out.println(hidden_word);
    }

    void initializeButtons() {
        buttons = new HashMap<>();
        buttons.put("A", KeyA);
        buttons.put("B", KeyB);
        buttons.put("C", KeyC);
        buttons.put("D", KeyD);
        buttons.put("E", KeyE);
        buttons.put("F", KeyF);
        buttons.put("G", KeyG);
        buttons.put("H", KeyH);
        buttons.put("I", KeyI);
        buttons.put("J", KeyJ);
        buttons.put("K", KeyK);
        buttons.put("L", KeyL);
        buttons.put("M", KeyM);
        buttons.put("N", KeyN);
        buttons.put("O", KeyO);
        buttons.put("P", KeyP);
        buttons.put("Q", KeyQ);
        buttons.put("R", KeyR);
        buttons.put("S", KeyS);
        buttons.put("T", KeyT);
        buttons.put("U", KeyU);
        buttons.put("V", KeyV);
        buttons.put("W", KeyW);
        buttons.put("X", KeyX);
        buttons.put("Y", KeyY);
        buttons.put("Z", KeyZ);
    }

    public void initialize() {
        stats_screen.setVisible(false);
        initializeBoard();
        System.out.println(System.getProperty("user.dir"));
        readWords();
        setHidden();
        initializeButtons();
        guesses_per_game.addAll(Collections.nCopies(7, 0));

    }

}