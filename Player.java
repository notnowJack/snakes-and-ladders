public class Player {
    private int position;

    public Player()
    {
        position = 0;
    }

    /**
     * Set the player position
     * @param position
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * Get the player position
     * @return position
     */
    public int getPosition()
    {
        return position;
    }
}
