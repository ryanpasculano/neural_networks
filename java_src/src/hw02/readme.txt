NAMES
Ryan Pasculano and Corrine Smith

PRIMARY RESOURCES
Our primary resource for this project was the "hw01 - Artificial Neural Net.docx" file that was given to us.

CONFIGURATIION FILES
TESTING
The configuration of the testing file is the input and output on one line seperated by commas.  The program assumes that you supply a test for every scenario.  If there is one imput it assumes there are only two tests in the file to compare against.  If there are two inputs then it assumes there are 4 tests to compare against.  If there are three inputs then it assumes there are 8 tests to compare against.  

Configuration of an AND gate testing file:
0,0,0
0,1,0
1,0,0
1,1,1

IMPORTING ANN
The configuration of the file to load an ANN has then number of neurons of each layer listed sperated by commas on the first line.  After that there are n-1 matricies denoting the weights of each edge between each layer moving from left to right.  Each layer of a matrix represents the weights of the coresponding neuron starting from the top most neuron and decending.  The weights in each row of the matricies corespond to the weights of each edge leading from the neuron to each neuron in the next layer starting from the top and decending.

Abstract configuaration file with a nodes in the input layer, b nodes in the hidden layer, and c nodes in the output layer:
a,b,c

a x b matrix of values

b x c matrix of values



