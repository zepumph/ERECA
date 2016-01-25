package michael.mobilecomputing.com.ereca;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by Michael on 1/25/2016.
 */


/**
 *  Not yet implemented yet
 *  Waiting for a rework of the ui organization
 *  Jack and Rebecca: Currently working on one button note taking.
 *  This POJO is to be used for holding a new note once collection occurs in one step.
 *   **** Michael, 3:45 1/25/16
 */

public class Note {

    private Location location;
    private String noteText;
    private Bitmap image;


    public Note(Location location, String noteText, Bitmap image) {
        this.location = location;
        this.noteText = noteText;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
