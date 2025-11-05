import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe mère pour lancer la calculatrice
 */
public class CalculatorApp extends Application {
    /**
     * Initialise et le lance le programme
     * @param primaryStage scène principale
     */
    @Override
    public void start(Stage primaryStage) {
        
        try {
            CalculatorModelInterface model = new CalculatorModel();
            
            CalculatorGUI view = new CalculatorGUI(primaryStage);
            primaryStage.setScene(view.getScene());
            
            view.setStage(primaryStage);
            
            CalculatorController controller = new CalculatorController(model, view);
            
            primaryStage.setTitle("Calculatrice RPN - Thématique");
            primaryStage.setScene(view.getScene());
            primaryStage.setResizable(true);
            
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}