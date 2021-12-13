/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: NAMES of team members
* Date: Oct 5, 2016
* Time: 1:17:36 PM
*
* Project: csci205_hw
* Package: hw01
* File: Neuron
* Description:
*
* ****************************************
 */
package hw01;

import java.util.ArrayList;

/**
 *
 * @author ces039
 */
public class Neuron {
    private double value;
    private ArrayList<Double> weights;

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

    /**
     * round value to 0 or 1 and returns it
     *
     * @return
     */
    public int activate() {
        double constant = 0.5;
        if (this.value - constant < 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        String s = "\tValue: " + value;
        for (int i = 0; i < weights.size(); i++) {
            s += "\n\t" + weights.get(i);
        }
        return s;
    }

}
