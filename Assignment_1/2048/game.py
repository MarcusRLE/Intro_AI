import pygame
import time
import tkinter as tk
from utils import *
from AI_agent import *

pygame.init()

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

def start_game():
    try:
        depth = int(entry_depth.get())
        a = int(entry_a.get())
        b = int(entry_b.get())

        # Close the Tkinter window
        root.destroy()

        # Start the game with user input values
        game = Game(depth=depth, a=a, b=b, snake_heu=True, empty_cell_heu=False, smoothness_heu=False)
        highscore, reached_2048, reached_4096 = game.run()
        print(f"highscore: {highscore}, reached 2048: {reached_2048}, reached 4096: {reached_4096}")
    
    except ValueError:
        label_error.config(text="Error: Please enter valid integers!")


# Create Tkinter UI
root = tk.Tk()
root.title("Game Settings")

tk.Label(root, text="Depth:").grid(row=0, column=0)
entry_depth = tk.Entry(root)
entry_depth.grid(row=0, column=1)

tk.Label(root, text="A:").grid(row=1, column=0)
entry_a = tk.Entry(root)
entry_a.grid(row=1, column=1)

tk.Label(root, text="B:").grid(row=2, column=0)
entry_b = tk.Entry(root)
entry_b.grid(row=2, column=1)

label_error = tk.Label(root, text="", fg="red")
label_error.grid(row=3, columnspan=2)

tk.Button(root, text="Start Game", command=start_game).grid(row=4, columnspan=2)

root.mainloop()
pygame.quit()

