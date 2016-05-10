package pl.dom3k.picipolodelux;

/**
 * Created by domek on 10.05.16.
 */
class UnderCard{
    public int left;
    public int right;
    public UnderCard(){
        left=GameState.getAction(1);
        right=GameState.getAction(2);
    }
}

public class GameState{
    int redPoints = 0;
    int bluePoints= 0;
    int turns = 1;
    public static final int ADD = 1;
    public static final int SUB = 2;
    public static final int MUL = 3;
    public static final int DIV = 4;
    public static final int POW = 10;
    public static final int SIL = 20;


    /**
     *
     * @param whichChosen
     */
    static public int getAction(int whichChosen){
        int random = (int) (Math.random()*100);
        if   (random%25==0) return POW;
        else if(random==99) return SIL;
        else
            return (random%4+1);
    }


}
