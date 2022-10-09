# MazeGame
Implemenation of breadth first search, depth first search, and generational algorithm to solve a maze randomly generated using Kruskal’s Algorithm for Fundamentals of Computer Science II assignment. Uses Northeastern University’s javalib.world image library to create GUI for visualizing solving the maze.

## To Run:

Download the mazegame package and add javalib.jar to classpath. Then run bigBang:
```
import mazegame.Maze;

int width = 50;
int height = 50;
int pixelsWide = 700;
int pixelsTall = 700;
double frameRate = 1.0 / 200.0;
new Maze(width, height)).bigBang(pixelsWide, pixelsTall, frameRate);
```

## Video of randomly generated maze being solved by BFS, DFS, and Genetic Algoirthm:
https://user-images.githubusercontent.com/69981613/194745369-725c768f-9300-4407-9255-ea45672a5b41.mov
