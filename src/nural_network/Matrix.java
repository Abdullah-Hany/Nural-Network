/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nural_network;

import java.util.ArrayList;

/**
 *
 * @author Beido
 */
public class Matrix {
    double []label;
    ArrayList<ArrayList<Double>> matrix_2D;
    
    public Matrix() {
    }

  
    public ArrayList<ArrayList<Double>> getMatrix_2D() {
        return matrix_2D;
    }

    public void setMatrix_2D(ArrayList<ArrayList<Double>> matrix_2D) {
        this.matrix_2D = matrix_2D;
    }

    public double[] getLabel() {
        return label;
    }

    public void setLabel(double label[]) {
        this.label = label;
    }
    
    
}
