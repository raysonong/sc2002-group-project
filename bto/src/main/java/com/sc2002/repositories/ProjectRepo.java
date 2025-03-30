package com.sc2002.repositories;
 
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Optional;
 import java.util.stream.Collectors;
 import com.sc2002.entities.BTOProjectModel;
 import com.sc2002.entities.ProjectFilterCriteria;
 import com.sc2002.enums.FlatType;

 public class ProjectRepo{
    private List<BTOProjectModel> projects = new ArrayList<>();

    public Optional<BTOProjectModel> findById(long projectID){
        for (BTOProjectModel project : projects) {
            if (project.getProjectID() == projectID) {
                return Optional.of(project);
            }
        }
        return Optional.empty();
    }
    
    public void save(BTOProjectModel project) {
        projects.add(project);
    }

    public List<BTOProjectModel> findByCriteria(ProjectFilterCriteria criteria) {
        return projects.stream()
        .filter(project -> {
            boolean locationMatch = criteria.getLocation() == null || criteria.getLocation().isEmpty() ||
                                    project.getNeighborhood().equalsIgnoreCase(criteria.getLocation());
            boolean flatTypeMatch = criteria.getFlatType() == null ||
                                    (criteria.getFlatType() == FlatType.TWO_ROOM && project.getTwoRoomCount() > 0) ||
                                    (criteria.getFlatType() == FlatType.THREE_ROOM && project.getThreeRoomCount() > 0);
            return locationMatch && flatTypeMatch;
        })
        .collect(Collectors.toList());
 
  }
}