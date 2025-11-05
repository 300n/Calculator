import java.util.Stack;
/**
 * Interface du modèle de la calculatrice
 */
public interface CalculatorModelInterface {
    /**
     *  Ajoute deux nombres entre eux
     */
    void add();
    /**
     *  Soustrait deux nombres entre eux
     */
    void subtract();
    /**
     *  Multiplie deux nombres entre eux
     */
    void multiply();
    /**
     *  Divise deux nombres entre eux
     */
    void divide();
    /**
     *  Inverse un nombre
     */
    void opposite();
    /**
     *  Ajoute un nombre à la pile
     */
    void push();
    /**
     *  Enlève le nombre au sommet de la pile pour le mettre dans l'accu
     */
    void pop();
    /**
     *  Enlève le nombre au sommet de la pile 
     */
    void drop();
    /**
     *  Echange le nombre au sommet de la pile et le nombre dans l'accu
     */
    void swap();
    /**
     *  Efface l'accu
     */
    void clear();
    /**
     *  Getter pour l'accu 
     */
    double getAccu();
    /**
     *  Getter pour la pile
     */
    Stack<Double> getMemory();
    /**
     *  Setter pour l'accu
     */
    void setAccu(double accu); 
}