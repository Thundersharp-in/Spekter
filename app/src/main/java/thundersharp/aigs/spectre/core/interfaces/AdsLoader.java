package thundersharp.aigs.spectre.core.interfaces;

public interface AdsLoader {
    void OnAdsFetchSuccess(String uri);
    void OnAdsNotAvailable(Exception e);
}
