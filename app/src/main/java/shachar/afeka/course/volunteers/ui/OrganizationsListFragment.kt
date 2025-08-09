package shachar.afeka.course.volunteers.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.EditOrganizationActivity
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.adapters.OrganizationsListAdapter
import shachar.afeka.course.volunteers.models.Organization
import shachar.afeka.course.volunteers.utilities.DBClient


class OrganizationsListFragment : Fragment() {
    private lateinit var _addBtn: MaterialButton
    private lateinit var _recyclerView: RecyclerView
    private var _organizations: List<Organization> = emptyList()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_organizations_list, container, false)

        findViews(view)
        initViews()
        loadOrganizations()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadOrganizations()
    }

    private fun loadOrganizations() {
        val user = firebaseAuth.currentUser

        if (user == null)
            return

        lifecycleScope.launch {
            _organizations = DBClient.getInstance().getOrganizationsByOwner(user.uid)
        }.invokeOnCompletion { _ ->
            val organizationsListAdapter = OrganizationsListAdapter(_organizations)
            _recyclerView.layoutManager = LinearLayoutManager(activity)
            _recyclerView.adapter = organizationsListAdapter
        }
    }

    private fun initViews() {
        _addBtn.setOnClickListener { _ ->
            startActivity(Intent(this.context, EditOrganizationActivity::class.java))
        }
    }

    private fun findViews(view1: View) {
        _addBtn = view1.findViewById(R.id.add_BTN)
        _recyclerView = view1.findViewById<RecyclerView>(R.id.organizations_RV)
    }
}