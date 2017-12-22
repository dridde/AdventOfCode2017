package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCodeDay22part1 {

	public static void main(String[] args) throws IOException {
		Map<Coord, Character> playfield = new HashMap<>();
		List<String> input = new ArrayList<>();

		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday22.txt";
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				input.add(line);
			}
		}
		// find middle, fill playfield
		int xMin = Math.floorDiv(input.get(0).length(), 2);
		int yMin = Math.floorDiv(input.size(), 2);
		for (int y = 0; y < input.size(); y++) {
			char[] charArray = input.get(y).toCharArray();
			for (int x = 0; x < input.get(0).length(); x++) {
				if (charArray[x] == '#') {
					playfield.put(new Coord(x - xMin, -(y - yMin)), '#');
				}
			}
		}
		// System.out.println(playfield);
		long infectCount = 0;
		Coord carrier = new Coord(0, 0);
		char dir = 'u';

		// do all the things
		for (int burst = 0; burst < 10000; burst++) {
			// printField(playfield, carrier, dir);
			dir = turn(playfield, carrier, dir);
			if (playfield.containsKey(carrier)) {
				playfield.remove(carrier);
			} else {
				playfield.put(new Coord(carrier.x, carrier.y), '#');
				infectCount++;
			}
			move(carrier, dir);
		}
		System.out.println(infectCount);

	}

	public static void printField(Map<Coord, Character> playfield, Coord carrier, char dir) {
		long maxX = carrier.x;
		long minX = carrier.x;
		long maxY = carrier.y;
		long minY = carrier.y;
		for (Coord c : playfield.keySet()) {
			if (c.x > maxX)
				maxX = c.x;
			if (c.x < minX)
				minX = c.x;
			if (c.y > maxY)
				maxY = c.y;
			if (c.y < minY)
				minY = c.y;
		}
		for (long y = maxY; y >= minY; y--) {
			StringBuilder sb = new StringBuilder();
			for (long x = minX; x <= maxX; x++) {
				Coord curr = new Coord(x, y);
				if (curr.equals(carrier)) {
					sb.append("[");
				} else {
					sb.append(" ");
				}
				if (playfield.containsKey(curr)) {
					sb.append("#");
				} else {
					sb.append(".");
				}
				if (curr.equals(carrier)) {
					sb.append("]");
				} else {
					sb.append(" ");
				}
			}
			System.out.println(sb.toString());
		}
		System.out.println("dir: " + dir + " ---");

	}

	private static char turn(Map<Coord, Character> playfield, Coord carrier, char dir) {
		if (playfield.containsKey(carrier)) {
			// node is infected, turn right
			switch (dir) {
			case 'u':
				dir = 'r';
				break;
			case 'r':
				dir = 'd';
				break;
			case 'd':
				dir = 'l';
				break;
			case 'l':
				dir = 'u';
				break;
			default:
				break;
			}
		} else {
			// node clean, turn left
			switch (dir) {
			case 'u':
				dir = 'l';
				break;
			case 'r':
				dir = 'u';
				break;
			case 'd':
				dir = 'r';
				break;
			case 'l':
				dir = 'd';
				break;
			default:
				break;
			}
		}
		return dir;
	}

	private static void move(Coord carrier, char dir) {
		switch (dir) {
		case 'u':
			carrier.y++;
			break;
		case 'd':
			carrier.y--;
			break;
		case 'l':
			carrier.x--;
			break;
		case 'r':
			carrier.x++;
			break;
		default:
			break;
		}
	}

}

class Coord {
	long x, y;

	public Coord(long x, long y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (x ^ (x >>> 32));
		result = prime * result + (int) (y ^ (y >>> 32));
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
		Coord other = (Coord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + x + "; " + y + "]";
	}

}
