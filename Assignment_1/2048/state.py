import copy
import random
from utils import *
from typing import List

A = 3.5
B = 1.9

EMPTY_WEIGHT_MATRIX = [[B**0, B**1, B**2, B**3],
                       [B**7, B**6, B**5, B**4],
                       [B**8, B**9, B**10, B**11],
                       [B**15, B**14, B**13, B**12]]

WEIGHT_MATRIX = [[A**15, A**14, A**13, A**12],
                 [A**8, A**9, A**10, A**11],
                 [A**7, A**6, A**5, A**4],
                 [A**0, A**1, A**2, A**3]]

USE_SNAKE = True
USE_EMPTY_CELL = False

class State:
    # children = []
    def __init__(self, grid):
        self.grid = grid
        self.util_val = None

class New_idx:
    def __init__(self, i, j, cell_value, probability):
        self.i = i
        self.j = j
        self.cell_value = cell_value
        self.probability = probability

# return a list of Action
def actions(s: State) -> List[Action]:
    possible_actions = []
    for action in Action:
        if can_move(s.grid, action):
            possible_actions.append(action)
    return possible_actions

def result(s: State, a: Action) -> State:
    return State(move_direction(grid=s.grid.copy(), direction=a))

def terminal_test(s: State) -> bool:
    # return True if the state is terminal
    for i in range(4):
        for j in range(4):
            if s.grid[i][j] == 2048:
                return True
    return False

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
    for i in range(4):
        for j in range(4):
            utility += utility_by_idx(i, j, s.grid[i][j], USE_SNAKE, USE_EMPTY_CELL)
    #utility += empty_cells_heuristic(s)
    #utility *= smoothness_heuristic(s)
    return utility

def expectimax(s: State, depth: int, isMaxPlayer: bool) -> float:
    # return the best action

    # if the depth is 0 or the state is terminal, return the utility of the state
    if depth == 0:
        if(s.util_val != None):
            return s.util_val
        return utility(s)

    # if the player is the maximizing player
    if isMaxPlayer:
        value = -float('inf')
        possible_actions = actions(s)
        for a in possible_actions:
            res = result(s, a)
            # s.children.append(res)
            value = max(value, expectimax(res, depth - 1, False))
        return value

    # if calculate eval based on probability
    else:
        value = 0
        new_idx_list = new_idx(s)
        for a in new_idx_list:
            old_cell_value = s.grid[a.i][a.j]
            s.grid[a.i][a.j] = a.cell_value
            s.util_val += (utility_by_idx(a.i, a.j, a.cell_value, USE_SNAKE, USE_EMPTY_CELL))
            temp_value = expectimax(s, depth - 1, True)
            value += a.probability * temp_value
            s.grid[a.i][a.j] = old_cell_value
        return value / len(new_idx_list)

def utility_by_idx(i,j, cell_value, snake: bool, empty_cells: bool) -> float:
    utility = 0
    if empty_cells & cell_value == 0:
        utility += EMPTY_WEIGHT_MATRIX[i][j]
    if snake:
        utility += WEIGHT_MATRIX[i][j] * cell_value
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
                current_state_util += utility_by_idx(i, j, s.grid[i][j], USE_SNAKE, USE_EMPTY_CELL)
    s.util_val = current_state_util
    return new_idx_list

def best_action(s: State, depth: int) -> Action:
    best_action = Action.UP
    best_value = -float('inf')
    possible_actions = actions(s)
    actions_str = ""
    for a in possible_actions:
        actions_str += str(a) + ""
        s_copy = copy.deepcopy(s)
        res = result(s_copy, a)
        # s.children.append(res)
        value = expectimax(res, depth, False)
        actions_str += " with value: " + str(value) + " "
        if value > best_value:
            best_value = value
            best_action = a
        # if value == best_value:
        #     if random.randint(0, 1) == 0:
        #         best_action = a

    # print("Actions: ", actions_str)
    # print("Best actions: ", best_action)
    return best_action

def random_action() -> Action:
    return Action(random.randint(0, 3))

