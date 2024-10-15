import java.util.Random;

public class Munro {
    private int tip, foot;
    
    public Munro()
    {
        Random rand = new Random();
            tip = rand.nextInt(25, 97);
            foot = rand.nextInt(4, tip-5);
    }

    /**
     * Get the tip
     * @return tip
     */
    public int getTip()
    {
        return tip;
    }

    /**
     * Get the foot
     * @return foot
     */
    public int getFoot()
    {
        return foot;
    }

    /**
     * Set the tip
     * @param tip
     */
    public void setTip(int tip)
    {
        this.tip = tip;
    }

    /**
     * Set the foot
     * @param foot
     */
    public void setFoot(int foot)
    {
        this.foot = foot;
    }
}

