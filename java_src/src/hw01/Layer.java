/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:26 PM
*
* Project: csci205_hw
* Package: hw01
* File: Layer
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
public class Layer {
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

    public ArrayList<Double> send() {
        // compute the weight times the value
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < this.neurons.get(0).getWeights().size(); i++) {
            values.add(0.0);
        }
        //System.out.println(this.neurons.get(0).getWeights().size());
        for (int i = 0; i < this.neurons.size(); i++) {
            Neuron neuron = this.neurons.get(i);
            for (int j = 0; j < neuron.getWeights().size(); j++) {
                double value = neuron.getValue() * neuron.getWeights().get(j);
                values.set(j, value + values.get(j));
            }
        }

        //values = activationFunction(values);
        //System.out.println(values.toString());
        return values;
    }

    public ArrayList<Double> activationFunction(ArrayList<Double> values) {

        // computes the f(net)
        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);
            value = 1 / (1 + Math.pow(Math.E, -value));
            values.set(i, value);
        }
        return values;
    }

    public void receive(ArrayList<Double> values) {
        for (int i = 0; i < this.neurons.size(); i++) {
            this.neurons.get(i).setValue(values.get(i));
        }
    }

    @Override
    public String toString() {
        String s = "";
        if (this instanceof InputLayer) {
            s += "InputLayer:\n";
        } else if (this instanceof HiddenLayer) {
            s += "HiddenLayer:\n";
        } else if (this instanceof OutputLayer) {
            s += "OutputLayer:\n";
        }
        for (int i = 0; i < neurons.size(); i++) {
            s += "Neuron " + i + " " + neurons.get(i).toString() + "\n";
        }
        return s;

    }

}
