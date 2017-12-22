package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdventOfCodeDay21 {

	public static void main(String[] args) throws IOException {
		Map<Matrix, Matrix> rulebook = new HashMap<>();

		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday20.txt";
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" => ");
				Matrix target = new Matrix(split[1]);
				Matrix sourceOrig = new Matrix(split[0]);
				Matrix source90 = rotate90(sourceOrig);
				Matrix source180 = rotate90(source90);
				Matrix source270 = rotate90(source180);
				Matrix sourceFlip = flip(sourceOrig);
				Matrix sourceFlip90 = rotate90(sourceFlip);
				Matrix sourceFlip180 = rotate90(sourceFlip90);
				Matrix sourceFlip270 = rotate90(sourceFlip180);
				rulebook.put(sourceOrig, target);
				rulebook.put(source90, target);
				rulebook.put(source180, target);
				rulebook.put(source270, target);
				rulebook.put(sourceFlip, target);
				rulebook.put(sourceFlip90, target);
				rulebook.put(sourceFlip180, target);
				rulebook.put(sourceFlip270, target);
			}
		}

		System.out.println("rulebook finished");
		Matrix initial = new Matrix(".#./..#/###");
		System.out.println(initial);

		for (int i = 0; i < 5; i++) {
			// TODO split start Matrix
			// TODO "improve" parts
			// TODO combine again
		}
		System.out.println(initial.getActiveCount());
	}

	private static Matrix flip(Matrix input) {
		Matrix output = new Matrix(input);
		return output;
	}

	private static Matrix rotate90(Matrix input) {
		Matrix output = new Matrix(input);
		return output;
	}
}

class Matrix {
	private final char[][] data; // M-by-N array
	private final int n; // size

	// create M-by-N matrix based on string
	public Matrix(String layout) {
		String[] split = layout.split("//");
		this.n = split.length;
		data = new char[this.n][this.n];
		for (int i = 0; i < this.n; i++) {
			data[i] = split[i].toCharArray();
		}
	}

	// create matrix based on 2d array
	public Matrix(char[][] data) {
		this.n = data.length;
		this.data = new char[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				this.data[i][j] = data[i][j];
	}

	// create matrix based on matrix
	public Matrix(Matrix input) {
		this.n = input.n;
		this.data = new char[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				this.data[i][j] = input.data[i][j];
	}

	public int getActiveCount() {
		int count = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
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
		result = prime * result + n;
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
		if (n != other.n)
			return false;
		return true;
	}

}
