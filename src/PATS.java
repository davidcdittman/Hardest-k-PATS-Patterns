import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.math.BigInteger;

public class PATS {

	public PATS(int m, int n, int k) throws IOException 
	{

		// m and n are dimensions for pattern (excluding starting L configuration)
		this.m = m;
		this.n = n;
		this.k = k;
		
		//Maximum glue values for e/w and n/s glues based on worst case tile set.
		max_ew_val = m * (n - 1);
		max_ns_val = n * (m - 1);
		
		//Number of tile permutations based on color, north/south and east/west glues.
		num_tiles = k * (int) Math.pow(max_ns_val, 2) * (int) Math.pow(max_ew_val, 2);
		
		//Create a new instance of the pattern class for our pattern size.
		pats = new Pattern(m, n, k);
		
		//Instantiate our array which holds tile sets at each pattern (index is value for pattern)
		patterns = new ArrayList[pats.num_pats()];
		
		// Create new instance of Matrix class, giving extra padding for starting L configuration
		mat = new Matrix(m + 1, n + 1);
		
		//Workhorse function which generates a set, verifies any unique pattern assembly, and stores results in patterns array.
		generate_sets();
		
		//Holds the pattern indices for the hardest patterns.
		ArrayList<Integer> result = findHardestPatterns();
		
		//Write results to a file.
		String filename = "results_" + m + "x" + n;
		writer = new PrintWriter(filename, "UTF-8");
		writeToFile(result);
		writer.close();
	}


	private void generate_sets() 
	{
		System.out.println("Generating sets...");
		
		//Loop through tile set sizes from size 1 (i = 0) to our worst case tile set size, m * n (i = m * n -1)
		for (BigInteger i = new BigInteger("0"); i.compareTo(BigInteger.valueOf(m * n)) < 0; i = i.add(BigInteger.valueOf(1)))
		{
			//num holds unique tile set value. Each tile has a value from 0 to num_tiles - 1 which dictates its color and glue values.
			//num will have a value based on the size of the tile set we are generating. Each tile value can then be extracted from num.
			//num = num_tiles^(i+1)
			BigInteger num = new BigInteger("0");
			num = num.add(BigInteger.valueOf(num_tiles));
			num = num.pow((i.add(BigInteger.valueOf(1))).intValue());
			
			System.out.println(num.toString());
			
			//temp = num_tiles^(i)
			BigInteger temp = new BigInteger("0");
			temp = temp.add(BigInteger.valueOf(num_tiles));
			temp = temp.pow(i.intValue());
			
			//Loop over the number of possible sets, continuing from the last iteration of i.
			for (BigInteger j = temp; j.compareTo(num) < 0; j = j.add(BigInteger.valueOf(1))) 
			{
				if(j.mod(BigInteger.valueOf(num_tiles)).compareTo(BigInteger.valueOf(0)) == 0)
					System.out.println("i: " + i.toString() + " j: " + j.toString() + " num: " + num.toString());
				
				//variable used to communicate between nested loops.
				boolean cont = false;

				//Instantiate our new set.
				ArrayList<Tile> set = new ArrayList<>();
				
				//Grab a copy of the value of the set for us to modify
				BigInteger cJ = j;
				
				//Loop through the size of the set, creating and adding tiles every iteration.
				do 
				{
					//Grab the overall tile value.
					BigInteger vals = cJ.mod(BigInteger.valueOf(num_tiles));
					
					//Calculate the specific values of the tile
					//Formula follows:
					//(property) = vals % (maximum property value)
					//vals /= (maximum property value)
					int north = (vals.mod(BigInteger.valueOf(max_ns_val))).intValue();
					vals = vals.divide(BigInteger.valueOf(max_ns_val));
					int west = (vals.mod(BigInteger.valueOf(max_ew_val))).intValue();
					vals = vals.divide(BigInteger.valueOf(max_ew_val));
					int south = (vals.mod(BigInteger.valueOf(max_ns_val))).intValue();
					vals = vals.divide(BigInteger.valueOf(max_ns_val));
					int east = (vals.mod(BigInteger.valueOf(max_ew_val))).intValue();
					vals = vals.divide(BigInteger.valueOf(max_ew_val));
					int color = (vals.mod(BigInteger.valueOf(k)).intValue());

					//Create a new tile with given values.
					Tile tile = new Tile(north, south, east, west, color);
					
					//Loop through the set and enforce condition that no two tiles in a set can have the same south and west glue values.
					//If the condition isn't met, set cont(continue) to true and break
					for (int l = 0; l < set.size(); l++) 
					{
						if (set.get(l).getW() == tile.getW() && set.get(l).getS() == tile.getS()) 
						{
							//System.out.println("True");
							cont = true;
							break;
						}
					}
					
					//If the matching south and west glue condition isn't met, discontinue the generation of this set.
					if (cont) 
					{
						break;
					}
					//Otherwise, add the tile to the set and alter k for the next tile's values (if the set isn't full)
					
					set.add(tile);
					
					cJ = cJ.divide(BigInteger.valueOf(num_tiles));

				} while (cJ.compareTo(BigInteger.valueOf(1)) >= 0);
				
				//If the matching south and west glue condition wasn't met, continue to the next set.
				if (cont)
					continue;
				
				//grab the pattern that the set uniquely generates (if any)
				//If no pattern is generates, simulate() returns -1
				int result = simulate(set);
				
				//reset our matrix for the next simulation.
				mat.reset();

				//If there is a result, add the set to the patterns array if there isn't already a set associated with this pattern.
				if (result != -1) 
				{
					if (patterns[result] == null) 
					{
						patterns[result] = set;
						
						//Helpful output to see when a set is stored.
						System.out.println("Pattern: " + result + " set size " + set.size() + " index: " + i);
					} 
				}
			}
		}
	}

	private int simulate(ArrayList<Tile> set) 
	{
		//The pattern that this set generates (if any).
		int pattern = 0;
		
		//Loop over x, y axes
		for (int y = 0; y < n; y++) 
		{
			for (int x = 0; x < m; x++) 
			{
				//Loop over tile set looking for matching glues.
				for (int k = 0; k < set.size(); k++) 
				{
					//if tile can be placed at (x, y)
					if (mat.cmpW(set.get(k), x, y) && mat.cmpS(set.get(k), x, y)) 
					{
						//Place the tile in the matrix
						mat.setXY(set.get(k), x, y);
						
						//Add to the pattern the position and value of the color
						pattern += pats.getPos(x, y) * set.get(k).getColor();
						
						break;						
					}
				}
				//If no tile was placed at (x, y), return -1
				if (mat.getTile(x, y).getColorChar() == 'e')
					return -1;
			}
		}

		// if the assembly has finished without error, return the pattern.
		return pattern;
	}

	private ArrayList<Integer> findHardestPatterns() 
	{
		System.out.println("finding hardest patterns");
		ArrayList<Integer> hardestPatterns = new ArrayList<>();
		hardestPatterns.add(0);
		
		//Loop through all patterns and find the maximum size in patterns[].
		//If two patterns have the same size, both are potential solutions.
		for (int i = 0; i < pats.num_pats(); i++)
		{
			
				if (patterns[i] != null && patterns[i].size() > patterns[hardestPatterns.get(0)].size()) 
				{
					hardestPatterns.clear();
					hardestPatterns.add(i);
				}else if(patterns[i] != null && patterns[i].size() == patterns[hardestPatterns.get(0)].size())
				{
					hardestPatterns.add(i);
				}
		}
		
		return hardestPatterns;
	}

	//Write the hardest patterns and associated minimum tile sets to a file.
	private void writeToFile(ArrayList<Integer> result) throws IOException 
	{
		for(int i = 0; i < result.size(); i++)
		{
			for (int j = 0; j < m; j++) 
			{
				for (int k = 0; k < n; k++) 
				{
					if (( result.get(i) & (1 << (m * j + k))) == 0)
						writer.print("w");
					else
						writer.print("b");
					writer.print(" ");
				}
				writer.println();
			}
			for(int l = 0; l < patterns[result.get(i)].size(); l++)
			{
				writer.println("    " + patterns[result.get(i)].get(l).getN());
		        writer.println(" |-----|");
		        writer.println(patterns[result.get(i)].get(l).getW() + "|  " + patterns[result.get(i)].get(l).getColorChar() + "  |" + patterns[result.get(i)].get(l).getE());
		        writer.println(" |-----|");
		        writer.println("    " + patterns[result.get(i)].get(l).getS());
			}
			writer.print("\n\n\n");
		}
	}

	private Matrix mat;
	private Pattern pats;
	private ArrayList<Tile>[] patterns;
	PrintWriter writer;
	private int num_tiles, max_ew_val, max_ns_val, m, n, k;
}
