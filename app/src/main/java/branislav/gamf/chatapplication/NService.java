package branislav.gamf.chatapplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NService extends Service {

    private NBinder nBinder = null;

    @Override
    public IBinder onBind(Intent intent){

        if(nBinder == null){
            nBinder = new NBinder();
        }

        return nBinder;
    }

    @Override
    public boolean onUnbind(Intent intent){
        nBinder.stop();
        return super.onUnbind(intent);
    }
}
