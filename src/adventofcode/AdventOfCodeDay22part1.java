package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * --- Day 22: Sporifica Virus ---
Diagnostics indicate that the local grid computing cluster has been contaminated with the Sporifica Virus. The grid computing cluster is a seemingly-infinite two-dimensional grid of compute nodes. Each node is either clean or infected by the virus.

To prevent overloading the nodes (which would render them useless to the virus) or detection by system administrators, exactly one virus carrier moves through the network, infecting or cleaning nodes as it moves. The virus carrier is always located on a single node in the network (the current node) and keeps track of the direction it is facing.

To avoid detection, the virus carrier works in bursts; in each burst, it wakes up, does some work, and goes back to sleep. The following steps are all executed in order one time each burst:

If the current node is infected, it turns to its right. Otherwise, it turns to its left. (Turning is done in-place; the current node does not change.)
If the current node is clean, it becomes infected. Otherwise, it becomes cleaned. (This is done after the node is considered for the purposes of changing direction.)
The virus carrier moves forward one node in the direction it is facing.
Diagnostics have also provided a map of the node infection status (your puzzle input). Clean nodes are shown as .; infected nodes are shown as #. This map only shows the center of the grid; there are many more nodes beyond those shown, but none of them are currently infected.

The virus carrier begins in the middle of the map facing up.

For example, suppose you are given a map like this:

..#
#..
...
Then, the middle of the infinite grid looks like this, with the virus carrier's position marked with [ ]:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . . #[.]. . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The virus carrier is on a clean node, so it turns left, infects the node, and moves left:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . .[#]# . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The virus carrier is on an infected node, so it turns right, cleans the node, and moves up:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . .[.]. # . . .
. . . . # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Four times in a row, the virus carrier finds a clean, infects it, turns left, and moves forward, ending in the same place and still facing up:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . #[#]. # . . .
. . # # # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Now on the same node as before, it sees an infection, which causes it to turn right, clean the node, and move forward:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . # .[.]# . . .
. . # # # . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
After the above actions, a total of 7 bursts of activity had taken place. Of them, 5 bursts of activity caused an infection.

After a total of 70, the grid looks like this, with the virus carrier facing up:

. . . . . # # . .
. . . . # . . # .
. . . # . . . . #
. . # . #[.]. . #
. . # . # . . # .
. . . . . # # . .
. . . . . . . . .
. . . . . . . . .
By this time, 41 bursts of activity caused an infection (though most of those nodes have since been cleaned).

After a total of 10000 bursts of activity, 5587 bursts will have caused an infection.

Given your actual map, after 10000 bursts of activity, how many bursts cause a node to become infected? (Do not count nodes that begin infected.)

Your puzzle answer was 5196.

--- Part Two ---
As you go to remove the virus from the infected nodes, it evolves to resist your attempt.

Now, before it infects a clean node, it will weaken it to disable your defenses. If it encounters an infected node, it will instead flag the node to be cleaned in the future. So:

Clean nodes become weakened.
Weakened nodes become infected.
Infected nodes become flagged.
Flagged nodes become clean.
Every node is always in exactly one of the above states.

The virus carrier still functions in a similar way, but now uses the following logic during its bursts of action:

Decide which way to turn based on the current node:
If it is clean, it turns left.
If it is weakened, it does not turn, and will continue moving in the same direction.
If it is infected, it turns right.
If it is flagged, it reverses direction, and will go back the way it came.
Modify the state of the current node, as described above.
The virus carrier moves forward one node in the direction it is facing.
Start with the same map (still using . for clean and # for infected) and still with the virus carrier starting in the middle and facing up.

Using the same initial state as the previous example, and drawing weakened as W and flagged as F, the middle of the infinite grid looks like this, with the virus carrier's position again marked with [ ]:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . . #[.]. . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
This is the same as before, since no initial nodes are weakened or flagged. The virus carrier is on a clean node, so it still turns left, instead weakens the node, and moves left:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . # . . .
. . .[#]W . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The virus carrier is on an infected node, so it still turns right, instead flags the node, and moves up:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . .[.]. # . . .
. . . F W . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
This process repeats three more times, ending on the previously-flagged node and facing right:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . W W . # . . .
. . W[F]W . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Finding a flagged node, it reverses direction and cleans the node:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . W W . # . . .
. .[W]. W . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
The weakened node becomes infected, and it continues in the same direction:

. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
. . W W . # . . .
.[.]# . W . . . .
. . . . . . . . .
. . . . . . . . .
. . . . . . . . .
Of the first 100 bursts, 26 will result in infection. Unfortunately, another feature of this evolved virus is speed; of the first 10000000 bursts, 2511944 will result in infection.

Given your actual map, after 10000000 bursts of activity, how many bursts cause a node to become infected? (Do not count nodes that begin infected.)

Your puzzle answer was 2511633.
 */
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
