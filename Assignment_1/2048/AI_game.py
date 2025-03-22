import pygame
import time
from utils import *
from state import *

def main():
    DEPTH = 3
    SCREEN_SIZE = (480, 480)
    GAME_STATE = State([[0, 0, 0, 0],
                        [0, 0, 0, 0],
                        [0, 0, 0, 0],
                        [0, 0, 0, 0]])

    reached_2048 = False
    reached_4096 = False

    add_new_value(GAME_STATE.grid)

    pygame.init()
    FONT = pygame.font.Font(None, 36)
    SCREEN = pygame.display.set_mode(SCREEN_SIZE)
    SCREEN.fill("black")
    # CLOCK = pygame.time.Clock()
    running = True
    game_over = False
    start_time = time.time()
    print("Playing with search depth: ", DEPTH)

    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                # print("Average smoothness value: ", average_smooth_value())
                # Turn profiling off

        action = best_action(GAME_STATE, DEPTH)
        new_grid = move_direction(grid=GAME_STATE.grid, direction=action)
        if new_grid != GAME_STATE.grid:
            GAME_STATE.grid = new_grid
            add_new_value(GAME_STATE.grid)

        # check if game is over
        if not game_over:
            game_over = True
            for action in Action:
                if can_move(GAME_STATE.grid, action):
                    game_over = False
                    break

        for y in range(4):
            for x in range(4):
                if GAME_STATE.grid[y][x] == 0:
                    pygame.draw.rect(SCREEN, "grey", (x * 120 + 5, y * 120 + 5, 110, 110))
                    continue
                if GAME_STATE.grid[y][x] == 2:
                    pygame.draw.rect(SCREEN, "light blue", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 4:
                    pygame.draw.rect(SCREEN, "blue", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 8:
                    pygame.draw.rect(SCREEN, "green", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 16:
                    pygame.draw.rect(SCREEN, "yellow", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 32:
                    pygame.draw.rect(SCREEN, "orange", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 64:
                    pygame.draw.rect(SCREEN, "red", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 128:
                    pygame.draw.rect(SCREEN, "purple", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 256:
                    pygame.draw.rect(SCREEN, "pink", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 512:
                    pygame.draw.rect(SCREEN, "brown", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 1024:
                    pygame.draw.rect(SCREEN, "light green", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 2048:
                    if(reached_2048 == False):
                        reached_2048 = True
                        time_2048 = time.time()
                        print("Time taken to reach 2048: ", time_2048 - start_time)
                    pygame.draw.rect(SCREEN, "dark green", (x * 120 + 5, y * 120 + 5, 110, 110))
                elif GAME_STATE.grid[y][x] == 4096:
                    if(reached_4096 == False):
                        reached_4096 = True
                        time_4096 = time.time()
                        print("Time taken to reach 4096: ", time_4096 - start_time)
                    pygame.draw.rect(SCREEN, "white", (x * 120 + 5, y * 120 + 5, 110, 110))
                else:
                    pygame.draw.rect(SCREEN, "white", (x * 120 + 5, y * 120 + 5, 110, 110))

                text = FONT.render(str(GAME_STATE.grid[y][x]), True, "black")
                text_rect = text.get_rect(center=(x * 120 + 60, y * 120 + 60))
                SCREEN.blit(text, text_rect)

        if game_over:
            # show game over screen
            text = FONT.render("Game Over", True, "white")
            text_rect = text.get_rect(center=(240, 240))
            pygame.draw.rect(SCREEN, "black", text_rect)
            SCREEN.blit(text, text_rect)

        pygame.display.flip()
        # clock.tick(60)
main()
