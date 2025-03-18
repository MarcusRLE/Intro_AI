from enum import Enum
import random
import pygame
import copy
import state as s
import time
from utils import Action
import utils as ut


reached_2048 = False
reached_4096 = False
screen_size = (480, 480)
grid_values = [[0, 0, 0, 0],
                [0, 0, 0, 0],
                [0, 0, 0, 0],
                [0, 0, 0, 0]]

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
start_time = time.time()

while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        # if event.type == pygame.KEYDOWN:
        #     print("Key pressed")
        #     grid_copy = copy.deepcopy(grid_values)
        #     action = s.best_action(s.State(grid_copy))
        #     print("\nChosen action: ", action)
        #     new_grid = ut.move_direction(grid=grid_values, direction=action)
        #     if new_grid != grid_values:
        #         grid_values = new_grid
        #         add_new_value()



    grid_copy = copy.deepcopy(grid_values)
    action = s.best_action(s.State(grid_copy))
    # print("Action: ", action)
    new_grid = ut.move_direction(grid=grid_values, direction=action)
    if new_grid != grid_values:
        grid_values = new_grid
        add_new_value()
    

    # check if game is over
    if not game_over:
        game_over = True
        for action in Action:
            if ut.move_direction(grid_values, action) != grid_values:
                game_over = False
                break
        

    screen.fill("grey")
    for y in range(4):
        for x in range(4):
            pygame.draw.rect(screen, "black", (x * 120, y * 120, 120, 120), 5)
            if grid_values[y][x] != 0:
                color = "white"
                if grid_values[y][x] == 2:
                    color = "light blue"
                elif grid_values[y][x] == 4:
                    color = "blue"
                elif grid_values[y][x] == 8:
                    color = "green"
                elif grid_values[y][x] == 16:
                    color = "yellow"
                elif grid_values[y][x] == 32:
                    color = "orange"
                elif grid_values[y][x] == 64:
                    color = "red"
                elif grid_values[y][x] == 128:
                    color = "purple"
                elif grid_values[y][x] == 256:
                    color = "pink"
                elif grid_values[y][x] == 512:
                    color = "brown"
                elif grid_values[y][x] == 1024:
                    color = "light green"
                elif grid_values[y][x] == 2048:
                    if(reached_2048 == False):
                        reached_2048 = True
                        time_2048 = time.time()
                        print("Time taken to reach 2048: ", time_2048 - start_time)
                    color = "dark green"
                elif grid_values[y][x] == 4096:
                    if(reached_4096 == False):
                        reached_4096 = True
                        time_4096 = time.time()
                        print("Time taken to reach 4096: ", time_4096 - start_time)
                    color = "white"
                pygame.draw.rect(screen, color, (x * 120 + 5, y * 120 + 5, 110, 110))
                font = pygame.font.Font(None, 36)
                text = font.render(str(grid_values[y][x]), True, "black")
                text_rect = text.get_rect(center=(x * 120 + 60, y * 120 + 60))
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
