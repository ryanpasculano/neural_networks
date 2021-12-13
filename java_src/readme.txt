Ryan Pasculano & Corrine Smith
hw02
readme.txt

Our ANN has three different activation functions (logarithm, agressive
logarithm, and arctan) and is either read in with the ANN Serializable
file or you are prompted to enter a number that coresponds to the activation
function.  The momentum constant is currently hardcoded to 0.5 but can
be changed by editing its declaration where all of the global variables 
are defined at the top of ANN.java.  Our program can read both integer 
and double files with the exception of doubles using scientific notation
which should not be used for input and output values because that is 
ridiculous.  Files are serialized after they are trained using the serializable 
interface.  You can reload the most recently trains file whoich is saved 
under output_train.txt.  Out training will be comple when all of the 
squared errors are below the given minumum error or we have reached the
given maximum epoch count.  Each time the program is run you have the 
option to either load in a ANN from a file with the file having been saved
in the correct format (Sierializable) or generate a new one by providing
the number of inputs, neurons in the middle layer, and outputs.  They 
will then be asked for another, and then if they want to train of test 
the ANN.  After training is succesfully completed the file the program 
will print the average SSE the number of epochs taken to train it, and 
the total time taken to train.  After a test is run it will print the 
average SSE over the entire file. All training data is saved to 
ANNTrainingLog.csv.  For this project we opted to not use the JOptionPane 
because it was requested by the client.  You will find that most if not all
of the requirements for this project are filfilled.  It may seem that 
there is a lack of citing our sources but that is because we did not use 
many external resources for this project and relied on our knowledge 
from our professors and a lot of trial and error and brute force.


