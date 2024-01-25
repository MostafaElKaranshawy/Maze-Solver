/**
 * Mostafa Mohamed Elkaranshawy
 * 21011375
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.io.File;

public interface IMazeSolver {
    /**
    * Read the maze file, and solve it using Breadth First Search
    * @param maze maze file
    * @return the coordinates of the found path from point ’S’
    *
    to point ’E’ inclusive, or null if no path is found.
    */
    public int[][] solveBFS(java.io.File myfile);
    /**
    * Read the maze file, and solve it using Depth First Search
    * @param maze maze file
    * @return the coordinates of the found path from point ’S’
    *
    to point ’E’ inclusive, or null if no path is found.
    */
    public int[][] solveDFS(java.io.File myfile);
}
class Stack{
    Node top;
    int size =0;

    class Node {
        Node next;
        Object data;
        Node(Object element){
            data = element;
            next = null;
        }
    }
    public Object pop(){
        if(isEmpty()){
            return null;
        }
        else if (size == 1){
            Object element = top.data;
            top = null;
            size--;
            return element;
        }
        else{
            Node temp = top;
            top = top.next;
            size--;
            return temp;
        }
        
    }
    public void push(Object element){
        size++;
        Node newNode = new Node(element);
        newNode.next = top;
        top = newNode;
    }

    public Object peek(){
        if(isEmpty()){
            return null;
        }
        else{
            return top.data;
        }
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }
}
class Queue{
    Node front;
    int length = 0;
    public class Node{
        Object data;
        Node next;
        Node(Object element){
            data = element;
            next = null;
        }
    }
    public void enqueue(Object item){
        Node newNode = new Node(item);
        newNode.next = front;
        front = newNode;
        length++;
    }
    public Object dequeue(){
        Object element;
        if (isEmpty())
            element = "Error";
        else{
            length--;
            Node last = front;
            if (front.next == null){
                element = front.data;
                front = null;
            }
            else{
                while(last.next.next != null){
                    last = last.next;
                }
                element = last.next.data;
                last.next = null;
            }
        }
        return element;
    }
    public boolean isEmpty(){
        return (front == null);
    }
    public int size(){
        return length;
    }
    public void print(){
        System.out.print('[');
        if (length > 0 ){
            Node last = front;
            while(last != null){
                if(last.next == null){
                    System.out.print(last.data);
                }
                else{
                    System.out.print(last.data+", ");
                }
                last = last.next;
            }
        }
        System.out.print(']');
    }
}


public class Solver implements IMazeSolver {
    int[] size = {0,0};
    char[][] temp = new char[100][100];

    // Take the file as input and return the maze arguments.
    public char[][] file_reader(File myfile, int[] Size) throws FileNotFoundException {
        Scanner read_file1 =  new Scanner(myfile);
        Size[0] = read_file1.nextInt();
        Size[1] = read_file1.nextInt();
        String[] maze = new String[Size[0]];
        try{
            for(int i = 0; i < Size[0]; i++){
                if(read_file1.hasNext()){
                    maze[i] = read_file1.next();
                }
            }
        }
        catch(Exception e){
            System.out.println("Error when handling the file");
        }
        read_file1.close();
        char[][] photo = convert_maze(maze);

        return photo;
    }

    // Return the bfs solution for the maze.
    public int[][] solveBFS(java.io.File myfile){
        Queue qx = new Queue();
        Queue qy = new Queue();
        Stack s1 = new Stack();
        Stack s2 = new Stack();
        Stack sx = new Stack();
        Stack sy = new Stack();
        int[] start = {-1,-1};
        
        try{
            char[][] photo = file_reader(myfile, size);
            int row = size[0];
            int col = size[1];
            for(int i = 0; i < row; i++){
                for(int j =0;j<col;j++){
                    temp[i][j] = photo[i][j];
                }
            }
            if(check_maze(photo, start, row, col)){
                bfs(qx, qy,sx,sy, photo, start[0], start[1], col, row);
                reverse(sx,sy,s1,s2);
            }
            else{
                System.out.println();
                for(int i = 0; i < size[0]; i++){
                    for(int j =0;j<size[1];j++){
                        System.out.print(temp[i][j]);
                    }
                    System.out.println();
                }
                System.out.println("No solution for this maze");
                System.exit(1);
            }
            
        }
        catch(Exception e){
            System.out.println("Error when handling the file");
            System.exit(1);
        }
        
        int[][] coordinates = new int[s1.size][2];
        if(s1.isEmpty()){
            for(int i = 0; i < size[0]; i++){
                for(int j =0;j<size[1];j++){
                    System.out.print(temp[i][j]);
                }
                System.out.println();
            }
            System.out.println("No solution for the maze");
            System.exit(0);
        }
        else{
            int n = s1.size();
            for(int i = 0; i < n && !s1.isEmpty(); i++){
                coordinates[i][0] = (int)s1.peek();
                coordinates[i][1] = (int)s2.peek();
                if(i != 0 && i != n-1){
                    temp[coordinates[i][0]][coordinates[i][1]] = '*';
                }
                s1.pop();
                s2.pop();
            }
        }
        System.out.println("\nMaze Solution\n");
        for(int i = 0; i < size[0]; i++){
            for(int j =0;j<size[1];j++){
                System.out.print(temp[i][j]);
            }
            System.out.println();
        }
        return coordinates;
    }

    // Return the dfs solution for the maze.
    public int[][] solveDFS(java.io.File myfile){
        Stack x = new Stack(); 
        Stack y = new Stack();
        int[] start = {-1,-1};
        int[] size = {0,0};
        try{
            char[][] photo = file_reader(myfile, size);
            int row = size[0];
            int col = size[1];
            for(int i = 0; i < row; i++){
                for(int j =0;j<col;j++){
                    temp[i][j] = photo[i][j];
                }
            }
            if(check_maze(photo, start, row, col))
                dfs(x, y, photo, start[0], start[1], col, row);
            else{
                System.out.println();
                for(int i = 0; i < size[0]; i++){
                    for(int j =0;j<size[1];j++){
                        System.out.print(temp[i][j]);
                    }
                    System.out.println();
                }
                System.out.println("No solution for the maze");
                System.exit(0);
            }
        }
        catch(Exception e){
            System.out.println("Error when handling the file");
            System.exit(1);
        }
        int[][] coordinates = new int[x.size][2];
        if(x.size == 0){
            for(int i = 0; i < size[0]; i++){
                for(int j =0;j<size[1];j++){
                    System.out.print(temp[i][j]);
                }
                System.out.println();
            }
            System.out.println("No solution for the maze");
            System.exit(0);
        }
        else{
            int n = x.size();
            for(int i = n-1; i >=0 && !x.isEmpty();i--){
                coordinates[i][0] = (int)x.peek();
                coordinates[i][1] = (int)y.peek();
                if(i != 0 && i != n-1){
                    temp[coordinates[i][0]][coordinates[i][1]] = '*';
                }
                x.pop();
                y.pop();
            }
        }
        System.out.println("\nMaze Solution\n");
        for(int i = 0; i < size[0]; i++){
            for(int j =0;j<size[1];j++){
                System.out.print(temp[i][j]);
            }
            System.out.println();
        }
        return coordinates;
    }

    // check the validation of the coordinates.
    public boolean isvalid(int i, int j, int width, int height){
        boolean flag = false;
        if(i >= 0 && i < height && j >= 0 && j < width){
            flag = true;
        }
        return flag;
    }
    public boolean ispath(char[][] maze, int i, int j, int width, int height){
        boolean flag = false;
        if((isvalid(i+1,j,width,height) && (maze[i+1][j] == '.' || maze[i+1][j] == 'E')) || (isvalid(i-1,j,width,height) && (maze[i-1][j] == '.' || maze[i-1][j] == 'E' )) || (isvalid(i,j+1,width,height) && (maze[i][j+1] == '.' || maze[i][j+1] == 'E')) || (isvalid(i,j-1,width,height) && (maze[i][j-1] == '.' || maze[i][j-1] == 'E'))){
            flag = true;
        }
        return flag;
    }

    public boolean flagdfs = false;
    public void dfs(Stack pathx, Stack pathy, char[][] maze, int x, int y, int width, int height){
        if(isvalid(x, y, width, height) && !flagdfs){
            if(maze[x][y] == '.' || maze[x][y] == 'S' && ispath(maze, x, y, width, height) ){
                pathx.push(x);
                pathy.push(y);
                maze[x][y] = '0';
                dfs(pathx, pathy, maze, x-1, y, width, height);
                dfs(pathx, pathy, maze, x+1, y, width, height);
                dfs(pathx, pathy, maze, x, y-1, width, height);
                dfs(pathx, pathy, maze, x, y+1, width, height);
                if(!ispath(maze, x, y, width, height) && flagdfs == false){
                    // System.out.println(pathx.peek() +" "+ pathy.peek());
                    pathx.pop();
                    pathy.pop();  
                }
            }
            else if(maze[x][y] == 'E'){
                pathx.push(x);
                pathy.push(y);
                flagdfs = true;
                return;
            }
        }
    }

    // check if the two points are adjacent or not.
    public boolean check_adjacent(int x, int y, int i, int j){
        boolean flag = false;
        if(x-i == 0 && y-j == 1)
            flag = true;
        if(x-i == 0 && y-j == -1)
            flag = true;
        if(x-i == 1 && y-j == 0)
            flag = true;
        if(x-i == -1 && y-j == 0)
            flag = true;
        return flag;
    }

    // Reverse the bfs points and remove the unwanted points, return the shortest path.
    public void reverse(Stack ssx,Stack ssy, Stack s1, Stack s2){

        while(!ssx.isEmpty()){
            // System.out.println(ssx.peek()+""+ssy.peek());
            if(s1.isEmpty()){
                s1.push(ssx.peek());
                s2.push(ssy.peek());
                ssx.pop();
                ssy.pop();
                
            }
            else{
                // System.out.println(ssx.peek()+""+ssy.peek()+" "+s1.peek()+""+s2.peek());
                if(check_adjacent((int)ssx.peek(),(int)ssy.peek() , (int)s1.peek(), (int)s2.peek())){
                    s1.push(ssx.peek());
                    s2.push(ssy.peek());
                }
                ssx.pop();
                ssy.pop();
            }
        }
    }

    public void bfs(Queue pathx, Queue pathy,Stack sx,Stack sy, char[][] maze, int x, int y,int width,int height){
        pathx.enqueue(x);
        pathy.enqueue(y);
        // maze[x][y] = '0';
        while(!pathx.isEmpty()){
            // System.out.println(x+""+y);
            if(isvalid(x,y,width,height)){
                // if(maze[x][y] == '.' || maze[x][y] == 'S' || maze[x][y] == 'E'){
                    // System.out.println(x+""+y);
                    x = (int)pathx.dequeue();
                    y = (int)pathy.dequeue();
                    
                    
                    
                    if(maze[x][y] == 'E'){
                        sx.push(x);
                        sy.push(y);
                        break;
                    }
                    
                    if(maze[x][y] == '.' || maze[x][y] == 'S'){
                        
                        // if (sx.isEmpty() || (!sx.isEmpty() && check_adjacent(x, y, (int)sx.peek(), (int)sy.peek()))){
                            sx.push(x);
                            sy.push(y);
                            // System.out.println(sx.peek()+""+sy.peek());
                        if(isvalid(x-1, y, width, height) && (maze[x-1][y] == '.' || maze[x-1][y] == 'S' || maze[x-1][y] == 'E')){
                            if(maze[x-1][y] == 'E'){
                                sx.push(x-1);
                                sy.push(y);
                                break;
                            }
                            // maze[x-1][y] = '0';
                            pathx.enqueue(x-1);
                            pathy.enqueue(y);
                        }
                        if(isvalid(x+1, y, width, height) && (maze[x+1][y] == '.' || maze[x+1][y] == 'S'|| maze[x+1][y] == 'E')){
                            if(maze[x+1][y] == 'E'){
                                sx.push(x+1);
                                sy.push(y);
                                break;
                            }
                            // maze[x+1][y] = '0';
                            pathx.enqueue(x+1);
                            pathy.enqueue(y);
                        }
                        if(isvalid(x, y-1, width, height) && (maze[x][y-1] == '.' || maze[x][y-1] == 'S'|| maze[x][y-1] == 'E')){
                            if(maze[x][y-1] == 'E'){
                                sx.push(x);
                                sy.push(y-1);
                                break;
                            }
                            // maze[x][y-1] = '0';
                            pathx.enqueue(x);
                            pathy.enqueue(y-1);
                        }
                        if(isvalid(x, y+1, width, height) && (maze[x][y+1] == '.' || maze[x][y+1] == 'S'|| maze[x][y+1] == 'E')){
                            if(maze[x][y+1] == 'E'){
                                sx.push(x);
                                sy.push(y+1);
                                break;
                            }
                            // maze[x][y+1] = '0';
                            pathx.enqueue(x);
                            pathy.enqueue(y+1);
                        }
                        // }
                        
                    }
                // }
                if(maze[x][y] == 'S'){
                    maze[x][y] = '1';
                }
                else{
                    maze[x][y] = '0';
                }
                
                // if(!ispath(maze,x, y, width, height) && !sx.isEmpty()){
                //     sx.pop();
                //     sy.pop();
                // }
                // System.out.println(x+" "+y+" "+sx.peek()+" "+sy.peek());
                while(!ispath(maze,x, y, width, height) && !sx.isEmpty()){
                    
                    x = (int)sx.peek();
                    y = (int)sy.peek();
                    if(!ispath(maze,x, y, width, height) && !sx.isEmpty()){sx.pop();
                    sy.pop();}
                }
                // System.out.println(sx.size);
            }
            
        }
    }

    // check the start and end points in the maze.
    public boolean check_maze(char[][] maze , int[] start_loc, int row, int col){
        boolean sflag = false;
        boolean eflag = false;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if(maze[i][j] == 'S'){
                    sflag = true;
                    start_loc[0] = i;
                    start_loc[1] = j;
                }
                if(maze[i][j] == 'E'){
                    eflag = true;
                }
                if(eflag && sflag){
                    break;
                }
            }
        }
        return sflag && eflag;
    }
    public char[][] convert_maze(String[] maze){
        int row =maze.length;
        int col = maze[0].length();
        char[][] photo = new char[row][col];
        for(int i =0; i < row; i++){
            for(int j = 0; j <col; j++){
                photo[i] = maze[i].toCharArray();
            }
        }
        return photo;
    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Your File Path -Full Path-");
        File myfile = null;
        String path = in.nextLine();
        myfile = new File(path);

        Solver s = new Solver();

        System.out.println("Choose the method of solution:\t-BFS or -DFS");
        String method = in.nextLine().toLowerCase();
        in.close();
        switch(method){
            case "dfs":
                int[][] p = s.solveDFS(myfile);
                
                System.out.print("\nDFS:");
                for(int i = 0;i<p.length;i++){
                    System.out.printf("{%d, %d} ",p[i][0],p[i][1]);
                }
                break;
            case "bfs":
                int[][] pp = s.solveBFS(myfile);
                System.out.print("\nBFS:");
                for(int i = 0;i<pp.length;i++){
                    System.out.printf("{%d, %d} ",pp[i][0],pp[i][1]);
                }
                break;
            default:
                System.out.println("Wrong choice");
                break;
        }
    }
}