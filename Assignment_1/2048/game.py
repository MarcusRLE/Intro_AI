import pygame
import time
from utils import *
from AI_agent import *

pygame.init()

class Game:
    def __init__(self, depth, a, b, snake_heu, empty_cell_heu, smoothness_heu):

        self.reached_2048 = False
        self.reached_4096 = False
        self.running = False
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

    def settings_screen(self):
        self.SCREEN_SIZE = (480, 480)
        self.FONT = pygame.font.Font(None, 36)
        self.SCREEN = pygame.display.set_mode(self.SCREEN_SIZE)

        SNAKE_HEU_TEXT = self.FONT.render("Snake Heuristic", True, "white")
        SNAKE_HEU_RECT = SNAKE_HEU_TEXT.get_rect(center=(240, 240))

        EMPTY_HEU_TEXT = self.FONT.render("Empty Cell Heuristic", True, "white")
        EMPTY_HEU_RECT = EMPTY_HEU_TEXT.get_rect(center=(240, 280))

        SMOOTH_HEU_TEXT = self.FONT.render("Empty Cell Heuristic", True, "white")
        SMOOTH_HEU_RECT = SMOOTH_HEU_TEXT.get_rect(center=(240, 320))

        START_BUTTON_TEXT = self.FONT.render("Start Game", True, "white")
        START_BUTTON_RECT = START_BUTTON_TEXT.get_rect(center=(240, 360))

        done_with_settings = False

        while not done_with_settings:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                    # print("Average smoothness value: ", average_smooth_value())
                    # Turn profiling off
                if event.type == pygame.MOUSEBUTTONUP:
                    if (START_BUTTON_RECT.collidepoint(event.pos)):
                        self.run(True)

            self.SCREEN.fill("black")

            pygame.draw.rect(self.SCREEN, "black", SNAKE_HEU_RECT)
            pygame.draw.rect(self.SCREEN, "black", EMPTY_HEU_RECT)
            pygame.draw.rect(self.SCREEN, "black", SMOOTH_HEU_RECT)
            pygame.draw.rect(self.SCREEN, "grey", START_BUTTON_RECT)

            self.SCREEN.blit(SNAKE_HEU_TEXT, SNAKE_HEU_RECT)
            self.SCREEN.blit(EMPTY_HEU_TEXT, EMPTY_HEU_RECT)
            self.SCREEN.blit(SMOOTH_HEU_TEXT, SMOOTH_HEU_RECT)
            self.SCREEN.blit(START_BUTTON_TEXT, START_BUTTON_RECT)

            pygame.display.flip()


    def run(self, gui):
        self.running = True

        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                    # print("Average smoothness value: ", average_smooth_value())
                    # Turn profiling off

            action = self.ai.get_best_action(self.GAME_STATE, self.depth)
            if action == None:
                self.game_over = True
                # self.running = False
                # return self.points, self.reached_2048, self.reached_4096
            new_grid, has_2048, points = move_direction_bool_points(grid=self.GAME_STATE.grid, direction=action)
            self.reached_2048 = has_2048
            self.points += points
            self.GAME_STATE.grid = new_grid
            add_new_value(self.GAME_STATE.grid)

            # check if game is over
            if not self.game_over:
                self.game_over = True
                for action in Action:
                    if can_move(self.GAME_STATE.grid, action):
                        self.game_over = False

            if not gui:
                continue

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
                # show game over screen
                text = self.FONT.render(f"""Game Over, Highscore: {self.points}""", True, "white")
                text_rect = text.get_rect(center=(240, 240))
                pygame.draw.rect(self.SCREEN, "black", text_rect)
                self.SCREEN.blit(text, text_rect)

            pygame.display.flip()
            # self.CLOCK.tick(60)

game = Game(depth=4, a=0, b=0, snake_heu=True, empty_cell_heu=True, smoothness_heu=False)
game.settings_screen()
# game.run(True)

pygame.quit()

