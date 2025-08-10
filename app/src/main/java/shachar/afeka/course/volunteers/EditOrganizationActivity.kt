package shachar.afeka.course.volunteers

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.ui.FormWithMapSearchActivity
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class EditOrganizationActivity :
    FormWithMapSearchActivity(
        R.layout.activity_edit_organization,
        R.id.main_FRAME_map,
        R.id.place_search_fragment,
        R.id.search_button
    ) {
    private lateinit var _saveBtn: MaterialButton
    private lateinit var _nameInput: TextInputEditText
    private lateinit var _aboutInput: TextInputEditText
    private val _firebaseAuth = FirebaseAuth.getInstance()
    private var _user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _user = _firebaseAuth.currentUser
    }

    override fun findViews() {
        super.findViews()
        _saveBtn = findViewById(R.id.organization_save_BTN)
        _nameInput = findViewById(R.id.organization_name_text_input)
        _aboutInput = findViewById(R.id.organization_about_text_input)
    }

    override fun initViews() {
        super.initViews()

        _saveBtn.setOnClickListener { _ ->
            if (_user != null) {
                updateControlsEnabledState()

                lifecycleScope.launch {
                    try {
                        DBClient.getInstance().addOrganization(
                            _nameInput.text.toString(),
                            _aboutInput.text.toString(),
                            location.lat,
                            location.lon,
                            _user!!.uid
                        )
                    } catch (err: Throwable) {
                        SignalManager.getInstance().toast(err.toString())
                    }
                }.invokeOnCompletion { finish() }
            }
        }
    }

    private fun updateControlsEnabledState() {
        placeSearch.isEnabled = !placeSearch.isEnabled
        placeSearchFragment.selectable = !placeSearchFragment.selectable
        _saveBtn.isEnabled = !_saveBtn.isEnabled
    }
}