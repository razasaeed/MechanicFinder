package motor.mechanic.finder.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class User_CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View myView;

    public User_CustomInfoWindow(Context context){
        myView = LayoutInflater.from(context).inflate(R.layout.custom_user_info_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickuptitle = myView.findViewById(R.id.txtPickupInfo);
        txtPickuptitle.setText(marker.getTitle());

        TextView txtPickupSnippet = myView.findViewById(R.id.txtPickupSnippet);
        txtPickupSnippet.setText(marker.getSnippet());

        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
