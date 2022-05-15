package thundersharp.aigs.spectre.core.interfaces;

import thundersharp.aigs.spectre.core.models.GeneralEventsDetails;
import thundersharp.aigs.spectre.core.models.WorkshopDetail;

public interface GeneralEventsLoader {

    void onGeneralEventsFetchSuccess(GeneralEventsDetails workshopDetails);
    void onDataError(Exception e);
}
