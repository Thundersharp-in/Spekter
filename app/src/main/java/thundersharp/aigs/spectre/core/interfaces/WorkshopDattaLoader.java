package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.WorkshopDetail;

public interface WorkshopDattaLoader {

    void onDataFetchSuccess(WorkshopDetail workshopDetails);
    void onDataError(Exception e);

}
