package tech.amcg.gvrp;

public class BinarySearch {
    public boolean bSearch(Route[] array, int first, int last, Route target) {
        boolean result = false;
        int middle;
        if (last > 1) {
            //adapted to search with varying population size during breeding
            if(array[last] == null) {
                last = last - 1;
            }
            //iteratively search array for target individual by fitness
                while (first <= last) {
                    middle = (first + last) / 2;
                    if (array[middle].getFitness() < target.getFitness()) {
                        first = middle + 1;
                    } else if (array[middle].getFitness() > target.getFitness()) {
                        last = middle - 1;
                    } else if (array[middle].getFitness() == target.getFitness()) {
                        result = true;
                        break;
                    }
                }
        }
        return result;
    }
}
