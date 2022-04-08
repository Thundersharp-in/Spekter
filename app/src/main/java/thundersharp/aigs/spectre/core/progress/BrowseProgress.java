package thundersharp.aigs.spectre.core.progress;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import thundersharp.aigs.spectre.core.interfaces.OnProgressChanged;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;

public class BrowseProgress {

    private static BrowseProgress browseProgress = null;
    private static int totalProjects;

    private String writerName;
    private SharedPreferences sharedPreferences;
    private Activity activity;

    private OnProgressChanged onProgressChangedListener;


    public static BrowseProgress getInstance(Activity activity) {
        if (browseProgress == null) browseProgress = new BrowseProgress(activity);
        return browseProgress;
    }

    public BrowseProgress(Activity activity) {
        this.activity = activity;
    }

    public BrowseProgress selectStorageInstanceByName(String writerName) {
        this.writerName = writerName;
        sharedPreferences = createStorageInstance(writerName);
        return this;
    }

    public void setPageBrowsed(ProjectBasicInfo projectBasicInfo) {
        if (!checkIfExists(projectBasicInfo.ID)) {
            saveToStorage(projectBasicInfo);
        }
    }

    public BrowseProgress setProjectsCount(int count){
        totalProjects = count;
        return this;
    }

    public BrowseProgress setOnProgressListener(OnProgressChanged onProgressChangedListener) {
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

    private boolean checkIfExists(String projectId) {
        if (sharedPreferences.getString(projectId, null) == null)
            return false;
        else
            return true;
    }

    private synchronized SharedPreferences createStorageInstance(String writerName) {
        return activity.getSharedPreferences(writerName, Context.MODE_PRIVATE);
    }

}
