/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:14 PM
*
* Project: csci205_hw
* Package: hw02
* File: ANN
* Description: Class that creates an artificial neural network
*
* ****************************************
 */
package hw02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * Class that creates the ANN
 *
 * @authors ces039 & rep015
 */
public class ANN implements Serializable {

    private static ArrayList<Layer> layers = new ArrayList();
    private static double learningConstant = 0.3;
    private static double momentumConstant = 0.5;
    private static ActivationFunction strategy;
    private static double time;
    private static int epochs;
    private static double averageSSE;

    /**
     * gets user input and responds accordingly
     *
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
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
            System.out.println(
                    "Which activation function do you want to use?\n1. Logistic\n2. AggressiveLogistic\n3. TanH Function");
            int num = scanner.nextInt();
            if (num == 1) {
                strategy = new LogisticFunction();
            } else if (num == 2) {
                strategy = new AggressiveLogisticFunction();
            } else if (num == 3) {
                strategy = new TanHFunction();
            }
            train(file);
        } else { // classification mode
            test(file);
        }

    }

    /**
     * computes the values in the output layer of the ann based on the input
     * values combination
     *
     * @param combination input values
     * @return values of output layer's neurons
     */
    private static ArrayList<Double> compute(ArrayList<Double> combination) {
        InputLayer input = (InputLayer) layers.get(0);
        input.inputData(combination);
        for (int i = 0; i < layers.size() - 1; i++) {
            layers.get(i + 1).receive(layers.get(i).send(), ANN.strategy);
        }
        OutputLayer layer = (OutputLayer) layers.get(layers.size() - 1);
        return layer.output(); //return layers.get(layers.size() - 1).send();
    }

    /**
     * takes in a arraylist of 1's and 0's representing a binary number and adds
     * one using binary addition
     *
     * @param combination arraylist of ones and zeros
     */
    private static void increment(ArrayList<Double> combination) {
        combination.set(0, combination.get(0) + 1);
        for (int i = 0; i < combination.size(); i++) {
            if (combination.get(i) > 1) {
                combination.set(i, 0.0);
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
    public static String test(File file) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in);
        ArrayList<Double> actualOutputs = new ArrayList<>();
        ArrayList<Double> expectedOutputs = new ArrayList<>();
        ArrayList<Double> errors = new ArrayList<>();
        int numInputs = layers.get(0).getNeurons().size();
        int numOutputs = layers.get(layers.size() - 1).getNeurons().size();
        int numTrials = 0;
        do {
            numTrials++;
            int inputCounter = 0;
            ArrayList<Double> inputs = new ArrayList<>();
            String number = "";
            expectedOutputs = new ArrayList<>();
            while (reader.ready() && inputCounter < numInputs) {
                int charRead = reader.read();
                if (charRead == '-' || (charRead - '0' >= 0 && charRead - '0' <= 9)) {
                    do {
                        number += (char) charRead;
                        charRead = reader.read();

                    } while (charRead != ',' && charRead != '\n');
                    inputCounter++;
                    inputs.add(Double.parseDouble(number));
                    number = "";
                }
            }
            number = "";
            int outputCounter = 0;
            while (reader.ready() && outputCounter < numOutputs) {
                int charRead = reader.read();
                if (charRead == '-' || (charRead - '0' >= 0 && charRead - '0' <= 9)) {
                    do {
                        number += (char) charRead;
                        charRead = reader.read();

                    } while (charRead != ',' && charRead != '\n');
                    outputCounter++;
                    //targetOutputs.add(Double.parseDouble(number));
                    expectedOutputs.add(Double.parseDouble(number));
                    number = "";
                }
            }
            ArrayList<Double> observedOutputs = compute(inputs);
            for (int i = 0; i < observedOutputs.size(); i++) {
                errors.add(expectedOutputs.get(i) - observedOutputs.get(i));
            }
            System.out.println(expectedOutputs + " " + observedOutputs);
        } while (reader.ready());
        double aveSSE = SSE(errors) / numTrials;
        System.out.println(aveSSE);
        return "YAY";
    }

    /**
     * trains the ANN based on the criteria in file
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String train(File file) throws FileNotFoundException, IOException {
        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(System.in);
        int maxEpochs;
        double maxSSE;
        System.out.println("What is the max number of epochs?");
        maxEpochs = scanner.nextInt();
        System.out.println("What is the max SSE?");
        maxSSE = scanner.nextDouble();
        OutputLayer output = (OutputLayer) layers.get(layers.size() - 1);
        int numInputs = layers.get(0).getNeurons().size();
        int numOutputs = output.getNeurons().size();
        ArrayList<Double> error = new ArrayList<>();

        // Set up Training Log
        PrintWriter outFile = new PrintWriter(new File("ANNTrainingLog.csv"));
        String time = new SimpleDateFormat("HH:mm:ss,dd/MM/yyyy").format(
                new Date());
        outFile.write("ANN-Ann," + time + "\n------------");
        outFile.write("\nLearning Constant" + learningConstant);
        outFile.write("\nMomentum Constant" + momentumConstant);

        for (int i = 0; i < numOutputs; i++) {
            error.add(0.0);
        }
        ArrayList<Double> sse = new ArrayList<>();
        ArrayList<Double> previousChangeInWeights = new ArrayList();
        int numWeights = getTotalWeights();
        for (int i = 0; i < numWeights; i++) {
            previousChangeInWeights.add(0.0);
        }
        ArrayList<Double> targetOutputs = new ArrayList<>();
        ArrayList<Double> actualOutputs = new ArrayList<>();
        ArrayList<Double> expectedOutputs = new ArrayList<>();
        int numTests = getNumTests(file);
        for (int i = 0; i < numTests; i++) {
            sse.add(0.0);
        }
        int numEpoch = 0;
        do {
            numEpoch++;
            InputStream in = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(in);
            actualOutputs = new ArrayList<>();
            expectedOutputs = new ArrayList<>();
            for (int x = 0; x < numTests; x++) {
                String epochStartTime = new SimpleDateFormat("HH:mm:ss").format(
                        new Date());
                targetOutputs = new ArrayList<>();

                sse.set(x, 0.0);
                for (int i = 0; i < error.size(); i++) {
                    error.set(i, 0.0);
                }
                int inputCounter = 0;
                ArrayList<Double> inputs = new ArrayList<>();
                String number = "";
                while (reader.ready() && inputCounter < numInputs) {
                    int charRead = reader.read();
                    if (charRead == '-' || (charRead - '0' >= 0 && charRead - '0' <= 9)) {
                        do {
                            number += (char) charRead;
                            charRead = reader.read();

                        } while (charRead != ',' && charRead != '\n');
                        inputCounter++;
                        inputs.add(Double.parseDouble(number));
                        number = "";
                    }
                }
                number = "";
                int outputCounter = 0;
                while (reader.ready() && outputCounter < numOutputs) {
                    int charRead = reader.read();
                    if (charRead == '-' || (charRead - '0' >= 0 && charRead - '0' <= 9)) {
                        do {
                            number += (char) charRead;
                            charRead = reader.read();

                        } while (charRead != ',' && charRead != '\n');
                        outputCounter++;
                        targetOutputs.add(Double.parseDouble(number));
                        expectedOutputs.add(Double.parseDouble(number));
                        number = "";
                    }
                }
                ArrayList<Double> observedOutputs = compute(inputs);
                //print();
                for (int j = 0; j < observedOutputs.size(); j++) {
                    error.set(j,
                              (double) (targetOutputs.get(j) - observedOutputs.get(
                                        j)));
                }
                // put errors in output layer as deltas
                for (int i = 0; i < error.size(); i++) {
                    double net = layers.get(layers.size() - 1).getNeurons().get(
                            i).getValue();
                    layers.get(layers.size() - 1).getNeurons().get(i).setDelta(
                            strategy.derivative(net) * error.get(i));
                }

                getDeltas();
                changeWeights(previousChangeInWeights);
                changeBias();
                sse.set(x, SSE(error));
                for (int j = 0; j < output.output().size(); j++) {
                    actualOutputs.add(output.output().get(j));
                }
                //System.out.print("Actual Outputs: " + observedOutputs.toString());

                outFile.write(
                        "\n---\n" + print() + "\n---\n" + epochs + "," + epochStartTime + "\n" + inputs + "\n");
            }

        } while (check(sse, maxSSE) && numEpoch < maxEpochs);
        outFile.write(new SimpleDateFormat("HH:mm:ss,dd/MM/yyyy").format(
                new Date()));
        outFile.close();
        if (check(sse, maxSSE)) {
            System.out.println(
                    "Unable to train! try again! maybe with more epochs or smaller error");
            return train(file);
        } else {
            ANN.averageSSE = avgSEE(sse);
            long endTime = System.nanoTime();
            ANN.time = (endTime - startTime) / 1000000000.0;
            ANN.epochs = numEpoch;

            System.out.println("Average SSE: " + ANN.averageSSE);
            System.out.println("EPOCH COUNT: " + ANN.epochs);
            System.out.println("Total time to train: " + ANN.time + " s");
            //test();
            return save();
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
     * Determines the deltas for each neuron and sets them as instance variables
     * of the neurons.
     */
    private static void getDeltas() {
        for (int i = layers.size() - 2; i >= 0; i--) {
            Layer currentLayer = layers.get(i);
            Layer nextLayer = layers.get(i + 1);
            for (int j = 0; j < currentLayer.getNeurons().size(); j++) {
                double sumWD = 0.0;
                for (int k = 0; k < nextLayer.getNeurons().size(); k++) {
                    sumWD += currentLayer.getNeurons().get(j).getWeights().get(k) * nextLayer.getNeurons().get(
                            k).getDelta();
                }
                double net = currentLayer.getNeurons().get(j).getValue();
                double delta = strategy.derivative(net) * sumWD;
                currentLayer.getNeurons().get(j).setDelta(delta);
            }
        }
    }

    /**
     * changes the weights of the edges based on the values in error
     *
     * @param errors
     */
    private static void changeWeights(ArrayList<Double> previousChangeInWeights) {
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer layer = layers.get(i);
            for (int j = 0; j < layer.getNeurons().size(); j++) {
                for (int k = 0; k < layer.getNeurons().get(j).getWeights().size(); k++) {
                    double weight = layer.getNeurons().get(j).getWeights().get(k);
                    double changeInWeight = (momentumConstant * previousChangeInWeights.get(
                                             0)) + (learningConstant * layer.getNeurons().get(
                                                    j).getValue() * layers.get(
                                                    i + 1).getNeurons().get(
                                                    k).getDelta());
                    previousChangeInWeights.remove(0);
                    previousChangeInWeights.add(changeInWeight);
                    layer.getNeurons().get(j).setWeight(k,
                                                        weight + changeInWeight);

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
     * Creates a string representation of a layer to be used in the training log
     *
     * @return The string of the layer to be printed to the training log file
     */
    private static String print() {
        String s = "";
        for (int i = 0; i < layers.size(); i++) {
            s += "Layer: " + i + " " + layers.get(i).toString();
        }
        return s;
    }

    /**
     * Updates the bias values associated with each neuron.
     */
    private static void changeBias() {
        for (int i = 1; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            for (int j = 0; j < layer.getNeurons().size(); j++) {
                double bias = layer.getNeurons().get(j).getBias();
                bias += (learningConstant * -1 * layer.getNeurons().get(
                         j).getDelta());
                layer.getNeurons().get(j).setBias(bias);
            }
        }
    }

    /**
     * reads in a configuration file and converts it to an ANN
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void getANN(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        // create readerScanner
        InputStream input = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(input);
        readObject(in);

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

        FileOutputStream output = new FileOutputStream("output_train.txt");
        ObjectOutputStream out = new ObjectOutputStream(output);
        writeObject(out);
        out.close();
        output.close();
        return "boop";
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
     * resets the ANN
     */
    public static void clear() {
        layers = new ArrayList<>();

    }

    /**
     * Gets the total number of weights within the ANN.
     *
     * @return the number of weights
     */
    private static int getTotalWeights() {
        int numWeights = 0;
        for (int i = 0; i < layers.size() - 1; i++) {
            numWeights += layers.get(i).getNeurons().size() * layers.get(i + 1).getNeurons().size();
        }
        return numWeights;
    }

    /**
     * Computes the average of the SSE values for each test.
     *
     * @param sse - list of SSE values
     * @return the average of the inputted values
     */
    private static double avgSEE(ArrayList<Double> sse) {
        double sum = 0.0;
        for (int i = 0; i < sse.size(); i++) {
            sum += sse.get(i);
        }
        return sum / sse.size();
    }

    /**
     * Method used to create a Serializable object that represents the ANN.
     *
     * @param out - ObjectOutputStream that is connected to the file to be
     * written to
     * @throws IOException
     */
    public static void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(epochs);
        out.writeDouble(time);
        out.writeDouble(averageSSE);
        out.writeObject(layers);
        out.writeDouble(learningConstant);
        out.writeDouble(momentumConstant);
        out.writeObject(strategy);

    }

    /**
     * Method used to create an ANN from a Serializable object that represents
     * it.
     *
     * @param in - ObjectInputStream that contains the file to be read from
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        epochs = in.readInt();
        time = in.readDouble();
        averageSSE = in.readDouble();
        layers = (ArrayList<Layer>) in.readObject();
        learningConstant = in.readDouble();
        momentumConstant = in.readDouble();
        strategy = (ActivationFunction) in.readObject();

    }

    /**
     * Determines how many tests there are so the ANN knows how many times to go
     * through the training loop before checking for good error values.
     *
     * @param file - the file containing the training data
     * @return the number of tests to be used for training
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static int getNumTests(File file) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in);
        ArrayList<Double> allNums = new ArrayList<>();
        String number = "";
        while (reader.ready()) {
            int charRead = reader.read();
            if (charRead == '-' || (charRead - '0' >= 0 && charRead - '0' <= 9)) {
                do {
                    number += (char) charRead;
                    charRead = reader.read();

                } while (charRead != ',' && charRead != '\n');
                allNums.add(Double.parseDouble(number));
                number = "";
            }

        }
        // divisor is total in and out puts
        int divisor = layers.get(0).getNeurons().size() + layers.get(
                layers.size() - 1).getNeurons().size();
        return allNums.size() / divisor;
    }
}
