package thundersharp.aigs.spectre.core.helpers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.core.interfaces.ExhibitionInterface;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class DatabaseHelpers {

    private static DatabaseHelpers databaseHelpers = null;
    private ExhibitionInterface exhibitionInterface;
    private ProjectListner.fetchParticipants fetchParticipants;

    private long projectId;

    public static DatabaseHelpers getInstance(){
        if (databaseHelpers == null){
            databaseHelpers = new DatabaseHelpers();
        }
        return databaseHelpers;
    }

    public DatabaseHelpers setProjectId(long projectId){
        this.projectId = projectId;
        return this;
    }

    public void setFetchParticipantsListeners(ProjectListner.fetchParticipants participantsListeners){
        this.fetchParticipants = participantsListeners;
        fetchParticipantsNow();
    }

    private void fetchParticipantsNow() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(String.valueOf(projectId))
                .child("PARTICIPANTS")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Participants> participantsList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                participantsList.add(dataSnapshot.getValue(Participants.class));
                            }
                            fetchParticipants.onParticipantsFetchSuccess(participantsList);
                        }else fetchParticipants.onError(new Exception("Data not found."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        fetchParticipants.onError(error.toException());
                    }
                });

    }

    public DatabaseHelpers setExhibitionListener(ExhibitionInterface exhibitionInterface){
        this.exhibitionInterface = exhibitionInterface;
        fetchExhibits();
        return this;
    }

    private void fetchExhibits(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<ProjectBasicInfo> projectBasicInfoList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                projectBasicInfoList.add(dataSnapshot.getValue(ProjectBasicInfo.class));
                            }
                            exhibitionInterface.onProjectsFetchSuccess(projectBasicInfoList);
                        }else exhibitionInterface.onProjectsFetchFailure(new Exception("No data Here !!"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        exhibitionInterface.onProjectsFetchFailure(error.toException());
                    }
                });
    }

}
