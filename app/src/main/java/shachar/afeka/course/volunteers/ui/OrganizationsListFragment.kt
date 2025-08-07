package shachar.afeka.course.volunteers.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import shachar.afeka.course.volunteers.AddOrganizationActivity
import shachar.afeka.course.volunteers.R


class OrganizationsListFragment : Fragment() {
    private lateinit var _addBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_organizations_list, container, false)

        findViews(view)
        initViews()

        return view
    }

    private fun initViews() {
        _addBtn.setOnClickListener { _ ->
            startActivity(Intent(this.context, AddOrganizationActivity::class.java))
        }
    }

    private fun findViews(view1: View) {
        _addBtn = view1.findViewById(R.id.add_BTN)
    }
}