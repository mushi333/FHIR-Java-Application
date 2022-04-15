package Model;

/**
 *  Interface for a Model.Iterator to iterate through an abstract list for the Model.Iterator design pattern.
 *
 * @author Kenneth Huynh
 */
public interface Iterator {

    /**
     *  Returns the next object in the list.
     *
     *  @return The next object
     */
    Object next();

    /**
     *  Returns whether the iterator has reached the end of the list or not.
     *
     *  @return A boolean value
     */
    boolean isDone();

    /**
     *  Returns the current object in the list.
     *
     *  @return The current object
     */
    Object currentItem();
}
