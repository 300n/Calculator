
/**
 * Classe qui permet de trouver les racines de polynômes de degrés 1,2 et 3
 */
public class Solveur{
    public Solveur(){

    }
    /**
     * Trouve la solution de ax + b = 0
     * @param a coefficient devant x
     * @param b terme constant
     */
    public NombreComplexe[] Resolve1D(double a, double b) {
        NombreComplexe[] solution = {new NombreComplexe(-b/a,0)};
        return solution;
    }
    /**
     * Trouve les 2 solutions de ax² + bx + c = 0
     * @param a coefficient devant x²
     * @param b coefficient devant x
     * @param c terme constant
     */
    public NombreComplexe[] Resolve2D(double a, double b, double c) {
        NombreComplexe[] solutions = new NombreComplexe[2];
        double delta = b*b-4*a*c;
        if (delta > 0) {
            solutions[0] = new NombreComplexe((-b + Math.sqrt(delta)) / (2*a), 0);
            solutions[1] = new NombreComplexe((-b - Math.sqrt(delta)) / (2*a), 0);
        } else if (delta == 0) {
            solutions[0] = new NombreComplexe(-b/(2*a));
            solutions[1] = new NombreComplexe(-b/(2*a));
        } else {
            solutions[0] = new NombreComplexe(-b/(2*a), Math.sqrt(-delta)/(2*a));
            solutions[1] = new NombreComplexe(-b/(2*a), -Math.sqrt(-delta)/(2*a));
        }
        return solutions;
    }

    /**
     * Utilise les formules de Cardan pour trouver les 3 solutions de ax³ + bx² + cx + d = 0
     * @param a coefficient devant x³
     * @param b coefficient devant x²
     * @param c coefficient devant x
     * @param d terme constant
     */
    public NombreComplexe[] Resolve3D(double a, double b, double c, double d){
        NombreComplexe[] solutions = new NombreComplexe[3];
        double p = (3*a*c - b*b) / (3*a*a);
        double q = (2*b*b*b - 9*a*b*c + 27*a*a*d) / (27*a*a*a);
        double delta = (q/2)*(q/2) + (p/3)*(p/3)*(p/3);
        double correc = -b/(3*a);
        if (delta>0) {
            NombreComplexe omega = new NombreComplexe(-1.0/2.0, Math.sqrt(3)/2);
            NombreComplexe omegaConj = omega.multiplication(omega);
            double u = Math.cbrt(-(q/2) + Math.sqrt(delta));
            double v = Math.cbrt(-(q/2) - Math.sqrt(delta));

            solutions[0] = new NombreComplexe(u+v+correc, 0);

            NombreComplexe sol2 =  omega.multiplication(new NombreComplexe(u,0))
            .addition(omegaConj.multiplication(new NombreComplexe(v,0)));
            solutions[1] = new NombreComplexe(sol2.getPartieReelle()+correc,sol2.getPartieImaginaire());

            NombreComplexe sol3 =  omegaConj.multiplication(new NombreComplexe(u,0))
            .addition(omega.multiplication(new NombreComplexe(v,0)));
            solutions[2] = new NombreComplexe(sol3.getPartieReelle()+correc,sol3.getPartieImaginaire());

        } else if (delta == 0) {
            if (p==0 && q==0) {
                for (int i = 0; i<3; i++)
                    solutions[i] = new NombreComplexe(correc, 0);
            } else {
                solutions[0] = new NombreComplexe(2*Math.cbrt(-q/2) + correc, 0);
                solutions[1] = new NombreComplexe(-Math.cbrt(-q/2) + correc, 0);
                solutions[2] = new NombreComplexe(-Math.cbrt(-q/2) + correc, 0);
            }
            
        } else {
            for (int k = 0; k<3; k++) {
                double solution = 2 * Math.sqrt(-p/3) *Math.cos((1.0/3)* (Math.acos((3*q)/(2*p) * Math.sqrt(-3/p)) - (2*Math.PI*k)/3));
                solutions[k] = new NombreComplexe(solution + correc, 0);
            }
        }
        return solutions;
    }

    // /**
    //  * Utilise les formules de Ferrari pour trouver les 4 solutions de ax⁴ + bx³ + cx² + dx + e = 0
    //  * @param a coefficient devant x⁴
    //  * @param b coefficient devant x³
    //  * @param c coefficient devant x²
    //  * @param d coefficient devant x
    //  * @param e terme constant
    //  */
    // public double[] Resolve4D(double a, double b, double c, double d, double e){
    //     double[] solutions;
    //     double p = (8*a*c - 3*b*b) / (8*a*a);
    //     double q = (b*b*b - 4*a*b*c + 8*(a*a)*d) / (8*a*a*a);
    //     double r = (-3*b*b*b*b + 256*(a*a*a)*e - 64*(a*a)*b*d + 16*a*b*b2*c) / (256*a*a*a*a);
    //     double correc = -b/(4*a);

    //     // On ajoute une variable z qui doit satisfaire 8z³ + 8pz² + (2p² - 8r) - q² = 0
    //     NombreComplexe[] racineZ = Resolve3D(8, 8*p, 2*p*p - 8*r, -q*q);
    //     // On récupère ensuite une racine réelle de racineZ
    //     NombreComplexe z0 = racineZ[0];
    //     for (int i = 1; i<3; i++) {
    //         // Comme il existe forcément une racine purement réelle on cherche la racine avec la partie imaginaire la plus faible 
    //         if (Math.abs(z0.getpartieImaginaire()) > Math.abs(racineZ[i].getpartieImaginaire())) {
    //             z0 = racineZ[i];
    //         }
    //     }
    //     double z0_reel = z0.getPartieReelle();
    //     double discriminant1 = p+2*z0_reel;
    //     if (discriminant1 >= 0) {
    //         double sqrtDisc1 = Math.sqrt(discriminant1);
            
    //         if (sqrtDisc1 != 0) {
    //             double discriminant2 = -(q / sqrtDisc1);
    //         } else {
    //             double discriminant2 = 0;
    //         }
    //         double arg1 = p/2+z0_reel -discriminant2;
    //         if (arg1 >= 0) {
    //             double sqrt1 = Math.sqrt(arg1);
    //             solutions[0] = new NombreComplexe(-sqrtDisc1/2+sqrt1+correc, 0);
    //             solutions[1] = new NombreComplexe(-sqrtDisc1/2-sqrt1+correc, 0);
    //         } else {
    //             double sqrtImag1 = Math.sqrt(-arg1);
    //             solutions[0] = new NombreComplexe(-sqrtDisc1/2+correc, sqrtImag1);
    //             solutions[1] = new NombreComplexe(-sqrtDisc1/2 +correc, -sqrtImag1);
    //         }
            
    //         double arg2 = p/2 + z0_reel+discriminant2;
    //         if (arg2 >= 0) {
    //             double sqrt2 = Math.sqrt(arg2);
    //             solutions[2] = new NombreComplexe(sqrtDisc1/2+sqrt2+correc, 0);
    //             solutions[3] = new NombreComplexe(sqrtDisc1/2-sqrt2+correc, 0);
    //         } else {
    //             double sqrtImag2 = Math.sqrt(-arg2);
    //             solutions[2] = new NombreComplexe(sqrtDisc1/2+correc, sqrtImag2);
    //             solutions[3] = new NombreComplexe(sqrtDisc1/2+correc, -sqrtImag2);
    //         }
            
    //     } else {
    //         // En construction 
    //     }
        
    //     return solutions;
    // }
}