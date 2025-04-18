package com.sc2002.interfaces;

import java.util.List;

/**
 * A blueprint for repository classes, defining standard ways to manage data.
 * Repositories handle storing, finding, and deleting items (like users or projects).
 *
 * @param <T> The type of item this repository works with (e.g., UserModel).
 * @param <ID> The type of the item's unique identifier (e.g., Integer for userID).
 */
public interface RepoInterface<T, ID> {
    
    /**
     * Saves an item. If it's new, it adds it. If it already exists, it might update it.
     *
     * @param entity The item to save.
     */
    void save(T entity);
    
    /**
     * Gets a list of all items stored in the repository.
     *
     * @return A list containing all items.
     */
    List<T> findAll();
    
    /**
     * Finds a specific item using its unique ID.
     * This is optional for repositories to implement fully.
     *
     * @param ID The unique ID of the item to find.
     * @return The found item, or null if no item has that ID.
     */
    default T findByID(ID ID) {
        return null;
    }
    
    /**
     * Deletes an item from the repository using its unique ID.
     *
     * @param ID The unique ID of the item to delete.
     * @return True if the item was found and deleted, false otherwise.
     */
    boolean delete(ID ID);
}
