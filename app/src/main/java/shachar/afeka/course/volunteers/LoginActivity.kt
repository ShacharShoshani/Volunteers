package shachar.afeka.course.volunteers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class LoginActivity : AppCompatActivity() {
    private val _signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        SignalManager.init(this)
        DBClient.init()

        if (FirebaseAuth.getInstance().currentUser == null) {
            signIn()
        } else {
            transactToMainActivity()
        }
    }

    private fun transactToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun signIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_Volunteers)
//            .setLogo(R.drawable.)
            .build()
        _signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                addToDBIfNotExist()
            }.invokeOnCompletion { _ -> transactToMainActivity() }
        } else {
            SignalManager.getInstance().toast("Sign in failed.")
            signIn()
        }
    }

    private suspend fun addToDBIfNotExist() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!

        if (DBClient.getInstance().userExist(firebaseUser.uid))
            return
        else {
            DBClient.getInstance().addUser(
                firebaseUser.uid,
                firebaseUser.displayName,
                firebaseUser.email,
                firebaseUser.phoneNumber,
            )
        }
    }
}