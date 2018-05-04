public class Tile
{
    //Constructor to set all tile values
    //(No setters for private members, tiles values are static)
    public Tile(int n, int s, int e, int w, int color)
    {
        this.n = n;
        this.s = s;
        this.e = e;
        this.w = w;
        this.color = color;
    }

    //Getters for private members
    public int getN() { return n; };
    public int getE() { return e; }
    public int getS() { return s; }
    public int getW() { return w; }
    
    //Return the character of the color value
    public char getColorChar()
    {
        if(color == 0)
            return 'w';
        else if (color == 1)
            return 'b';
        else if (color == -1)
            return 'e';
        else if (color == -2)
            return 's';
        else return '?';
    }
    
    //Return the integer value of the color
    public int getColor()
    {
        return color;
    }
    
    //Useful for testing.
    public void print()
    {
        System.out.println("    " + n);
        System.out.println(" |-----|");
        System.out.println(w + "|  " + getColorChar() + "  |" + e);
        System.out.println(" |-----|");
        System.out.println("    " + s);
    }

    //Tile glue types
    private int n, s, e, w;

    //Label for our tile, in our case, either b(black), w(white), or e(empty)
    private int color;

}
