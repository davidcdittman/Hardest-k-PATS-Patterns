//Each bit in the pattern integer represents that position being either black(1) or white(0). Given a point (x, y) in the matrix, find position of the bit by calculating 2^(ny + x).
public class Pattern
{
    public Pattern(int m, int n, int k)
    {
        this.m = m;
        this.n = n;
        num_pats = (int)Math.pow(k, (m * n));
    }
    //Return the position in a pattern.
    public int getPos(int x, int y)
    {
    	return (1 << (m * y + x));
    }
    public int num_pats(){ return num_pats; };

    //There are 2^(m*n) unique patterns over an n x m matrix.
    private int m, n, num_pats;

}
