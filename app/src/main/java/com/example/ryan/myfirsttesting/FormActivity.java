package com.example.ryan.myfirsttesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FormActivity extends AppCompatActivity {

    TextView nameError, emailError, phoneError, productError, qtyError;
    EditText nameForm, emailForm, phoneForm, qtyForm;
    Spinner productForm;
    String name, email, phone, product, qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //<summary>
        // This function binds every forms in the menu
        // </summary>
        nameForm = findViewById(R.id.cNameForm);
        emailForm = findViewById(R.id.emailForm);
        phoneForm = findViewById(R.id.phoneNumberForm);
        productForm = findViewById(R.id.productListForm);
        qtyForm = findViewById(R.id.productNumberForm);

        // <summary>
        // This function bind error text
        // </summary>
        nameError = findViewById(R.id.cnameError);
        nameError.setVisibility(View.INVISIBLE);
        nameError.setText("Your name input is empty");
        emailError = findViewById(R.id.emailError);
        emailError.setVisibility(View.INVISIBLE);
        emailError.setText("Your email input is empty");
        phoneError = findViewById(R.id.phoneError);
        phoneError.setVisibility(View.INVISIBLE);
        phoneError.setText("Your phone number input is empty");
        productError = findViewById(R.id.productListError);
        productError.setVisibility(View.INVISIBLE);
        productError.setText("Your selected product is invalid");
        qtyError = findViewById(R.id.productQtyError);
        qtyError.setVisibility(View.INVISIBLE);
        qtyError.setText("Your quantity input is empty");

        Button submitButton = findViewById(R.id.submitButtonForm);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                name = nameForm.getText().toString();
                email = emailForm.getText().toString();
                phone = phoneForm.getText().toString();
                product = productForm.getSelectedItem().toString();
                qty = qtyForm.getText().toString();
                sendForm(name, email, phone, product, qty);
            }
        });
    }

    public void sendForm(String name, String email, String phoneNumber, String product, String productNumber) {
        //The actual how the checking of empty form works
        if (checkEmpty(name, email, phoneNumber, productNumber)) {
            //When empty, you got error information and be focused on the empty field
            //From name, email, phone number, and product number
            if (name.matches("")) {
                nameError.setVisibility(View.VISIBLE);
                nameForm.requestFocus();
                Log.e("Error", "Your name input is empty");
            } else if (email.matches("")) {
                emailError.setVisibility(View.VISIBLE);
                emailForm.requestFocus();
                Log.e("Error", "Your email input is empty");
            } else if (phoneNumber.matches("")) {
                phoneError.setVisibility(View.VISIBLE);
                phoneForm.requestFocus();
                Log.e("Error", "Your phone number input is empty");
            } else if (productNumber.matches("")) {
                qtyError.setVisibility(View.VISIBLE);
                qtyForm.requestFocus();
                Log.e("Error", "Your product quantity input is empty");
            }
        }
        //When you fill everything
        else {
            //Check if there's a false pattern of email
            if (!checkPatternEmail(email)) {
                emailError.setText("Your email input is invalid");
                emailError.setVisibility(View.VISIBLE);
                emailForm.requestFocus();
                Log.e("Error", "Your email is invalid");
            } else if (product.matches("Pilih item")) {
                productError.setVisibility(View.VISIBLE);
                productForm.requestFocus();
                Log.e("Error", "Your product is not on the list");
            } else if (productNumber.matches("0")) {
                qtyError.setText("Your product quantity input must be more than 0");
                qtyError.setVisibility(View.VISIBLE);
                qtyForm.requestFocus();
                Log.e("Error", "Your product quantity input must be more than 0");
            }
            // <summary>
            // Your information is sent to database
            // </summary>
            else {
                // <summary>
                // Build new body contains of our input from the form
                // </summary>
                BackgroundTask sendDataAnonymously = new BackgroundTask(this);
                String method = "submitForm";
                sendDataAnonymously.execute(method, name, email, phoneNumber, product, productNumber);
                String outputDebug = name + " " + email + " " + phoneNumber + " " + product + " " + productNumber;
                Log.d("Debug Output", outputDebug);
            }
        }
    }

    //Checking if your email fulfilled the formatting
    protected boolean checkPatternEmail(CharSequence yourEmailInput) {
        return Patterns.EMAIL_ADDRESS.matcher(yourEmailInput).matches();
    }

    // Function to check empty form
    protected boolean checkEmpty(String inputName, String inputEmail, String inputPhoneNumber, String inputProductNumber) {
        return (inputName.matches("") || inputEmail.matches("") || inputPhoneNumber.matches("") || inputProductNumber.matches(""));
    }
}