import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.util.List;
import java.beans.Expression;
import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Classe qui implémente l'interface graphique de la calculatrice
 */
public class CalculatorGUI implements CalculatorGUIInterface {
    private Scene scene;
    private Scene settingsScene;
    private Scene customiseScene;
    private Scene graphScene;
    private Scene eqScene;

    private TextField accumulatorDisplay;
    private ListView<Double> stackDisplay;
    private Button[] digitButtons;
    private Button decimalButton;
    private Button addButton;
    private Button subtractButton;
    private Button multiplyButton;
    private Button divideButton;
    private Button oppositeButton;
    private Button pushButton;
    private Button popButton;
    private Button dropButton;
    private Button swapButton;
    private Button clearButton;
    private Button settingsButton;

    private int width = 345;
    private int height = 600;
    private Stage stage;

    private AnchorPane mainLayout;
    private AnchorPane settingsLayout;
    private AnchorPane customiseLayout;
    private AnchorPane graphLayout;
    private AnchorPane eqLayout;

    private ThemeManager themeManager = new ThemeManager();

    /**
     * Classe interne pour concentrer les dimensions des boutons
     */
    private class ButtonDimensions {
        double width, height, spacing, fontSize, borderWidth, borderRadius;
        
        ButtonDimensions(double baseWidth, double baseHeight) {
            this.width = baseWidth / 6.0;
            this.height = baseHeight / 12.0;
            this.spacing = baseWidth / 40.0;
            this.fontSize = Math.min(baseWidth, baseHeight) / 25.0;
            this.borderWidth = Math.max(2.0, baseWidth / 150.0);
            this.borderRadius = this.width / 6.0;
        }
        
        ButtonDimensions(double baseWidth, double baseHeight, double widthDivisor, double heightDivisor) {
            this.width = baseWidth / widthDivisor;
            this.height = baseHeight / heightDivisor;
            this.spacing = baseWidth / 40.0;
            this.fontSize = Math.min(baseWidth, baseHeight) / 25.0;
            this.borderWidth = Math.max(2.0, baseWidth / 150.0);
            this.borderRadius = this.width / 6.0;
        }
    }

    /**
     * Construit la scène principale de la calculatrice
     * @param stage Fenêtre principale JavaFX
     */
    public CalculatorGUI(Stage stage) {
        this.stage = stage;
        this.mainLayout = new AnchorPane();
        this.scene = new Scene(mainLayout, 345, 600);

        try {
            initializeComponents();
            buildLayout();
            setupSceneListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Définit la fenêtre principale
     * @param stage La fenêtre JavaFX à utiliser
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Retourne la scène actuelle
     * @return La scène JavaFX
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Ajoute des listener pour chaque changement de valeur des dimensions de la
     * fenêtre
     */
    private void setupSceneListeners() {
        this.scene.widthProperty().addListener((obs, old, newVal) -> {
            this.width = newVal.intValue();
            updateLayout();
        });

        this.scene.heightProperty().addListener((obs, old, newVal) -> {
            this.height = newVal.intValue();
            updateLayout();
        });
    }

    /**
     * Configure les listeners pour une scène donnée
     */
    private void setupSceneListenersFor(Scene targetScene, Runnable updateMethod) {
        targetScene.widthProperty().addListener((obs, old, newVal) -> {
            this.width = newVal.intValue();
            updateMethod.run();
        });

        targetScene.heightProperty().addListener((obs, old, newVal) -> {
            this.height = newVal.intValue();
            updateMethod.run();
        });
    }

    /**
     * Initialise tout les composant de la fenêtre (boutons & accumulateur)
     */
    private void initializeComponents() {
        accumulatorDisplay = new TextField("0");
        accumulatorDisplay.setStyle("-fx-text-fill: black;");
        accumulatorDisplay.setEditable(false);
        accumulatorDisplay.setAlignment(Pos.CENTER_RIGHT);

        stackDisplay = new ListView<>();

        digitButtons = new Button[10];
        for (int i = 0; i <= 9; i++) {
            digitButtons[i] = new Button(String.valueOf(i));
        }

        decimalButton = new Button(".");
        addButton = new Button("+");
        subtractButton = new Button("-");
        multiplyButton = new Button("*");
        divideButton = new Button("÷");
        oppositeButton = new Button("¹/");

        pushButton = new Button("Push");
        popButton = new Button("Pop");
        dropButton = new Button("Drop");
        swapButton = new Button("Swap");
        clearButton = new Button("Clear");
        settingsButton = new Button("≡");
    }

    /**
     * Crée un rectangle de fond avec image de thème
     * @return Rectangle avec le fond appliqué
     */
    private Rectangle createBackgroundRectangle() {
        Rectangle backgroundRect = new Rectangle(width - 30, height - 30);
        backgroundRect.setFill(Color.web(themeManager.getCurrentTheme().getBackgroundColor()));
        backgroundRect.setOpacity(0.3);
        
        Image image = new Image(themeManager.getCurrentTheme().getImageUrl(), true);
        image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            if (newProgress.doubleValue() >= 1.0 && !image.isError()) {
                backgroundRect.setFill(new ImagePattern(image));
                backgroundRect.setOpacity(1.0);
            }
        });
        
        image.errorProperty().addListener((obs, oldError, newError) -> {
            if (newError) {
                backgroundRect.setFill(Color.web(themeManager.getCurrentTheme().getBackgroundColor()));
                backgroundRect.setOpacity(0.3);
            }
        });
        
        return backgroundRect;
    }

    /**
     * Crée un rectangle stylisé pour les zones d'affichage
     * @param x Position X
     * @param y Position Y
     * @param width Largeur
     * @param height Hauteur
     * @return Rectangle stylisé
     */
    private Rectangle createStyledDisplayRect(double x, double y, double width, double height) {
        String displayStyle = "-fx-fill: rgba(255, 255, 255, 1); "
                + "-fx-stroke: " + themeManager.getCurrentTheme().getBackgroundColor() + "; "
                + "-fx-stroke-width: 3px; "
                + "-fx-effect: dropshadow(gaussian, " + themeManager.getCurrentTheme().getBackgroundColor()
                + ", 10, 0.5, 0, 0); "
                + "-fx-arc-width: 20px; "
                + "-fx-arc-height: 20px;";
        
        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setStyle(displayStyle);
        return rect;
    }

    /**
     * Crée un bouton retour standard
     * @return Bouton retour configuré
     */
    private Button createBackButton() {
        Button backButton = new Button("↩");
        ButtonDimensions dims = new ButtonDimensions(width, height);
        styleButton(backButton, width / 6.0, height / 12.0, Math.min(width, height) / 20.0, Math.max(2.0, width / 150.0), width / 36.0);
        if (this.scene.getWindow() != null) {
            this.scene.getWindow().setWidth(stage.getWidth());
            this.scene.getWindow().setHeight(stage.getHeight());
        }
        backButton.setOnAction(e -> stage.setScene(this.scene));
        return backButton;
    }


    /**
     * Ajoute un rectangle de fond à un layout
     * @param layout Layout cible
     */
    private void addBackgroundToLayout(AnchorPane layout) {
        Rectangle backgroundRect = createBackgroundRectangle();
        AnchorPane.setTopAnchor(backgroundRect, 15.0);
        AnchorPane.setLeftAnchor(backgroundRect, 15.0);
        layout.getChildren().add(backgroundRect);
    }

    /**
     * Crée une grille de boutons
     * @param layout Layout où ajouter les boutons
     * @param buttons Tableau de boutons
     * @param startTop Position de départ en haut
     * @param startLeft Position de départ à gauche
     * @param columns Nombre de colonnes
     * @param dims Dimensions des boutons
     */
    private void createButtonGrid(AnchorPane layout, Button[] buttons, double startTop, double startLeft, int columns, ButtonDimensions dims) {
        for (int i = 0; i < buttons.length; i++) {
            Button btn = buttons[i];
            styleButton(btn, dims.width, dims.height, dims.fontSize, dims.borderWidth, dims.borderRadius);
            
            int row = i / columns;
            int col = i % columns;
            double topPos = startTop + (row * (dims.height + dims.spacing));
            double leftPos = startLeft + (col * (dims.width + dims.spacing));
            
            AnchorPane.setTopAnchor(btn, topPos);
            AnchorPane.setLeftAnchor(btn, leftPos);
            layout.getChildren().add(btn);
        }
    }

    /**
     * Construit le Layout "basique" de la fenêtre principale
     */
    private void buildLayout() {
        mainLayout.getChildren().clear();
        mainLayout.setStyle("-fx-background-color: rgb(20, 20, 20);");

        addBackgroundToLayout(mainLayout);
        createDisplayArea();
        createButtons();
    }

    /**
     * Place les différents éléments correctement sur le layout basique crée
     * précédement
     */
    private void createDisplayArea() {
        Rectangle displayRect = createStyledDisplayRect(width / 8, height / 16, width * 0.75, height / 3.5);
        AnchorPane.setTopAnchor(displayRect, (double) height / 16);
        AnchorPane.setLeftAnchor(displayRect, (double) width / 8);
        mainLayout.getChildren().add(displayRect);

        accumulatorDisplay.setStyle(
                "-fx-background-color: transparent; "
                        + "-fx-text-fill: " + this.themeManager.getCurrentTheme().getBackgroundColor() + "; "
                        + "-fx-font-size: 28px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-border-width: 0;");
        accumulatorDisplay.setPrefWidth(width * 0.7);
        accumulatorDisplay.setPrefHeight(50);
        AnchorPane.setTopAnchor(accumulatorDisplay, (double) height / 16 + 10);
        AnchorPane.setLeftAnchor(accumulatorDisplay, (double) width / 8 + 10);
        mainLayout.getChildren().add(accumulatorDisplay);

        String scrollCss = ".scroll-pane {"
                + "   -fx-background-color: transparent;"
                + "   -fx-text-fill: " + this.themeManager.getCurrentTheme().getBackgroundColor() + ";"
                + "   -fx-font-size: 14px;"
                + "   -fx-border-width: 0;"
                + "}"
                + ".scroll-bar .thumb { -fx-background-color: " + this.themeManager.getCurrentTheme().getBorderColor()
                + "; }"
                + ".scroll-bar .increment-arrow, .scroll-bar .decrement-arrow { -fx-background-color: "
                + this.themeManager.getCurrentTheme().getBorderColor() + "; }";
        stackDisplay.getStylesheets().add("data:text/css," + scrollCss.replace(" ", "%20"));

        stackDisplay.setPrefWidth(width * 0.7);
        stackDisplay.setPrefHeight(height / 6);
        AnchorPane.setTopAnchor(stackDisplay, (double) height / 16 + 70);
        AnchorPane.setLeftAnchor(stackDisplay, (double) width / 8 + 10);
        mainLayout.getChildren().add(stackDisplay);
    }

    /**
     * Crée l'environement de chaque bouton
     */
    private void createButtons() {
        ButtonDimensions dims = new ButtonDimensions(width, height);

        Button[] allButtons = {
                digitButtons[7], digitButtons[8], digitButtons[9], divideButton,
                digitButtons[4], digitButtons[5], digitButtons[6], multiplyButton,
                digitButtons[1], digitButtons[2], digitButtons[3], subtractButton,
                digitButtons[0], decimalButton, oppositeButton, addButton
        };

        double startTop = height / 1.70;
        double startLeft = width / 8.0;

        createButtonGrid(mainLayout, allButtons, startTop - height / 10, startLeft, 4, dims);

        // Bouton paramètres
        styleButton(settingsButton, dims.width, dims.height, dims.fontSize, dims.borderWidth, dims.borderRadius);
        AnchorPane.setTopAnchor(settingsButton, height / 2.6);
        AnchorPane.setLeftAnchor(settingsButton, width / 8.0);
        settingsButton.setOnAction(e -> showSettingsMenu());
        mainLayout.getChildren().add(settingsButton);

        // Boutons de pile (plus petits, en bas)
        Button[] stackButtons = { pushButton, popButton, dropButton, swapButton, clearButton };
        double smallBtnWidth = dims.width * 0.8;
        double smallBtnHeight = dims.height * 0.7;

        for (int i = 0; i < stackButtons.length; i++) {
            Button btn = stackButtons[i];
            styleButton(btn, smallBtnWidth, smallBtnHeight, dims.fontSize * 0.7, dims.borderWidth, dims.borderRadius * 0.8);

            double topPos = height - smallBtnHeight - 20;
            double leftPos = startLeft + (i * (smallBtnWidth + dims.spacing));

            AnchorPane.setTopAnchor(btn, topPos);
            AnchorPane.setLeftAnchor(btn, leftPos);
            mainLayout.getChildren().add(btn);
        }
    }

    /**
     * Applique un style prédéfini pour un bouton
     * @param btn          Objet bouton
     * @param width        Largeur du bouton
     * @param height       Hauteur du bouton
     * @param fontSize     Taille de police utilisée pour le texte du bouton
     * @param borderWidth  Largeur de la bordure du bouton
     * @param borderRadius rayon de la bordure du bouton
     */
    private void styleButton(Button btn, double width, double height, double fontSize, double borderWidth,
            double borderRadius) {
        String normalStyle = "-fx-background-color: " + this.themeManager.getCurrentTheme().getBackgroundColor() + "; "
                + "-fx-border-color: " + this.themeManager.getCurrentTheme().getBorderColor() + "; "
                + "-fx-border-width: " + borderWidth + "px; "
                + "-fx-border-radius: " + borderRadius + "px; "
                + "-fx-background-radius: " + (borderRadius + 2) + "px; "
                + "-fx-font-size: " + fontSize + "px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: " + this.themeManager.getCurrentTheme().getBorderColor() + "; "
                + "-fx-min-width: " + width + "px; "
                + "-fx-max-width: " + width + "px; "
                + "-fx-min-height: " + height + "px; "
                + "-fx-max-height: " + height + "px; "
                + "-fx-effect: dropshadow(gaussian, " + this.themeManager.getCurrentTheme().getBorderColor()
                + ", 10, 0.5, 0, 0);";

        String pressedStyle = normalStyle + "-fx-translate-y: 2px;";

        btn.setStyle(normalStyle);
        btn.setOnMousePressed(e -> {
            btn.setStyle(pressedStyle);
            e.consume();
        });
        btn.setOnMouseReleased(e -> {
            btn.setStyle(normalStyle);
            e.consume();
        });
    }

    /**
     * Met à jour la taille des éléments de la fenêtre en cas de changement de
     * dimensions de celle-ci
     */
    private void updateLayout() {
        buildLayout();
    }

    /**
     * Mise en place du menu paramètre si jamais le bouton paramètre est activé
     */
    private void showSettingsMenu() {
        if (stage == null)
            return;
            
        if (settingsLayout == null) {
            settingsLayout = new AnchorPane();
            settingsScene = new Scene(settingsLayout, width, height);
            setupSceneListenersFor(settingsScene, this::buildSettingsLayout);
        }
        
        buildSettingsLayout();
        stage.setScene(settingsScene);
    }
    
    /**
     * Construit le layout du menu paramètres
     */
    private void buildSettingsLayout() {
        settingsLayout.getChildren().clear();
        settingsLayout.setStyle("-fx-background-color: #202020;");

        addBackgroundToLayout(settingsLayout);

        Button backButton = createBackButton();
        AnchorPane.setTopAnchor(backButton, 20.0);
        AnchorPane.setLeftAnchor(backButton, 20.0);
        settingsLayout.getChildren().add(backButton);

        ButtonDimensions dims = new ButtonDimensions(width, height);

        Button customiseButton = new Button("Personnaliser la calculatrice");
        styleButton(customiseButton, width / 1.5, dims.height, dims.fontSize,
                dims.borderWidth, width / 36.0);
        customiseButton.setOnAction(e -> showCustomiseScene());
        AnchorPane.setTopAnchor(customiseButton, height*0.11+height/16.0);
        AnchorPane.setLeftAnchor(customiseButton, (width - width / 1.5) / 2);
        settingsLayout.getChildren().add(customiseButton);

        Button grapheButton = new Button("Menu des graphes");
        styleButton(grapheButton, width / 1.5, dims.height, dims.fontSize,
                dims.borderWidth, width / 36.0);
        grapheButton.setOnAction(e -> showGraphMenu());
        AnchorPane.setTopAnchor(grapheButton, height/4+height/16.0);
        AnchorPane.setLeftAnchor(grapheButton, (width - width / 1.5) / 2);
        settingsLayout.getChildren().add(grapheButton);

        Button eqButton = new Button("Menu des polynômes");
        styleButton(eqButton, width / 1.5, dims.height, dims.fontSize,
                dims.borderWidth, width / 36.0);
        eqButton.setOnAction(e -> showEqMenu());
        AnchorPane.setTopAnchor(eqButton, height*0.38+height/16.0);
        AnchorPane.setLeftAnchor(eqButton, (width - width / 1.5) / 2);
        settingsLayout.getChildren().add(eqButton);
    }

    /**
     * Mise en place du menu customistation si le bouton customize est utilisé
     */
    private void showCustomiseScene() {
        if (stage == null)
            return;

        if (customiseLayout == null) {
            customiseLayout = new AnchorPane();
            customiseScene = new Scene(customiseLayout, width, height);
            setupSceneListenersFor(customiseScene, this::buildCustomiseLayout);
        }
        
        buildCustomiseLayout();
        stage.setScene(customiseScene);
    }
    
    /**
     * Construit le layout du menu customisation
     */
    private void buildCustomiseLayout() {
        customiseLayout.getChildren().clear();
        customiseLayout.setStyle("-fx-background-color: #202020;");

        Button backButton = new Button("↩");
        ButtonDimensions dims = new ButtonDimensions(width, height);
        styleButton(backButton, width / 6.0, height / 12.0, 
                    Math.min(width, height) / 20.0, 
                    Math.max(2.0, width / 150.0), 
                    width / 36.0);
        backButton.setOnAction(e -> stage.setScene(this.scene));
        AnchorPane.setTopAnchor(backButton, 20.0);
        AnchorPane.setLeftAnchor(backButton, 20.0);
        customiseLayout.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #202020; -fx-border-color: transparent;");
        AnchorPane.setTopAnchor(scrollPane, 70.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);

        VBox contentBox = new VBox(40);
        contentBox.setStyle("-fx-background-color: #202020;");
        contentBox.setPadding(new Insets(20));

        for (Theme theme : this.themeManager.getThemes()) {
            AnchorPane card = new AnchorPane();

            Rectangle borderRect = new Rectangle(width * 0.8, height / 4);
            borderRect.setArcWidth(20);
            borderRect.setArcHeight(20);
            borderRect.setStroke(Color.web(theme.getBackgroundColor()));
            borderRect.setStrokeWidth(3);
            borderRect.setFill(Color.color(1, 1, 1, 0.08));
            card.getChildren().add(borderRect);

            Rectangle preview = new Rectangle(width * 0.4, height / 4);
            preview.setArcWidth(10);
            preview.setArcHeight(10);
            preview.setFill(Color.web(theme.getBackgroundColor()));
            preview.setOpacity(0.3);

            Image previewImage = new Image(theme.getImageUrl(), true);
            previewImage.progressProperty().addListener((obs, old, progress) -> {
                if (progress.doubleValue() >= 1.0 && !previewImage.isError()) {
                    preview.setFill(new ImagePattern(previewImage));
                    preview.setOpacity(1.0);
                }
            });

            AnchorPane.setTopAnchor(preview, 10.0);
            AnchorPane.setLeftAnchor(preview, 10.0);
            card.getChildren().add(preview);

            Text title = new Text(theme.getName());
            title.setFill(Color.WHITE);
            title.setFont(Font.font("Arial", Math.min(width, height) / 25.0));
            AnchorPane.setTopAnchor(title, 60.0);
            AnchorPane.setLeftAnchor(title, width * 0.5);
            card.getChildren().add(title);

            card.setOnMouseClicked(e -> {
                this.themeManager.setCurrentTheme(theme);
                stage.setScene(this.scene);
                buildLayout();
            });

            contentBox.getChildren().add(card);
        }

        scrollPane.setContent(contentBox);
        customiseLayout.getChildren().add(scrollPane);
    }

    /**
     * Dessine les axes et la grille dans le displayRect
     * @param gc Graphic context pour dessiner dessus
     * @param displayRectWidth largeur de displayRect
     * @param displayRectHeight hauteur de displayRect
     * @param displayRectStartingX Coordonée en x du point haut gauche de displayRect
     * @param displayRectStartingY Coordonée en y du point haut gauche de displayRect
     * @param epsilon Variable pour éviter l'overlapping entre le display rect et les axes du repère Cartésien
     * @param zoomLevel Zoom du graphe
     */
    private void drawAxes(GraphicsContext gc, double displayRectWidth, double displayRectHeight,
            double displayRectStartingX, double displayRectStartingY, double epsilon, double zoomLevel) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        double originx = displayRectWidth / 2 + displayRectStartingX;
        double originy = displayRectStartingY + displayRectHeight / 2;

        // Axes
        gc.strokeLine(displayRectStartingX + epsilon, originy, (displayRectWidth + displayRectStartingX) - epsilon,
                originy); // Axe X
        gc.strokeLine(originx, displayRectStartingY + epsilon, originx,
                (displayRectHeight + displayRectStartingY) - epsilon); // Axe Y
        double step = 20 * zoomLevel;

        gc.setStroke(Color.GRAY);
        for (double x = originx; x < originx + displayRectWidth / 2; x += step)
            gc.strokeLine(x, originy - 5, x, originy + 5);
        for (double x = originx; x > originx - displayRectWidth / 2; x -= step)
            gc.strokeLine(x, originy - 5, x, originy + 5);
        for (double y = originy; y < originy + displayRectHeight / 2; y += step)
            gc.strokeLine(originx - 5, y, originx + 5, y);
        for (double y = originy; y > originy - displayRectHeight / 2; y -= step)
            gc.strokeLine(originx - 5, y, originx + 5, y);

        // Labels
        for (int i = -(int) Math.floor(displayRectWidth / (step * 2) + 0.5)
                + 1; i <= (int) Math.floor(displayRectWidth / (step * 2) + 0.5) - 1; i++) {
            if (i == 0)
                continue;
            gc.fillText(String.valueOf(i), originx + i * step - 5, originy + 15);
        }
        for (int i = -(int) Math.floor(displayRectHeight / (step * 2) + 0.5)
                + 1; i <= (int) Math.floor(displayRectHeight / (step * 2) + 0.5) - 1; i++) {
            if (i == 0)
                continue;
            gc.fillText(String.valueOf(i), originx + 5, originy - i * step + 5);
        }

        gc.setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.4));
        for (double i = displayRectStartingX + step / 2; i < displayRectWidth + displayRectStartingX; i += step) {
            gc.strokeLine(i, displayRectStartingY + epsilon, i, displayRectHeight + displayRectStartingY - epsilon);
        }

        for (double j = displayRectStartingY + step; j < displayRectHeight + displayRectStartingY; j += step) {
            gc.strokeLine(displayRectStartingX + epsilon, j, displayRectWidth + displayRectStartingX - epsilon, j);
        }
    }
    
    /**
     * Crée le menu pour le graphe
     */
    private void showGraphMenu() {
        if (stage == null)
            return;
            
        if (graphLayout == null) {
            graphLayout = new AnchorPane();
            graphScene = new Scene(graphLayout, width, height);
            setupSceneListenersFor(graphScene, this::buildGraphLayout);
        }
        
        buildGraphLayout();
        stage.setScene(graphScene);
    }
    
    /**
     * Construit le layout du menu graphe
     */
    private void buildGraphLayout() {
        graphLayout.getChildren().clear();
        graphLayout.setStyle("-fx-background-color: #202020;");
        
        // Variable pour éviter l'overlapping entre le display rect et les axes du repère Cartésien
        double epsilon = width / 60;
        final double[] zoomLevel = { 1.0 };
        final double minZoom = 0.1;
        final double maxZoom = 10.0;
        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

        addBackgroundToLayout(graphLayout);

        // Canvas pour tracer
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // --- zone d'affichage
        Rectangle displayRect = createStyledDisplayRect(width / 8, height / 8, width * 0.75, height / 4);
        AnchorPane.setTopAnchor(displayRect, displayRect.getY());
        AnchorPane.setLeftAnchor(displayRect, displayRect.getX());
        graphLayout.getChildren().add(displayRect);

        // --- zone de saisie
        Rectangle funRect = createStyledDisplayRect(width / 8, height / 2.125, width * 0.75, height / 12);
        AnchorPane.setTopAnchor(funRect, (double) height / 2.125);
        AnchorPane.setLeftAnchor(funRect, (double) width / 8);
        graphLayout.getChildren().add(funRect);

        // Champ de texte pour afficher la fonction saisie
        TextField functionField = new TextField();
        functionField.setStyle("-fx-background-color: transparent; -fx-text-fill: "
                + themeManager.getCurrentTheme().getBackgroundColor() + "; -fx-font-size: 18px;");
        AnchorPane.setTopAnchor(functionField, (double) height / 2.125 + 10);
        AnchorPane.setLeftAnchor(functionField, (double) width / 8 + 20);
        AnchorPane.setRightAnchor(functionField, (double) width / 8 + 20);
        graphLayout.getChildren().add(functionField);

        // --- Tracer le repère
        drawAxes(gc, displayRect.getWidth(), displayRect.getHeight(), displayRect.getX(), displayRect.getY(), epsilon,
                zoomLevel[0]);
        graphLayout.getChildren().add(canvas);

        // --- Boutons
        ButtonDimensions dims = new ButtonDimensions(width, height, 6.0, 18.0);

        Button[] allButtons = {
                new Button("x"), new Button("("), new Button(")"), new Button("⌫"),
                new Button("cos"), new Button("sin"), new Button("exp"), new Button("ln"),
                new Button("7"), new Button("8"), new Button("9"), new Button("÷"),
                new Button("4"), new Button("5"), new Button("6"), new Button("*"),
                new Button("1"), new Button("2"), new Button("3"), new Button("-"),
                new Button("0"), new Button("."), new Button("¹/"), new Button("+")
        };

        double startTop = height / 1.50;
        double startLeft = width / 8.0;

        createButtonGrid(graphLayout, allButtons, startTop - height / 10, startLeft, 4, dims);

        // Ajouter les handlers pour les boutons
        for (int i = 0; i < allButtons.length; i++) {
            Button btn = allButtons[i];
            if (i == 3) {
                btn.setOnAction(e -> {
                    String text = functionField.getText();
                    if (!text.isEmpty()) {
                        functionField.setText(text.substring(0, text.length() - 1));
                    }
                });
            } else if (i == 7) {
                btn.setOnAction(e -> {
                    functionField.appendText("log");
                });
            } else {
                btn.setOnAction(e -> {
                    functionField.appendText(btn.getText());
                });
            }
        }

        // Bouton "Tracer"
        Button plotButton = new Button("Tracer");
        styleButton(plotButton, dims.width * 1.5, dims.height, dims.fontSize, dims.borderWidth, dims.borderRadius);
        AnchorPane.setTopAnchor(plotButton, height - dims.height * 10.75);
        AnchorPane.setLeftAnchor(plotButton, (double) width / 8);
        graphLayout.getChildren().add(plotButton);

        // --- Action de tracé
        plotButton.setOnAction(e -> {
            gc.clearRect(0, 0, width, height);
            gc.setLineWidth(1.5);

            drawAxes(gc, displayRect.getWidth(), displayRect.getHeight(), displayRect.getX(), displayRect.getY(),
                    epsilon, zoomLevel[0]);

            String expr = functionField.getText();
            if (expr == null || expr.isEmpty())
                return;

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            double scaleX = 40 * zoomLevel[0];
            double scaleY = 40 * zoomLevel[0];
            double originX = width * 0.75 / 2 + width / 8;
            double originY = height / 16 + (height / 2.5) / 2;

            double xmin = -((width * 0.75) / 2) / scaleX;
            double xmax = ((width * 0.75) / 2) / scaleX;

            double prevX = xmin;
            double prevY = expressionEvaluator.evaluate(functionField.getText(), prevX);

            for (double x = xmin + 0.01; x <= xmax; x += 0.01) {
                double y = expressionEvaluator.evaluate(functionField.getText(), x);
                if ((Double.isNaN(y) || Double.isInfinite(y))) {
                    prevX = x;
                    prevY = y;
                    continue;
                }

                double px1 = originX + prevX * scaleX;
                double py1 = originY - prevY * scaleY;
                double px2 = originX + x * scaleX;
                double py2 = originY - y * scaleY;
                // On vérifie que la courbe est toujours bien in bound
                if ((py2 > displayRect.getY() + epsilon
                        && py2 < (displayRect.getY() + displayRect.getHeight()) - epsilon))
                    gc.strokeLine(px1, py1, px2, py2);

                prevX = x;
                prevY = y;
            }
        });

        Button backButton = new Button("↩");
        styleButton(backButton, width / 6.0, height / 12.0, 
                    Math.min(width, height) / 20.0, 
                    Math.max(2.0, width / 150.0), 
                    width / 36.0);
        backButton.setOnAction(e -> stage.setScene(this.scene));
        AnchorPane.setTopAnchor(backButton, 20.0);
        AnchorPane.setLeftAnchor(backButton, 20.0);
        graphLayout.getChildren().add(backButton);

        Button zoomInButton = new Button("+");
        styleButton(zoomInButton, dims.width, dims.height, dims.fontSize, dims.borderWidth, dims.borderRadius);
        AnchorPane.setTopAnchor(zoomInButton, height - dims.height * 10.75);
        AnchorPane.setLeftAnchor(zoomInButton, (double) width / 8 + dims.width * 1.5 + dims.spacing);
        graphLayout.getChildren().add(zoomInButton);
        zoomInButton.setOnAction(e -> {
            if (zoomLevel[0] < maxZoom) {
                zoomLevel[0] *= 1.5;
                plotButton.fire();
            }
        });

        Button zoomOutButton = new Button("-");
        styleButton(zoomOutButton, dims.width, dims.height, dims.fontSize, dims.borderWidth, dims.borderRadius);
        AnchorPane.setTopAnchor(zoomOutButton, height - dims.height * 10.75);
        AnchorPane.setLeftAnchor(zoomOutButton, (double) width / 8 + dims.width * 2.5 + dims.spacing * 2);
        graphLayout.getChildren().add(zoomOutButton);
        zoomOutButton.setOnAction(e -> {
            if (zoomLevel[0] > minZoom) {
                zoomLevel[0] /= 1.5;
                plotButton.fire();
            }
        });
    }

    /**
     * Crée le menu des polynômes
     */
    public void showEqMenu(){
        if (stage == null)
            return;
            
        if (eqLayout == null) {
            eqLayout = new AnchorPane();
            eqScene = new Scene(eqLayout, width, height);
            setupSceneListenersFor(eqScene, this::buildEqLayout);
        }
        
        buildEqLayout();
        stage.setScene(eqScene);
    }
    
    /**
     * Construit le layout du  menu des polynômes
     */
    private void buildEqLayout(){
        eqLayout.getChildren().clear();
        eqLayout.setStyle("-fx-background-color: #202020;");

        addBackgroundToLayout(eqLayout);

        Rectangle displayRect = createStyledDisplayRect(width / 8, height / 8, width * 0.75, height / 4);
        AnchorPane.setTopAnchor(displayRect, displayRect.getY());
        AnchorPane.setLeftAnchor(displayRect, displayRect.getX());
        eqLayout.getChildren().add(displayRect);

        Text polynomialText = new Text();
        final List<NombreComplexe[]> racines = new ArrayList<>();
        Solveur solveur = new Solveur();

        polynomialText.setStyle("-fx-font-size: " + (Math.min(width, height) / 20.0) + "px; -fx-fill: "+themeManager.getCurrentTheme().getBackgroundColor()+";");
        AnchorPane.setTopAnchor(polynomialText, displayRect.getY() + height / 64);
        AnchorPane.setLeftAnchor(polynomialText, displayRect.getX() + width / 20);
        eqLayout.getChildren().add(polynomialText);

        double[] coefficients = new double[4]; 
        int[] currentDegree = {1}; 
        int[] currentCoeffIndex = {0}; 
        StringBuilder[] coeffInputs = new StringBuilder[4];
        for (int i = 0; i < 4; i++) {
            coeffInputs[i] = new StringBuilder("0");
        }

        Runnable updatePolynomial = () -> {
            StringBuilder poly = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                try {
                    coefficients[i] = Double.parseDouble(coeffInputs[i].toString());
                } catch (NumberFormatException e) {
                    coefficients[i] = 0;
                }
            }
            if (currentDegree[0] == 1) {
                poly.append("P(x) = ").append(String.format("%.1f", coefficients[0])).append("x + ").append(String.format("%.2f", coefficients[1]));
            } else if (currentDegree[0] == 2) {
                poly.append("P(x) = ").append(String.format("%.1f", coefficients[0])).append("x² + ")
                    .append(String.format("%.1f", coefficients[1])).append("x + ")
                    .append(String.format("%.1f", coefficients[2]));
            } else if (currentDegree[0] == 3) {
                poly.append("P(x) = ").append(String.format("%.1f", coefficients[0])).append("x³ + ")
                    .append(String.format("%.1f", coefficients[1])).append("x² + ")
                    .append(String.format("%.1f", coefficients[2])).append("x + ")
                    .append(String.format("%.1f", coefficients[3]));
            }
            poly.append("\n");
            if (!racines.isEmpty()){
                for (int i = 0; i<racines.get(0).length;i++) {
                    NombreComplexe racine = racines.get(0)[i];
                    poly.append("\nr"+i+" = ").append(String.format("%.2f", racine.getPartieReelle()));
                    if (racine.getPartieImaginaire()>0.0) {
                        poly.append(" + ").append(String.format("%.2f", racine.getPartieImaginaire())).append("i");
                    } else if (racine.getPartieImaginaire()<0.0) {
                        poly.append(" - ").append(String.format("%.2f", Math.abs(racine.getPartieImaginaire()))).append("i");
                    }
                }
            }
            polynomialText.setText(poly.toString());
        };

        ButtonDimensions dims = new ButtonDimensions(width, height);

        Button[] allButtons = {
                new Button("7"), new Button("8"), new Button("9"), 
                new Button("4"), new Button("5"), new Button("6"), 
                new Button("1"), new Button("2"), new Button("3"), 
                new Button("0"), new Button("."), new Button("⌫"), 
        };
        
        double startTop = height / 1.50;
        double totalPanelWidth = 3 * dims.width + 2 * dims.spacing;
        double startLeft = (width - totalPanelWidth) / 2.0;

        createButtonGrid(eqLayout, allButtons, startTop - height / 10, startLeft, 3, dims);

        // Ajouter les handlers pour les boutons
        for (Button btn : allButtons) {
            if (!btn.getText().equals("⌫")) {
                btn.setOnAction(e -> {
                    if (coeffInputs[currentCoeffIndex[0]].toString().equals("0")) {
                        coeffInputs[currentCoeffIndex[0]] = new StringBuilder();
                    }
                    coeffInputs[currentCoeffIndex[0]].append(btn.getText());
                    updatePolynomial.run();
                });
            } else {
                btn.setOnAction(e -> {
                    if (coeffInputs[currentCoeffIndex[0]].length() > 0) {
                        coeffInputs[currentCoeffIndex[0]].deleteCharAt(coeffInputs[currentCoeffIndex[0]].length() - 1);
                        if (coeffInputs[currentCoeffIndex[0]].length() == 0) {
                            coeffInputs[currentCoeffIndex[0]].append("0");
                        }
                        updatePolynomial.run();
                    }
                });
            }
        }
        
        Button backButton = new Button("↩");
        styleButton(backButton, width / 6.0, height / 12.0, 
                    Math.min(width, height) / 20.0, 
                    Math.max(2.0, width / 150.0), 
                    width / 36.0);
        backButton.setOnAction(e -> stage.setScene(this.scene));
        AnchorPane.setTopAnchor(backButton, 20.0);
        AnchorPane.setLeftAnchor(backButton, 20.0);
        eqLayout.getChildren().add(backButton);

        Button degreeLabel = new Button("Degré: " + currentDegree[0]);
        styleButton(degreeLabel, width / 3.5, height / 12.0, dims.fontSize, dims.borderWidth, dims.borderRadius);
        AnchorPane.setTopAnchor(degreeLabel, 20.0);
        AnchorPane.setLeftAnchor(degreeLabel, (width/2.0 - width/7.0));
        eqLayout.getChildren().add(degreeLabel);

        Button decreaseDegree = new Button("-");
        styleButton(decreaseDegree, width / 8.0, height / 12.0, dims.fontSize, dims.borderWidth, dims.borderRadius);
        decreaseDegree.setOnAction(e -> {
            if (currentDegree[0] > 1) {
                currentDegree[0]--;
                degreeLabel.setText("Degré: " + currentDegree[0]);
                currentCoeffIndex[0] = 0;
                updatePolynomial.run();
            }
        });
        AnchorPane.setTopAnchor(decreaseDegree, 20.0);
        AnchorPane.setLeftAnchor(decreaseDegree, width/2.0 - width/7.0 - width/8.0);
        eqLayout.getChildren().add(decreaseDegree);

        Button increaseDegree = new Button("+");
        styleButton(increaseDegree, width / 8.0, height / 12.0, dims.fontSize, dims.borderWidth, dims.borderRadius);
        increaseDegree.setOnAction(e -> {
            if (currentDegree[0] < 3) {
                currentDegree[0]++;
                degreeLabel.setText("Degré: " + currentDegree[0]);
                currentCoeffIndex[0] = 0;
                updatePolynomial.run();
            }
        });
        AnchorPane.setTopAnchor(increaseDegree, 20.0);
        AnchorPane.setLeftAnchor(increaseDegree, width/2.0 + width/7.0);
        eqLayout.getChildren().add(increaseDegree);

        Button upArrow = new Button("◁");
        styleButton(upArrow, width / 10.0, height / 15.0, dims.fontSize * 0.8, dims.borderWidth, dims.borderRadius);
        upArrow.setOnAction(e -> {
            if (currentCoeffIndex[0] > 0) {
                currentCoeffIndex[0]--;
            }
        });
        AnchorPane.setTopAnchor(upArrow, height / 2.25);
        AnchorPane.setLeftAnchor(upArrow, width * 0.5 - width/5.0 - width/10.0);
        eqLayout.getChildren().add(upArrow);

        Button downArrow = new Button("▷");
        styleButton(downArrow, width / 10.0, height / 15.0, dims.fontSize * 0.8, dims.borderWidth, dims.borderRadius);
        downArrow.setOnAction(e -> {
            int maxIndex = currentDegree[0];
            if (currentCoeffIndex[0] < maxIndex) {
                currentCoeffIndex[0]++;
            }
        });
        AnchorPane.setTopAnchor(downArrow, height / 2.25);
        AnchorPane.setLeftAnchor(downArrow, width * 0.5 + width/5.0);
        eqLayout.getChildren().add(downArrow);

        Button findRacinesButton = new Button("Trouver les racines");
        styleButton(findRacinesButton, width / 2.5, height / 15.0, dims.fontSize * 0.8, dims.borderWidth, dims.borderRadius);

        findRacinesButton.setOnAction(e -> {
            NombreComplexe[] result = switch(currentDegree[0]){
                case 1 -> solveur.Resolve1D(coefficients[0], coefficients[1]); 
                case 2 -> solveur.Resolve2D(coefficients[0], coefficients[1], coefficients[2]);
                case 3 -> solveur.Resolve3D(coefficients[0], coefficients[1], coefficients[2], coefficients[3]);
                default -> throw new IllegalArgumentException();
            };
            racines.clear();
            racines.add(result);
            updatePolynomial.run();
        });
        
        AnchorPane.setTopAnchor(findRacinesButton, height / 2.25);
        AnchorPane.setLeftAnchor(findRacinesButton, width * 0.5 - width/5.0);
        eqLayout.getChildren().add(findRacinesButton);

        updatePolynomial.run();
    }

    @Override
    public void affichage() {

    }

    /**
     * Met à jour le texte de l'accumulateur
     * @param accu Objet représentant l'accumulateur
     */
    @Override
    public void change(String accu) {
        accumulatorDisplay.setText(accu);
    }

    /**
     * Met à jour le texte de la pile
     * @param stackData Objet représentant la pile
     */
    @Override
    public void change(List<Double> stackData) {
        stackDisplay.getItems().clear();
        for (int i = stackData.size() - 1; i >= 0; i--) {
            stackDisplay.getItems().add(stackData.get(i));
        }
    }

    /**
     * Lie le bouton add à l'action add
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setAddButtonHandler(EventHandler<ActionEvent> handler) {
        addButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton subtrac à l'action subtrac
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setSubtractButtonHandler(EventHandler<ActionEvent> handler) {
        subtractButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton multiply à l'action multiply
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setMultiplyButtonHandler(EventHandler<ActionEvent> handler) {
        multiplyButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton divide à l'action divide
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setDivideButtonHandler(EventHandler<ActionEvent> handler) {
        divideButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton opposite à l'action opposite
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setOppositeButtonHandler(EventHandler<ActionEvent> handler) {
        oppositeButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton push à l'action push
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setPushButtonHandler(EventHandler<ActionEvent> handler) {
        pushButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton pop à l'action pop
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setPopButtonHandler(EventHandler<ActionEvent> handler) {
        popButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton drop à l'action drop
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setDropButtonHandler(EventHandler<ActionEvent> handler) {
        dropButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton swap à l'action swap
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setSwapButtonHandler(EventHandler<ActionEvent> handler) {
        swapButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie le bouton clear à l'action clear
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setClearButtonHandler(EventHandler<ActionEvent> handler) {
        clearButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Lie les boutons chiffres à l'action ajouter un chiffre
     * @param digit   Chiffre d'entrée
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setDigitButtonHandler(int digit, EventHandler<ActionEvent> handler) {
        if (digit >= 0 && digit <= 9) {
            digitButtons[digit].addEventFilter(ActionEvent.ACTION, handler);
        }
    }

    /**
     * Lie le bouton décimal à l'action ajouter une virgule
     * @param handler Objet représentant l'action de l'utilisateur
     */
    public void setDecimalPointHandler(EventHandler<ActionEvent> handler) {
        decimalButton.addEventFilter(ActionEvent.ACTION, handler);
    }

    /**
     * Getter pour l'accumulateur 
     * @return l'accumulateur
     */
    public String getAccuDisplay() {
        return accumulatorDisplay.getText();
    }
}