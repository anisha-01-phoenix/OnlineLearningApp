package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NewVideo extends AppCompatActivity {

    public static final String EXTRA_TITLE=
            "com.example.newproject.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION=
            "com.example.newproject.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE=
            "com.example.newproject.EXTRA_DATE";

    TextInputLayout inputTitle, inputDescription;
    RadioGroup radioGroup;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video);
        inputTitle=findViewById(R.id.inputTitle);
        inputDescription=findViewById(R.id.inputDescription);
        radioGroup=findViewById(R.id.options);
        int selectedOption=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(selectedOption);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Video");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_video,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save :
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void save() {

        String title=inputTitle.getEditText().getText().toString().trim();
        String description=inputDescription.getEditText().getText().toString().trim();

        if (title.isEmpty())
        {
            inputTitle.setError("Enter Title");
            return;
        }

        if (description.isEmpty())
        {
            inputDescription.setError("Enter Description");
            return;
        }

        Intent intent=new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_DESCRIPTION,description);
        String date=getIntent().getStringExtra(EXTRA_DATE);
        if (date!=null)
            intent.putExtra(EXTRA_DATE,date);
        setResult(RESULT_OK,intent);
        finish();
    }
}