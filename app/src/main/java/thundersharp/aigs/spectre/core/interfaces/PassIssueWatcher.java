package thundersharp.aigs.spectre.core.interfaces;

import thundersharp.aigs.spectre.core.models.PassData;

public interface PassIssueWatcher {

    void OnPassIssued(PassData passData);
    void OnPassNotIssued(Exception e);
}
