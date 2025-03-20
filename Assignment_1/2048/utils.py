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
    def can_merge_or_shift(line: list[int]) -> bool:
        current_num = None
        non_zero_found = False
        """Check if a row or column can be moved by shifting or merging."""
        for i in range(4):
            if line[i] != 0:
                non_zero_found = True
                if current_num is None:
                    current_num = line[i]
                else:
                    if current_num == line[i]:
                        # Can merge
                        return True
                    if current_num != line[i]:
                        current_num = line[i]
            else:
                if non_zero_found:
                    # Can shift
                    return True
        return False
        for i in range(3):
            if line[i] == line[i + 1] and line[i] != 0:
                return True  # Merge is possible
            if line[i] == 0 and line[i + 1] != 0:
                return True  # Shift is possible
        return False
    
    if direction == Action.RIGHT:
        for row in grid:
            if can_merge_or_shift(row):
                return True
    elif direction == Action.LEFT:
        for row in grid:
            if can_merge_or_shift(row[::-1]):  # Check reversed row
                return True
    elif direction == Action.DOWN:
        for col in range(4):
            if can_merge_or_shift([grid[row][col] for row in range(4)]):
                return True
    elif direction == Action.UP:
        for col in range(4):
            if can_merge_or_shift([grid[row][col] for row in range(3, -1, -1)]):  # Check reversed column
                return True
    
    return False