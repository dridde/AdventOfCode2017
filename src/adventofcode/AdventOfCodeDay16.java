package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
