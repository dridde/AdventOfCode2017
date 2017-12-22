package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * --- Day 21: Fractal Art ---
You find a program trying to generate some art. It uses a strange process that involves repeatedly enhancing the detail of an image through a set of rules.

The image consists of a two-dimensional square grid of pixels that are either on (#) or off (.). The program always begins with this pattern:

.#.
..#
###
Because the pattern is both 3 pixels wide and 3 pixels tall, it is said to have a size of 3.

Then, the program repeats the following process:

If the size is evenly divisible by 2, break the pixels up into 2x2 squares, and convert each 2x2 square into a 3x3 square by following the corresponding enhancement rule.
Otherwise, the size is evenly divisible by 3; break the pixels up into 3x3 squares, and convert each 3x3 square into a 4x4 square by following the corresponding enhancement rule.
Because each square of pixels is replaced by a larger one, the image gains pixels and so its size increases.

The artist's book of enhancement rules is nearby (your puzzle input); however, it seems to be missing rules. The artist explains that sometimes, one must rotate or flip the input pattern to find a match. (Never rotate or flip the output pattern, though.) Each pattern is written concisely: rows are listed as single units, ordered top-down, and separated by slashes. For example, the following rules correspond to the adjacent patterns:

../.#  =  ..
          .#

                .#.
.#./..#/###  =  ..#
                ###

                        #..#
#..#/..../#..#/.##.  =  ....
                        #..#
                        .##.
When searching for a rule to use, rotate and flip the pattern as necessary. For example, all of the following patterns match the same rule:

.#.   .#.   #..   ###
..#   #..   #.#   ..#
###   ###   ##.   .#.
Suppose the book contained the following two rules:

../.# => ##./#../...
.#./..#/### => #..#/..../..../#..#
As before, the program begins with this pattern:

.#.
..#
###
The size of the grid (3) is not divisible by 2, but it is divisible by 3. It divides evenly into a single square; the square matches the second rule, which produces:

#..#
....
....
#..#
The size of this enhanced grid (4) is evenly divisible by 2, so that rule is used. It divides evenly into four squares:

#.|.#
..|..
--+--
..|..
#.|.#
Each of these squares matches the same rule (../.# => ##./#../...), three of which require some flipping and rotation to line up with the rule. The output for the rule is the same in all four cases:

##.|##.
#..|#..
...|...
---+---
##.|##.
#..|#..
...|...
Finally, the squares are joined into a new grid:

##.##.
#..#..
......
##.##.
#..#..
......
Thus, after 2 iterations, the grid contains 12 pixels that are on.

How many pixels stay on after 5 iterations?

Your puzzle answer was 176.

--- Part Two ---
How many pixels stay on after 18 iterations?

Your puzzle answer was 2368161.
 */
public class AdventOfCodeDay21 {

	public static void main(String[] args) throws IOException {
		Map<Matrix, Matrix> rulebook = new HashMap<>();

		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday21.txt";
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" => ");
				Matrix source = new Matrix(split[0]);
				Matrix target = new Matrix(split[1]);
				// create all rotations/flipped roattions in rulebook
				rulebook.put(source, target); // orig
				source = Matrix.rot90(source); // 90
				rulebook.put(source, target);
				source = Matrix.rot90(source);// 180
				rulebook.put(source, target);
				source = Matrix.rot90(source);// 270
				rulebook.put(source, target);
				source = Matrix.flip(source); // flip
				rulebook.put(source, target);
				source = Matrix.rot90(source); // flip90
				rulebook.put(source, target);
				source = Matrix.rot90(source);// flip180
				rulebook.put(source, target);
				source = Matrix.rot90(source); // flip270
				rulebook.put(source, target);
			}
		}
		System.out.println("rulebook finished");

		Matrix initial = new Matrix(".#./..#/###");
		 System.out.println("Initial");
		 initial.prettyPrint();

		for (int iteration = 0; iteration < 18; iteration++) {
			// split start Matrix
			Matrix[][] matrices = initial.split();
			// "improve" parts, replace them by version from rulebook
			for (int i = 0; i < matrices.length; i++) {
				for (int j = 0; j < matrices.length; j++) {
					matrices[i][j] = rulebook.get(matrices[i][j]);
				}
			}
			// combine again
			initial = Matrix.combine(matrices);
			// initial.prettyPrint();
		}
		System.out.println(initial.getActiveCount());
		// iteration 5 : 176 pixels
		// iteration 18: 2368161 pixels
	}
}

/**
 * helper class for printing, managing and storing stuff in hashmap don't want
 * to fiddle around with arrays to much and growing arrays. so i use an array of
 * matrices and grow/replace the matrices with their substitute and later get
 * 
 * @author dridde
 *
 */
class Matrix {
	private final char[][] data; // M-by-N array
	private final int size; // size

	// create M-by-N matrix based on string
	public Matrix(String layout) {
		String[] split = layout.split("/");
		this.size = split.length;
		data = new char[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			data[i] = split[i].toCharArray();
		}
	}

	// create matrix based on 2d array
	public Matrix(char[][] data) {
		this.size = data.length;
		this.data = new char[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				this.data[i][j] = data[i][j];
	}

	// create matrix based on matrix
	public Matrix(Matrix input) {
		this.size = input.size;
		this.data = input.getData().clone();
	}

	public char[][] getData() {
		return data;
	}

	public int getSize() {
		return size;
	}

	public int getActiveCount() {
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (data[i][j] == '#') {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(data);
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(data, other.data))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	public void prettyPrint() {
		for (int i = 0; i < size; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < size; j++) {
				sb.append(data[i][j]);
			}
			System.out.println(sb.toString());
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				sb.append(data[i][j]);
			}
			if (i != size - 1) {
				sb.append("/");
			}
		}
		return sb.toString();
	}

	/**
	 * split matrix up to array of 2x2 or 3x3 matrix
	 * 
	 * @param initial
	 * @return
	 */
	public Matrix[][] split() {
		int div = 2;
		if (size % 2 == 0) {
			div = 2;
		} else if (size % 3 == 0) {
			div = 3;
		}
		int i = this.size / div;
		Matrix[][] matrices = new Matrix[i][i];

		char[][] newData = new char[div][div];
		// create part data arrays for new matrices
		for (int a = 0; a < i; a++) {
			for (int b = 0; b < i; b++) {
				// create actual part-matrix
				for (int c = 0; c < div; c++) {
					for (int d = 0; d < div; d++) {
						newData[c][d] = this.data[(a * div) + c][(b * div) + d];
					}
				}
				matrices[a][b] = new Matrix(newData);
			}
		}
		return matrices;
	}

	/**
	 * combine matrix array to one new big matrix
	 * 
	 * @param matrices
	 * @return
	 */
	public static Matrix combine(Matrix[][] matrices) {
		int n = matrices.length;
		int m = matrices[0][0].getSize();
		char[][] newInput = new char[n * m][n * m];
		// matrixarray
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < n; b++) {
				// matrix
				char[][] data = matrices[a][b].getData();
				for (int c = 0; c < m; c++) {
					for (int d = 0; d < m; d++) {
						newInput[(a * m) + c][(b * m) + d] = data[c][d];
					}
				}
			}
		}
		return new Matrix(newInput);
	}

	/**
	 * rotate matrix 90deg
	 * 
	 * @param input
	 * @return
	 */
	public static Matrix rot90(Matrix input) {
		int n = input.getSize();
		char[][] data = input.getData();
		char[][] newArray = new char[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				newArray[i][j] = data[n - j - 1][i];
			}
		}
		return new Matrix(newArray);
	}

	/**
	 * flip matrix on middle axis
	 * 
	 * @param input
	 * @return
	 */
	public static Matrix flip(Matrix input) {
		char[][] data = input.getData();
		char[][] out = new char[data.length][data.length];
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data.length; y++) {
				out[(data.length - 1) - x][y] = data[x][y];
			}
		}
		return new Matrix(out);
	}
}
