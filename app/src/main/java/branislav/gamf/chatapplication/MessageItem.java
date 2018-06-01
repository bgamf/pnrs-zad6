package branislav.gamf.chatapplication;

public class MessageItem {
    private String messageText;
    private String messageID;
    private boolean backgroundColor;

    public MessageItem(String messageText, boolean backgroundColor,String messageID) {
        this.messageText = messageText;
        this.backgroundColor = backgroundColor;
        this.messageID = messageID;
    }

    public String getMessageID(){
        return messageID;
    }

    public void setMessageID(String messageID){
        this.messageID = messageID;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String messageText) {
        this.messageText = messageText;
    }

    public boolean isBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(boolean backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
