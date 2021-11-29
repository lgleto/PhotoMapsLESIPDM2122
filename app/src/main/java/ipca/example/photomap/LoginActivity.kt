package ipca.example.photomap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ipca.example.photomap.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonLogin.isEnabled = false

        auth = Firebase.auth

        binding.editTextEmail.doOnTextChanged { text, start, before, count ->
            binding.buttonLogin.isEnabled =
                text.toString().isValidEmail() &&
                        binding.editTextPassword.text.toString().isValidPassword()
        }
        binding.editTextPassword.doOnTextChanged { text, start, before, count ->
            binding.buttonLogin.isEnabled =
                binding.editTextEmail.text.toString().isValidEmail() &&
                        text.toString().isValidPassword()
        }

        binding.buttonLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        //val user = auth.currentUser
                        val intent = Intent(this@LoginActivity , MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object{
        const val TAG = "LoginActivity"
    }

}