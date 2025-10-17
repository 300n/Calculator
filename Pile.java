import java.util.ArrayList;

public class Pile {
    public ArrayList<double> pile = new ArrayList<double>;

    public void push(int valeur) {
        this.pile.add(valeur);
    }

    public double pop() {
        double out = this.pile.get(this.pile.size()-1);
        this.pile.remove(this.pile.size()-1);
        return out;
    }

    public void drop() {
        this.pile.remove(this.pile.size()-1);
    }

    public void swap() {
        double temp = this.pile.get(this.pile.size()-2);
        this.pile.get(this.pile.size()-2) = this.pile.get(this.pile.size()-1);
        this.pile.get(this.pile.size()-1) = temp;
    }

    public void dropAll() {
        int sizePile = this.pile.size();
        for (int i = 0; i< sizePile ; i++) {
            this.drop();
        }
    }

    public boolean isEmpy() {
        return this.pile.size() == 0;
    }
}