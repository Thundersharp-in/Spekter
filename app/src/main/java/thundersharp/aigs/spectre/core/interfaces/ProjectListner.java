package thundersharp.aigs.spectre.core.interfaces;

import java.util.List;

import thundersharp.aigs.spectre.core.models.Participants;

public interface ProjectListner {

    interface fetchParticipants{
        void onParticipantsFetchSuccess(List<Participants> participantsList);
        void onError(Exception e);
    }
}
