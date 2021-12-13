/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: NAMES of team members
* Date: Oct 5, 2016
* Time: 1:17:36 PM
*
* Project: csci205_hw
* Package: hw02
* File: Neuron
* Description: Creates class that represents a Neuron in an ANN.
*
* ****************************************
 */
package hw02;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that creates an object to represent a Neuron in an ANN.
 *
 * @authors ces039 & rep015
 */
public class Neuron implements Serializable {
    /**
     * Value of neuron
     */
    private double value;
    /**
     * list of the weights of all edges going out from the neuron
     */
    private ArrayList<Double> weights;
    /**
     * delta value stored within this neuron
     */
    private double delta = 0.0;
    /**
     * bias value that corresponds to this neuron
     */
    private double bias = 0.0;

    public Neuron(double value) {
        this.value = value;
        this.weights = new ArrayList<>();
    }

    public Neuron() {
        this.value = 0.0;
        this.weights = new ArrayList<>();
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Double> getWeights() {
        return this.weights;
    }

    /**
     * adds another value to the list weights
     *
     * @param weight
     */
    public void addWeight(double weight) {
        this.weights.add(weight);
    }

    public void setWeight(int index, double weight) {
        this.weights.set(index, weight);
    }

    public double getDelta() {
        return this.delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getBias() {
        return this.bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    /**
     * round value to 0 or 1 and returns it
     *
     * @return
     */
    public double activate() {
        double fNet = 0;
        fNet = 1 / (1 + Math.pow(Math.E, -this.value));
        return fNet;
    }

    @Override
    public String toString() {
        String s = "\tValue: " + this.value + "\n\tBias:  " + this.bias + "\n\t\tWeights:";
        for (int i = 0; i < this.weights.size(); i++) {
            s += "\n\t\t" + this.weights.get(i);
        }

        return s;
    }

}
