import java.util.List;
/**
 * Interface qui définit les méthode de la classe CalculatorGUI
 */
public interface CalculatorGUIInterface {
    /**
     * Affiche l'UI
     */
    void affichage();  
    /**
     * Modifie l'accu
     */
    void change(String accu);
    /**
     * Modifie la pile
     */
    void change(List<Double> stackData);
}