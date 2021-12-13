/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:14 PM
*
* Project: csci205_hw
* Package: hw01
* File: ANN
* Description:
*
* ****************************************
 */
package hw01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ces039
 */
public class ANN {

    private static ArrayList<Layer> layers = new ArrayList();
    private static final double learningConstant = 0.03;

    /**
     * computes the values in the output layer of the ann based on the input
     * values combination
     *
     * @param combination input values
     * @return values of output layer's neurons
     */
    private static ArrayList<Double> compute(ArrayList<Integer> combination) {
        InputLayer input = (InputLayer) layers.get(0);
        input.inputData(combination);
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i + 1).receive(layers.get(i).send());
        }
        return layers.get(layers.size() - 2).send();
    }

    /**
     * takes in a arraylist of 1's and 0's representing a binary number and adds
     * one using binary addition
     *
     * @param combination arraylist of ones and zeros
     */
    private static void increment(ArrayList<Integer> combination) {
        combination.set(0, combination.get(0) + 1);
        for (int i = 0; i < combination.size(); i++) {
            if (combination.get(i) > 1) {
                combination.set(i, 0);
                if (i + 1 != combination.size()) {
                    combination.set(i + 1, combination.get(i + 1) + 1);
                }
            }
        }
    }

    /**
     * computes the outputs for all possible inputs of the loaded ANN and
     * outputs them on the screen and into a text file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String test() throws FileNotFoundException, IOException {

        int numInputs = layers.get(0).getNeurons().size();
        File outputFile = new File("output_test.txt");
        FileWriter writer = new FileWriter(outputFile);
        int combinations = (int) Math.pow(2, numInputs);
        ArrayList<Integer> combination = new ArrayList<>(numInputs);
        OutputLayer output;
        ArrayList<Integer> outputNums;
        String s = "";
        for (int i = 0; i < numInputs; i++) {
            combination.add(0);
        }
        for (int j = 0; j < combinations; j++) {
            compute(combination);
            output = (OutputLayer) layers.get(layers.size() - 1);
            outputNums = output.output();
            for (int i = 0; i < combination.size(); i++) {
                s += combination.get(i) + ",";
            }
            for (int i = 0; i < outputNums.size(); i++) {
                s += outputNums.get(i);
                if (i != outputNums.size() - 1) {
                    s += ",";
                }
            }
            s += "\n";
            System.out.print(s);
            // add to output file
            writer.write(s);
            increment(combination);
        }
        writer.close();
        return s;
    }

    /**
     * trains the ANN based on the criteria in file
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String train(File file) throws FileNotFoundException, IOException {
        OutputLayer output = (OutputLayer) layers.get(layers.size() - 1);
        int numInputs = layers.get(0).getNeurons().size();
        int numOutputs = output.getNeurons().size();
        ArrayList<Double> error = new ArrayList<>();
        for (int i = 0; i < numOutputs; i++) {
            error.add(0.0);
        }
        ArrayList<Double> sse = new ArrayList<>();
        ArrayList<Integer> targetOutputs = new ArrayList<>();
        ArrayList<Integer> actualOutputs = new ArrayList<>();
        ArrayList<Integer> expectedOutputs = new ArrayList<>();
        int numTests = (int) Math.pow(2, layers.get(0).getNeurons().size());
        for (int i = 0; i < numTests; i++) {
            sse.add(0.0);
        }
        do {
            InputStream in = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(in);
            actualOutputs = new ArrayList<>();
            expectedOutputs = new ArrayList<>();
            for (int x = 0; x < numTests; x++) {
                sse.set(x, 0.0);
                for (int i = 0; i < error.size(); i++) {
                    error.set(i, 0.0);
                }
                int inputCounter = 0;
                ArrayList<Integer> inputs = new ArrayList<>();
                while (reader.ready() && inputCounter < numInputs) {
                    int charRead = reader.read();
                    if (charRead == 48) {
                        inputs.add(0);
                        inputCounter++;
                    } else if (charRead == 49) {
                        inputs.add(1);
                        inputCounter++;
                    }
                }
                int outputCounter = 0;
                targetOutputs = new ArrayList<>();
                while (reader.ready() && outputCounter < numOutputs) {
                    int charRead = reader.read();
                    if (charRead == 48) {
                        targetOutputs.add(0);
                        expectedOutputs.add(0);
                        outputCounter++;
                    } else if (charRead == 49) {
                        targetOutputs.add(1);
                        expectedOutputs.add(1);
                        outputCounter++;
                    }
                }
                ArrayList<Double> observedOutputs = compute(inputs);
                print();
                for (int j = 0; j < observedOutputs.size(); j++) {
                    error.set(j,
                              (double) (targetOutputs.get(j) - observedOutputs.get(
                                        j)));
                }
                changeWeights(error);
                sse.set(x, SSE(error));
                for (int j = 0; j < output.output().size(); j++) {
                    actualOutputs.add(output.output().get(j));
                }
            }
        } while (!actualOutputs.equals(expectedOutputs));

        return save();
    }

    /**
     * gets user input and responds accordingly
     *
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to input an ANN?");
        char response = scanner.next().charAt(0);
        if (response == 'y' || response == 'Y') {
            System.out.println(
                    "What is the name of the file you wish to import?");
            String fileName = scanner.next();
            File file = new File(fileName);
            getANN(file);
        } else {
            createANN();
        }
        System.out.println("What is the name of the file you want to import?");
        String fileName = scanner.next();
        // open file
        File file = new File(fileName);
        System.out.println("Do you want to go into training mode? (y/n)");
        response = scanner.next().charAt(0);
        if (response == 'y' || response == 'Y') {
            train(file);
        } else { // classification mode
            test();
        }

    }

    /**
     * creates random weights for a new ANN
     */
    private static void generateRandomWeights() {
        int m = layers.get(0).getNeurons().size() * layers.get(1).getNeurons().size();
        Random rand = new Random();
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer l1 = layers.get(i);
            Layer l2 = layers.get(i + 1);
            int numOutputNodes = l2.getNeurons().size();
            int numInputNodes = l1.getNeurons().size();
            for (int j = 0; j < numInputNodes; j++) {
                for (int k = 0; k < numOutputNodes; k++) {
                    l1.getNeurons().get(j).addWeight(
                            (4.8 / m) * (rand.nextDouble() - 0.5));

                }
            }
        }
    }

    /**
     * changes the weights of the edges based on the values in error
     *
     * @param errors
     */
    private static void changeWeights(ArrayList<Double> errors) {
        double error = errors.get(0);
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer layer = layers.get(i);
            for (int j = 0; j < layer.getNeurons().size(); j++) {
                for (int k = 0; k < layer.getNeurons().get(0).getWeights().size(); k++) {
                    double net = fNet(
                            layers.get(i + 1).getNeurons().get(k).getValue());
                    double weight = layer.getNeurons().get(j).getWeights().get(k);
                    weight += learningConstant * layer.getNeurons().get(j).getValue() * error * net * (1 - net);
                    layer.getNeurons().get(j).setWeight(k, weight);
                }
            }
        }
    }

    /**
     * calculates the sum of the squared errors
     *
     * @param error
     * @return
     */
    private static double SSE(ArrayList<Double> error) {

        double sse = 0.0;
        for (int i = 0; i < error.size(); i++) {
            sse += Math.pow(error.get(i), 2);
        }
        return sse / 2;
    }

    /**
     * checks that all values in values are below the threshold threshold
     *
     * @param values
     * @param threshold
     * @return true if all of the values are below threshold else otherwise
     */
    private static boolean check(ArrayList<Double> values, double threshold) {

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) > threshold) {
                return true;
            }
        }
        return false;
    }

    /**
     * prints all of the relevent information in layers for debugging purposes
     */
    private static void print() {
        for (int i = 0; i < layers.size(); i++) {
            System.out.println(layers.get(i));
        }
    }

    /**
     * reads in a configuration file and converts it to an ANN
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void getANN(File file) throws FileNotFoundException, IOException {
        // create readerScanner
        InputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in);
        int letter = ',';
        ArrayList<Integer> neuronsPerLayer = new ArrayList<>();
        while (reader.ready() && letter != '\n') {
            letter = reader.read();
            if (letter != ',' && letter != '\n') {
                neuronsPerLayer.add(letter - '0');
            }
        }
        for (int i = 0; i < neuronsPerLayer.size(); i++) {
            if (i == 0) { // input layer
                InputLayer inputLayer = new InputLayer(new ArrayList<>());
                for (int j = 0; j < neuronsPerLayer.get(i); j++) {
                    inputLayer.getNeurons().add(new Neuron());
                }
                layers.add(inputLayer);
            } else if (i == neuronsPerLayer.size() - 1) {// output layer
                OutputLayer outputLayer = new OutputLayer(new ArrayList<>());
                for (int j = 0; j < neuronsPerLayer.get(i); j++) {
                    outputLayer.getNeurons().add(new Neuron());
                }
                layers.add(outputLayer);
            } else { // hidden layer
                HiddenLayer hiddenLayer = new HiddenLayer(new ArrayList<>());
                for (int j = 0; j < neuronsPerLayer.get(i); j++) {
                    hiddenLayer.getNeurons().add(new Neuron());
                }
                layers.add(hiddenLayer);

            }
        }

        double weight = 0.0;
        // make a list of all of the weights
        String number = "";
        ArrayList<Double> weights = new ArrayList<>();
        while (reader.ready()) {
            int charRead = reader.read();
            if (charRead == '-' || charRead == '0') {
                do {
                    number += (char) charRead;
                    charRead = reader.read();

                } while (charRead != ',' && charRead != '\n');
                weights.add(Double.parseDouble(number));
                number = "";
            }
        }
        print();
        addWeights(weights);
    }

    /**
     * calculates the f net function 1/(1+e^-x)
     *
     * @param net the parameter to the function
     * @return the value of the function at net
     */
    private static double fNet(double net) {
        return 1 / (1 + Math.pow(Math.E, -net));
    }

    /**
     * makes a new ann based on the response from the user and gives it random
     * weights
     */
    private static void createANN() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many inputs does the network have?");
        int numInputs = scanner.nextInt();
        InputLayer input = new InputLayer(new ArrayList<Neuron>());
        for (int i = 0; i < numInputs; i++) {
            input.getNeurons().add(new Neuron());
        }
        System.out.println("How many items in the middle layer");
        int numHidden = scanner.nextInt();
        HiddenLayer hiddenLayer = new HiddenLayer(new ArrayList<>());
        for (int i = 0; i < numHidden; i++) {
            hiddenLayer.getNeurons().add(new Neuron());
        }

        System.out.println("How many outputs does the network have?");
        int numOutputs = scanner.nextInt();
        OutputLayer output = new OutputLayer(new ArrayList<Neuron>());
        for (int i = 0; i < numOutputs; i++) {
            output.getNeurons().add(new Neuron());
        }
        layers.add(input);
        layers.add(hiddenLayer);
        layers.add(output);
        generateRandomWeights();
    }

    /**
     * saves the ANN of a training session to a text file
     *
     * @throws IOException
     */
    private static String save() throws IOException {
        String s = "";
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            s += layer.getNeurons().size() + ",";
        }
        s = s.substring(0, s.length() - 1);
        s += "\n\n";
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer l1 = layers.get(i);
            Layer l2 = layers.get(i + 1);
            int numOutputNodes = l2.getNeurons().size();
            int numInputNodes = l1.getNeurons().size();
            for (int j = 0; j < numInputNodes; j++) {
                for (int k = 0; k < numOutputNodes; k++) {
                    s += l1.getNeurons().get(j).getWeights().get(k) + ",";
                }
                s = s.substring(0, s.length() - 1) + "\n";
            }
            s += "\n";
        }
        File output_train = new File("output_train.txt");
        FileWriter writer = new FileWriter(output_train);
        writer.write(s);
        writer.close();
        return s;
    }

    /**
     * adds the weights from the list to the ANN
     *
     * @param weights
     */
    private static void addWeights(ArrayList<Double> weights) {
        System.out.println(weights);
        print();
        int count = 0;
        for (int i = 0; i < layers.size() - 1; i++) {
            int subCount = 0;
            for (int j = 0; j < layers.get(i).getNeurons().size(); j++) {
                for (int k = 0; k < layers.get(i + 1).getNeurons().size(); k++) {
                    layers.get(i).getNeurons().get(j).addWeight(
                            weights.get(count + subCount + k));
                }
                subCount += layers.get(i + 1).getNeurons().size();
            }
            count += layers.get(i).getNeurons().size() * layers.get(i + 1).getNeurons().size();
        }
    }

    /**
     * rests the ANN
     */
    public static void clear() {
        layers = new ArrayList<>();

    }

}
