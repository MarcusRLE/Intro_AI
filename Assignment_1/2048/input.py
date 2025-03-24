import pygame

class InputBox:
    def __init__(self, x, y, w, h, text=''):
        self.rect = pygame.Rect(x, y, w, h)
        self.color = pygame.Color('white')
        self.text = text
        self.font = pygame.font.Font(None, 26)
        self.txt_surface = self.font.render(text, True, self.color)
        self.active = False

    def handle_event(self, event):
        if event.type == pygame.MOUSEBUTTONDOWN:
            self.active = self.rect.collidepoint(event.pos)
        if event.type == pygame.KEYDOWN and self.active:
            if event.key == pygame.K_RETURN:
                self.active = False
            elif event.key == pygame.K_BACKSPACE:
                self.text = self.text[:-1]
            elif event.unicode.isnumeric():
                self.text += event.unicode
            self.txt_surface = self.font.render(self.text, True, self.color)

    def draw(self, screen):
        screen.blit(self.txt_surface, (self.rect.x + 5, self.rect.y + 5))
        pygame.draw.rect(screen, self.color, self.rect, 2)

    def get_value(self):
        return int(self.text) if self.text.isdigit() else 0


class ToggleButton:
    def __init__(self, x, y, w, h, label):
        self.rect = pygame.Rect(x, y, w, h)
        self.label = label
        self.active = False
        self.font = pygame.font.Font(None, 28)

    def handle_event(self, event):
        if event.type == pygame.MOUSEBUTTONDOWN and self.rect.collidepoint(event.pos):
            self.active = not self.active

    def draw(self, screen):
        color = "green" if self.active else "grey"
        text_color = "white" if self.active else "black"
        pygame.draw.rect(screen, color, self.rect)
        text = self.font.render(f"{self.label}: {'ON' if self.active else 'OFF'}", True, text_color)
        screen.blit(text, (self.rect.x + 5, self.rect.y + 10))

    def get_value(self):
        return self.active
