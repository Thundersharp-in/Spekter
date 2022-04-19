package thundersharp.aigs.spectre.core.interfaces;

import thundersharp.aigs.spectre.core.models.InovativeChallangeDetails;

public interface ChallengeLoader {

    void OnChallengeLoadSuccess(InovativeChallangeDetails inovativeChallangeDetails);
    void OnLoadError(Exception e);
}
