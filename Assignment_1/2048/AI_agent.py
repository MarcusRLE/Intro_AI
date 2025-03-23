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
    def __init__(self, a, b, snake_heu, empty_cell_heu, smoothness_heu):
        self.WEIGHT_MATRIX = [[a**15, a**14, a**13, a**12],
                              [a**8,  a**9,  a**10, a**11],
                              [a**7,  a**6,  a**5,  a**4],
                              [a**0,  a**1,  a**2,  a**3]]

        self.EMPTY_WEIGHT_MATRIX = [[b**0,  b**1,  b**2,  b**3],
                                    [b**7,  b**6,  b**5,  b**4],
                                    [b**8,  b**9,  b**10, b**11],
                                    [b**15, b**14, b**13, b**12]]

        self.SNAKE_HEU = snake_heu
        self.EMPTY_CELL_HEU = empty_cell_heu
        self.SMOOTHNESS_HEU = smoothness_heu


    # Funciton called from game to get the best action.
    # Parameters: s: State (the current state of the game), depth: int (the depth of the search tree)
    # Returns: Action (the best action to take)
    def get_best_action(self, s: State, depth: int):
        depth = depth - (1 - (depth % 2)) # Makes sure that the depth is always uneven
        _, best_action = self.expectimax(s, depth, True)
        return best_action

    # Function to get the possible actions from a state.
    # Parameters: s: State (the state of the game)
    # Returns: List[Action] (the possible actions from the state)
    def get_possible_actions(self, s: State) -> List[Action]:
        possible_actions = []
        for action in Action:
            if can_move(s.grid, action):
                possible_actions.append(action)
        return possible_actions


    # Function to get the best action using expectimax algorithm
    # Parameters: s: State (the current state of the game), depth: int (the depth of the search tree), isMaxPlayer: bool (whether the player is the maximizing player)
    # Returns: float (the best value), Action (the best action)
    def expectimax(self, s: State, depth: int, isMaxPlayer: bool):

        # If depth is reached, return the utility value of the state
        if depth == 0:
            if s.util_val != None:
                return s.util_val, None
            return self.utility(s), None

        if isMaxPlayer:
            value = -float('inf')
            best_action = None
            possible_actions = self.get_possible_actions(s)
            for a in possible_actions:
                res = self.result(s, a)
                temp_value, _ = self.expectimax(res, depth - 1, False)
                if temp_value > value:
                    value = temp_value
                    best_action = a
            return value, best_action
        else:
            idx_list = self.get_idx_list(s)
            if idx_list == []: return s.util_val, None
            value = 0
            for i in idx_list:
                old_cell_value = s.grid[i.y][i.x]

                # Update the state by adding the new cell value to the cell and adding the utility value of the cell to the state
                s.grid[i.y][i.x] = i.cell_value
                s.util_val += self.utility_by_idx(i.x, i.y, i.cell_value)

                # Calculate the value of the state and add it to the total value
                temp_value, _ = self.expectimax(s, depth - 1, True)
                value += i.probability * temp_value

                # Reset original state by removing the utility value of the cell from the state and setting the cell value to the original value
                s.util_val -= self.utility_by_idx(i.x, i.y, i.cell_value)
                s.grid[i.y][i.x] = old_cell_value
                
            return value / len(idx_list), None

    # Heuristic function to calculate 'smoothness' of a state
    # Parameters: s: State (the state of the game)
    # Returns: float (the smoothness heuristic value)
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
        return smoothness

    # Funciton to calculate the value of a given state depending on heuristics
    # Parameters: s: State (the state of the game)
    # Returns: float (the utility value of the state)
    def utility(self, s: State) -> float:
        utility = 0
        for i in range(4):
            for j in range(4):
                utility += self.utility_by_idx(i, j, s.grid[i][j])
        #utility += self.empty_cells_heuristic(s)
        if self.SMOOTHNESS_HEU:
            utility += self.smoothness_heuristic(s)
        return utility

    # Function to calculate the utility of a cell based on the cell value and the position of the cell
    # Parameters: x: int (the x-coordinate of the cell), y: int (the y-coordinate of the cell), cell_value: int (the value of the cell)
    # Returns: float (the utility value of the cell)
    def utility_by_idx(self, x, y, cell_value) -> float:
        utility = 0
        if self.EMPTY_CELL_HEU and cell_value == 0:
            utility += self.EMPTY_WEIGHT_MATRIX[x][y]
        if self.SNAKE_HEU:
            utility += self.WEIGHT_MATRIX[x][y] * cell_value
        # if cell_value == 0:
        #     utility += 1
        return utility

    # Function to get the list of new indices and their probabilities
    # Parameters: s: State (the state of the game)
    # Returns: List[New_idx] (the list of new indices and their probabilities)
    def get_idx_list(self, s: State) -> list[New_idx]:
        new_idx_list = []
        current_state_util = 0
        for y in range(4):
            for x in range(4):
                if s.grid[y][x] == 0:
                    new_idx_list.append(New_idx(x, y, 2, 0.9))
                    new_idx_list.append(New_idx(x, y, 4, 0.1))
                else:
                    # Calculate the utility of the current state
                    current_state_util += self.utility_by_idx(x, y, s.grid[y][x])
        # Set the utility value of the current state
        s.util_val = current_state_util
        return new_idx_list

    # Function to get the resulting state of taking an action in a state
    # Parameters: s: State (the state of the game), a: Action (the action to take)
    # Returns: State (the resulting state of the game)
    def result(self, s: State, a: Action) -> State:
        return State(move_direction(grid=s.grid.copy(), direction=a))

