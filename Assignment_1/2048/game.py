import pygame
import time
from utils import *
from AI_agent import *
from input import InputBox, ToggleButton

pygame.init()

def start_screen(screen):
    clock = pygame.time.Clock()
    font = pygame.font.Font(None, 36)

    input_boxes = {
        "depth": InputBox(200, 50, 100, 36),
        "a": InputBox(200, 100, 100, 36),
        "b": InputBox(200, 150, 100, 36),
    }

    toggles = {
        "snake": ToggleButton(50, 220, 180, 40, "Snake"),
        "empty": ToggleButton(250, 220, 180, 40, "Empty Cell"),
        "smooth": ToggleButton(150, 280, 180, 40, "Smoothness"),
    }

    start_button = pygame.Rect(190, 360, 100, 40)

    running = True
    while running:
        screen.fill("black")
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()
            for box in input_boxes.values():
                box.handle_event(event)
            for toggle in toggles.values():
                toggle.handle_event(event)
            if event.type == pygame.MOUSEBUTTONDOWN and start_button.collidepoint(event.pos):
                return {
                    "depth": input_boxes["depth"].get_value(),
                    "a": input_boxes["a"].get_value(),
                    "b": input_boxes["b"].get_value(),
                    "snake": toggles["snake"].get_value(),
                    "empty": toggles["empty"].get_value(),
                    "smooth": toggles["smooth"].get_value(),
                }

        for label, box in input_boxes.items():
            label_text = font.render(label.capitalize() + ":", True, "white")
            screen.blit(label_text, (100, box.rect.y + 5))
            box.draw(screen)

        # Draw 'Heuristics' text
        heuristics_text = font.render("Heuristics:", True, "white")
        screen.blit(heuristics_text, (50, 200))



        for toggle in toggles.values():
            toggle.draw(screen)

        pygame.draw.rect(screen, "blue", start_button)
        start_text = font.render("START", True, "white")
        screen.blit(start_text, (start_button.x + 13, start_button.y + 7))

        pygame.display.flip()
        clock.tick(30)

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
                text = self.FONT.render(f"Game Over, Highscore: {self.points}", True, "white")
                text_rect = text.get_rect(center=(240, 240))
                pygame.draw.rect(self.SCREEN, "black", text_rect)
                self.SCREEN.blit(text, text_rect)

                # self.running = False
                # return self.points, self.reached_2048, self.reached_4096

            pygame.display.flip()
            # self.CLOCK.tick(60)

SCREEN = pygame.display.set_mode((480, 480))
pygame.display.set_caption("2048 AI Settings")

settings = start_screen(SCREEN)

game = Game(
    depth=settings["depth"],
    a=settings["a"],
    b=settings["b"],
    snake_heu=settings["snake"],
    empty_cell_heu=settings["empty"],
    smoothness_heu=settings["smooth"]
)
game.run()

pygame.quit()

