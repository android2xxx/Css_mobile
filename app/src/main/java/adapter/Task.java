package adapter;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by User on 4/15/2015.
 */
@ParseClassName("Task")
public class Task  extends ParseObject {
    public Task(){}
    public boolean isCompleted(){
        return getBoolean("Completed");
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
