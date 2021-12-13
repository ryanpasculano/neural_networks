/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 16, 2016
* Time: 3:31:51 PM
*
* Project: csci205_hw
* Package: hw02
* File: AggressiveLogisticFunction
* Description: Creates a class that represents an activation function to be used in an ANN.
*
* ****************************************
 */
package hw02;

import java.io.Serializable;

/**
 * Creates an object that represents a more aggressive version of the logistic
 * activation function.
 *
 * @author ces039 & rep015
 */
public class AggressiveLogisticFunction implements ActivationFunction,
                                                   Serializable {
    @Override
    public double activationFunction(double value) {
        value = 1 / (1 + Math.pow(Math.E, 10 * (-value)));
        return value;
    }

    @Override
    public double derivative(double x) {
        return x * (1 - x);
    }

}
