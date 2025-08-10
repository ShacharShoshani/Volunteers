package shachar.afeka.course.volunteers

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.ui.FormWithMapSearchActivity
import shachar.afeka.course.volunteers.utilities.DBClient
import shachar.afeka.course.volunteers.utilities.SignalManager

class EditVolunteeringActivity : FormWithMapSearchActivity(
    R.layout.activity_edit_volunteering,
    R.id.volunteering_main_FRAME_map,
    R.id.volunteering_place_search_fragment,
    R.id.volunteering_place_search_button
) {
    private lateinit var _categoriesSpinner: Spinner
    private lateinit var _nameInput: TextInputEditText
    private lateinit var _aboutInput: TextInputEditText
    private lateinit var _saveBtn: MaterialButton
    private var _organizationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _organizationId = intent.getStringExtra(R.string.param_organization_id.toString())

        if (_organizationId == null)
            finish()
    }

    override fun initViews() {
        super.initViews()
        initCategoriesSpinner()
        initSaveButton()
    }

    override fun findViews() {
        super.findViews()
        _categoriesSpinner = findViewById(R.id.volunteering_category_spinner)
        _nameInput = findViewById(R.id.volunteering_name_text_input)
        _aboutInput = findViewById(R.id.volunteering_about_text_input)
        _saveBtn = findViewById(R.id.volunteering_save_BTN)
    }

    private fun initCategoriesSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                _categoriesSpinner.adapter = adapter
            }
    }

    private fun initSaveButton() {
        _saveBtn.setOnClickListener { _ ->
            updateControlsEnabledState()

            lifecycleScope.launch {
                if (_organizationId != null) {
                    try {
                        DBClient.getInstance().addVolunteering(
                            _nameInput.text.toString(),
                            _categoriesSpinner.selectedItem.toString(),
                            _aboutInput.text.toString(),
                            location.lat,
                            location.lon,
                            emptyList(),
                            _organizationId!!
                        )
                    } catch (err: Throwable) {
                        val message = if (err.message == null) err.toString() else err.message

                        Log.e("DB", message, err)
                        SignalManager.getInstance().toast(err.toString())
                    }
                }
            }.invokeOnCompletion { finish() }
        }
    }

    private fun updateControlsEnabledState() {
        placeSearch.isEnabled = !placeSearch.isEnabled
        placeSearchFragment.selectable = !placeSearchFragment.selectable
        _nameInput.isEnabled = !_nameInput.isEnabled
        _aboutInput.isEnabled = !_aboutInput.isEnabled
        _saveBtn.isEnabled = !_saveBtn.isEnabled
    }
}