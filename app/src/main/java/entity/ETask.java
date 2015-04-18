package entity;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by User on 4/15/2015.
 */
@ParseClassName("Task")
public class ETask extends ParseObject {
    public ETask(){}
    /*
    private boolean completed;
    public boolean getCompleted(){
        return completed;
    }
    public void setCompleted(boolean completed){
       this.completed = completed;
    }
    */

    public Boolean getCompleted(){
        return getBoolean("completed");
    }
    public void setCompleted(boolean completed){
        put("completed",completed);
    }

    public String getDescription(){
        return getString("description");
    }
    public void setDescription(String description){
        put("description",description);
    }

}
