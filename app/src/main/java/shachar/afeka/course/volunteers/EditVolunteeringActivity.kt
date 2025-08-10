package shachar.afeka.course.volunteers

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import shachar.afeka.course.volunteers.ui.FormWithMapSearchActivity

class EditVolunteeringActivity : FormWithMapSearchActivity(
    R.layout.activity_edit_volunteering,
    R.id.volunteering_main_FRAME_map,
    R.id.volunteering_place_search_fragment,
    R.id.volunteering_place_search_button
) {
    private lateinit var _categoriesSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
        super.initViews()
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

    override fun findViews() {
        super.findViews()
        _categoriesSpinner = findViewById(R.id.volunteering_category_spinner)
    }
}