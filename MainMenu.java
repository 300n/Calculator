import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MainMenu extends Application {

    private Font customFont;
    private Font customFontTitle;
    private int width = 345;
    private int height = 425;
    private Canvas canvas;
    private Stage primaryStage;
    private AnchorPane menuLayout; // Stocker la référence

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            customFont = Font.loadFont(getClass().getResourceAsStream("/ressources/Jaro.ttf"), 30);
            customFontTitle = Font.loadFont(getClass().getResourceAsStream("/ressources/Jaro.ttf"), 50);
            if (customFont == null) {
                throw new Exception("Font not loaded");
            }
        } catch (Exception e) {
            System.err.println("Erreur de police : " + e.getMessage());
            customFont = Font.font("Arial", 20);
        }

        canvas = new Canvas(width, height);

        Scene scene = createMainMenu();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculator");
        primaryStage.show();
    }

    private Scene createMainMenu() {
        menuLayout = new AnchorPane();
        menuLayout.setStyle("-fx-background-color: rgb(20, 20, 20);");

        Scene scene = new Scene(menuLayout, width, height);

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.intValue();
            updateLayout(); 
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.intValue();
            updateLayout(); 
        });

        updateLayout();

        return scene;
    }

    private void updateLayout() {
        // Nettoyer tous les enfants
        menuLayout.getChildren().clear();
        
        canvas.setWidth(width);
        canvas.setHeight(height);
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        menuLayout.getChildren().add(canvas);
        

        String sharedStyle = "-fx-fill: rgb(20, 20, 20); "
            + "-fx-stroke: rgba(0, 255, 255, 0.8); "
            + "-fx-stroke-width: 3px; "
            + "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.8), 10, 0.5, 0, 0); "
            + "-fx-arc-width: 20px; "  
            + "-fx-arc-height: 20px;";
        

        Rectangle rect1 = new Rectangle(this.width - 30, this.height - 30);
        rect1.setStyle(sharedStyle);
        AnchorPane.setTopAnchor(rect1, 15.0);
        AnchorPane.setLeftAnchor(rect1, 15.0);
        menuLayout.getChildren().add(rect1);
      
        ArrayList<Button> buttonList = new ArrayList<>();
        String[] buttonLabels = {
            "7", "8", "9", "+", "4", "5", "6", "-", "1", "2",
            "3", "*", "0", " ,", "±", "/", 
        };
        
        double btnWidth = width / 6.0;  // Largeur 
        double btnHeight = height / 10.0;  // Hauteur 
        double spacing = width / 40.0;  // Espacement 
        double fontSize = Math.min(width, height) / 20.0;  // Taille de police 
        double borderWidth = Math.max(2.0, width / 150.0);  // Épaisseur de bordure 
        double borderRadius = btnWidth / 6.0;  // Rayon de bordure 
        
        for (String label : buttonLabels) {
            Button btn = new Button(label);
            
            String normalStyle = "-fx-background-color: rgb(13, 13, 13); "
                    + "-fx-border-color: rgba(0, 255, 255, 0.8); "
                    + "-fx-border-width: " + borderWidth + "px; "
                    + "-fx-border-radius: " + borderRadius + "px; "
                    + "-fx-background-radius: " + (borderRadius + 2) + "px; "
                    + "-fx-font-size: " + fontSize + "px; "
                    + "-fx-font-weight: bold; "
                    + "-fx-text-fill: rgb(255, 255, 255); "
                    + "-fx-min-width: " + btnWidth + "px; "
                    + "-fx-max-width: " + btnWidth + "px; "
                    + "-fx-min-height: " + btnHeight + "px; "
                    + "-fx-max-height: " + btnHeight + "px; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0, 255, 255, 0.8), 10, 0.5, 0, 0);";
        
            String pressedStyle = "-fx-background-color: rgb(13, 13, 13); "
                    + "-fx-border-color: rgba(0, 255, 255, 0.8); "
                    + "-fx-border-width: " + borderWidth + "px; "
                    + "-fx-border-radius: " + borderRadius + "px; "
                    + "-fx-background-radius: " + (borderRadius + 2) + "px; "
                    + "-fx-font-size: " + fontSize + "px; "
                    + "-fx-font-weight: bold; "
                    + "-fx-text-fill: rgb(255, 255, 255); "
                    + "-fx-min-width: " + btnWidth + "px; "
                    + "-fx-max-width: " + btnWidth + "px; "
                    + "-fx-min-height: " + btnHeight + "px; "
                    + "-fx-max-height: " + btnHeight + "px; "
                    + "-fx-translate-y: 2px;"; 
        
            btn.setStyle(normalStyle);
            btn.setOnMousePressed(e -> btn.setStyle(pressedStyle));
            btn.setOnMouseReleased(e -> btn.setStyle(normalStyle));
            
            buttonList.add(btn);
        }
        
        double startTop = height / 2.25;  
        double startLeft = width / 8.0;  
        
        for (int i = 0; i < buttonList.size(); i++) {
            Button btn = buttonList.get(i);
            
            int row = i / 4;  
            int col = i % 4;  
            
            double topPos = startTop + (row * (btnHeight + spacing));
            double leftPos = startLeft + (col * (btnWidth + spacing));
            
            AnchorPane.setTopAnchor(btn, topPos);
            AnchorPane.setLeftAnchor(btn, leftPos);
            
            menuLayout.getChildren().add(btn);
        }





        
        Rectangle displayRect = new Rectangle();
        displayRect.setStyle(sharedStyle);
        
        displayRect.widthProperty().bind(menuLayout.widthProperty().multiply(0.75)); // 3/4
        displayRect.heightProperty().bind(menuLayout.heightProperty().divide(3)); // 1/8
        
        menuLayout.widthProperty().addListener((obs, old, newVal) -> {
            AnchorPane.setLeftAnchor(displayRect, newVal.doubleValue() / 8);
            AnchorPane.setTopAnchor(displayRect, newVal.doubleValue() / 16);
        });
        
        AnchorPane.setTopAnchor(displayRect, (double) this.height / 16);
        AnchorPane.setLeftAnchor(displayRect, (double) this.width / 8);
        
        menuLayout.getChildren().add(displayRect);
    }

    public static void main(String[] args) {
        launch(args);
    }
}