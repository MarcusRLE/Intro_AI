import copy
from enum import Enum
import math
import random
import utils as ut
from utils import Action
from typing import List
import time

a = 3.5
b = 1.9
smoothness_weight = 1
total_smooth_value = 0
smooth_called = 0

use_snake = True
use_empty_cell = False

empty_weight_matrix = [[b**0, b**1, b**2, b**3],
                       [b**7, b**6, b**5, b**4],
                       [b**8, b**9, b**10, b**11],
                       [b**15, b**14, b**13, b**12]]

weight_matrix = [[a**15, a**14, a**13, a**12],
                 [a**8, a**9, a**10, a**11],
                 [a**7, a**6, a**5, a**4],
                 [a**0, a**1, a**2, a**3]]

class State:
    util_val = None
    has_util_val = False

    def __init__(self, grid):
        self.grid = grid
    
    def set_util_val(self, val):
        self.util_val = val
        self.has_util_val = True

    def add_util_val(self, val):
        self.util_val += val

    def get_util_val(self):
        return self.util_val
    
    def has_utility(self):
        return self.has_util_val
    

    


class Probability_State:
    def __init__(self, state, probability, added_utility):
        self.state = state
        self.probability = probability
        self.added_utility = added_utility

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
        grid_copy = [row[:] for row in s.grid]
        if ut.can_move(s.grid, action):
            possible_actions.append(action)
    return possible_actions

def result(s: State, a: Action) -> State:
    new_state_grid = [row[:] for row in s.grid]
    return State(ut.move_direction(grid=new_state_grid, direction=a))
    
def chance_actions(s: State) -> List[Probability_State]:
    possible_states = []
    current_state_util = 0
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 0:
                # Create a shallow copy of the grid for efficiency
                new_grid_2 = [row[:] for row in s.grid]
                new_grid_4 = [row[:] for row in s.grid]
                
                new_grid_2[i][j] = 2
                new_grid_4[i][j] = 4

                possible_states.append(Probability_State(State(new_grid_2), 0.9, utility_by_idx(i, j, 2)))
                possible_states.append(Probability_State(State(new_grid_4), 0.1, utility_by_idx(i, j, 4)))
            else:
                current_state_util += utility_by_idx(i, j, s.grid[i][j])
    s.set_util_val(current_state_util)
    return possible_states

def utility_by_idx(i,j, cell_value) -> float:
    return weight_matrix[i][j] * cell_value

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
    utility += snake_heuristic(s)
    #utility += empty_cells_heuristic(s)
    #utility *= smoothness_heuristic(s)
    return utility

def expectimax(s: State, depth: int, isMaxPlayer: bool) -> float:
    # return the best action
    
    # if the depth is 0 or the state is terminal, return the utility of the state
    if depth == 0:
        if(s.has_utility()):
            return s.get_util_val()
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
        new_idx_list = new_idx(s)
        for a in new_idx_list:
            i = a.i
            j = a.j
            old_cell_value = s.grid[i][j]
            s.grid[i][j] = a.cell_value
            s.add_util_val(utility_by_idx(i, j, a.cell_value, use_snake, use_empty_cell))
            temp_value = expectimax(s, depth - 1, True)
            value += a.probability * temp_value
            s.grid[i][j] = old_cell_value
        return value / len(new_idx_list)

def utility_by_idx(i,j, cell_value, snake: bool, empty_cells: bool) -> float:
    utility = 0
    if empty_cells & cell_value == 0:
        utility += empty_weight_matrix[i][j]
    if snake:
        utility += weight_matrix[i][j] * cell_value
    return utility

def new_idx(s: State) -> list[New_idx]:
    new_idx_list = []
    current_state_util = 0
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 0:
                new_idx_list.append(New_idx(i, j, 2, 0.9))
                new_idx_list.append(New_idx(i, j, 4, 0.1))
            else:
                current_state_util += utility_by_idx(i, j, s.grid[i][j], use_snake, use_empty_cell)
    s.set_util_val(current_state_util)
    return new_idx_list

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