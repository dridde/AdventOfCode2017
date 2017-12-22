package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCodeDay22part2 {

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
					playfield.put(new Coord(x - xMin, -(y - yMin)), 'I');
				}
			}
		}
		// System.out.println(playfield);
		long infectCount = 0;
		Coord carrier = new Coord(0, 0);
		char dir = 'u';

		// do all the things
		for (int burst = 0; burst < 10000000; burst++) {
			// printField(playfield, carrier, dir);
			dir = turn(playfield, carrier, dir);
			infectCount = infect(playfield, infectCount, carrier);
			move(carrier, dir);
		}
		System.out.println(infectCount);

	}

	private static long infect(Map<Coord, Character> playfield, long infectCount, Coord carrier) {
		if (playfield.containsKey(carrier)) {
			Character node = playfield.get(carrier);
			switch (node) {
			case 'W':
				playfield.put(new Coord(carrier.x, carrier.y), 'I');
				infectCount++;
				break;
			case 'I':
				playfield.put(new Coord(carrier.x, carrier.y), 'F');
				break;
			default:
				playfield.remove(carrier);
				break;
			}
		} else {
			playfield.put(new Coord(carrier.x, carrier.y), 'W');
		}
		return infectCount;
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
					sb.append(playfield.get(curr));
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

	/**
	 * Decide which way to turn based on the current node: If it is clean, it
	 * turns left. If it is weakened, it does not turn, and will continue moving
	 * in the same direction. If it is infected, it turns right. If it is
	 * flagged, it reverses direction, and will go back the way it came.
	 * 
	 * @param playfield
	 * @param carrier
	 * @param dir
	 * @return
	 */
	private static char turn(Map<Coord, Character> playfield, Coord carrier, char dir) {
		if (playfield.containsKey(carrier)) {
			Character node = playfield.get(carrier);
			if (node.equals('I')) {
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
			} else if (node.equals('F')) {
				// node is flagged, reverse direction
				switch (dir) {
				case 'u':
					dir = 'd';
					break;
				case 'r':
					dir = 'l';
					break;
				case 'd':
					dir = 'u';
					break;
				case 'l':
					dir = 'r';
					break;
				default:
					break;
				}

			} else {
				// weakened or anything else, do not turn.
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
