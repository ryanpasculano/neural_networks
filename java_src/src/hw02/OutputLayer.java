/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:19:54 PM
*
* Project: csci205_hw
* Package: hw02
* File: OutputLayer
* Description: Class that creates a representation of an output layer in an ANN.
*
* ****************************************
 */
package hw02;

import java.util.ArrayList;

/**
 * Creates an object that represents an output layer in an ANN.
 *
 * @author ces039
 */
public class OutputLayer extends Layer {

    public OutputLayer(ArrayList<Neuron> neurons) {
        super(neurons);
    }

    /**
     * rounds each value to 0 or 1 and returns as a list of integer
     *
     * @return
     */
    public ArrayList<Double> output() {
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < this.getNeurons().size(); i++) {
            values.add(this.getNeurons().get(i).getValue());
        }
        return values;
    }
}
