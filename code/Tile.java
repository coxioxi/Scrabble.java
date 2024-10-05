public class Tile {
    private final int score;
    private final String letter;
    private final Point location;
    private boolean isNew;
    public Tile(String letter, int score, Point location){
        this.letter = letter;
        this.score = score;
        this.location = location;
        isNew = true;
    }
    public String getLetter() {
        return this.letter;
    }
    public int getScore() {
        return this.score;
    }
    public Point getLocation() {
        return location;
    }
    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean isNew){
        this.isNew = isNew;
    }
}
