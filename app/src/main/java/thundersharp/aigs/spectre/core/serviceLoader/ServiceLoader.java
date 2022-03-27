package thundersharp.aigs.spectre.core.serviceLoader;


import java.util.List;

import thundersharp.aigs.spectre.core.exceptions.DataRenderError;
import thundersharp.aigs.spectre.core.models.ServiceTopicModel;

public interface ServiceLoader {

    void OndataCallback();

    interface OnServiceLoaded{
        void courseLoaded(List<ServiceTopicModel> topicModels);
        void liveCoursesLoaded(List<ServiceTopicModel> topicModels);
        void freeCourcesLoaded(List<ServiceTopicModel> topicModels);

        void dataRenderException(int type, DataRenderError dataRenderError);
    }

}
