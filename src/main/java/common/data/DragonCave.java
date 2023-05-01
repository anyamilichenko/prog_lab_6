package common.data;

public class DragonCave {
    private Double depth; //Поле может быть null
    private float numberOfTreasures; //Значение поля должно быть больше 0


    public DragonCave(Double depth, float numberOfTreasures){
        this.depth = depth;
        this.numberOfTreasures = numberOfTreasures;
    }
    @Override
    public String toString() {
        return "Глубина: " + depth +
                ", количество сокровищ: " + numberOfTreasures;
    }
}
