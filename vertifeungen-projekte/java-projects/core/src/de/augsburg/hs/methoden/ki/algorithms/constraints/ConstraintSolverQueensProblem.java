package de.augsburg.hs.methoden.ki.algorithms.constraints;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class ConstraintSolverQueensProblem {

    private Model model;

    private int nQueens;

    public int[] solve(int nQueens) {

        model = new Model(nQueens + "-queens problem");

        IntVar[] vars = new IntVar[nQueens];
        for(int q = 0; q < nQueens; q++){
            vars[q] = model.intVar("Q_"+q, 1, nQueens);
        }
        for(int i  = 0; i < nQueens-1; i++){
            for(int j = i + 1; j < nQueens; j++){
                model.arithm(vars[i], "!=",vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }

        Solution solution = model.getSolver().findSolution();

        int[] solutionValues = new int[nQueens];
        for(int i = 0; i < nQueens; i++) {
            solutionValues[i] = solution.getIntVal(vars[i]);
        }

        return solutionValues;
    }

}
