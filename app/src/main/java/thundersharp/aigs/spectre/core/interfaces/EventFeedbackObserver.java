package thundersharp.aigs.spectre.core.interfaces;

import org.json.JSONObject;

public interface EventFeedbackObserver {
    void OnFeedbackSent(JSONObject jsonObject);
    void OnError(Exception e);

}
