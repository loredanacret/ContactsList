package  com.project.lrdn.contactslist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editName, editPhone, editEmail, editAddress, editWeb;
    private String[] newEntry = new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editWeb = (EditText) findViewById(R.id.editWeb);

        btnAdd = (Button) findViewById(R.id.btnDelete);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newEntry[1] = editName.getText().toString();
                newEntry[2] = editEmail.getText().toString();
                newEntry[3] = editWeb.getText().toString();
                newEntry[4] = editPhone.getText().toString();
                newEntry[5] = editAddress.getText().toString();
                //name and phone are mandatory fields
                if ( editName.length() != 0 & editPhone.length() != 0 ) {
                    if (android.util.Patterns.PHONE.matcher(newEntry[4]).matches()) {
                        // if there is an email, check to be a valid one
                        if ( editEmail.length() == 0 || android.util.Patterns.EMAIL_ADDRESS.matcher(newEntry[2]).matches()) {
                            AddData(newEntry);
                            editName.setText("");
                            editEmail.setText("");
                            editWeb.setText("");
                            editPhone.setText("");
                            editAddress.setText("");
                        } else {toastMessage("You must set a valid email address");}
                    }else{toastMessage("You must set a valid phone number");}
                } else {toastMessage("You must set a name and a phone number!");}

            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });

    }

    public void AddData(String[] newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
