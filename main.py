import random
from collections import deque

# Dimensions of board
WIDTH = 20
HEIGHT = 16

# 2D list
board = []

# Constants representing various cell contents
EMPTY = 0
SNAKE = 1
FOOD = 2

# List of points, where the right of the deque is the snake's head
snake = deque()

food = (0, 0)

# Constants representing directions
UP = 0
RIGHT = 1
DOWN = 2
LEFT = 3

# Set board to 2D list of dimensions HEIGHT x WIDTH, of all EMPTY
def initialize_board():
    for y in range(HEIGHT):
        board.append([])
        for x in range(WIDTH):
            board[y].append(EMPTY)

# Print board using cells' string representations
VALUE_TO_STRING = {EMPTY: '.', SNAKE: 'O', FOOD: 'X'}
def print_board():
    for row in board:
        for cell in row:
            print(VALUE_TO_STRING[cell], end="")
        print()

# Return random point within bounds
def random_point():
    x = random.randint(0, WIDTH - 1)
    y = random.randint(0, HEIGHT - 1)
    return (x, y)

def place(content, point):
    x, y = point
    board[y][x] = content

def place_randomly(content):
    place(content, random_point())

# The generate_ functions update both the board and corresponding lists of points
def generate_snake():
    point = random_point()
    place(SNAKE, point)
    snake.append(point)

def generate_food():
    global food
    food = random_point()
    place(FOOD, food)

DIRECTION_TO_JUMP = {UP: (0, -1), RIGHT: (1, 0), DOWN: (0, 1), LEFT: (-1, 0)}
def move(direction):
    jump = DIRECTION_TO_JUMP[direction]
    #snake_head = 

initialize_board()
generate_snake()
generate_food()
print_board()
print(snake)
print(food)