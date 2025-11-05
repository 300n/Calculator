import java.util.Stack;

/**
 * Classe qui implémente le modèle de la calculatrice
 */
public class CalculatorModel implements CalculatorModelInterface {
    
    private double accu;
    private Stack<Double> memory;
    /**
     *  Constructeur de Calculator Model
     */
    public CalculatorModel() {
        this.accu = 0.0;
        this.memory = new Stack<>();
    }

    /**
     * Opération d'ajout entre le dernier élément de la pile et l'élément présent dans l'accumuateur
     */
    @Override
    public void add(){
        if (!this.memory.isEmpty()) {this.accu += this.memory.pop();}
    }

    /**
     * Opération de soustraction entre le dernier élément de la pile et l'élément présent dans l'accumuateur
     */
    @Override
    public void substract(){
        if (!this.memory.isEmpty()) {this.accu -= this.memory.pop();}
    }

    /**
     * Opération de multiplication entre le dernier élément de la pile et l'élément présent dans l'accumuateur
     */
    @Override
    public void multiply(){
        if (!this.memory.isEmpty()) {this.accu *= this.memory.pop();}
    }

    /**
     * Opération de division entre le dernier élément de la pile et l'élément présent dans l'accumuateur
     */
    @Override
    public void divide(){
        if (!this.memory.isEmpty()) {this.accu /= this.memory.pop();}
    }

    /**
     * Opération d'inverse de l'ément présent dans l'accumulateur
     */
    @Override
    public void opposite(){
        this.accu = 1/this.accu;
    }

    /**
     * Ajoute l'élément de l'accumulateur au sommet de la pile 
     */
    @Override
    public void push(){
        this.memory.push(accu);
        this.accu = 0;
    }

    /**
     * Supprime l'élément présent au sommet de la pile pour le mettre dans l'accumulateur
     */
    @Override
    public void pop(){
        if (!this.memory.isEmpty()) {this.accu = this.memory.pop();}
    }

    /**
     * Supprime l'élément présent au sommet de la pile
     */
    @Override
    public void drop(){
        if (!this.memory.isEmpty()) {this.memory.pop();}
    }

    /**
     * Echange l'élément présent au sommet de la pile et l'élément de l'accumulateur
     */
    @Override
    public void swap(){
        if (!this.memory.isEmpty()) {
            double temp = this.accu;
            this.accu = this.memory.pop();
            this.memory.push(temp);
        }
    }

    /**
     * Supprime l'élément présent dans l'accumulateur
     */
    @Override
    public void clear(){
        accu = 0.0;
    }

    /**
     * Getter pour l'accumulateur
     * @return l'accumulateur
     */
    @Override
    public double getAccu() {
        return accu;
    }
    
    /**
     * Getter pour la pile
     * @return la pile
     */
    @Override
    public Stack<Double> getMemory() {
        return memory;
    }
    
    /**
     * Défini la valeur de l'accumulateur
     * @param accu la valeur de l'accumulateur
     */
    public void setAccu(double accu) {
        this.accu = accu;
    }
}