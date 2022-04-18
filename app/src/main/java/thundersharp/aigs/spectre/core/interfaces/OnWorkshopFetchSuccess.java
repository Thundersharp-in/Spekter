package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.Workshops;

public interface OnWorkshopFetchSuccess {

    void onFetchProjectsSuccess(List<Workshops> response);
    void onFetchError(Exception e);

}
