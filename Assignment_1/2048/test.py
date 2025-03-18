from enum import Enum
import random
import pygame
import copy
import state as s
import time
from utils import Action
import utils as ut

a_values = [2, 2.5, 3, 3.5, 4]
b_values = [1.1, 1.3, 1.5, 1.7, 1.9]
iterations = 10

depth = 1
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


print("Playing with search depth: ", depth)

for i in range(5):
    a = a_values[i]
    for j in range(5):
        b = b_values[j]
        print("\n ----------------- \n")
        print("Playing with a = ", a, "and b = ", b)
        s.set_a(a)
        s.set_b(b)
        total_points = 0
        for i in range(iterations):

            grid_values = [[0, 0, 0, 0],
                            [0, 0, 0, 0],
                            [0, 0, 0, 0],
                            [0, 0, 0, 0]]
            add_new_value()

            iteration_points = 0
            running = True
            game_over = False
            while running:
                grid_copy = copy.deepcopy(grid_values)
                action = s.best_action(s.State(grid_copy), depth)
                new_grid, has_2048, points = ut.move_direction_bool_points(grid=grid_values, direction=action)
                iteration_points += points
                
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

                if game_over:
                    print("Iteration: ", i, " - Points: ", iteration_points)
                    total_points += iteration_points
                    running = False
                    break
        
        print("Average points for a = ", a, "and b = ", b, "is: ", total_points / iterations)


                    


           
    
