package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * --- Day 20: Particle Swarm ---
Suddenly, the GPU contacts you, asking for help. Someone has asked it to simulate too many particles, and it won't be able to finish them all in time to render the next frame at this rate.

It transmits to you a buffer (your puzzle input) listing each particle in order (starting with particle 0, then particle 1, particle 2, and so on). For each particle, it provides the X, Y, and Z coordinates for the particle's position (p), velocity (v), and acceleration (a), each in the format <X,Y,Z>.

Each tick, all particles are updated simultaneously. A particle's properties are updated in the following order:

Increase the X velocity by the X acceleration.
Increase the Y velocity by the Y acceleration.
Increase the Z velocity by the Z acceleration.
Increase the X position by the X velocity.
Increase the Y position by the Y velocity.
Increase the Z position by the Z velocity.
Because of seemingly tenuous rationale involving z-buffering, the GPU would like to know which particle will stay closest to position <0,0,0> in the long term. Measure this using the Manhattan distance, which in this situation is simply the sum of the absolute values of a particle's X, Y, and Z position.

For example, suppose you are only given two particles, both of which stay entirely on the X-axis (for simplicity). Drawing the current states of particles 0 and 1 (in that order) with an adjacent a number line and diagram of current X positions (marked in parenthesis), the following would take place:

p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>                         (0)(1)

p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
p=< 2,0,0>, v=<-2,0,0>, a=<-2,0,0>                      (1)   (0)

p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
p=<-2,0,0>, v=<-4,0,0>, a=<-2,0,0>          (1)               (0)

p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
p=<-8,0,0>, v=<-6,0,0>, a=<-2,0,0>                         (0)   
At this point, particle 1 will never be closer to <0,0,0> than particle 0, and so, in the long run, particle 0 will stay closest.

Which particle will stay closest to position <0,0,0> in the long term?

Your puzzle answer was 376.

--- Part Two ---
To simplify the problem further, the GPU would like to remove any particles that collide. Particles collide if their positions ever exactly match. Because particles are updated simultaneously, more than two particles can collide at the same time and place. Once particles collide, they are removed and cannot collide with anything else after that tick.

For example:

p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>    
p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>    (0)   (1)   (2)            (3)
p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>

p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0>    
p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0>             (0)(1)(2)      (3)   
p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>

p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0>    
p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0>                       X (3)      
p=< 1,0,0>, v=<-1,0,0>, a=< 0,0,0>

------destroyed by collision------    
------destroyed by collision------    -6 -5 -4 -3 -2 -1  0  1  2  3
------destroyed by collision------                      (3)         
p=< 0,0,0>, v=<-1,0,0>, a=< 0,0,0>
In this example, particles 0, 1, and 2 are simultaneously destroyed at the time and place marked X. On the next tick, particle 3 passes through unharmed.

How many particles are left after all collisions are resolved?

Your puzzle answer was 574.


 */
public class AdventOfCodeDay20 {

	public static void main(String[] args) throws IOException {

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
			
			// check for collisions for part 2
			Set<Particle> removeSet = new HashSet<>();
			for (Particle p1 : particles) {
				for (Particle p2 : particles) {
					if (!p1.getpName().equals(p2.getpName())) {
						if (p1.getPos().equals(p2.getPos())) {
							removeSet.add(p1);
							removeSet.add(p2);
						}
					}
				}
			}
			//remove collided particles
			particles.removeAll(removeSet);
			
			if (lastMinDistName.equals(minDistName)) {
				stableCounter++;
			} else {
				// we have a new contender
				stableCounter = 0;
				lastMinDistName = minDistName;
			}
			// break if we are stable
			if (stableCounter == 1000) {
				break;
			}
		}
		System.out.println(lastMinDistName);
		// 100 particle241
		// 1000 particle376
		// 10000 particle376
		// 100000 particle376
		// 1000000 particle376
		System.out.println(particles.size());
		// 100 574
	}

}

class Particle {
	private Tripel pos;
	private Tripel vel;
	private Tripel acc;
	private String pName;

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

	public Tripel getPos() {
		return pos;
	}
}

class Tripel {
	private long x, y, z;

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
