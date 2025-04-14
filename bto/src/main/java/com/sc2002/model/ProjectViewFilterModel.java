package com.sc2002.model;

import com.sc2002.enums.FlatType;
import com.sc2002.enums.Neighborhood;

/**
 * Represents a model for filtering projects based on Neighborhood and flat type.
 * This class is encapsulated within the user as an attribute to facilitate
 * project filtering operations in ProjectView.
 */
public class ProjectViewFilterModel {
    /**
     * The Neighborhood filter criterion for projects.
     */
    private Neighborhood neighborhood;
    
    /**
     * The flat type filter criterion for projects.
     */
    private FlatType flatType;

    /**
     * Constructs a ProjectFilterModel instance with null filters.
     * By default, no filtering is applied.
     */
    public ProjectViewFilterModel(){
        this.neighborhood=null;
        this.flatType=null;
    }

    /**
     * Sets the Neighborhood filter criterion.
     * 
     * @param neighborhood The Neighborhood to filter projects by
     */
    public void setNeighborhood(Neighborhood neighborhood){
        System.out.println("Sttin");
        this.neighborhood=neighborhood;
    }

    /**
     * Sets the flat type filter criterion.
     * 
     * @param flatType The flat type to filter projects by
     */
    public void setFlatType(FlatType flatType){
        this.flatType=flatType;
    }

    /**
     * Clears the Neighborhood filter by setting it to null.
     * This effectively removes the Neighborhood filtering criterion.
     */
    public void setNeighborhood() {
        this.neighborhood = null;
    }

    /**
     * Clears the flat type filter by setting it to null.
     * This effectively removes the flat type filtering criterion.
     */
    public void setFlatType() {
        this.flatType = null;
    }
    /**
     * Gets the Neighborhood filter criterion.
     * 
     * @return The Neighborhood used for filtering projects, or null if no filter is set.
     */
    public Neighborhood getNeighborhood() {
        return this.neighborhood;
    }

    /**
     * Gets the flat type filter criterion.
     * 
     * @return The flat type used for filtering projects, or null if no filter is set.
     */
    public FlatType getFlatType() {
        return this.flatType;
    }


}
