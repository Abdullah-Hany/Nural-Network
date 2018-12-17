/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nural_network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Beido
 */
public class Nural_network {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        int input_layer_nodes, hidden_layer_nodes, output_layer_nodes, input_number;
        Scanner input = new Scanner(new File("train.txt"));

//reading number of nodes in each layer
        input_layer_nodes = input.nextInt();
        hidden_layer_nodes = input.nextInt();
        output_layer_nodes = input.nextInt();
        input_number = input.nextInt();

//        System.out.println("input " + input_number);
//        System.out.println("hidden " + hidden_layer_nodes);
//        System.out.println("input " + output_layer_nodes);
//        

for (int z = 0 ; z<input_number ;z++)
{
    

//creating the input layer matrix
        Matrix input_layer = new Matrix();
        ArrayList<ArrayList<Double>> input_matrix = new ArrayList();

        for (int i = 0; i < input_layer_nodes; i++) {
            ArrayList<Double> node = new ArrayList();
            node.add(input.nextDouble());
            input_matrix.add(node);
        }

        input_layer.setLabel(input.nextDouble());

        input_layer.setMatrix_2D(input_matrix);

//        print_matrix(input_layer);
//      System.out.println(input_layer.getLabel());

//creating the hidden layer matrix with random weights
        Matrix hidden_layer = new Matrix();
        ArrayList<ArrayList<Double>> hidden_matrix = new ArrayList();
        //number of rows
        for (int i = 0; i < hidden_layer_nodes; i++) {
            ArrayList<Double> hidden_node = new ArrayList();
            //number of columns
            for (int j = 0; j < input_layer_nodes; j++) {
                double rand = randFloat(-5, 5);
                hidden_node.add(rand);
            }
            hidden_matrix.add(hidden_node);
        }
        hidden_layer.setMatrix_2D(hidden_matrix);

//        print_matrix(hidden_layer);

//creating the output layer matrix using random weights
        Matrix output_layer = new Matrix();
        ArrayList<ArrayList<Double>> output_matrix = new ArrayList();
        //num of rows
        for (int i = 0; i < output_layer_nodes; i++) {
            ArrayList<Double> output_node = new ArrayList();
            //num of columns
            for (int j = 0; j < hidden_layer_nodes; j++) {
                   double rand = randFloat(-5, 5);
             
                output_node.add(rand);
            }
            output_matrix.add(output_node);
        }
        output_layer.setMatrix_2D(output_matrix);

//        print_matrix(hidden_layer);
//        System.out.println();
//        print_matrix(output_layer);
        Matrix result = matrix_mulitplication(hidden_layer, input_layer);
        result = activation_function(result);
        
        result = matrix_mulitplication(output_layer, result);
        result = activation_function(result);
        print_matrix(result);
        
        

    }
    }

    public static Matrix matrix_mulitplication(Matrix matrix1, Matrix matrix2) {
        Matrix result_matrix = new Matrix();

//        System.out.println("Matrix 1 : ");
//        print_matrix(matrix1);
//       System.out.println("\n Matrix 2 : ");
//        print_matrix(matrix2);
        int matrix1_col, matrix1_rows, matrix2_rows, matrix2_col;
        double val = 0;
        matrix1_rows = matrix1.getMatrix_2D().size();
        matrix1_col = matrix1.getMatrix_2D().get(0).size();
        matrix2_rows = matrix2.getMatrix_2D().size();
        matrix2_col = matrix2.getMatrix_2D().get(0).size();

        //check if there is somthing wrong with matrix dimensions
        System.out.println("\n"+matrix1_rows + " x " + matrix1_col + "  x  " + matrix2_rows + " x " + matrix2_col);
        if (matrix1_col != matrix2_rows) {
            System.out.println("matrix mulitplication error !!");
            System.exit(0);
        }

        //intiallize result matrix with 0
        ArrayList<ArrayList<Double>> Result = new ArrayList();
        for (int i = 0; i < matrix1_rows; i++) {
            ArrayList<Double> result = new ArrayList();
            for (int j = 0; j < matrix2_col; j++) {
                result.add(0.0);

            }
            Result.add(result);
        }
        result_matrix.setMatrix_2D(Result);

        for (int i = 0; i < matrix1_rows; i++) {
            for (int j = 0; j < matrix2_col; j++) {
                for (int k = 0; k < matrix2_rows; k++) {
                    val = matrix1.getMatrix_2D().get(i).get(k) * matrix2.getMatrix_2D().get(k).get(j);
                    Result.get(i).set(j, Result.get(i).get(j) + val);
                }
            }
        }

        result_matrix.setMatrix_2D(Result);
 

        return result_matrix;
    }

    public static Matrix activation_function(Matrix matrix)
    {
  //applying sogmoid activation function for the matrix
        int rows = matrix.getMatrix_2D().size();
        int columns = matrix.getMatrix_2D().get(0).size();
        
        for (int i = 0 ; i< rows ; i++)
        {
            for (int j = 0 ; j < columns ;j++)
            {
                double before_activation =matrix.getMatrix_2D().get(i).get(j);
                
                matrix.getMatrix_2D().get(i).set(j, sigmoid(before_activation));
            }
        }
        return matrix;
    }
    
    public static double sigmoid (double number)
    {
        number = 1 / (1+Math.exp(-number));
        return number;
    }
    
    
    public static void print_matrix(Matrix matrix) {
        for (int i = 0; i < matrix.getMatrix_2D().size(); i++) {
            System.out.println(matrix.getMatrix_2D().get(i));
        }
    }
    
    //function that returns a random number between 2 limits 
    public static double randFloat(float min, float max) {

        Random rand = new Random();
        double result = rand.nextFloat() * (max - min) + min;
        return result;

    }


}
