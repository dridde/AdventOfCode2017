package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author dridde
 *
 *--- Day 7: Recursive Circus ---

Wandering further through the circuits of the computer, you come upon a tower of programs that have gotten themselves into a bit of trouble. A recursive algorithm has gotten out of hand, and now they're balanced precariously in a large tower.

One program at the bottom supports the entire tower. It's holding a large disc, and on the disc are balanced several more sub-towers. At the bottom of these sub-towers, standing on the bottom disc, are other programs, each holding their own disc, and so on. At the very tops of these sub-sub-sub-...-towers, many programs stand simply keeping the disc below them balanced but with no disc of their own.

You offer to help, but first you need to understand the structure of these towers. You ask each program to yell out their name, their weight, and (if they're holding a disc) the names of the programs immediately above them balancing on that disc. You write this information down (your puzzle input). Unfortunately, in their panic, they don't do this in an orderly fashion; by the time you're done, you're not sure which program gave which information.

For example, if your list is the following:

pbga (66)
xhth (57)
ebii (61)
havc (66)
ktlj (57)
fwft (72) -> ktlj, cntj, xhth
qoyq (66)
padx (45) -> pbga, havc, qoyq
tknk (41) -> ugml, padx, fwft
jptl (61)
ugml (68) -> gyxo, ebii, jptl
gyxo (61)
cntj (57)
...then you would be able to recreate the structure of the towers that looks like this:

                gyxo
              /     
         ugml - ebii
       /      \     
      |         jptl
      |        
      |         pbga
     /        /
tknk --- padx - havc
     \        \
      |         qoyq
      |             
      |         ktlj
       \      /     
         fwft - cntj
              \     
                xhth
In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft. Those programs are, in turn, holding up other programs; in this example, none of those programs are holding up any other programs, and are all the tops of their own towers. (The actual tower balancing in front of you is much larger.)

Before you're ready to help them, you need to make sure your information is correct. What is the name of the bottom program?


--- Part Two ---

The programs explain the situation: they can't get down. Rather, they could get down, if they weren't expending all of their energy trying to keep the tower balanced. Apparently, one program has the wrong weight, and until it's fixed, they're stuck here.

For any program holding a disc, each program standing on that disc forms a sub-tower. Each of those sub-towers are supposed to be the same weight, or the disc itself isn't balanced. The weight of a tower is the sum of the weights of the programs in that tower.

In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and jptl must all have the same weight, and they do: 61.

However, for tknk to be balanced, each of the programs standing on its disc and all programs above it must each match. This means that the following sums must all be the same:

ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two. Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to be 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change were made, its weight would be 60.

Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?

Although it hasn't changed, you can still get your puzzle input.
 */
public class AdventOfCodeDay7 {

	public static void main(String[] args) throws IOException {
		// read input and build lists and progsMap
		List<String> progNames = new LinkedList<>();
		List<String> progNamesBeingHeld = new LinkedList<>();
		Map<String, Program> progs = new HashMap<>();
		String inputFilelocation = "H:\\git\\AdventOfCode2017\\src\\adventofcode\\inputday7.txt";
		
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				// ugly text parsing stuff. didn't want to do text editor magic for this kind of
				// list and wanted to use helper class anyways
				String[] split = line.split(" ");
				String name = split[0];
				int weight = Integer.parseInt(split[1].replaceAll("\\(", "").replaceAll("\\)", ""));
				List<String> holding = new LinkedList<>();
				if (split.length > 3) {
					for (int i = 3; i < split.length; i++) {
						// get all held programs by name and add them to held list
						String nameComma = split[i];
						String progname = nameComma.replaceAll(",", "");
						progNamesBeingHeld.add(progname);
						holding.add(progname);
					}
				}
				// remember all names 
				progNames.add(name);
				//we'll surely need this for part 2
				progs.put(name, new Program(name, weight, holding));
			}
		}

		// find base program - the one nobody is holding
		progNames.removeAll(progNamesBeingHeld);
		String baseProgram = progNames.get(0);
		System.out.println("Base Program: " + baseProgram);

		// find unbalanced branches
		listWeights(baseProgram, progs);

	}

	/**
	 * list all programs that carry unbalanced weight.
	 * checks all programs being held for unbalanced branches
	 * last level output should contain a program which sum is bigger than the others, but it's carrying nothing or a balanced branches
	 * this prog's weight needs to be changed
	 * since I'm lazy and the code is horrible enough I'll do this by hand ;)
	 * 
	 * @param baseProgram
	 * @param progs
	 * @return 
	 */
	private static int listWeights(String baseProgram, Map<String, Program> progs) {
		int weight = calculateWeight(baseProgram, progs);
		Program program = progs.get(baseProgram);

		// calculate weight and output for all children
		Map<String, Integer> childrenWeight = new HashMap<>();
		for (String children : program.getChildren()) {
			int sum = listWeights(children, progs);
			childrenWeight.put(children, sum);
		}

		// check for unbalanced branch, if we have children
		if (!childrenWeight.isEmpty()) {
			int sumWeight = 0;
			int singleweight = 0;
			for (int childWeight : childrenWeight.values()) {
				sumWeight += childWeight;
				singleweight = childWeight;
			}
			
			//check which program causes imbalance, write output
			if (sumWeight / childrenWeight.size() != singleweight) {
				for (String name : childrenWeight.keySet()) {
					if (childrenWeight.get(name) != singleweight) {
						System.out.println("  Program: " + name + ", OwnWeight: " + progs.get(name).getWeight()
								+ ", SumWeight: " + childrenWeight.get(name) + " Adjust Weight by: "
								+ (singleweight - childrenWeight.get(name)));
					}
				}
			}
		}
		return weight;
	}

	/**
	 * get sum weight of a program.
	 * 
	 * @param baseProgram
	 * @param progs
	 * 
	 * @return own weight plus all children
	 */
	private static int calculateWeight(String baseProgram, Map<String, Program> progs) {
		Program program = progs.get(baseProgram);
		int sum = program.getWeight();
		for (String prog : program.getChildren()) {
			sum += calculateWeight(prog, progs);
		}
		return sum;
	}
}

/**
 * helper class for my prog map
 * 
 * @author dridde
 *
 */
class Program {
	private String name;
	private int weight;
	private List<String> children;

	public Program(String name, int weight, List<String> children) {
		super();
		this.name = name;
		this.weight = weight;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public int getWeight() {
		return weight;
	}

	public List<String> getChildren() {
		return children;
	}

}
