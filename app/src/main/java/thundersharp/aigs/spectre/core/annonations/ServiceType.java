package thundersharp.aigs.spectre.core.annonations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ServiceType.TYPE_NORMAL_COURSE,
        ServiceType.TYPE_LIVE_COURSE,
        ServiceType.TYPE_FREE_COURSE})
public @interface ServiceType {
    int TYPE_NORMAL_COURSE = 0;
    int TYPE_LIVE_COURSE = 1;
    int TYPE_FREE_COURSE = 2;
}
