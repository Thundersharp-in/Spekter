package thundersharp.aigs.spectre.core.helpers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import thundersharp.aigs.spectre.core.interfaces.LoginInterface;
import thundersharp.aigs.spectre.core.interfaces.RegisterPresenter;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class LoginProvider {

    public static class RegistrationDataBuilder{

        private String name;
        private String email;
        private String phone;
        private String passWord;
        private Boolean acharyan;

        public static RegistrationDataBuilder getInstance(){
            return new RegistrationDataBuilder();
        }

        private RegistrationDataBuilder(){}

        public RegistrationDataBuilder(String name, String email, String phone, String passWord, Boolean acharyan) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.passWord = passWord;
            this.acharyan = acharyan;
        }

        public Boolean getAcharyan() {
            return acharyan;
        }

        public RegistrationDataBuilder setAcharyan(Boolean acharyan) {
            this.acharyan = acharyan;
            return this;
        }

        public String getName() {
            return name;
        }

        public RegistrationDataBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public RegistrationDataBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public RegistrationDataBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getPassWord() {
            return passWord;
        }

        public RegistrationDataBuilder setPassWord(String passWord) {
            this.passWord = passWord;
            return this;
        }
    }

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
    private RegistrationDataBuilder registrationDataBuilder;
    private RegisterPresenter registerPresenter;

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


    public LoginProvider registerUser(RegistrationDataBuilder registrationDataBuilder){
        this.registrationDataBuilder = registrationDataBuilder;
        return this;
    }

    public void attachRegistrationObserver(RegisterPresenter registerPresenter){
        this.registerPresenter = registerPresenter;
        createNewUser(registrationDataBuilder);
    }

    private void createNewUser(RegistrationDataBuilder registrationDataBuilder) {

        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(registrationDataBuilder.getEmail(), registrationDataBuilder.getPassWord())
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(registrationDataBuilder.getName()).build()).addOnCompleteListener(runnable -> {});
                       registrationDataBuilder.setPassWord(null);
                       FirebaseDatabase
                               .getInstance()
                               .getReference(CONSTANTS.DATABASE_NODE_USERS)
                               .child(FirebaseAuth.getInstance().getUid())
                               .setValue(registrationDataBuilder)
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> taskd) {
                                       if (taskd.isSuccessful()){
                                           FirebaseAuth
                                                   .getInstance()
                                                   .getCurrentUser()
                                                   .sendEmailVerification()
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> taske) {
                                                           if (taske.isSuccessful())
                                                               registerPresenter.onRegisterSuccess(task,true);
                                                           else registerPresenter.onRegisterSuccess(task,false);
                                                       }
                                                   });


                                       }else {
                                           FirebaseAuth
                                                   .getInstance()
                                                   .getCurrentUser()
                                                   .delete()
                                                   .addOnCompleteListener(task1 -> {
                                                       registerPresenter.onRegistrationFailure(new Exception("INTERNAL ERROR"));
                                                   });
                                       }
                                   }
                               });

                   }else registerPresenter.onRegistrationFailure(task.getException());
                });

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
