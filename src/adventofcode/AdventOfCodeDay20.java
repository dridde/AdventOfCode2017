package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCodeDay20 {

	public static void main(String[] args) throws NumberFormatException, IOException {

		List<Particle> particles = new ArrayList<>();

		// read input
		String inputFilelocation = "/Users/dridde/git/AdventOfCode2017/src/adventofcode/inputday20.txt";
		File file = new File(inputFilelocation);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			long i = 0;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(", ");
				String substring = split[0].substring(3, split[0].length()-1);
				String[] values = substring.split(",");
				Tripel pos = new Tripel(Long.parseLong(values[0]), Long.parseLong(values[1]), Long.parseLong(values[2]));
				substring = split[1].substring(3, split[1].length()-1);
				values = substring.split(",");
				Tripel vel = new Tripel(Long.parseLong(values[0]), Long.parseLong(values[1]), Long.parseLong(values[2]));
				substring = split[2].substring(3, split[2].length()-1);
				values = substring.split(",");
				Tripel acc = new Tripel(Long.parseLong(values[0]), Long.parseLong(values[1]), Long.parseLong(values[2]));

				particles.add(new Particle("particle" + i, pos, vel, acc));
				i++;
			}
			br.close();
		}

		long stableCounter = 0;
		String lastMinDistName = "";
		String minDistName = "";
		// tick until minDistParticle is stable for a given amount of ticks
		while (true) {
			long minDist = Long.MAX_VALUE;
			// tick all particles
			for (Particle p : particles) {
				p.tick();
				// check minDist per tick
				if (p.getDistanceToOrigin() < minDist) {
					minDist = p.getDistanceToOrigin();
					minDistName = p.getpName();
				}
			}
			//check for collisions
			// TODO
			
			if (lastMinDistName.equals(minDistName)) {
				stableCounter++;
			} else {
				// we have a new contender
				stableCounter = 0;
				lastMinDistName = minDistName;
			}
			// break if we are stable
			if (stableCounter == 10000) {
				break;
			}
		}
		System.out.println(lastMinDistName);
		// 100 particle241
		// 1000 particle376
		// 10000 particle376
		// 100000 particle376
		// 1000000 particle376
	}

}

class Particle {
	Tripel pos;
	Tripel vel;
	Tripel acc;
	String pName;

	public Particle(String pName, Tripel pos, Tripel vel, Tripel acc) {
		this.pos = pos;
		this.vel = vel;
		this.acc = acc;
		this.pName = pName;
	}

	/**
	 * Increase the X velocity by the X acceleration. 
	 * Increase the Y velocity by the Y acceleration. 
	 * Increase the Z velocity by the Z acceleration.
	 * Increase the X position by the X velocity. 
	 * Increase the Y position by the Y velocity. 
	 * Increase the Z position by the Z velocity.
	 */
	void tick() {
		vel.add(acc);
		pos.add(vel);
	}

	public String getpName() {
		return pName;
	}

	long getDistanceToOrigin() {
		return pos.getManhattenDist();
	}
}

class Tripel {
	long x, y, z;

	public Tripel(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tripel) {
			Tripel t = (Tripel) obj;
			return (this.x == t.x && this.y == t.y && this.z == t.z) ? true : false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public void add(Tripel t) {
		this.x += t.x;
		this.y += t.y;
		this.z += t.z;
	}
	
	public long getManhattenDist() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}
}
