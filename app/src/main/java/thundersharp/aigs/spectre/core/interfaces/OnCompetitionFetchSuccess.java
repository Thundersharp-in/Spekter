package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.Competitions;
import thundersharp.aigs.spectre.core.models.Workshops;

public interface OnCompetitionFetchSuccess {

    void onFetchProjectsSuccess(List<Competitions> response);
    void onFetchError(Exception e);

}
