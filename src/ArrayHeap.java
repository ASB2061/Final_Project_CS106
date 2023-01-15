import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
/**
 *
 * This lab takes in candidates from a CSV file and  creates an arrayheap and removes and prints the
 * elements of maximum priority from that list one by one. This main class
 * also utilizes heapsort with an unsorted array and prints the result
 * of the application.
 */
public class ArrayHeap<E extends Comparable<E>> implements PriorityQueue<E> {
    private ArrayList<E> heap;

    public ArrayHeap() {
        this.heap = new ArrayList<E>();
    }

    /***
     * This method removes the root, checks to see if the leafs are in the correct order, and if not swaps them.
     * @return the old root
     */
    public E removeMax() {
        if(size() == 2){
            E max = heap.get(1);
            heap.remove(max);
            return max;
        }
        if(size() == 1){
            return heap.get(0);
        }
        E oldRoot = heap.get(0);
        swap(0, heap.indexOf(heap.get(size() - 1)));
        heap.remove(size()-1);
        bubbleDown(0);
        return oldRoot;
    }

    public E removeMin(){
        E oldRoot = heap.get(0);
        swap(0,heap.indexOf(heap.get(size()-1)));
        heap.remove(size()-1);
        bubbleUp(0);
        return oldRoot;
    }
    /**
     * This finds the size of the arrayheap/arraylist
     *
     * @return the int number size
     */
    public int size() {
        return heap.size();
    }

    /**
     * This checks to see if arrayheap is empty or not.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Inserts the inputted element at to the end and then making sure it maintains heap property.
     *
     * @param element
     */
    public void insert(E element) {
        heap.add(element);
        bubbleUp(heap.size() - 1);
    }

    /**
     * This method finds the parent using the index of the element queried
     *
     * @param i this represents the index of the element queried
     * @return returns the parent's index
     */
    private int findParent(int i) {
        int index = (int) Math.floor(((i - 1) / 2));
        if (index < 0) {
            return 0;
        }
        return index;
    }

    /**
     * This method finds the left child's index
     *
     * @param i represents the queried element's index
     * @return the index of the leftchild
     */
    private int findLeftChild(int i) {
        return (int) Math.floor(2 * i + 1);
    }

    /**
     * This method finds the right child
     *
     * @param j represents the index of the queried element
     * @return the index of the right child
     */

    private int findRightChild(int j) {
        return (int) Math.floor(2 * j + 2);
    }

    /**
     * finds the max element in the arrayHeap, which should be the last e
     *
     * @return max element
     */
    public E max() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    /**
     * This method swaps the two elements in the locations i and j.
     *
     * @param i the first element's index
     * @param j the second element's index
     */
    private void swap(int i, int j) {
        E element = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, element);
    }

    /**
     * This method moves larger elements to maintain correct implementation.
     *
     * @param i takes in the element's index
     */
    protected void bubbleUp(int i) {
        E temp = heap.get(i);
        while (i > 0 && temp.compareTo(heap.get(findParent(i))) > 0) {
            heap.set(i, heap.get(findParent(i)));
            i = findParent(i);
        }
        heap.set(i, temp);
    }

    /**
     * This method creates a ArrayHeap using the inputted arrayList
     *
     * @param array is the desired arraylist for the heap
     */
    public void buildMaxHeap(ArrayList<E> array) {
        ArrayHeap<E> maxHeap = new ArrayHeap<E>();
        for (int j = 0; j < array.size(); j++) {
            maxHeap.insert(array.get(j));
        }
        this.heap = maxHeap.heap;
    }

    /**
     * This element down heapifies the tree.
     *
     * @param element the element's index
     */
    public void bubbleDown(int element) {
            if (element >= (heap.size() / 2) - 1 && element <= heap.size()) {
                return;
            }

            if (heap.get(element).compareTo(heap.get(findLeftChild(element))) < 0 || heap.get(element).compareTo(heap.get(findRightChild(element))) < 0) {
                if (heap.get(findLeftChild(element)).compareTo(heap.get(findRightChild(element))) > 0 ||
                        heap.get(element).compareTo(heap.get(findLeftChild(element))) == 0) {
                    swap(element, findLeftChild(element));
                    bubbleDown(findLeftChild(element));
                } else{
                    swap(element, findRightChild(element));
                    bubbleDown(findRightChild(element));
                }
            }

        }
    /**
     * This method sorts the new buildmaxheap in the correct order using the returned values from removeMax method
     *
     * @param array this is the nonsorted arraylist
     * @return the now sorted array
     */
    public ArrayList<E> sort(ArrayList<E> array) {
        int temp = array.size();
        buildMaxHeap(array);
        ArrayList<E> sortedList = new ArrayList<E>();
        for (int i = 0; i < temp; i++) {
            sortedList.add(removeMax());
        }
        return sortedList;
    }




    /**
     * This method prints the heap in the form of a binary tree using the power of 2's principle, and then printing the
     * remaining elements in the last line.
     *
     * @return a string version of my arrayHeap
     */
    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        int counter = 0;
        int index = 0;
        while (heap.size() - index > 0) {
            for (double i = Math.pow(2, counter); i > 0; i--) {
                if (heap.size() == index) {
                    return a.toString();
                }
                a.append(heap.get(index).toString());
                a.append(" ");
                index++;
            }
            a.append("\n");
            counter++;
        }
        return a.toString();
    }

}

