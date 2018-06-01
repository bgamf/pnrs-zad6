package branislav.gamf.chatapplication;

public class Message {
    private String mMessageID;
    private String mSenderID;
    private String mReceiverID;
    private String mMessage;

    public Message(String mMessageID, String mSenderID, String mReceiverID, String mMessage) {
        this.mMessageID = mMessageID;
        this.mSenderID = mSenderID;
        this.mReceiverID = mReceiverID;
        this.mMessage = mMessage;
    }

    public String getmMessageID() {
        return mMessageID;
    }

    public String getmSenderID() {
        return mSenderID;
    }

    public String getmReceiverID() {
        return mReceiverID;
    }

    public String getmMessage() {
        return mMessage;
    }
}
