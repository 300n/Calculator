import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Calculator extends Application {

    private TextField display;
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        // Créer l'affichage
        display = new TextField("0");
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle("-fx-font-size: 24px; -fx-background-color: #1a1a1a; -fx-text-fill: #00ffff; -fx-border-color: #00ffff; -fx-border-width: 2px;");
        display.setPrefHeight(80);

        // Créer la grille de boutons
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #141414;");

        // Définir les boutons
        String[][] buttons = {
            {"7", "8", "9", "+"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "*"},
            {"C", "0", "=", "/"},
            {"±", "."}
        };

        // Créer et ajouter les boutons à la grille
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String buttonText = buttons[row][col];
                Button btn = createButton(buttonText);
                
                if (row == 4 && col == 0) { // Bouton ±
                    grid.add(btn, col, row, 1, 1);
                } else if (row == 4 && col == 1) { // Bouton .
                    grid.add(btn, col, row, 2, 1); // Prend 2 colonnes
                } else {
                    grid.add(btn, col, row);
                }
            }
        }

        // Assembler l'interface
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #141414;");
        root.setTop(display);
        root.setCenter(grid);
        BorderPane.setMargin(display, new Insets(20, 20, 0, 20));

        // Créer la scène
        Scene scene = new Scene(root, 400, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculatrice");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(80, 60);
        
        String normalStyle = "-fx-background-color: #0d0d0d; " +
                "-fx-border-color: #00ffff; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 12px; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.8), 8, 0.5, 0, 0);";

        String pressedStyle = normalStyle + "-fx-translate-y: 2px;";

        btn.setStyle(normalStyle);
        btn.setOnMousePressed(e -> btn.setStyle(pressedStyle));
        btn.setOnMouseReleased(e -> btn.setStyle(normalStyle));

        // Ajouter l'action au bouton
        btn.setOnAction(e -> handleButtonClick(text));

        return btn;
    }

    private void handleButtonClick(String value) {
        switch (value) {
            case "C":
                clear();
                break;
            case "=":
                calculate();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                handleOperator(value);
                break;
            case "±":
                toggleSign();
                break;
            case ".":
                addDecimal();
                break;
            default:
                addDigit(value);
                break;
        }
    }

    private void addDigit(String digit) {
        if (startNewNumber) {
            currentNumber = digit;
            startNewNumber = false;
        } else {
            currentNumber += digit;
        }
        display.setText(currentNumber);
    }

    private void addDecimal() {
        if (startNewNumber) {
            currentNumber = "0.";
            startNewNumber = false;
        } else if (!currentNumber.contains(".")) {
            currentNumber += ".";
        }
        display.setText(currentNumber);
    }

    private void handleOperator(String op) {
        if (!currentNumber.isEmpty()) {
            if (!operator.isEmpty()) {
                calculate();
            } else {
                firstNumber = Double.parseDouble(currentNumber);
            }
            operator = op;
            startNewNumber = true;
        }
    }

    private void calculate() {
        if (!operator.isEmpty() && !currentNumber.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        display.setText("Erreur");
                        clear();
                        return;
                    }
                    break;
            }

            // Formater le résultat
            if (result == (long) result) {
                currentNumber = String.format("%d", (long) result);
            } else {
                currentNumber = String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
            }

            display.setText(currentNumber);
            firstNumber = result;
            operator = "";
            startNewNumber = true;
        }
    }

    private void toggleSign() {
        if (!currentNumber.isEmpty() && !currentNumber.equals("0")) {
            if (currentNumber.startsWith("-")) {
                currentNumber = currentNumber.substring(1);
            } else {
                currentNumber = "-" + currentNumber;
            }
            display.setText(currentNumber);
        }
    }

    private void clear() {
        currentNumber = "";
        operator = "";
        firstNumber = 0;
        startNewNumber = true;
        display.setText("0");
    }

    public static void main(String[] args) {
        launch(args);
    }
}