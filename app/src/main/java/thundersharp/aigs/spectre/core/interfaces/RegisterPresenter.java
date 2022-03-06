package thundersharp.aigs.spectre.core.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface RegisterPresenter {
    void onRegisterSuccess(Task<AuthResult> authResultTask, boolean verificationLinkSent);
    void onRegistrationFailure(Exception e);
}
