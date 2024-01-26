package pcs.springframework.pointsshow.model;

public class UpdatePointsRequest {
    private int teamId;
    private String category;
    private int points;


    @Override
    public String toString() {
        return "UpdatePointsRequest{" +
                "teamId=" + teamId +
                ", category='" + category + '\'' +
                ", points=" + points +
                '}';
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
