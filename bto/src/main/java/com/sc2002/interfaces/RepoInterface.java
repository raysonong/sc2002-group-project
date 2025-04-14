package com.sc2002.interfaces;

import java.util.List;

/**
 * Generic repository interface that defines common operations.
 * @param <T> The entity type this repository manages
 * @param <ID> The type of the entity's identifier
 */
public interface RepoInterface<T, ID> {
    
    /**
     * Save a new entity or update an existing one
     * @param entity Entity to save
     */
    void save(T entity);
    
    /**
     * Retrieve all entities
     * @return List of all entities
     */
    List<T> findAll();
    
    /**
     * Find an entity by its ID - default implementation makes this optional
     * @param id The entity identifier
     * @return The entity if found, or null
     */
    default T findByID(ID ID) {
        return null;
    }
    
    /**
     * Delete an entity by its ID
     * @param id The entity identifier
     * @return true if deleted successfully, false otherwise
     */
    boolean delete(ID id);
}
