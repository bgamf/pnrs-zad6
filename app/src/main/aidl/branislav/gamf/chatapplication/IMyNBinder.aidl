// IMyNBinder.aidl
package branislav.gamf.chatapplication;

// Declare any non-default types here with import statements
import branislav.gamf.chatapplication.IMyNCallback;

interface IMyNBinder {

    void setCallback(in IMyNCallback callback);

}
