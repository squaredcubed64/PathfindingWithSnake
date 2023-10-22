import random
from collections import deque
from enum import Enum
import tkinter as tk
random.seed(4)

# Dimensions of board
WIDTH = 8
HEIGHT = 5

# Size of each cell in GUI
CELL_WIDTH = 40
CELL_HEIGHT = 40

# 2D list
board = []

class Content(Enum):
    EMPTY = 0
    SNAKE = 1
    FOOD = 2

# List of points, where the right of the deque is the head
snake = deque()

food = (0, 0)

class Direction(Enum):
    UP = 0
    RIGHT = 1
    DOWN = 2
    LEFT = 3

# Set board to 2D list of dimensions HEIGHT x WIDTH, of all EMPTY
def initialize_board():
    for y in range(HEIGHT):
        board.append([])
        for x in range(WIDTH):
            board[y].append(Content.EMPTY)

# Print board using cells' string representations
VALUE_TO_STRING = {Content.EMPTY: '.', Content.SNAKE: 'O', Content.FOOD: 'X'}
def print_board():
    for row in board:
        for cell in row:
            print(VALUE_TO_STRING[cell], end="")
        print()
    print()

# Return random point within bounds
def random_point():
    x = random.randint(0, WIDTH - 1)
    y = random.randint(0, HEIGHT - 1)
    return (x, y)

def place(content, point):
    x, y = point
    board[y][x] = content

def get(point):
    x, y = point
    return board[y][x]

'''def place_randomly(content):
    place(content, random_point())'''

# Generates a snake of length 1
def generate_snake():
    point = random_point()
    place(Content.SNAKE, point)
    snake.append(point)

def generate_food():
    global food
    food = random_point()

    # If there is no place to put the food, avoid an infinite loop
    if len(snake) == WIDTH * HEIGHT:
        win()
        return
    
    # Place the food in a random EMPTY cell
    while get(food) != Content.EMPTY:
        food = random_point()
    place(Content.FOOD, food)

# Add one SNAKE cell to the head, and remove one SNAKE cell to the tail if grow is false
DIRECTION_TO_JUMP = {Direction.UP: (0, -1), Direction.RIGHT: (1, 0), Direction.DOWN: (0, 1), Direction.LEFT: (-1, 0)}
def move(direction):
    grow = False
    jump = DIRECTION_TO_JUMP[direction]
    snake_head = snake[-1]

    new_snake_head = (snake_head[0] + jump[0], snake_head[1] + jump[1])
    snake.append(new_snake_head)

    if inBounds(new_snake_head) and get(new_snake_head) != Content.SNAKE:
        grow = get(new_snake_head) == Content.FOOD
        place(Content.SNAKE, new_snake_head)
    else:
        lose()

    if grow:
        generate_food()
    else:
        old_snake_tail = snake.popleft()
        place(Content.EMPTY, old_snake_tail)

def inBounds(point):
    x, y = point
    return 0 <= x <= WIDTH - 1 and 0 <= y <= HEIGHT - 1

def lose():
    print("LOSE")

def win():
    print("WIN")

# To be rendered
cells = None

CONTENT_TO_COLOR = {Content.EMPTY : "black", Content.SNAKE : "white", Content.FOOD : "red"}
def create_table(data, window):
    global cells
    cells = [[None for _ in range(WIDTH)] for _ in range(HEIGHT)]

    for i in range(HEIGHT):
        for j in range(WIDTH):
            color = CONTENT_TO_COLOR[board[i][j]]
            cell = tk.Canvas(window, width=CELL_WIDTH, height=CELL_HEIGHT, bg=color)
            cell.grid(row=i, column=j)
            cells[i][j] = cell
    
    return cells

def update_grid(data):
    for i in range(len(data)):
        for j in range(len(data[0])):
            color = CONTENT_TO_COLOR[board[i][j]]
            cells[i][j].configure(bg=color)

def on_key(event):
    if event.keysym == "Up":
        move(Direction.UP)
    elif event.keysym == "Down":
        move(Direction.DOWN)
    elif event.keysym == "Left":
        move(Direction.LEFT)
    elif event.keysym == "Right":
        move(Direction.RIGHT)
    
    update_grid(board)

def main():
    initialize_board()

    generate_snake()
    generate_food()

    window = tk.Tk()
    window.title("Snake AI Visualizer")

    create_table(board, window)

    window.bind("<KeyPress>", on_key)

    window.mainloop()

if __name__ == "__main__":
    main()