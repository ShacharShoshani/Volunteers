package shachar.afeka.course.volunteers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.adapters.VolunteeringListAdapter
import shachar.afeka.course.volunteers.models.Volunteering
import shachar.afeka.course.volunteers.utilities.DBClient

class VolunteeringListFragment : Fragment() {
    private lateinit var _recyclerView: RecyclerView
    private var _volunteeringList: List<Volunteering> = emptyList()
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_volunteering_list, container, false)
        findViews(view)
        loadVolunteerList()

        return view
    }

    override fun onResume() {
        super.onResume()
        loadVolunteerList()
    }

    private fun loadVolunteerList() {
        val user = firebaseAuth.currentUser

        if (user == null)
            return

        lifecycleScope.launch {
            _volunteeringList = DBClient.getInstance().getVolunteersByOrganizationOwner(user.uid)
        }.invokeOnCompletion { _ ->
            val volunteeringListAdapter = VolunteeringListAdapter(_volunteeringList)
            _recyclerView.layoutManager = LinearLayoutManager(activity)
            _recyclerView.adapter = volunteeringListAdapter
        }
    }

    private fun findViews(view1: View) {
        _recyclerView = view1.findViewById<RecyclerView>(R.id.volunteering_RV)
    }
}