/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:26 PM
*
* Project: csci205_hw
* Package: hw02
* File: Layer
* Description: Creates a class that represents a layer in an ANN.
*
* ****************************************
 */
package hw02;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that creates an object that represents a layer in an ANN.
 *
 * @authors ces039 & rep015
 */
public class Layer implements Serializable {
    /**
     * list that holds all neurons within the layer
     */
    private ArrayList<Neuron> neurons;

    public Layer(ArrayList<Neuron> neurons) {
        this.neurons = neurons;
    }

    public Layer() {
        this.neurons = new ArrayList<>();
    }

    public ArrayList<Neuron> getNeurons() {
        return this.neurons;
    }

    public void setNeurons(int index, Neuron neuron) {
        this.neurons.set(index, neuron);
    }

    public void setNuerons(ArrayList<Neuron> neurons) {
        this.neurons = neurons;

    }

    /**
     * Computes net for each neuron in a given layer and returns them.
     *
     * @return list of net values
     */
    public ArrayList<Double> send() {
        // compute the weight times the value
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < this.neurons.get(0).getWeights().size(); i++) {
            double net = 0.0;
            for (int j = 0; j < this.getNeurons().size(); j++) {
                net += this.getNeurons().get(j).getValue() * this.getNeurons().get(
                        j).getWeights().get(i);
            }
            values.add(net);
        }
        if (this instanceof OutputLayer) {
            for (int i = 0; i < this.neurons.size(); i++) {
                values.add(this.neurons.get(i).getValue());
            }
        }
        return values;
    }

    /**
     * takes in net values and calls the activation function on them and sets
     * the result to the value.
     *
     * @param values - list of net values
     * @param strategy - activation function to implement
     */
    public void receive(ArrayList<Double> values, ActivationFunction strategy) {
        for (int i = 0; i < this.neurons.size(); i++) {
            values.set(i, values.get(i) - this.neurons.get(i).getBias());

        }
        //values = strategy.activationFuction(value);
        for (int i = 0; i < this.neurons.size(); i++) {
            // value = strategy.activationFunction(values.get(i));
            values.set(i, strategy.activationFunction((double) values.get(i)));
            this.neurons.get(i).setValue(values.get(i));

        }

    }

    @Override
    public String toString() {
        String s = "";
        if (this instanceof InputLayer) {
            s += "\tInputLayer:\n";
        } else if (this instanceof HiddenLayer) {
            s += "\tHiddenLayer:\n";
        } else if (this instanceof OutputLayer) {
            s += "\tOutputLayer:\n";
        }
        for (int i = 0; i < neurons.size(); i++) {
            s += "\tNeuron " + i + " " + neurons.get(i).toString() + "\n";
        }
        return s;

    }

}
