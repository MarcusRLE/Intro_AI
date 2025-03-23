import copy
import random
from utils import *
from typing import List

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

class AI:
    def __init__(self, a = 3.5, b = 1.9):
        self.EMPTY_WEIGHT_MATRIX = [[b**0, b**1, b**2, b**3],
                               [b**7, b**6, b**5, b**4],
                               [b**8, b**9, b**10, b**11],
                               [b**15, b**14, b**13, b**12]]

        self.WEIGHT_MATRIX = [[a**15, a**14, a**13, a**12],
                         [a**8, a**9, a**10, a**11],
                         [a**7, a**6, a**5, a**4],
                         [a**0, a**1, a**2, a**3]]

        self.USE_SNAKE = True
        self.USE_EMPTY_CELL = False

    # return a list of Action
    def actions(self, s: State) -> List[Action]:
        possible_actions = []
        for action in Action:
            if can_move(s.grid, action):
                possible_actions.append(action)
        return possible_actions

    def result(self, s: State, a: Action) -> State:
        return State(move_direction(grid=s.grid.copy(), direction=a))

    def terminal_test(self, s: State) -> bool:
        # return True if the state is terminal
        for i in range(4):
            for j in range(4):
                if s.grid[i][j] == 2048:
                    return True
        return False

    def smoothness_heuristic(self, s: State) -> float:
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

    def utility(self, s: State) -> float:
        utility = 0
        for i in range(4):
            for j in range(4):
                utility += self.utility_by_idx(i, j, s.grid[i][j], self.USE_SNAKE, self.USE_EMPTY_CELL)
        #utility += empty_cells_heuristic(s)
        #utility *= smoothness_heuristic(s)
        return utility

    def expectimax(self, s: State, depth: int, isMaxPlayer: bool) -> float:
        # return the best action

        # if the depth is 0 or the state is terminal, return the utility of the state
        if depth == 0:
            if(s.util_val != None):
                return s.util_val
            return self.utility(s)

        # if the player is the maximizing player
        if isMaxPlayer:
            value = -float('inf')
            possible_actions = self.actions(s)
            for a in possible_actions:
                res = self.result(s, a)
                # s.children.append(res)
                value = max(value, self.expectimax(res, depth - 1, False))
            return value

        # if calculate eval based on probability
        else:
            value = 0
            new_idx_list = self.new_idx(s)
            for a in new_idx_list:
                old_cell_value = s.grid[a.i][a.j]
                s.grid[a.i][a.j] = a.cell_value
                s.util_val += (self.utility_by_idx(a.i, a.j, a.cell_value, self.USE_SNAKE, self.USE_EMPTY_CELL))
                temp_value = self.expectimax(s, depth - 1, True)
                value += a.probability * temp_value
                s.grid[a.i][a.j] = old_cell_value
            return value / len(new_idx_list)

    def utility_by_idx(self, i, j, cell_value, snake: bool, empty_cells: bool) -> float:
        utility = 0
        if empty_cells & cell_value == 0:
            utility += self.EMPTY_WEIGHT_MATRIX[i][j]
        if snake:
            utility += self.WEIGHT_MATRIX[i][j] * cell_value
        return utility

    def new_idx(self, s: State) -> list[New_idx]:
        new_idx_list = []
        current_state_util = 0
        for i in range(4):
            for j in range(4):
                if s.grid[i][j] == 0:
                    new_idx_list.append(New_idx(i, j, 2, 0.9))
                    new_idx_list.append(New_idx(i, j, 4, 0.1))
                else:
                    current_state_util += self.utility_by_idx(i, j, s.grid[i][j], self.USE_SNAKE, self.USE_EMPTY_CELL)
        s.util_val = current_state_util
        return new_idx_list

    def best_action(self, s: State, depth: int) -> Action:
        best_action = Action.UP
        best_value = -float('inf')
        possible_actions = self.actions(s)
        actions_str = ""
        for a in possible_actions:
            actions_str += str(a) + ""
            s_copy = copy.deepcopy(s)
            res = self.result(s_copy, a)
            # s.children.append(res)
            value = self.expectimax(res, depth, False)
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

