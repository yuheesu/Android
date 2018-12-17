package example.boostcamptest.RecyclerView;


public class item {
    private String image;
    private String title;
    private String link;
    private String subtitle;
    private String year;
    private String director;
    private String actor;
    private String ratingBar;

    public item() {
    }

    public item(String image, String title, String subtitle, String year, String director, String actor, String ratingBar, String link) {
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.year = year;
        this.director = director;
        this.actor = actor;
        this.ratingBar = ratingBar;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
