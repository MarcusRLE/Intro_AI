import random
from enum import Enum

class Action(Enum):
    UP = "up"
    DOWN = "down"
    LEFT = "left"
    RIGHT = "right"

def compress(grid):
    """Moves all nonzero values to the left side without merging."""
    has_2048 = False
    new_grid = [[0] * 4 for _ in range(4)]
    for i in range(4):
        pos = 0  # Position to place the next number
        for j in range(4):
            if grid[i][j] == 2048:
                has_2048 = True
            if grid[i][j] != 0:
                new_grid[i][pos] = grid[i][j]
                pos += 1
    return new_grid, has_2048

def merge(grid):
    """Merges adjacent cells if they are equal."""
    total_points = 0
    for i in range(4):
        for j in range(3):  # Stop at index 2 to prevent out-of-bounds
            if grid[i][j] != 0 and grid[i][j] == grid[i][j + 1]:
                total_points += grid[i][j] * 2
                grid[i][j] *= 2
                grid[i][j + 1] = 0  # Empty the merged cell
    return grid, total_points

def reverse(grid):
    """Reverses each row for moving right."""
    return [row[::-1] for row in grid]

def transpose(grid):
    """Transposes the matrix for vertical moves."""
    return [list(row) for row in zip(*grid)]

def move_direction(grid: list[list[int]], direction: Action):
    grid, _, _ = move_direction_bool_points(grid, direction)
    return grid

def move_direction_bool_points(grid: list[list[int]], direction: Action):
    has_2048 = False
    total_points = 0
    if direction == Action.LEFT:
        grid, has_2048 = compress(grid)
        grid, total_points = merge(grid)
        grid, _ = compress(grid)
    elif direction == Action.RIGHT:
        grid = reverse(grid)
        grid, has_2048 = compress(grid)
        grid, total_points = merge(grid)
        grid, _ = compress(grid)
        grid = reverse(grid)
    elif direction == Action.UP:
        grid = transpose(grid)
        grid, has_2048 = compress(grid)
        grid, total_points = merge(grid)
        grid, _ = compress(grid)
        grid = transpose(grid)
    elif direction == Action.DOWN:
        grid = transpose(grid)
        grid = reverse(grid)
        grid, has_2048 = compress(grid)
        grid, total_points = merge(grid)
        grid, _ = compress(grid)
        grid = reverse(grid)
        grid = transpose(grid)
    return grid, has_2048, total_points

def can_move(grid: list[list[int]], direction: Action) -> bool:
    """Returns True if the grid can be moved in the specified direction."""
    return grid != move_direction(grid, direction)

# def print_grid(grid: list[list[int]]) -> None:
#     """Prints the grid to the console."""
#     for row in grid:
#         print(row)