package thundersharp.aigs.spectre.core.interfaces;

import org.json.JSONObject;

public interface FeedbackObserver {
    void OnFeedbackSent(JSONObject jsonObject);
    void OnError(Exception e);

}
