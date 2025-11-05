import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui implémente le controller
 */
public class CalculatorController implements CalculatorControllerInterface {
    
    private CalculatorModelInterface model;
    private CalculatorGUIInterface view;
    
    /**
     * Initialise le controlleur
     * @param model model utilisé par la partie logique
     * @param view interface graphique de la calculatrice
     */
    public CalculatorController(CalculatorModelInterface model, CalculatorGUIInterface view) {
        this.model = model;
        this.view = view;
        initializeEventHandlers();
        updateView();
    }

    /**
     * Modifie la vue de l'accumulateur 
     * @param accu l'accumulateur
     */
    @Override
    public void changeAccu(String accu) {
        view.change(accu);
    }
    
    /**
     * Modifie la vue de la pile 
     * @param stackData pile des données
     */
    @Override
    public void changeStackData(List<Double> stackData) {
        view.change(stackData);
    }
    
    /**
     * Met à jour la vue de la calculatrice
     */
    private void updateView() {
        changeAccu(String.valueOf(model.getAccu()));
        changeStackData(new ArrayList<>(model.getMemory()));
    }
    
    /**
     * S'occupe de la gestion des nouveaux chiffres
     * @param digit chiffre à ajouter 
     */
    private void handleDigitInput(int digit) {
        String currentAccu = ((CalculatorGUI)view).getAccuDisplay();
        
        // Vérifier si la valeur est 0 (quelle que soit sa représentation : "0", "0.0", "0.00", etc.)
        try {
            double value = Double.parseDouble(currentAccu);
            if (value == 0.0) {
                currentAccu = String.valueOf(digit);
            } else {
                currentAccu += digit;
            }
        } catch (NumberFormatException e) {
            // Si ce n'est pas un nombre valide, remplacer par le chiffre
            currentAccu = String.valueOf(digit);
        }
        
        changeAccu(currentAccu);
        model.setAccu(Double.parseDouble(currentAccu));
    }
    
    /**
     * S'occupe d'ajouter un point pour les nombres décimaux
     */
    private void handleDecimalPoint() {
        String currentAccu = ((CalculatorGUI)view).getAccuDisplay();
        
        if (!currentAccu.contains(".")) {
            currentAccu += ".";
            changeAccu(currentAccu);
        }
    }
    
    /**
     * Initialise le contrôleur d'évènements
     */
    private void initializeEventHandlers() {
        for (int i = 0; i <= 9; i++) {
            final int digit = i;
            ((CalculatorGUI)view).setDigitButtonHandler(digit, e -> {
                handleDigitInput(digit);
            });
        }
        
        ((CalculatorGUI)view).setDecimalPointHandler(e -> {
            handleDecimalPoint();
        });
        
        ((CalculatorGUI)view).setAddButtonHandler(e -> {
            model.add();
            updateView();
        });
        
        ((CalculatorGUI)view).setSubtractButtonHandler(e -> {
            model.subtract();
            updateView();
        });
        
        ((CalculatorGUI)view).setMultiplyButtonHandler(e -> {
            model.multiply();
            updateView();
        });
        
        ((CalculatorGUI)view).setDivideButtonHandler(e -> {
            model.divide();
            updateView();
        });
        
        ((CalculatorGUI)view).setOppositeButtonHandler(e -> {
            model.opposite();
            updateView();
        });
        
        ((CalculatorGUI)view).setPushButtonHandler(e -> {
            model.push();
            updateView();
        });
        
        ((CalculatorGUI)view).setPopButtonHandler(e -> {
            model.pop();
            updateView();
        });
        
        ((CalculatorGUI)view).setDropButtonHandler(e -> {
            model.drop();
            updateView();
        });
        
        ((CalculatorGUI)view).setSwapButtonHandler(e -> {
            model.swap();
            updateView();
        });
        
        ((CalculatorGUI)view).setClearButtonHandler(e -> {
            model.clear();
            updateView();
        });
    }
}