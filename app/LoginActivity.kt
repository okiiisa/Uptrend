// LoginActivity.kt
// This activity handles the user login process.

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find views by their IDs from the layout file
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)

        // Set a click listener for the Login button
        loginButton.setOnClickListener {
            // Retrieve the user input
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Basic validation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // --- TODO: Add your user authentication logic here ---
                // For example, you would make a call to your backend or Firebase Authentication
                // to verify the user's credentials.
                // For now, we'll just show a success message.

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // After successful login, navigate to the main part of your app
                // val intent = Intent(this, MainActivity::class.java)
                // // Clear the activity stack so the user can't go back to the login screen
                // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // startActivity(intent)

            } else {
                // Show an error message if fields are empty
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set a click listener to navigate to the Sign Up screen
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
