package michael.mobilecomputing.com.ereca.gridviewer;

import android.util.Log;

import michael.mobilecomputing.com.ereca.http.AsyncResponse;

/**
 * Created by Xander on 2/5/16.
 *
 * This is a callback class for when http helper gets a list of noteIDs from the server
 * After it has the list it calls more http helpers to download each note from the server
 */
public class GetListResponse implements AsyncResponse {

    GridViewActivity gridViewActivity;
    public GetListResponse(GridViewActivity gva){
        gridViewActivity = gva;
    }

    @Override
    public void processFinish(String result) {

        /* array of noteIDs */
        String[] noteIDs = getArrayFromJson(result);

        /* iterate thru note ids and get ethe notes one by one */
        for (int i = 0; i < noteIDs.length; i ++){
            if (noteIDs[i].length() > 0) {
                gridViewActivity.getNote(null, noteIDs[i]);
            }
        }


    }

    private String[] getArrayFromJson(String input){

        /* make the string to an array */
        input = input.replace("[", "");
        input = input.replace("]", "");
        input = input.replace('"', ' ');
        String[] output = input.split(",");
        for (int i = 0; i < output.length; i ++){
            output[i] = output[i].trim();
            System.out.println( output[i] );

        }
        return output;
    }
}
