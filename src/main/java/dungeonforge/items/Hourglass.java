package dungeonforge.items;

public class Hourglass implements Item{

    public int getCharges() {
        return charges;
    }

    private int charges;


    public Hourglass(int charges){
        this.charges = charges;
    }

    public boolean hasCharges(){
        return charges > 0;
    }

    public void spendCharges(){
        if(charges > 0) charges--;
    }

    @Override
    public String getName() {
        return "Chronomaster's hourglass";
    }

    @Override
    public double getWeight() {
        return 0.5;
    }

    @Override
    public int getValue() {
        return 250;
    }

    @Override
    public String describe() {
        return getName() + " [" + charges + " charges] (rewinds time)";
    }
}