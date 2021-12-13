/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 16, 2016
* Time: 3:53:21 PM
*
* Project: csci205_hw
* Package: hw02
* File: TanHFunction
* Description: Class that creates a representation of an activation function to be used in an ANN.
*
* ****************************************
 */
package hw02;

import java.io.Serializable;

/**
 * Creates an object that represents the tanh activation function.
 *
 * @author ces039
 */
public class TanHFunction implements ActivationFunction, Serializable {
    @Override
    public double activationFunction(double value) {
        value = (((2 / (1 + Math.pow(Math.E, -2 * value))) - 1) + 1) / 2;

        return value;
    }

    @Override
    public double derivative(double x) {
        x = Math.tan(2 * x - 1);
        x = (Math.pow(1 / (Math.cosh(x)), 2)) / 2;
        return x;
    }

}
