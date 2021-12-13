/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 5, 2016
* Time: 1:19:54 PM
*
* Project: csci205_hw
* Package: hw01
* File: OutputLayer
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
public class OutputLayer extends Layer {

    public OutputLayer(ArrayList<Neuron> neurons) {
        super(neurons);
    }

    /**
     * rounds each value to 0 or 1 and returns as a list of integer
     *
     * @return
     */
    public ArrayList<Integer> output() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < this.getNeurons().size(); i++) {
            values.add(this.getNeurons().get(i).activate());
        }
        return values;
    }
}
