import pygame
import time
import tkinter as tk
from utils import *
from AI_agent import *

pygame.init()

# Screen setup
WIDTH, HEIGHT = 500, 400
SCREEN = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption("Game Settings")

# Colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
GRAY = (200, 200, 200)
BLUE = (0, 0, 255)

# Font
FONT = pygame.font.Font(None, 36)

# Input boxes
input_boxes = {
    "depth": pygame.Rect(200, 50, 100, 30),
    "a": pygame.Rect(200, 100, 100, 30),
    "b": pygame.Rect(200, 150, 100, 30),
}

# Default input values
input_texts = {
    "depth": "",
    "a": "",
    "b": "",
}

active_box = None


def draw_ui():
    """Draws the input screen UI."""
    SCREEN.fill(WHITE)

    # Labels for input fields
    labels = ["Depth:", "A:", "B:"]
    y_positions = [50, 100, 150]

    for i, label in enumerate(labels):
        text = FONT.render(label, True, BLACK)
        SCREEN.blit(text, (50, y_positions[i]))

    # Draw input boxes
    for key, rect in input_boxes.items():
        pygame.draw.rect(SCREEN, BLUE if active_box == key else BLACK, rect, 2)
        text_surface = FONT.render(input_texts[key], True, BLACK)
        SCREEN.blit(text_surface, (rect.x + 5, rect.y + 5))

    # Draw start button
    pygame.draw.rect(SCREEN, GRAY, (150, 250, 200, 50))
    start_text = FONT.render("Start Game", True, BLACK)
    SCREEN.blit(start_text, (180, 260))

    pygame.display.flip()


def run_input_screen():
    """Handles user input in Pygame for depth, a, b values."""
    global active_box
    running = True
    while running:
        draw_ui()
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                return None  # Quit if window is closed

            if event.type == pygame.MOUSEBUTTONDOWN:
                # Check if "Start Game" button is clicked
                if pygame.Rect(150, 250, 200, 50).collidepoint(event.pos):
                    return input_texts  # Start game with input values

                # Check if an input box is clicked
                for key, rect in input_boxes.items():
                    if rect.collidepoint(event.pos):
                        active_box = key
                        break
                else:
                    active_box = None

            if event.type == pygame.KEYDOWN:
                if active_box:
                    if event.key == pygame.K_BACKSPACE:
                        input_texts[active_box] = input_texts[active_box][:-1]
                    elif event.key in range(pygame.K_0, pygame.K_9 + 1):  # Allow only numbers
                        input_texts[active_box] += event.unicode

    return None



class Game:
    def __init__(self, depth, a, b, snake_heu, empty_cell_heu, smoothness_heu):

        self.reached_2048 = False
        self.reached_4096 = False
        self.running = True
        self.game_over = False
        self.points = 0
        self.ai = AI(a, b, snake_heu, empty_cell_heu, smoothness_heu)
        self.start_time = time.time()
        self.depth = depth
        self.GAME_STATE = State([[0, 0, 0, 0],
                                 [0, 0, 0, 0],
                                 [0, 0, 0, 0],
                                 [0, 0, 0, 0]])
        add_new_value(self.GAME_STATE.grid)

        print(f"depth: {depth}, a: {a}, b: {b}, snake heu: {snake_heu}, empty cell heu: {empty_cell_heu}, smoothness heu: {smoothness_heu}")

        # pygame
        self.SCREEN_SIZE = (480, 480)
        self.FONT = pygame.font.Font(None, 36)
        self.SCREEN = pygame.display.set_mode(self.SCREEN_SIZE)
        # self.CLOCK = pygame.time.Clock()

    def run(self):
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                    # print("Average smoothness value: ", average_smooth_value())
                    # Turn profiling off

            action = self.ai.get_best_action(self.GAME_STATE, self.depth)
            # assert(action != None)
            if action == None:
                self.game_over = True
                self.running = False
                return self.points, self.reached_2048, self.reached_4096
            new_grid, has_2048, points = move_direction_bool_points(grid=self.GAME_STATE.grid, direction=action)
            self.reached_2048 = has_2048
            self.points += points
            assert(new_grid != self.GAME_STATE.grid)
            self.GAME_STATE.grid = new_grid
            add_new_value(self.GAME_STATE.grid)

            # check if game is over
            if not self.game_over:
                self.game_over = True
                for action in Action:
                    if can_move(self.GAME_STATE.grid, action):
                        self.game_over = False

            self.SCREEN.fill("black")
            for y in range(4):
                for x in range(4):
                    if self.GAME_STATE.grid[y][x] == 0:
                        pygame.draw.rect(self.SCREEN, "grey", (x * 120 + 5, y * 120 + 5, 110, 110))
                        continue
                    if self.GAME_STATE.grid[y][x] == 2:
                        pygame.draw.rect(self.SCREEN, "light blue", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 4:
                        pygame.draw.rect(self.SCREEN, "blue", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 8:
                        pygame.draw.rect(self.SCREEN, "green", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 16:
                        pygame.draw.rect(self.SCREEN, "yellow", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 32:
                        pygame.draw.rect(self.SCREEN, "orange", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 64:
                        pygame.draw.rect(self.SCREEN, "red", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 128:
                        pygame.draw.rect(self.SCREEN, "purple", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 256:
                        pygame.draw.rect(self.SCREEN, "pink", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 512:
                        pygame.draw.rect(self.SCREEN, "brown", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 1024:
                        pygame.draw.rect(self.SCREEN, "light green", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 2048:
                        if(self.reached_2048 == False):
                            self.reached_2048 = True
                            print("Time taken to reach 2048: ", time.time() - self.start_time)
                        pygame.draw.rect(self.SCREEN, "dark green", (x * 120 + 5, y * 120 + 5, 110, 110))
                    elif self.GAME_STATE.grid[y][x] == 4096:
                        if(self.reached_4096 == False):
                            self.reached_4096 = True
                            print("Time taken to reach 4096: ", time.time() - self.start_time)
                        pygame.draw.rect(self.SCREEN, "white", (x * 120 + 5, y * 120 + 5, 110, 110))
                    else:
                        pygame.draw.rect(self.SCREEN, "white", (x * 120 + 5, y * 120 + 5, 110, 110))

                    text = self.FONT.render(str(self.GAME_STATE.grid[y][x]), True, "black")
                    text_rect = text.get_rect(center=(x * 120 + 60, y * 120 + 60))
                    self.SCREEN.blit(text, text_rect)

            if self.game_over:
                # # show game over screen
                # text = self.FONT.render("Game Over", True, "white")
                # text_rect = text.get_rect(center=(240, 240))
                # pygame.draw.rect(self.SCREEN, "black", text_rect)
                # self.SCREEN.blit(text, text_rect)

                self.running = False
                return self.points, self.reached_2048, self.reached_4096

            pygame.display.flip()
            # self.CLOCK.tick(60)

user_inputs = run_input_screen()

if user_inputs:
    try:
        # Convert inputs to integers
        depth = int(user_inputs["depth"])
        a = int(user_inputs["a"])
        b = int(user_inputs["b"])

        # Run the game 10 times with user inputs
        for _ in range(10):
            game = Game(depth=depth, a=a, b=b, snake_heu=True, empty_cell_heu=False, smoothness_heu=False)
            highscore, reached_2048, reached_4096 = game.run()
            print(f"highscore: {highscore}, reached 2048: {reached_2048}, reached 4096: {reached_4096}")

        pygame.quit()

    except ValueError:
        print("Invalid input! Please enter numbers.")