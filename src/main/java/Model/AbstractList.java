package Model;

/**
 *  Interface for a list of items for the Model.Iterator design pattern.
 *
 * @author Kenneth Huynh
 */
public interface AbstractList {

    /**
     *  Creates an iterator of for the list.
     *
     *  @return A new iterator for the list
     */
    Iterator createIterator();

    /**
     *  Returns the amount of elements in the list.
     *
     *  @return The list size
     */
    int count();

    /**
     *  Appends an object to the list.
     *
     *  @param object: The item to be appended onto the list
     *  @return If the addition of the object was possible
     */
    boolean append(Object object);

    /**
     *  Removes an object from the list.
     *
     *  @param object:  The item to be removed from the list
     *  @return If the removal of the object was possible
     */
    boolean remove(Object object);

    /**
     *  Finds the index of an object from the list.
     *
     *  @param object:  The item used to find the index
     *  @return The index of the item within the list
     */
    int indexOf(Object object);
}
