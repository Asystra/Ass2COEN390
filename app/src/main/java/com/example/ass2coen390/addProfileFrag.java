package com.example.ass2coen390;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addProfileFrag extends DialogFragment {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText idEditText;
    private EditText gpaEditText;
    private Button saveButton;
    private Button cancelButton;

    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_add_profil, null);

        //Initialize UI components
        nameEditText = view.findViewById(R.id.textViewstudentNameFrag);
        surnameEditText = view.findViewById(R.id.textViewstudentSurnameFrag);
        idEditText = view.findViewById(R.id.textViewstudentIDFrag);
        gpaEditText = view.findViewById(R.id.textViewstudentGPAFrag);

        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        // Handle the Save button click event
        saveButton.setOnClickListener(v -> onSaveClicked(dialog));

        // Handle the Cancel button click event
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }

    // Method called when the Save button is clicked
    private void onSaveClicked(Dialog dialog) {
        if (validateInput()) {
            int id = Integer.parseInt(idEditText.getText().toString());
            String name = nameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();
            float gpa = Float.parseFloat(gpaEditText.getText().toString());

            // Get current date and time as creation date
            String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Create a Profile object with the entered information
            Profile profile = new Profile(id, name, surname, gpa, creationDate);

            // Attempt to cast the hosting Activity to MainActivity and save the profile
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.saveProfileToDatabase(profile);
                dialog.dismiss();
            }
        } else {
            Toast.makeText(getContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to validate user input
    private boolean validateInput() {

        return !TextUtils.isEmpty(nameEditText.getText().toString()) &&
                !TextUtils.isEmpty(surnameEditText.getText().toString()) &&
                !TextUtils.isEmpty(idEditText.getText().toString()) &&
                !TextUtils.isEmpty(gpaEditText.getText().toString()) &&
                idEditText.getText().toString().matches("\\d{8}") &&
                Float.parseFloat(gpaEditText.getText().toString()) >= 0 &&
                Float.parseFloat(gpaEditText.getText().toString()) <= 4.3;
    }
}
