package branislav.gamf.chatapplication;

public class Contact {
    private String cUserID;
    private String cUserName;
    private String cFirstName;
    private String cLastName;

    Contact(String cUserID,String cUserName,String cFirstName,String cLastName){
        this.cUserID = cUserID;
        this.cUserName = cUserName;
        this.cFirstName = cFirstName;
        this.cLastName = cLastName;
    }

    public String getcUserID(){
        return this.cUserID;
    }

    public void setcUserID(String cUserID){
        this.cUserID = cUserID;
    }

    public String getcUserName(){
        return this.cUserName;
    }

    public void setcUserName(String cUserName){
        this.cUserName = cUserName;
    }

    public String getcFirstName(){
        return this.cFirstName;
    }

    public void setcFirstName(String cFirstName){
        this.cFirstName = cFirstName;
    }

    public String getcLastName(){
        return this.cLastName;
    }

    public void setcLastName(String cLastName){
        this.cLastName = cLastName;
    }
}
