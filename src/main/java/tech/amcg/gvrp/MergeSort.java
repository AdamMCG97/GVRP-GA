package tech.amcg.gvrp;

public class MergeSort {
    //recursively sort population with mergesort
    public void sort(Route[] array, int length) {
        if (length < 2) {
            return;
        }
        int middle = length / 2;
        Route[] leftArray = new Route[middle];
        Route[] rightArray = new Route[length - middle];

        for (int i = 0; i < middle; i++) {
            leftArray[i] = array[i];
        }
        for (int i = middle; i < length; i++) {
            rightArray[i - middle] = array[i];
        }
        sort(leftArray, middle);
        sort(rightArray, length - middle);

        merge(array, leftArray, rightArray, middle, length - middle);
    }

    public void merge(Route[] array, Route[] leftArray, Route[] rightArray, int left, int right) {

        int rightIndex = 0;
        int leftIndex = 0;
        int arrayIndex = 0;

        while (rightIndex < left && leftIndex < right) {
            if(leftArray[rightIndex] == null) {
                rightIndex = rightIndex + 1;
            }
            else if(rightArray[leftIndex] == null) {
                leftIndex = leftIndex + 1;
            }
            else {
                if (leftArray[rightIndex].getFitness() <= rightArray[leftIndex].getFitness()) {
                    array[arrayIndex] = leftArray[rightIndex];
                    rightIndex = rightIndex + 1;
                } else {
                    array[arrayIndex] = rightArray[leftIndex];
                    leftIndex = leftIndex + 1;
                }
                arrayIndex = arrayIndex + 1;
            }
        }

        while (rightIndex < left) {
            array[arrayIndex] = leftArray[rightIndex];
            arrayIndex = arrayIndex + 1;
            rightIndex = rightIndex + 1;
        }

        while (leftIndex < right) {
            array[arrayIndex] = rightArray[leftIndex];
            arrayIndex = arrayIndex + 1;
            leftIndex = leftIndex + 1;
        }
    }

}
