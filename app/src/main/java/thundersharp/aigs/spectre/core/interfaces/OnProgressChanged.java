package thundersharp.aigs.spectre.core.interfaces;

import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;

public interface OnProgressChanged {

    void onProgressUpdated(double finalPercent, ProjectBasicInfo projectBasicInfo);
    void onItemDeletedInReSync(String projectId);

}
