/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:17:46 PM
*
* Project: csci205_hw
* Package: hw01
* File: InputLayer
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
public class InputLayer extends Layer {

    public InputLayer(ArrayList<Neuron> neurons) {
        super(neurons);
    }

    /**
     * adds the values in combination to the neuron's values
     *
     * @param combination
     */
    public void inputData(ArrayList<Integer> combination) { //[0, 0]
        ArrayList<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < combination.size(); i++) {
            this.getNeurons().get(i).setValue(combination.get(i));
            neurons.add(new Neuron(combination.get(i)));

        }

    }

}
