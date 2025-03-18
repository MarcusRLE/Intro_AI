import copy
from enum import Enum
import random
import utils as ut
from utils import Action
from typing import List

a = 4

#weight_matrix = [
#    [a**15, a**8, a**7, a**0],
#    [a**14, a**9, a**6, a**1],
#    [a**13, a**10,  a**5, a**2],
#    [a**12, a**11,  a**4, a**3]
#]


weight_matrix = [[a**15, a**14, a**13, a**12],
                 [a**8, a**9, a**10, a**11],
                 [a**7, a**6, a**5, a**4],
                 [a**0, a**1, a**2, a**3]]

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
        if ut.can_move(grid_copy, action):
            possible_actions.append(action)
    return possible_actions

def result(s: State, a: Action) -> State:
    new_state = copy.deepcopy(s)
    return State(ut.move_direction(grid=new_state.grid, direction=a))
    
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
    if depth == 0:
        return utility2(s)
    
    # if the player is the maximizing player
    if isMaxPlayer:
        value = -float('inf')
        possible_actions = actions(s)
        for a in possible_actions:
            value = max(value, expectimax(result(s, a), depth - 1, False))
        # print("Depth to go:", depth, "  -  Call from Max Player with value: ", value)
        return value
    # if calculate eval based on probability
    else:
        value = 0
        possibilities = chance_actions(s)
        for a in possibilities:
            value += a.probability * expectimax(a.state, depth - 1, True)
        # print("Depth to go:", depth, "  -  Call from probability with value: ", value)
        return value / len(possibilities)
    
def best_action(s: State) -> Action:
    # print("\n----------------------------------\n")
    # print("Current grid:")
    # ut.print_grid(s.grid)
    best_action = Action.UP
    best_value = -float('inf')
    actions_list = actions(s)
    # actions_str = "Possbilie actions: \n"
    # for a in actions_list:
    #    actions_str += a.name + "  -  "
    # print("\nPossible actions: ", actions_str)
    for a in actions(s):
        # print("\nAction: ", a)
        # print("Resulting grid:")
        # ut.print_grid(result(s, a).grid)
        s_copy = copy.deepcopy(s)
        act_value = utility2(result(s_copy, a))
        value = expectimax(result(s_copy, a), 4, False)
        # print("Actual Value: ", act_value)
        # print("Expectimax Value: ", value)
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