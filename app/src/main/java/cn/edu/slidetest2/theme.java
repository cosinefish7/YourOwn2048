package cn.edu.slidetest2;

public class theme {
    private String theme;
    private String cards;

    public theme(String theme, String cards) {
        this.theme = theme;
        this.cards = cards;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }
}
