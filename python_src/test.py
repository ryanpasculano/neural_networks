import random as rand_obj

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
    in_to_hidden_weights = [[0 for x in range(num_in * num_hid)]]
    hidden_to_hidden_weights = [[0 for x in range(num_hid ** 2)]for y in range(num_hid_layers - 1)]
    hidden_to_out_weights = [[0 for x in range(num_hid * num_out)]]
    weight_deltas = in_to_hidden_weights + hidden_to_hidden_weights + hidden_to_out_weights
    # activations
    activations = [[0 for x in range(num_in)]]+ [[0 for x in range(num_hid)] for y in range(num_hid_layers)] + [[0 for x in range(num_out)]]
    # errors
    errors = [[0 for x in range(num_out)]]
    # thetas (randomized)
    thetas = [[rand_obj.choice([-0.5, 0.5]) for x in range(num_hid)] for y in range(num_hid_layers)] + [[rand_obj.choice([-0.5, 0.5]) for x in range(num_out)]]
    # delta_thetas
    theta_deltas = [[0 for x in range(num_hid)] for y in range(num_hid_layers)] + [[0 for x in range(num_out)]]
    
    structure = (weights, weight_deltas, activations, errors, thetas, theta_deltas)
    print(structure)
create_neural_structure(4, 3, 2, 1, rand_obj)