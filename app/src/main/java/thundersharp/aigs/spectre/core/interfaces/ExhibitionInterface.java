package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;

public interface ExhibitionInterface {



    void onProjectsFetchSuccess(List<ProjectBasicInfo> projectBasicInfoList);
    void onProjectsFetchFailure(Exception e);

}
