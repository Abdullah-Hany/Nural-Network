/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nural_network;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Beido
 */
public class Neural_network {

    static int input_layer_nodes, hidden_layer_nodes, output_layer_nodes, input_number;
    static Matrix input_layer[], hidden_layer, output_layer[], result[], hidden_val[];
    static double error[][];
    static boolean weights = true;

    static void initialization() throws FileNotFoundException {
        Scanner input = new Scanner(new File("train.txt"));

        //Reading number of nodes in each layer
        input_layer_nodes = input.nextInt();
        hidden_layer_nodes = input.nextInt();
        output_layer_nodes = input.nextInt();

        //Reading number of test examples
        input_number = input.nextInt();
        input_layer = new Matrix[input_number];
        output_layer = new Matrix[input_number];
        for (int z = 0; z < input_number; z++) {

            //creating the input layer matrix
            input_layer[z] = new Matrix();
            ArrayList<ArrayList<Double>> input_matrix = new ArrayList();
            for (int i = 0; i < input_layer_nodes; i++) {
                ArrayList<Double> node = new ArrayList();
                node.add(input.nextDouble());
                input_matrix.add(node);
            }
            input_layer[z].setLabel(null);
            input_layer[z].setMatrix_2D(input_matrix);

            //         print_matrix(input_layer[z]);
//          System.out.println(input_layer.getLabel());
//          creating the output layer matrix using random weights
            output_layer[z] = new Matrix();
            ArrayList<ArrayList<Double>> output_matrix = new ArrayList();
            //num of rows
            double label[] = new double[output_layer_nodes];
            for (int i = 0; i < output_layer_nodes; i++) {
                ArrayList<Double> output_node = new ArrayList();
                label[i] = input.nextDouble();
                //num of columns
                for (int j = 0; weights && j < hidden_layer_nodes; j++) {
                    double rand = randFloat(-5, 5);
                    output_node.add(rand);
                }
                output_matrix.add(output_node);
            }
            weights = false;
            output_layer[z].setLabel(label);
            output_layer[z].setMatrix_2D(output_matrix);
            //           print_matrix(output_layer[z]);
//            System.out.println("Output");
            /*      for(int i = 0 ; i<arr.length; i++)
                System.out.println(arr[i]);*/
        }

//      creating the hidden layer matrix with random weights
        hidden_layer = new Matrix();
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

        //       print_matrix(hidden_layer);
    }

    static void feedforward(int i) {
        result[i] = new Matrix();
        result[i] = matrix_multiplication(hidden_layer, input_layer[i]);
        result[i] = activation_function(result[i]);
        hidden_val[i] = result[i];
        //  System.out.println("--------------------------");
        // print_matrix(result[i]);
        // System.out.println("--------------------------");
        // hidden_layer.setLabel(result[i]);
        result[i] = matrix_multiplication(output_layer[0], result[i]);
        result[i] = activation_function(result[i]);
        //    System.out.println("--------------------------");
        //   print_matrix(result[i]);
        //   System.out.println("--------------------------");
    }

    static void print_inputs() {
        System.out.println("input " + input_layer_nodes);
        System.out.println("hidden " + hidden_layer_nodes);
        System.out.println("input " + output_layer_nodes);
    }

    static void print_layers() {
        for (int i = 0; i < input_layer.length; i++) {
            System.out.println("Input layer: " + i);
            print_matrix(input_layer[i]);
        }
        System.out.println("Hidden layer: ");
        print_matrix(hidden_layer);
        System.out.println("Output layer:\n\tWeights:");
        print_matrix(output_layer[0]);
        System.out.print("Output labels:");
        for (int i = 0; i < output_layer_nodes; i++) {
            for (int j = 0; j < output_layer[i].getLabel().length; j++) {
                System.out.print(output_layer[i].getLabel()[j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        float mse = 0;
//      Handles all the reading from file
        initialization();
        Matrix input_layer1[] = new Matrix[1];
        Matrix output_layer1[] = new Matrix[1];
        Matrix hidden_layer1 = new Matrix();
        hidden_val = new Matrix[input_number];
        result = new Matrix[input_number];
        error = new double[input_number][output_layer_nodes];
        for (int i = 0; i < 500; i++) {
//      //Printing input values read from file
            //print_inputs();
//      //Printing each layer values
            //print_layers();

//      Handles feedforward calculations
            for (int j = 0; j < input_number; j++) {
                feedforward(j);
                get_error(j);

                backprobagation(j);
            }
            hidden_layer1 = hidden_layer;
            output_layer1 = output_layer;
            mse = mean_error();
            if (mse < 0.01) {
                break;
            }
        }

        /*
        Printing weights STEP HERE
        
         */
        BufferedWriter writer = new BufferedWriter(new FileWriter("wights.txt"));
        write_to_folder(hidden_layer1,"hidden" , writer);
        write_to_folder(output_layer1[0],"output",writer);
        writer.close();
        
        initialization();
        hidden_layer = hidden_layer1;
        output_layer = output_layer1;
        System.out.println("Mean Squre Error Of Testing: " + mse);
        for (int j = 0; j < input_number; j++) {
            feedforward(j);
            get_error(j);
        }
        mse = mean_error();
        System.out.println("Mean Squre Error: " + mse);

    }

    static boolean done = false;

    static void get_error(int index) {
        for (int i = 0; i < output_layer_nodes; i++) {
            error[index][i] = sigmoid(output_layer[index].getLabel()[i]) - result[0].getMatrix_2D().get(i).get(0);

        }

    }

    static float mean_error() {
        float mean_err = 0;
        for (int index = 0; index < input_number; index++) {
            for (int i = 0; i < output_layer_nodes; i++) {
                mean_err += error[index][i];
            }
        }
        return mean_err / 2;
    }

    static void backprobagation(int i) {
        Double learning_rate = 0.2;
        Matrix outer = new Matrix();
        Matrix hidden = new Matrix();
        ArrayList<Double> output_node = new ArrayList();
        ArrayList<Double> hidden_node = new ArrayList();
        ArrayList<ArrayList<Double>> hidden_matrix = new ArrayList();
        Double[] error_O = new Double[output_layer_nodes];
        Double[] error_h = new Double[hidden_layer_nodes];
        for (int j = 0; j < output_layer_nodes; j++) {
            error_O[j] = -(error[i][j] * (result[i].getMatrix_2D().get(j).get(0) * (1 - result[i].getMatrix_2D().get(j).get(0))));
        }
        for (int j = 0; j < output_layer_nodes; j++) {
            output_node = new ArrayList();
            for (int k = 0; k < hidden_layer_nodes; k++) {
                output_node.add(output_layer[0].getMatrix_2D().get(j).get(k) - (learning_rate * error_O[j] * hidden_val[i].getMatrix_2D().get(k).get(0)));

            }
            hidden_matrix.add(output_node);
        }
        outer.setMatrix_2D(hidden_matrix);
        for (int j = 0; j < hidden_layer_nodes; j++) {
            error_h[j] = 0.;
            for (int k = 0; k < output_layer_nodes; k++) {
                error_h[j] += error_O[k] * output_layer[0].getMatrix_2D().get(k).get(j);
            }
            error_h[j] *= (hidden_val[i].getMatrix_2D().get(j).get(0) * (1 - hidden_val[i].getMatrix_2D().get(j).get(0)));
        }
        hidden_matrix = new ArrayList();
        for (int j = 0; j < hidden_layer_nodes; j++) {
            hidden_node = new ArrayList();
            for (int k = 0; k < input_layer_nodes; k++) {
                hidden_node.add(hidden_layer.getMatrix_2D().get(j).get(k) - (learning_rate * error_h[j] * input_layer[i].getMatrix_2D().get(k).get(0)));

            }
            hidden_matrix.add(hidden_node);
        }
        hidden.setMatrix_2D(hidden_matrix);
        output_layer[0].setMatrix_2D(outer.getMatrix_2D());
        hidden_layer = hidden;
    }

    public static Matrix matrix_multiplication(Matrix matrix1, Matrix matrix2) {
        Matrix result_matrix = new Matrix();

//      System.out.println("Matrix 1 : ");
//      print_matrix(matrix1);
//      System.out.println("\n Matrix 2 : ");
//      print_matrix(matrix2);
        int matrix1_col, matrix1_rows, matrix2_rows, matrix2_col;
        double val = 0;
        matrix1_rows = matrix1.getMatrix_2D().size();
        matrix1_col = matrix1.getMatrix_2D().get(0).size();
        matrix2_rows = matrix2.getMatrix_2D().size();
        matrix2_col = matrix2.getMatrix_2D().get(0).size();

        //check if there is something wrong with matrix dimensions
        //System.out.println("\n"+matrix1_rows + " x " + matrix1_col + "  x  " + matrix2_rows + " x " + matrix2_col);
        if (matrix1_col != matrix2_rows) {
            System.out.println("matrix multiplication error !!");
            System.exit(0);
        }

//      initialize result matrix with 0
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

    public static Matrix activation_function(Matrix matrix) {
//      applying segmoid activation function for the matrix
        int rows = matrix.getMatrix_2D().size();
        int columns = matrix.getMatrix_2D().get(0).size();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double before_activation = matrix.getMatrix_2D().get(i).get(j);

                matrix.getMatrix_2D().get(i).set(j, sigmoid(before_activation));
            }
        }
        return matrix;
    }

    public static double sigmoid(double number) {
        number = 1 / (1 + Math.exp(-number));
        return number;
    }

    public static void print_matrix(Matrix matrix )  {
       
      
            
        for (int i = 0; i < matrix.getMatrix_2D().size(); i++) {
            System.out.println(matrix.getMatrix_2D().get(i));
        }
    }

    public static void write_to_folder (Matrix matrix , String name , BufferedWriter writer ) throws IOException {
       
        
        try {
            writer.write("weights of layer :  " + name );
            writer.newLine();
            writer.newLine();
        for (int i = 0; i < matrix.getMatrix_2D().size(); i++) {
            writer.write(String.valueOf(matrix.getMatrix_2D().get(i)));
            writer.newLine();
            writer.newLine();
        }
        
        writer.newLine();
        writer.newLine();
        writer.newLine();
        writer.newLine();
        
        } 
        catch (IOException ex) {
            Logger.getLogger(Neural_network.class.getName()).log(Level.SEVERE, null, ex);
        }
     
               }

    //function that returns a random number between 2 limits 
    public static double randFloat(float min, float max) {

        Random rand = new Random();
        double result = rand.nextFloat() * (max - min) + min;
        return result;

    }

}
