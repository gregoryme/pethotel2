package me.readln.petshotel.junever.generate;

public class RandomIndex {

    public RandomIndex() {
    }

    public int getIndex (int maximalIndex) {
        int index = (int) (Math.random() * ++maximalIndex);
        return (index > maximalIndex) ? 0 : index; // @ToDo refactor
    }
}
