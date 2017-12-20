package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/*
 * --- Day 16: Permutation Promenade ---
You come upon a very unusual sight; a group of programs here appear to be dancing.

There are sixteen programs in total, named a through p. They start by standing in a line: a stands in position 0, b stands in position 1, and so on until p, which stands in position 15.

The programs' dance consists of a sequence of dance moves:

Spin, written sX, makes X programs move from the end to the front, but maintain their order otherwise. (For example, s3 on abcde produces cdeab).
Exchange, written xA/B, makes the programs at positions A and B swap places.
Partner, written pA/B, makes the programs named A and B swap places.
For example, with only five programs standing in a line (abcde), they could do the following dance:

s1, a spin of size 1: eabcd.
x3/4, swapping the last two programs: eabdc.
pe/b, swapping programs e and b: baedc.
After finishing their dance, the programs end up in order baedc.

You watch the dance for a while and record their dance moves (your puzzle input). In what order are the programs standing after their dance?

Your puzzle answer was kgdchlfniambejop.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
Now that you're starting to get a feel for the dance moves, you turn your attention to the dance as a whole.

Keeping the positions they ended up in from their previous dance, the programs perform it again and again: including the first dance, a total of one billion (1000000000) times.

In the example above, their second dance would begin with the order baedc, and use the same dance moves:

s1, a spin of size 1: cbaed.
x3/4, swapping the last two programs: cbade.
pe/b, swapping programs e and b: ceadb.
In what order are the programs standing after their billion dances?
 */
public class AdventOfCodeDay16 {

	public static void main(String[] args) throws IOException {
		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday16.txt";
		File file = new File(inputFilelocation);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String input = br.readLine();
		br.close();
		String[] operations = input.split(",");

		Map<String, String> memory = new HashMap<>();
		char[] programs = "abcdefghijklmnop".toCharArray();
		Instant start = Instant.now();

		for (int i = 1; i <= 1000000000; i++) {
			String startSeq = String.valueOf(programs);
			if (memory.containsKey(startSeq)) {
				//check if we found the cycle
				if ("abcdefghijklmnop".equals(startSeq) && i != 1) {
					System.out.println("Hit! Cycle Length: " + (i-1));
					//jump a few hundred million steps ahead ;)
					i = 1000000000 - (1000000000 % (i-1))+1;
				}
				programs = memory.get(startSeq).toCharArray();
				continue;
			}
			
			for (String operation : operations) {
				String type = operation.substring(0, 1);
				switch (type) {
				case "s":
					programs = spin(operation, programs);
					break;
				case "p":
					programs = partner(operation, programs);
					break;
				case "x":
					programs = exchange(operation, programs);
					break;
				default:
					break;
				}
			}
			memory.put(startSeq, String.valueOf(programs));
			if (i == 1) {
				Instant tick = Instant.now();
				System.out.println("Result sequence part 1: " + String.valueOf(programs) + " Time: " + Duration.between(start, tick));
			}
		}
		Instant end = Instant.now();
		System.out.println("Result sequence part 2: " + String.valueOf(programs) + " Time: " + Duration.between(start, end));

		// part 1 : kgdchlfniambejop
		// part 2 : fjpmholcibdgeakn
	}

	private static char[] exchange(String operation, char[] progs) {
		String[] indices = operation.substring(1).split("/");
		swap(progs, Integer.parseInt(indices[0]), Integer.parseInt(indices[1]));
		return progs;
	}

	private static char[] partner(String operation, char[] progs) {
		String[] prognames = operation.substring(1).split("/");
		String progStr = String.valueOf(progs);
		swap(progs, progStr.indexOf(prognames[0]), progStr.indexOf(prognames[1]));
		return progs;
	}

	private static void swap(char[] progs, int prog1index, int prog2index) {
		char helper = progs[prog1index];
		progs[prog1index] = progs[prog2index];
		progs[prog2index] = helper;
	}

	private static char[] spin(String operation, char[] progs) {
		int index = Integer.parseInt(operation.substring(1));
		String progStr = String.valueOf(progs);
		String result = progStr.substring(progStr.length() - index) + progStr.substring(0, progStr.length() - index);
		return result.toCharArray();
	}

}
