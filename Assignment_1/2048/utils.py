import random
from enum import Enum

class Action(Enum):
    UP = "up"
    DOWN = "down"
    LEFT = "left"
    RIGHT = "right"

def compress(grid):
    """Moves all nonzero values to the left side without merging."""
    new_grid = [[0] * 4 for _ in range(4)]
    for i in range(4):
        pos = 0  # Position to place the next number
        for j in range(4):
            if grid[i][j] != 0:
                new_grid[i][pos] = grid[i][j]
                pos += 1
    return new_grid

def merge(grid):
    """Merges adjacent cells if they are equal."""
    for i in range(4):
        for j in range(3):  # Stop at index 2 to prevent out-of-bounds
            if grid[i][j] != 0 and grid[i][j] == grid[i][j + 1]:
                grid[i][j] *= 2
                grid[i][j + 1] = 0  # Empty the merged cell
    return grid

def reverse(grid):
    """Reverses each row for moving right."""
    return [row[::-1] for row in grid]

def transpose(grid):
    """Transposes the matrix for vertical moves."""
    return [list(row) for row in zip(*grid)]

def move_direction(grid: list[list[int]], direction: Action) -> list[list[int]]:
    if direction == Action.LEFT:
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
    elif direction == Action.RIGHT:
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
    elif direction == Action.UP:
        grid = transpose(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = transpose(grid)
    elif direction == Action.DOWN:
        grid = transpose(grid)
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
        grid = transpose(grid)
    return grid

def can_move(grid: list[list[int]], direction: Action) -> bool:
    """Returns True if the grid can be moved in the specified direction."""
    return grid != move_direction(grid, direction)

# def print_grid(grid: list[list[int]]) -> None:
#     """Prints the grid to the console."""
#     for row in grid:
#         print(row)