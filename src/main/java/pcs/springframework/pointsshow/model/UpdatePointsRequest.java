package pcs.springframework.pointsshow.model;


public class UpdatePointsRequest {
    private int teamId;
    private int categoryId;
    private int points;

    @Override
    public String toString() {
        return "UpdatePointsRequest{" +
                "teamId=" + teamId +
                ", category='" + categoryId + '\'' +
                ", points=" + points +
                '}';
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
