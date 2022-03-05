package thundersharp.aigs.spectre.core.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface LoginInterface {
    void onLoginSuccess(String provider, Task<AuthResult> authResultTask, boolean verified);
    void onLoginFailure(Exception exception);
}
