import java.util.Random;

public class Selkie {
    private int head, tail;
    
    public Selkie()
    {
        Random rand = new Random();
            head = rand.nextInt(25, 97);
            tail = rand.nextInt(4, head-5);
    }

    /**
     * get the head
     * @return head
     */
    public int getHead()
    {
        return head;
    }

    /**
     * Get the tail
     * @return tail
     */
    public int getTail()
    {
        return tail;
    }

    /**
     * Set the head
     * @param head
     */
    public void setHead(int head)
    {
        this.head = head;
    }

    /**
     * Set the tail
     * @param tail
     */
    public void setTail(int tail)
    {
        this.tail = tail;
    }
}
