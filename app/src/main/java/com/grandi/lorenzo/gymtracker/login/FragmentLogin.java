package com.grandi.lorenzo.gymtracker.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.grandi.lorenzo.gymtracker.FlagList;
import com.grandi.lorenzo.gymtracker.home.HomeActivity;
import com.grandi.lorenzo.gymtracker.R;

import static com.grandi.lorenzo.gymtracker.KeyLoader.*;

public class FragmentLogin extends Fragment {

    private TextInputLayout til_name, til_password;
    private Button b_login;
    private Context context;
    private View view;
    private String name, password;
    private String FILL_FORM_MESSAGE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity activity = getActivity();

        if (activity != null) {
            initViewComponents(activity);
            initTaskComponents(activity);
            this.b_login.setOnClickListener(v -> {
                this.loadTextInput();
                v.setPressed(true);
                if (!checkFields()) {
                    Toast.makeText(this.context, this.FILL_FORM_MESSAGE, Toast.LENGTH_SHORT).show();
                } else {
                    this.preferenceSaver(activity);
                    Intent intent = new Intent(this.context, HomeActivity.class);
                    new FlagList(intent);
                    startActivity(intent);
                }
            });
        }
    }

    private void initViewComponents(FragmentActivity activity) {
        this.context = getContext();
        this.view = getView();
        this.til_name = activity.findViewById(R.id.et_profile_name);
        this.til_password = activity.findViewById(R.id.et_profile_password);
        this.b_login = activity.findViewById(R.id.b_login);
    }
    private void initTaskComponents(FragmentActivity activity) {
        this.FILL_FORM_MESSAGE = activity.getString(R.string.fill_form);
    }

    //TODO: Database handler
    private boolean checkFields() {
        boolean flag = true;
        this.clearTextInput();
        if (getName().isEmpty()) {
            til_name.setError(this.context.getString(R.string.name_error));
            flag = false;
        }
        if (getPassword().isEmpty()) {
            til_password.setError(this.context.getString(R.string.password_error));
            flag = false;
        }
        return flag;
    }
    private void loadTextInput() {
        this.til_name = this.view.findViewById(R.id.et_profile_name);
        this.til_password = this.view.findViewById(R.id.et_profile_password);
        this.name = this.til_name.getEditText().getText().toString();
        this.password = this.til_password.getEditText().getText().toString();
    }
    private void clearTextInput() {
        this.til_name.setErrorEnabled(false);
        this.til_password.setErrorEnabled(false);
    }
    private void preferenceSaver(FragmentActivity activity) {
        SharedPreferences loginPreference = activity.getSharedPreferences(LOGIN_PREFERENCE_FILE.getValue(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putString(nameKey.getValue(), this.name);
        editor.putString(passwordKey.getValue(), this.password);
        editor.putBoolean(loggedKey.getValue(), true);
        editor.apply();
    }

    private String getName() {
        return this.name;
    }
    private String getPassword() {
        return this.password;
    }
}