import java.util.Random;

public class Nessie {
    private int space;
    
    public Nessie()
    {
        Random rand = new Random();
        space = rand.nextInt(2, 95);
    }

    /**
     * Get the space
     * @return space
     */
    public int getSpace()
    {
        return space;
    }

    /**
     * Set the space
     * @param space
     */
    public void setSpace(int space)
    {
        this.space = space;
    }
}

