/* *****************************************
* CSCI205 - Software Engineering and Design
* Fall 2016
*
* Name: Corrine Smith and Ryan Pasculano
* Date: Oct 16, 2016
* Time: 3:18:39 PM
*
* Project: csci205_hw
* Package: hw02
* File: ActivationFunction
* Description: Creates an interface for the activation functions to be used in the ANN
*
* ****************************************
 */
package hw02;

/**
 * Interface that is implemented by the various activation functions that can be
 * used.
 *
 * @author ces039 & rep015
 */
public interface ActivationFunction {
    /**
     * This code was based on information found at
     * https://en.wikipedia.org/wiki/Strategy_pattern
     *
     * @see
     * <a href=" https://en.wikipedia.org/wiki/Strategy_pattern">
     * https://en.wikipedia.org/wiki/Strategy_pattern</a>
     * @param value - the value to call the activation function on
     * @return - the result of the activation function
     */
    public double activationFunction(double value);

    /**
     * Computes the derivative of the activation function on an input.
     *
     * @param x
     * @return the derivative of the activation function computed for a specific
     * input
     */
    public double derivative(double x);

}
