import copy
from enum import Enum
import random
import utils as g
from utils import Action
from typing import List

weight_matrix = [
    [4**15, 4**11, 4**4, 4**3],
    [4**14, 4**10, 4**5, 4**2],
    [4**13, 4**9,  4**6, 4**1],
    [4**12, 4**8,  4**7, 4**0]
]


#weight_matrix = [[4**15, 4**14, 4**13, 4**12],
#                 [4**11, 4**10, 4**9, 4**8],
#                 [4**4, 4**5, 4**6, 4**7],
#                 [4**3, 4**2, 4**1, 4**0]]

class State:
    def __init__(self, grid):
        self.grid = grid

class Probability_State:
    def __init__(self, state, probability):
        self.state = state
        self.probability = probability

# return a list of Action
def actions(s: State) -> List[Action]:
    possible_actions = []
    for action in Action:
        grid_copy = copy.deepcopy(s.grid)
        if g.can_move(grid_copy, action):
            possible_actions.append(action)
    return possible_actions

def result(s: State, a: Action) -> State:
    new_state = copy.deepcopy(s)
    return State(g.move_direction(grid=new_state.grid, direction=a))
    
def chance_actions(s: State) -> List[Probability_State]:
    possible_states = []
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 0:
                grid_with_2 = copy.deepcopy(s.grid)
                grid_with_4 = copy.deepcopy(s.grid)
                grid_with_2[i][j] = 2
                grid_with_4[i][j] = 4
                possible_states.append(Probability_State(State(grid_with_2), 0.9))
                possible_states.append(Probability_State(State(grid_with_4), 0.1))
    return possible_states

def terminal_test(s: State) -> bool:
    # return True if the state is terminal
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 2048:
                return True
    return False

def utility(s: State) -> float:
    # return the value of the state
    numbers_as_list = []
    for x in range(4):
        if x % 2 == 0:
            for y in range(4):
                numbers_as_list.append(s.grid[x][y])
        else:
            for y in range(3, -1, -1):
                numbers_as_list.append(s.grid[x][y])
    
    # calculate how ordered the list is (descending)
    utility = 0
    length = len(numbers_as_list)
    for i in range(length):
        if numbers_as_list[i] > numbers_as_list[i - 1]:
            utility -= i - length
    
    return utility

def utility2(s: State) -> float:
    global weight_matrix
    utility = 0
    for i in range(4):
        for j in range(4):
            utility += weight_matrix[i][j] * s.grid[i][j]
    return utility

def expectimax(s: State, depth: int, isMaxPlayer: bool) -> float:
    # return the best action
    
    # if the depth is 0 or the state is terminal, return the utility of the state
    if depth == 0 or terminal_test(s):
        return utility2(s)
    
    # if the player is the maximizing player
    if isMaxPlayer:
        value = -float('inf')
        for a in actions(s):
            value = max(value, expectimax(result(s, a), depth - 1, False))
        return value
    # if calculate eval based on probability
    else:
        value = 0
        for a in chance_actions(s):
            value += a.probability * expectimax(a.state, depth - 1, True)
        return value / len(actions(s))
    
def best_action(s: State) -> Action:
    #print("\n----------------------------------\n")
    #print("Current grid:")
    g.print_grid(s.grid)
    best_action = Action.UP
    best_value = -float('inf')
    actions_list = actions(s)
    #actions_str = "Possbilie actions: \n"
    #for a in actions_list:
    #    actions_str += a.name + "  -  "
    #print("\nPossible actions: ", actions_str)
    for a in actions(s):
        #print("\nAction: ", a)
        #print("Resulting grid:")
        g.print_grid(result(s, a).grid)
        s_copy = copy.deepcopy(s)
        value = expectimax(result(s_copy, a), 4, False)
        #print("Value: ", value)
        if value > best_value:
            best_value = value
            best_action = a
    return best_action

def random_action() -> Action:
    random_nr = random.randint(0, 3)
    if random_nr == 0:
        return Action.UP
    elif random_nr == 1:
        return Action.DOWN
    elif random_nr == 2:
        return Action.LEFT
    else:
        return Action.RIGHT