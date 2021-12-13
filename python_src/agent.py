# Some potentially useful modules
# Whether or not you use these (or others) depends on your implementation!
import random as rand_obj
#import numpy
import math
import matplotlib.pyplot as plt
WEIGHTS = 0
WEIGHT_DELTAS = 1
ACTIVATIONS = 2
ERRORS = 3
THETAS = 4
THETA_DELTAS = 5

class NeuralMMAgent(object):
	'''
	Class to for Neural Net Agents
	'''

	def __init__(self, num_in_nodes, num_hid_nodes, num_hid_layers, num_out_nodes, \
	            learning_rate = 0.2, max_epoch=10000, max_sse=.01, momentum=0.2, \
	            creation_function=None, activation_function=None, random_seed=1):
	    '''
	    Arguments:s
	        num_hid_nodes -- total # of hidden nodes for each hidden layer
	            in the Neural Net
	        num_hid_layers -- total # of hidden layers for Neural Net
	        num_out_nodes -- total # of output nodes for Neural Net
	        learning_rate -- learning rate to be used when propagating error
	        creation_function -- function that will be used to create the
	            neural network given the input
	        activation_function -- list of two functions:
	            1st function will be used by network to determine activation given a weighted summed input
	            2nd function will be the derivative of the 1st function
	        random_seed -- used to seed object random attribute.
	            This ensures that we can reproduce results if wanted
	    '''
	    assert num_in_nodes > 0 and num_hid_layers > 0 and num_hid_nodes and\
	        num_out_nodes > 0, "Illegal number of input, hidden, or output layers!"
	    
	    self.activation_function = activation_function
	    self.creation_function = creation_function
	    self.num_hid_layers = num_hid_layers
	    self.num_hid_nodes = num_hid_nodes
	    self.num_out_nodes = num_out_nodes
	    self.learning_rate = learning_rate
	    self.num_in_nodes = num_in_nodes
	    self.random_seed = random_seed
	    self.max_epoch = max_epoch
	    self.momentum = momentum
	    self.max_sse = max_sse
	    self.structure = ([],[],[],[],[])
	    self.structure = self.create_neural_structure(self.num_in_nodes, self.num_hid_nodes, self.num_hid_layers, self.num_out_nodes, rand_obj)
        

	def train_net(self, input_list, output_list, max_num_epoch=100000, \
                    max_sse=0.1):
		total_err = 1000
		epoch = 0
		all_err = []
		while(total_err > max_sse and epoch < max_num_epoch):
            #Some code...#
			epoch = epoch + 1
			if epoch % 100000 ==0:
				print(epoch, max_num_epoch, all_err[-1])
			errors = []
			for index in range(len(input_list)):
				self.propogate_neural_net(input_list[index])
				self.calculate_errors(output_list[index])
				self._calculate_deltas()
				self._adjust_weights_thetas()
				errors.append(self.structure[ERRORS][0])

			total_err = 0
			total_err = self.calculate_sse(errors)
		
			all_err.append(total_err)
		print(all_err[0], all_err[-1])
        	#Show us how our error has changed
		#self.print_structure()
		plt.plot(all_err)
		plt.show()

	def propogate_neural_net(self, input_values):
		# node values 
		# set inputs in the activation part of structure
		self.structure[ACTIVATIONS][0] = input_values

		# iterate over each layer
		for layer in range(self.num_hid_layers + 1):
			# iterate over each node in the layer 
			for end in range(len(self.structure[ACTIVATIONS][layer + 1])):
				net = 0 # reset net each time

				for start in range(len(self.structure[ACTIVATIONS][layer])):
					
					w = self.structure[WEIGHTS][layer][start * (len(self.structure[ACTIVATIONS][layer + 1])) + end]
					i = self.structure[ACTIVATIONS][layer][start]
					net = net + i * w
				# calculate the activation function
				self.structure[ACTIVATIONS][layer + 1][end] = self.sigmoid_af(net + self.structure[THETAS][layer+1][end])
	 

	def calculate_errors(self, output_values):
        
		for index in range(len(output_values)):
			
			self.structure[ERRORS][0][index] = output_values[index] - self.structure[ACTIVATIONS][-1][index]
        

	def _calculate_deltas(self):
		'''Used to calculate all weight deltas for our neural net
		    Arguments:
			out_error -- output error (typically SSE), obtained using target
			    output and actual output
		'''
		#Calculate error gradient for each output node & propgate error
		#   (calculate weight deltas going backward from output_nodes)

		# negative one to access the output layer
		for index in range(len(self.structure[WEIGHT_DELTAS][-1])):
			self.structure[WEIGHT_DELTAS][-1][index] = self.structure[ERRORS][0][index] * self.sigmoid_af_deriv(self.structure[ACTIVATIONS][-1][index])
			# not sure if the negative 1 should be here but we will try taking it out later as a last ditch effort
			self.structure[THETA_DELTAS][-1][index] = self.structure[WEIGHT_DELTAS][-1][index] * self.learning_rate * -1

		for layer in range(self.num_hid_layers, 0, -1): # dont want to look at layer 0
			for start in range(len(self.structure[WEIGHT_DELTAS][layer])):
				summation = 0
				for end in range(len(self.structure[WEIGHT_DELTAS][layer + 1])):
					w = self.structure[WEIGHTS][layer][start * (len(self.structure[ACTIVATIONS][layer + 1])) + end]
					d = self.structure[WEIGHT_DELTAS][layer + 1][end] 
					
					summation = summation + (w * d)
				self.structure[WEIGHT_DELTAS][layer][start] = self.sigmoid_af_deriv(self.structure[ACTIVATIONS][layer][start]) * summation
				# not sure if the negative 1 should be here but we will try taking it out later as a last ditch effort
				self.structure[THETA_DELTAS][layer][start] = self.learning_rate * self.structure[WEIGHT_DELTAS][layer][start]
        



	def _adjust_weights_thetas(self):
		'''Used to apply deltas
		'''
		for layer in range(self.num_hid_layers + 1): #go to all hidden layers
			for start in range(len(self.structure[WEIGHT_DELTAS][layer])):
				for end in range(len(self.structure[WEIGHT_DELTAS][layer + 1])):
					activation = self.structure[ACTIVATIONS][layer][start] 
					delta = self.structure[WEIGHT_DELTAS][layer + 1][end]
					current_weight = self.structure[WEIGHTS][layer][start * (len(self.structure[WEIGHT_DELTAS][layer+1])) + end]
					current_weight = current_weight + self.learning_rate * activation * delta # + self.momentum * delta
					self.structure[WEIGHTS][layer][start * (len(self.structure[WEIGHT_DELTAS][layer + 1])) + end] = current_weight
		for layer in range(1, self.num_hid_layers+2): # got to all non input layers
			for node in range(len(self.structure[THETAS][layer])):
				self.structure[THETAS][layer][node] = self.structure[THETAS][layer][node] + self.structure[THETA_DELTAS][layer][node]

	def calculate_sse(self, errors):
		sse = 0
		for iteration in errors:
			for elt in iteration:
				sse = sse + (elt ** 2)
		return sse


	@staticmethod
	def create_neural_structure(num_in, num_hid, num_hid_layers, num_out, rand_obj):
		''' Creates the structures needed for a simple backprop neural net
		This method creates random weights [-0.5, 0.5]
		Arguments:
		    num_in -- total # of input nodes for Neural Net
		    num_hid -- total # of hidden nodes for each hidden layer
			in the Neural Net
		    num_hid_layers -- total # of hidden layers for Neural Net
		    num_out -- total # of output nodes for Neural Net
		    rand_obj -- the random object that will be used to selecting
			random weights
		Outputs:
		    Tuple w/ the following items
			1st - 2D list of initial weights
			2nd - 2D list for weight deltas
			3rd - 2D list for activations
			4th - 2D list for errors
			5th - 2D list of thetas for threshold
			6th - 2D list for thetas deltas
		'''
        
		# weights (randomized)
		in_to_hidden_weights = [[rand_obj.choice([-0.5, 0.5]) for x in range(num_in * num_hid)]]
		hidden_to_hidden_weights = [[rand_obj.choice([-0.5, 0.5]) for x in range(num_hid ** 2)]for y in range(num_hid_layers - 1)]
		hidden_to_out_weights = [[rand_obj.choice([-0.5, 0.5]) for x in range(num_hid * num_out)]]
		weights = in_to_hidden_weights + hidden_to_hidden_weights + hidden_to_out_weights
		# weights_delta
		weight_deltas = [[0 for x in range(num_in)]]+ [[0 for x in range(num_hid)] for y in range(num_hid_layers)] + [[0 for x in range(num_out)]]
		# activations
		activations = [[0 for x in range(num_in)]]+ [[0 for x in range(num_hid)] for y in range(num_hid_layers)] + [[0 for x in range(num_out)]]
		# errors
		errors = [[0 for x in range(num_out)]]
		# thetas (randomized)
		thetas = [[rand_obj.choice([-0.5, 0.5]) for x in range(num_in)]]+ [[rand_obj.choice([-0.5, 0.5]) for x in range(num_hid)] for y in range(num_hid_layers)] + [[rand_obj.choice([-0.5, 0.5]) for x in range(num_out)]]
		# delta_thetas
		theta_deltas = [[0 for x in range(num_in)]]+ [[0 for x in range(num_hid)] for y in range(num_hid_layers)] + [[0 for x in range(num_out)]]

		return [weights, weight_deltas, activations, errors, thetas, theta_deltas]




	#-----Begin ACCESSORS-----#
	def set_weights(self, weights):
		self.structure[WEIGHTS] = weights

	def set_thetas(self, thetas):
		self.structure[THETAS] = thetas

	def get_weights(self):
		return self.structure[WEIGHTS]

	def get_weight_deltas(self):
		return self.structure[WEIGHT_DELTAS]

	def get_activations(self):
		return self.structure[ACTIVATIONS]

	def get_errors(self):
		return self.structure[ERRORS]

	def get_thetas(self):
		return self.structure[THETAS]

	def get_theta_deltas(self):
		return self.structure[THETA_DELTAS]
	#-----End ACCESSORS-----#


	def print_structure(self):
		print("***************** START ***************************")
		print("WEIGHTS: " + str(self.structure[WEIGHTS]))
		print("WEIGHT_DELTAS: " + str(self.structure[WEIGHT_DELTAS]))
		print("ACTIVATIONS: " + str(self.structure[ACTIVATIONS]))
		print("ERRORS: " + str(self.structure[ERRORS]))
		print("THETAS: " + str(self.structure[THETAS]))
		print("THETA_DELTAS: " + str(self.structure[THETA_DELTAS]))
		print("****************** END ****************************")

	@staticmethod
	def sigmoid_af(summed_input):
		#Sigmoid function
		return ( 1 / ( 1 + math.e ** -summed_input ) )

	@staticmethod
	def sigmoid_af_deriv(sig_output):
		#the derivative of the sigmoid function
		return sig_output * ( 1 - sig_output)
def print_structure(structure):
	print("***************** START ***************************")
	print("WEIGHTS: " + str(structure[WEIGHTS]))
	print("WEIGHT_DELTAS: " + str(structure[WEIGHT_DELTAS]))
	print("ACTIVATIONS: " + str(structure[ACTIVATIONS]))
	print("ERRORS: " + str(structure[ERRORS]))
	print("THETAS: " + str(structure[THETAS]))
	print("THETA_DELTAS: " + str(structure[THETA_DELTAS]))
	print("****************** END ****************************")

# RUN THIS TO RUN A FULL TRAINING it takes about 3,000,000 to converge but it still does
test_agent = NeuralMMAgent(2, 2, 1, 1, random_seed=5, max_epoch=10000000, learning_rate=0.5, momentum=0)
test_in = [[1,0],[0,0],[1,1],[0,1]]
test_out = [[1],[0],[0],[1]]
test_agent.set_weights([[-.37,.26,.1,-.24],[-.01,-.05]])
test_agent.set_thetas([[0,0],[0,0],[0]])
test_agent.train_net(test_in, test_out, max_sse = test_agent.max_sse, \
	             max_num_epoch = test_agent.max_epoch)
'''
test_agent = NeuralMMAgent(2, 2, 1, 1, random_seed=5, max_epoch=10000, learning_rate=0.2, momentum=0)
test_in = [[1,0],[0,0],[1,1],[0,1]]
test_out = [[1],[0],[0],[1]]
#test_agent.set_weights([[-.37,.26,.1,-.24],[-.01,-.05]])
#test_agent.set_thetas([[0,0],[0,0],[0]])
test_agent.train_net(test_in, test_out, max_sse = test_agent.max_sse, \
		     max_num_epoch = test_agent.max_epoch)

'''

'''
# RUN THIS TO TEST JUST ONE ITERATION OF THE FIRST EPOCH
test_agent = NeuralMMAgent(2, 2, 1, 1,random_seed=5, max_epoch=1000000, \
                            learning_rate=0.2, momentum=0)
test_in = [[1,0],[0,0],[1,1],[0,1]]
test_out = [[1],[0],[0],[1]]
test_agent.set_weights([[-.37,.26,.1,-.24],[-.01,-.05]])
test_agent.set_thetas([[0,0],[0,0],[0]])
print("BEFORE")
print_structure(test_agent.structure)
test_agent.propogate_neural_net([1,0])
#print_structure(test_agent.structure)
test_agent.calculate_errors([1])
#print_structure(test_agent.structure)
test_agent._calculate_deltas()
#print_structure(test_agent.structure)
test_agent._adjust_weights_thetas()
print("AFTER")
print_structure(test_agent.structure)
'''

