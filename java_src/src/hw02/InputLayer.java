/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:46 PM
*
* Project: csci205_hw
* Package: hw02
* File: InputLayer
* Description: Class that creates a representation of an input layer for an ANN.
*
* ****************************************
 */
package hw02;

import java.util.ArrayList;

/**
 * Creates an object that represents an input layer for an ANN.
 *
 * @author ces039 & rep015
 */
public class InputLayer extends Layer {

    public InputLayer(ArrayList<Neuron> neurons) {
        super(neurons);
    }

    /**
     * adds the values in combination to the neuron's values
     *
     * @param combination
     */
    public void inputData(ArrayList<Double> combination) { //[0, 0]
        ArrayList<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < combination.size(); i++) {
            this.getNeurons().get(i).setValue(combination.get(i));
            neurons.add(new Neuron(combination.get(i)));

        }

    }

}
