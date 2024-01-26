package pcs.springframework.pointsshow.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableInfo {

    private int numTeams;
    private List<String> categories;
    private List<List<Integer>> points;
    private int totalPoints;

    @Override
    public String toString() {
        return "TableInfo{" +
                "numTeams=" + numTeams +
                ", categories=" + categories +
                ", points=" + points +
                ", totalPoints=" + totalPoints +
                '}';
    }


    // Getters and setters for each field

    public int getNumTeams() {
        return numTeams;
    }

    public void setNumTeams(int numTeams) {
        this.numTeams = numTeams;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<List<Integer>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Integer>> points) {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}