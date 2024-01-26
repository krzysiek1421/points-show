package pcs.springframework.pointsshow.model;

import java.util.List;

public class TableCreationRequest {
    private Integer numberOfTeams;
    private List<String> categories;


    @Override
    public String toString() {
        return "TableCreationRequest{" +
                "numberOfTeams=" + numberOfTeams +
                ", categories=" + categories +
                '}';
    }
    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}