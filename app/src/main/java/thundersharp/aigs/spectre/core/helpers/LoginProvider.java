package thundersharp.aigs.spectre.core.helpers;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import thundersharp.aigs.spectre.core.interfaces.LoginInterface;

public class LoginProvider {

    public static class Builder {

        private String email_id;
        private String password;
        private String phone_no;

        public static Builder getInstance() {
            return new Builder();
        }

        public String getEmail_id() {
            return email_id;
        }

        public Builder setEmail_id(String email_id) {
            this.email_id = email_id;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public Builder setPhone_no(String phone_no) {
            this.phone_no = phone_no;
            return this;
        }

    }

    public LoginProvider() {
    }

    private static LoginProvider loginProvider;
    private LoginProvider.Builder builder;
    private LoginInterface loginInterface;


    public LoginProvider setCredentials(LoginProvider.Builder builder) {
        this.builder = builder;
        return this;
    }

    public static LoginProvider getInstance() {
        if (loginProvider == null) {
            loginProvider = new LoginProvider();
        }
        return loginProvider;
    }

    public void attachLoginObserver(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
        if (builder == null) {
            loginInterface.onLoginFailure(new Exception("Internal Error!! Have you called setCredentials() ??"));
        } else {
            if (builder.getEmail_id() != null) {
                logInWithEmail(builder);
            } else {
                logInWithPhone(builder);
            }
        }
    }

    private void logInWithPhone(Builder builder) {

    }

    private void logInWithEmail(Builder builder) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(builder.getEmail_id(), builder.getPassword())
                .addOnCompleteListener(taskA -> {
                    if (taskA.isSuccessful()) {
                        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
                            loginInterface.onLoginSuccess("email", taskA, true);
                        } else {
                            FirebaseAuth
                                    .getInstance()
                                    .getCurrentUser()
                                    .sendEmailVerification()
                                    .addOnCompleteListener(taask -> {
                                        loginInterface.onLoginSuccess("email", taskA, false);
                                    });
                        }
                    }else loginInterface.onLoginFailure(taskA.getException());
                });
    }
}
