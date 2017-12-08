package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author dridde
 *
 *--- Day 8: I Heard You Like Registers ---

You receive a signal directly from the CPU. Because of your recent assistance with jump instructions, it would like you to compute the result of a series of unusual register instructions.

Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's value, the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. The registers all start at 0. The instructions look like this:

b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10
These instructions would be processed as follows:

Because a starts at 0, it is not greater than 1, and so b is not modified.
a is increased by 1 (to 1) because b is less than 5 (it is 0).
c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
c is increased by -20 (to -10) because c is equal to 10.
After this process, the largest value in any register is 1.

You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.

What is the largest value in any register after completing the instructions in your puzzle input?

--- Part Two ---

To be safe, the CPU also needs to know the highest value held in any register during this process so that it can decide how much memory to allocate to these operations. For example, in the above instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).

 */
public class AdventOfCodeDay8 {

	private static Map<String, Integer> registers = new HashMap<>();
	private static int allTimeHigh = Integer.MIN_VALUE;

	public static void main(String[] args) throws IOException {
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday8.txt";
		
		//read input
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				parseLine(line);
			}
		}
		
		//check for biggest value in registers
		int max = Integer.MIN_VALUE;
		for (Integer value : registers.values()) {
			if (value > max) max = value;
		}
		System.out.println("Max value after run: " + max);
		System.out.println("Max alltime value: " + allTimeHigh);
		
	}

	private static void parseLine(String line) {
		//System.out.println("Line is: " + line);
		String[] split = line.split(" ");
		String registerName = split[0];
		String operation = split[1];
		int value = Integer.parseInt(split[2]);
		
		//check condition
		String checkReg = split[4];
		String checkCond = split[5];
		int checkValue = Integer.parseInt(split[6]);
		
		int currRegValue = getRegister(registerName);
		if (doit(checkReg, checkCond, checkValue)){
			//System.out.println("  Condition met!");
			if ("inc".equals(operation)) {
				currRegValue += value;
			} else {
				currRegValue -= value;
			}
		}
		if (currRegValue > allTimeHigh) allTimeHigh = currRegValue;
		registers.put(registerName, currRegValue);
	}

	private static boolean doit(String checkReg, String checkCond, int checkValue) {
		boolean doit = false;
		int checkRegVal = getRegister(checkReg);
		//System.out.println("  " + checkRegVal + " " + checkCond + " " + checkValue);
		switch (checkCond) {
		case "==":
			if (checkRegVal == checkValue)
				doit = true;
			break;
		case "!=":
			if (checkRegVal != checkValue)
				doit = true;
			break;
		case "<=":
			if (checkRegVal <= checkValue)
				doit = true;
			break;
		case ">=":
			if (checkRegVal >= checkValue)
				doit = true;
			break;
		case "<":
			if (checkRegVal < checkValue)
				doit = true;
			break;
		case ">":
			if (checkRegVal > checkValue)
				doit = true;
			break;
		default:
			break;
		}
		return doit;
	}

	static int getRegister(String regName){
		if (!registers.containsKey(regName)) {
			//System.out.println("-->Register " + regName + " not found, creating with initial value 0!");
			registers.put(regName, 0);
		}
		return registers.get(regName);
	}
}
