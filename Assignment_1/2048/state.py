import copy
from utils import *
from typing import List

class State:
    def __init__(self, grid):
        self.grid = grid
        self.util_val = None

class New_idx:
    def __init__(self, x, y, cell_value, probability):
        self.x = x
        self.y = y
        self.cell_value = cell_value
        self.probability = probability

class AI:
    def __init__(self, a, b, use_snake, use_empty_cell):
        self.WEIGHT_MATRIX = [[a**15, a**14, a**13, a**12],
                              [a**8,  a**9,  a**10, a**11],
                              [a**7,  a**6,  a**5,  a**4],
                              [a**0,  a**1,  a**2,  a**3]]

        self.EMPTY_WEIGHT_MATRIX = [[b**0,  b**1,  b**2,  b**3],
                                    [b**7,  b**6,  b**5,  b**4],
                                    [b**8,  b**9,  b**10, b**11],
                                    [b**15, b**14, b**13, b**12]]

        self.USE_SNAKE = use_snake
        self.USE_EMPTY_CELL = use_empty_cell

    def get_best_action(self, s: State, depth: int):
        possible_actions = self.get_possible_actions(s)
        if len(possible_actions) == 0: return None
        best_value = -float('inf')
        best_action = possible_actions[0]
        # actions_str = ""
        for a in possible_actions:
            # actions_str += str(a) + ""
            s_copy = copy.deepcopy(s)
            res = self.result(s_copy, a)
            # s.children.append(res)
            value = self.expectimax(res, depth, False)
            # actions_str += " with value: " + str(value) + " "
            if value > best_value:
                best_value = value
                best_action = a
            # if value == best_value:
            #     if random.randint(0, 1) == 0:
            #         best_action = a
        # print("Actions: ", actions_str)
        # print("Best actions: ", best_action)
        return best_action

    def get_possible_actions(self, s: State) -> List[Action]:
        possible_actions = []
        for action in Action:
            if can_move(s.grid, action):
                possible_actions.append(action)
        return possible_actions

    def expectimax(self, s: State, depth: int, isMaxPlayer: bool) -> float:
        # if the depth is 0 or the state is terminal, return the utility of the state
        if depth == 0:
            if(s.util_val != None):
                return s.util_val
            return self.utility(s)
        # if the player is the maximizing player
        if isMaxPlayer:
            value = -float('inf')
            possible_actions = self.get_possible_actions(s)
            for i in possible_actions:
                res = self.result(s, i)
                # s.children.append(res)
                value = max(value, self.expectimax(res, depth - 1, False))
            return value
        # if calculate eval based on probability
        else:
            # TODO: improve this?
            idx_list = self.get_idx_list(s)
            if idx_list == []: return s.util_val
            value = 0
            for i in idx_list:
                old_cell_value = s.grid[i.y][i.x]
                s.grid[i.y][i.x] = i.cell_value
                s.util_val += self.utility_by_idx(i.x, i.y, i.cell_value)
                value += i.probability * self.expectimax(s, depth - 1, True)
                s.grid[i.y][i.x] = old_cell_value
            return value / len(idx_list)

    def terminal_test(self, s: State) -> bool:
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
                utility += self.utility_by_idx(i, j, s.grid[i][j])
        #utility += empty_cells_heuristic(s)
        #utility *= smoothness_heuristic(s)
        return utility

    def utility_by_idx(self, x, y, cell_value) -> float:
        utility = 0
        if self.USE_EMPTY_CELL and cell_value == 0:
            utility += self.EMPTY_WEIGHT_MATRIX[y][x]
        if self.USE_SNAKE:
            utility += self.WEIGHT_MATRIX[y][x] * cell_value
        return utility

    def get_idx_list(self, s: State) -> list[New_idx]:
        new_idx_list = []
        current_state_util = 0
        for y in range(4):
            for x in range(4):
                if s.grid[y][x] == 0:
                    new_idx_list.append(New_idx(x, y, 2, 0.9))
                    new_idx_list.append(New_idx(x, y, 4, 0.1))
                else:
                    current_state_util += self.utility_by_idx(x, y, s.grid[y][x])
        s.util_val = current_state_util
        return new_idx_list

    def result(self, s: State, a: Action) -> State:
        return State(move_direction(grid=s.grid.copy(), direction=a))

