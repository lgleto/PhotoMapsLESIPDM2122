package ipca.example.photomap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val firebaseAuth = Firebase.auth
            if (firebaseAuth.currentUser!=null){
                val intent = Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(
                    this@SplashScreenActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        },1000L)

    }
}