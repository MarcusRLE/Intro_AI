import copy
from enum import Enum
import math
import random
import utils as ut
from utils import Action
from typing import List

a = 3.5
b = 1.9
smoothness_weight = 1
total_smooth_value = 0
smooth_called = 0

empty_weight_matrix = [[b**0, b**1, b**2, b**3],
                       [b**7, b**6, b**5, b**4],
                       [b**8, b**9, b**10, b**11],
                       [b**15, b**14, b**13, b**12]]

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

def set_a(a_value):
    global a
    a = a_value

def set_b(b_value):
    global b
    b = b_value

def set_smoothness_weight(smoothness_weight_value):
    global smoothness_weight
    smoothness_weight = smoothness_weight_value

def average_smooth_value():
    global total_smooth_value
    global smooth_called
    if smooth_called == 0:
        return 0
    return total_smooth_value / smooth_called

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

def snake_heuristic(s: State) -> float:
    global weight_matrix
    utility = 0
    for i in range(4):
        for j in range(4):
            utility += weight_matrix[i][j] * s.grid[i][j]
    return utility

def empty_cells_heuristic(s: State) -> float:
    global empty_weight_matrix
    utility = 0
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 0:
                utility += empty_weight_matrix[i][j]
    return utility

def sigmoid(x):
    return 1 / (1 + math.exp(-x))

def smoothness_heuristic(s: State) -> float:
    smoothness = 0
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] != 0:
                if i > 0 and s.grid[i - 1][j] != 0:
                    smoothness -= abs(s.grid[i][j] - s.grid[i - 1][j])
                if i < 3 and s.grid[i + 1][j] != 0:
                    smoothness -= abs(s.grid[i][j] - s.grid[i + 1][j])
                if j > 0 and s.grid[i][j - 1] != 0:
                    smoothness -= abs(s.grid[i][j] - s.grid[i][j - 1])
                if j < 3 and s.grid[i][j + 1] != 0:
                    smoothness -= abs(s.grid[i][j] - s.grid[i][j + 1])
    utility = smoothness_weight ** smoothness
    global total_smooth_value
    global smooth_called
    total_smooth_value += utility
    smooth_called += 1
    return utility

def utility(s: State) -> float:
    utility = 0
    #utility += snake_heuristic(s)
    utility += empty_cells_heuristic(s)
    #utility *= smoothness_heuristic(s)
    return utility

def expectimax(s: State, depth: int, isMaxPlayer: bool) -> float:
    # return the best action
    
    # if the depth is 0 or the state is terminal, return the utility of the state
    if depth == 0:
        return utility(s)
    
    # if the player is the maximizing player
    if isMaxPlayer:
        value = -float('inf')
        possible_actions = actions(s)
        for a in possible_actions:
            value = max(value, expectimax(result(s, a), depth - 1, False))
        return value
    # if calculate eval based on probability
    else:
        value = 0
        possibilities = chance_actions(s)
        for a in possibilities:
            value += a.probability * expectimax(a.state, depth - 1, True)
        return value / len(possibilities)
    
def best_action(s: State, depth: int) -> Action:
    best_action = None
    best_value = -float('inf')
    possible_actions = actions(s)
    actions_str = ""
    for a in possible_actions:
        actions_str += str(a) + ""
        s_copy = copy.deepcopy(s)
        value = expectimax(result(s_copy, a), depth, False)
        actions_str += " with value: " + str(value) + " "
        if value > best_value:
            best_value = value
            best_action = a
        if value == best_value:
            if random.randint(0, 1) == 0:
                best_action = a
    # print("Actions: ", actions_str)
    # print("Best actions: ", best_action)
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