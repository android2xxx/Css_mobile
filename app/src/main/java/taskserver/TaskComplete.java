package taskserver;

/**
 * Created by HieuHT on 04/14/2015.
 */
public class TaskComplete {
    public OnTaskCompleted onTaskListenerComplete;

    public interface OnTaskCompleted {
        void onTaskCompleted(String json);
    }

    public void setOnTaskComplete(OnTaskCompleted onTaskListenerComplete){
        this.onTaskListenerComplete = onTaskListenerComplete;
    }
}
