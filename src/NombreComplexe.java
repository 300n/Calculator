/**
 * Cette classe représente un nombre complexe 
 */
public class NombreComplexe {
    private double partieReelle;
    private double partieImaginaire;

    /**
     * Constructeur pour créer un nombre complexe avec les parties réelle et imaginaire spécifiées.
     * @param partieReelle la partie réelle 
     * @param partieImaginaire la partie imaginaire 
     */
    public NombreComplexe(double partieReelle, double partieImaginaire) {
        this.partieReelle = partieReelle;
        this.partieImaginaire = partieImaginaire;
    }

    /**
     * Constructeur pour créer un nombre complexe avec seulement une partie réelle.
     * @param partieReelle la partie réelle 
     */
    public NombreComplexe(double partieReelle) {
        this(partieReelle, 0);
    }

    /**
     * Retourne la partie réelle de ce nombre complexe.
     * @return la partie réelle
     */
    public double getPartieReelle() {
        return partieReelle;
    }

    /**
     * Retourne la partie imaginaire de ce nombre complexe.
     * @return la partie imaginaire
     */
    public double getPartieImaginaire() {
        return partieImaginaire;
    }

    /**
     * Effectue l'addition de ce nombre complexe avec un autre nombre complexe.
     * @param autre l'autre nombre complexe à ajouter
     * @return la somme
     */
    public NombreComplexe addition(NombreComplexe autre) {
        return new NombreComplexe(
            this.partieReelle + autre.partieReelle,
            this.partieImaginaire + autre.partieImaginaire
        );
    }

    /**
     * Effectue la soustraction de ce nombre complexe par un autre nombre complexe.
     * @param autre l'autre nombre complexe à soustraire
     * @return la différence
     */
    public NombreComplexe soustraction(NombreComplexe autre) {
        return new NombreComplexe(
            this.partieReelle - autre.partieReelle,
            this.partieImaginaire - autre.partieImaginaire
        );
    }

    /**
     * Effectue la multiplication de ce nombre complexe avec un autre nombre complexe.
     * @param autre l'autre nombre complexe à multiplier
     * @return le produit
     */
    public NombreComplexe multiplication(NombreComplexe autre) {
        double nouvellePartieReelle = this.partieReelle * autre.partieReelle - this.partieImaginaire * autre.partieImaginaire;
        double nouvellePartieImaginaire = this.partieReelle * autre.partieImaginaire + this.partieImaginaire * autre.partieReelle;
        return new NombreComplexe(nouvellePartieReelle, nouvellePartieImaginaire);
    }

    /**
     * Effectue la division de ce nombre complexe par un autre nombre complexe.
     * @param autre le diviseur 
     * @return le quotient
     */
    public NombreComplexe division(NombreComplexe autre) {
        double denominateur = autre.partieReelle * autre.partieReelle + 
                              autre.partieImaginaire * autre.partieImaginaire;
        
        if (denominateur == 0) {
            throw new ArithmeticException("Division par zéro");
        }
        
        double nouvellePartieReelle = (this.partieReelle * autre.partieReelle + 
                                        this.partieImaginaire * autre.partieImaginaire) / denominateur;
        double nouvellePartieImaginaire = (this.partieImaginaire * autre.partieReelle - 
                                            this.partieReelle * autre.partieImaginaire) / denominateur;
        return new NombreComplexe(nouvellePartieReelle, nouvellePartieImaginaire);
    }

    /**
     * Retourne le conjugué de ce nombre complexe.
     * @return le conjugué de ce nombre complexe
     */
    public NombreComplexe conjugue() {
        return new NombreComplexe(this.partieReelle, -this.partieImaginaire);
    }
}