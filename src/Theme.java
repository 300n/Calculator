public class Theme {
    private final String name;
    private final String imageUrl;
    private final String borderColor;
    private final String backgroundColor;

    public Theme(String name, String imageUrl, String borderColor, String backgroundColor) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
    }

    public String getName(){
        return this.name;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
    public String getBorderColor(){
        return this.borderColor;
    }
    public String getBackgroundColor(){
        return this.backgroundColor;
    }
}