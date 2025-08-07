package shachar.afeka.course.volunteers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class UserEditFragment : Fragment() {
    private lateinit var _nameInput: TextInputEditText
    private lateinit var _residenceInput: TextInputEditText
    private lateinit var _emailInput: TextInputEditText
    private lateinit var _phoneInput: TextInputEditText
    private lateinit var _saveBtn: MaterialButton

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_edit, container, false)

        findViews(view)

        user = firebaseAuth.currentUser

        initViews()

        return view
    }

    private fun initViews() {
        if (user != null)
            lifecycleScope.launch {
                loadUserProfile()
            }

        _saveBtn.setOnClickListener { _ ->
            if (user != null)
                updateControlsEnabledState()

            lifecycleScope.launch {
                try {
                    DBClient.getInstance().updateUser(
                        user!!.uid,
                        _nameInput.text.toString(),
                        _emailInput.text.toString(),
                        _phoneInput.text.toString(),
                        _residenceInput.text.toString()
                    )
                } catch (err: Throwable) {
                    SignalManager.getInstance().toast(err.toString())
                }

            }.invokeOnCompletion { _ -> updateControlsEnabledState() }
        }
    }

    private fun findViews(view1: View) {
        _nameInput = view1.findViewById(R.id.username_text_input)
        _residenceInput = view1.findViewById(R.id.residence_text_input)
        _emailInput = view1.findViewById(R.id.email_text_input)
        _phoneInput = view1.findViewById(R.id.phone_text_input)
        _saveBtn = view1.findViewById(R.id.save_BTN)
    }

    private suspend fun loadUserProfile() {
        try {
            updateControlsEnabledState()

            val profile = DBClient.getInstance().getUserByUID(user!!.uid)

            if (profile == null)
                return

            _nameInput.setText(profile.name)
            _residenceInput.setText(profile.residence)
            _emailInput.setText(profile.email)
            _phoneInput.setText(profile.phone)

            updateControlsEnabledState()
        } catch (err: Throwable) {
            SignalManager.getInstance().toast(err.toString())
        }

    }

    private fun updateControlsEnabledState() {
        _nameInput.isEnabled = !_nameInput.isEnabled
        _residenceInput.isEnabled = !_residenceInput.isEnabled
        _emailInput.isEnabled = !_emailInput.isEnabled
        _phoneInput.isEnabled = !_phoneInput.isEnabled
        _saveBtn.isEnabled = !_saveBtn.isEnabled
    }
}