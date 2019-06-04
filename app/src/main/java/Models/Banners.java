package Models;

public class Banners {
    private String image, link;

    public Banners() {
    }

    public Banners(String image, String link) {
        this.image = image;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
