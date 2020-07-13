package cn.edu.slidetest2;

public class ScoreRate {
    private String id;
    private String theme;
    private int score;

    public ScoreRate(String id, String theme, int score) {
        this.id = id;
        this.theme = theme;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
