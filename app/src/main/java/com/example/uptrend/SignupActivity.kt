// SignUpActivity.kt
// This activity handles the user registration process using standard Android widgets.

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.uptrend.LoginActivity
import com.example.uptrend.R

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Find views by their IDs from the layout file
        // We now use the standard EditText widget.
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val loginTextView = findViewById<TextView>(R.id.loginTextView)

        // Set a click listener for the Sign Up button
        signUpButton.setOnClickListener {
            // Retrieve the user input from the EditText fields
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Basic validation to ensure fields are not empty
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // --- TODO: Add your user registration logic here ---
                // For example, you would make a call to your backend or Firebase Authentication
                // to create a new user with the provided credentials.
                // For now, we'll just show a success message.

                Toast.makeText(this, "Account created for $name!", Toast.LENGTH_SHORT).show()

                // Optionally, navigate to the main part of the app after successful sign-up
                // val intent = Intent(this, MainActivity::class.java)
                // startActivity(intent)
                // finish() // Close the SignUpActivity
            } else {
                // Show an error message if any of the fields are empty
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set a click listener to navigate to the Login screen
        loginTextView.setOnClickListener {
            // The Intent will navigate the user to the LoginActivity screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
