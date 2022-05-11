package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.Initiative;

public interface InitiativesListner {
    void OnInitiativesFetched(List<Initiative> initiativeList);
    void OnDataFetchError(Exception e);
}
