package thundersharp.aigs.spectre.core.progress;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import thundersharp.aigs.spectre.core.interfaces.OnProgressChanged;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class BrowseProgressStall {

    private static BrowseProgressStall browseProgress = null;
    private static int totalProjects;

    private String writerName;
    private SharedPreferences sharedPreferences;
    private Activity activity;

    private OnProgressChanged onProgressChangedListener;


    public static BrowseProgressStall getInstance(Activity activity) {
        if (browseProgress == null) browseProgress = new BrowseProgressStall(activity);
        return browseProgress;
    }

    public BrowseProgressStall(Activity activity) {
        this.activity = activity;
    }

    public BrowseProgressStall selectStorageInstanceByName(String writerName) {
        this.writerName = writerName;
        sharedPreferences = createStorageInstance(writerName);
        return this;
    }

    public void setPageBrowsed(ProjectBasicInfo projectBasicInfo) {
        if (!checkIfExists(projectBasicInfo.ID)) {
            saveToStorage(projectBasicInfo);
        }
    }

    public BrowseProgressStall setProjectsCount(int count){
        totalProjects = count;
        return this;
    }

    public BrowseProgressStall setOnProgressListener(OnProgressChanged onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
        return this;
    }

    public void reSyncStorageWithDatabase(List<ProjectBasicInfo> data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Map<String,?> storageData = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry: storageData.entrySet()) {
            boolean contain = false;
            for (ProjectBasicInfo projectBasicInfo : data){
                if (entry.getKey().equalsIgnoreCase(projectBasicInfo.ID)){
                    contain = true;
                }
            }

            if (!contain){
                editor.remove(entry.getKey());
                onProgressChangedListener.onItemDeletedInReSync(entry.getKey());
                editor.apply();
            }

        }

        setProjectsCount(data.size());

    }

    private void saveToStorage(ProjectBasicInfo projectBasicInfo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(projectBasicInfo.ID, projectBasicInfo.ID);
        editor.apply();
        if (onProgressChangedListener != null) {
            Map<String, ?> prefsMap = sharedPreferences.getAll();
            double percent = ((double)prefsMap.size()/totalProjects)*100;
            onProgressChangedListener.onProgressUpdated(percent,projectBasicInfo);
        }
    }

    public double getOverviewProgress(){
        Map<String, ?> prefsMap = sharedPreferences.getAll();
        return ((double)prefsMap.size()/totalProjects)*100;
    }

    private boolean checkIfExists(String projectId) {
        if (sharedPreferences.getString(projectId, null) == null)
            return false;
        else
            return true;
    }

    private synchronized SharedPreferences createStorageInstance(String writerName) {
        return activity.getSharedPreferences(writerName, Context.MODE_PRIVATE);
    }

    public void clear() {
        activity.getSharedPreferences(CONSTANTS.EXHIBITION_VISIT_PROGRESS,Context.MODE_PRIVATE).edit().clear().apply();
        activity.getSharedPreferences(CONSTANTS.STALLS_VISIT_PROGRESS,Context.MODE_PRIVATE).edit().clear().apply();
    }

}
