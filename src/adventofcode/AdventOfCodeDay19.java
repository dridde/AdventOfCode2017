package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * --- Day 19: A Series of Tubes ---
Somehow, a network packet got lost and ended up here. It's trying to follow a routing diagram (your puzzle input), but it's confused about where to go.

Its starting point is just off the top of the diagram. Lines (drawn with |, -, and +) show the path it needs to take, starting by going down onto the only line connected to the top of the diagram. It needs to follow this path until it reaches the end (located somewhere within the diagram) and stop there.

Sometimes, the lines cross over each other; in these cases, it needs to continue going the same direction, and only turn left or right when there's no other option. In addition, someone has left letters on the line; these also don't change its direction, but it can use them to keep track of where it's been. For example:

     |          
     |  +--+    
     A  |  C    
 F---|----E|--+ 
     |  |  |  D 
     +B-+  +--+ 

Given this diagram, the packet needs to take the following path:

Starting at the only line touching the top of the diagram, it must go down, pass through A, and continue onward to the first +.
Travel right, up, and right, passing through B in the process.
Continue down (collecting C), right, and up (collecting D).
Finally, go all the way left through E and stopping at F.
Following the path to the end, the letters it sees on its path are ABCDEF.

The little packet looks up at you, hoping you can help it find the way. What letters will it see (in the order it would see them) if it follows the path? (The routing diagram is very wide; make sure you view it without line wrapping.)

Your puzzle answer was MOABEUCWQS.

--- Part Two ---
The packet is curious how many steps it needs to go.

For example, using the same routing diagram from the example above...

     |          
     |  +--+    
     A  |  C    
 F---|--|-E---+ 
     |  |  |  D 
     +B-+  +--+ 

...the packet would go:

6 steps down (including the first line at the top of the diagram).
3 steps right.
4 steps up.
3 steps right.
4 steps down.
3 steps right.
2 steps up.
13 steps left (including the F it stops on).
This would result in a total of 38 steps.

How many steps does the packet need to go?

Your puzzle answer was 18058.
 */
public class AdventOfCodeDay19 {

	public static void main(String[] args) throws IOException {
		List<ArrayList<Character>> playfield = new ArrayList<>();
		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday19.txt";
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				ArrayList<Character> lineList = new ArrayList<>();
				char[] charArray = line.toCharArray();
				for (char c : charArray) {
					lineList.add(c);
				}
				playfield.add(lineList);
			}
			br.close();
		}

		List<Character> letters = new ArrayList<>();

		// Find entry point
		int curX = 0;
		int curY = 0;
		char curDir = 'd';
		char curChar = '|';
		ArrayList<Character> currLine = playfield.get(0);
		for (int i = 0; i < currLine.size(); i++) {
			Character c = currLine.get(i);
			if (c == '|') {
				curX = i;
				break;
			}
		}

		boolean movePossible = true;
		// traverse the playfield
		char up = ' ';
		char down = ' ';
		char left = ' ';
		char right = ' ';
		int steps = 0;
		while (movePossible) {
			if (curChar == '|' || curChar == '-' || isLetter(curChar)) {
				switch (curDir) {
				case 'd':
					curY++;
					break;
				case 'u':
					curY--;
					break;
				case 'l':
					curX--;
					break;
				case 'r':
					curX++;
					break;
				default:
					System.out.println("Ups, we screwed up!");
					movePossible = false;
				}
				curChar = playfield.get(curY).get(curX);
				steps++;
				if (isLetter(curChar)) {
					letters.add(curChar);
				}
			} else if (curChar == '+') {
				// check where to go. going back is not an option
				// check left and right when going up or down
				if (curDir == 'u' || curDir == 'd') {
					try {
						left = playfield.get(curY).get(curX - 1);
					} catch (IndexOutOfBoundsException e) {
						left = ' ';
					}
					try {
						right = playfield.get(curY).get(curX + 1);
					} catch (IndexOutOfBoundsException e) {
						right = ' ';
					}
					if (left == '-' || left == '+' || isLetter(left)) {
						curChar = left;
						curDir = 'l';
						curX--;
					} else if (right == '-' || right == '+' || isLetter(right)) {
						// right then?
						curChar = right;
						curDir = 'r';
						curX++;
					} else {
						// nope
						movePossible = false;
					}
				} else if (curDir == 'l' || curDir == 'r') {
					try {
						up = playfield.get(curY - 1).get(curX);
					} catch (IndexOutOfBoundsException e) {
						up = ' ';
					}
					try {
						down = playfield.get(curY + 1).get(curX);
					} catch (IndexOutOfBoundsException e) {
						down = ' ';
					}
					if (up == '|' || up == '+' || isLetter(up)) {
						curChar = up;
						curDir = 'u';
						curY--;
					} else if (down == '|' || down == '+' || isLetter(down)) {
						// down then?
						curChar = down;
						curDir = 'd';
						curY++;
					} else {
						// nope
						movePossible = false;
					}
				}
				steps++;
				if (isLetter(curChar)) {
					letters.add(curChar);
				}
			} else {
				movePossible = false;
			}
		}
		StringBuilder builder = new StringBuilder(letters.size());
		for (Character ch : letters) {
			builder.append(ch);
		}
		System.out.println(builder.toString());
		System.out.println(steps);
	}

	static boolean isLetter(char c) {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return letters.indexOf(c) > -1 ? true : false;
	}

}
