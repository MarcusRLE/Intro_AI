from enum import Enum
import random
import pygame
import copy
import state as s
import time
from utils import Action

screen_size = (480, 480)
grid_values = [[0, 0, 0, 0],
                [0, 0, 0, 0],
                [0, 0, 0, 0],
                [0, 0, 0, 0]]

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
    if direction == Action.UP:
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
    elif direction == Action.DOWN:
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
    elif direction == Action.LEFT:
        grid = transpose(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = transpose(grid)
    elif direction == Action.RIGHT:
        grid = transpose(grid)
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
        grid = transpose(grid)
    return grid
    
def move_direction_key(grid: list[list[int]], direction: pygame.key) -> list[list[int]]:
    if direction == pygame.K_UP:
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
    elif direction == pygame.K_DOWN:
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
    elif direction == pygame.K_LEFT:
        grid = transpose(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = transpose(grid)
    elif direction == pygame.K_RIGHT:
        grid = transpose(grid)
        grid = reverse(grid)
        grid = compress(grid)
        grid = merge(grid)
        grid = compress(grid)
        grid = reverse(grid)
        grid = transpose(grid)
    return grid

def add_new_value():
    random_i = random.randint(0, 3)
    random_j = random.randint(0, 3)
    random_value = random.randint(1, 10) == 1 and 4 or 2
    while grid_values[random_i][random_j] != 0:
        random_i = random.randint(0, 3)
        random_j = random.randint(0, 3)
    grid_values[random_i][random_j] = random_value

add_new_value()

pygame.init()
screen = pygame.display.set_mode(screen_size)
clock = pygame.time.Clock()
running = True
game_over = False

while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.KEYDOWN:
            print("Key pressed")
            grid_copy = copy.deepcopy(grid_values)
            action = s.best_action(s.State(grid_copy))
            print("Action: ", action)
            new_grid = move_direction(grid=grid_values, direction=action)
            if new_grid != grid_values:
                grid_values = new_grid
                add_new_value()
            

    # check if game is over
    if not game_over:
        game_over = True
        for action in Action:
            if move_direction(grid_values, action) != grid_values:
                game_over = False
                break
        

    screen.fill("grey")
    for i in range(4):
        for j in range(4):
            pygame.draw.rect(screen, "black", (i * 120, j * 120, 120, 120), 5)
            if grid_values[i][j] != 0:
                color = "white"
                if grid_values[i][j] == 2:
                    color = "light blue"
                elif grid_values[i][j] == 4:
                    color = "blue"
                elif grid_values[i][j] == 8:
                    color = "green"
                elif grid_values[i][j] == 16:
                    color = "yellow"
                elif grid_values[i][j] == 32:
                    color = "orange"
                elif grid_values[i][j] == 64:
                    color = "red"
                elif grid_values[i][j] == 128:
                    color = "purple"
                elif grid_values[i][j] == 256:
                    color = "pink"
                elif grid_values[i][j] == 512:
                    color = "brown"
                elif grid_values[i][j] == 1024:
                    color = "light green"
                elif grid_values[i][j] == 2048:
                    color = "dark green"
                pygame.draw.rect(screen, color, (i * 120 + 5, j * 120 + 5, 110, 110))
                font = pygame.font.Font(None, 36)
                text = font.render(str(grid_values[i][j]), True, "black")
                text_rect = text.get_rect(center=(i * 120 + 60, j * 120 + 60))
                screen.blit(text, text_rect)
           
    if game_over:
        # show game over screen
        font = pygame.font.Font(None, 36)
        text = font.render("Game Over", True, "white")
        text_rect = text.get_rect(center=(240, 240))
        pygame.draw.rect(screen, "black", text_rect)
        screen.blit(text, text_rect)
     
    pygame.display.flip()
    clock.tick(60)
