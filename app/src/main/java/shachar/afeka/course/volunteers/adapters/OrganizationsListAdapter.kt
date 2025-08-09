package shachar.afeka.course.volunteers.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shachar.afeka.course.volunteers.MapActivity
import shachar.afeka.course.volunteers.OrganizationVolunteeringsActivity
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.databinding.OrganizationsListItemBinding
import shachar.afeka.course.volunteers.models.Organization

class OrganizationsListAdapter(private val organizations: List<Organization>) :
    RecyclerView.Adapter<OrganizationsListAdapter.OrganizationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrganizationViewHolder {
        val binding =
            OrganizationsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OrganizationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrganizationViewHolder,
        position: Int
    ) {
        with(holder) {
            with(organizations[position]) {
                binding.organizationName.text = name
                binding.organizationAbout.text = about
                binding.organizationJoinedAt.text =
                    buildString {
                        append("Joined at: ")
                        append(createdAt)
                    }
                binding.organizationUpdatedAt.text =
                    buildString {
                        append("Updated at: ")
                        append(updatedAt)
                    }

                binding.btnShowOrganizationOnMap.setOnClickListener { _ ->
                    Intent(binding.root.context, MapActivity::class.java).apply {
                        putExtra(R.string.param_title.toString(), name)
                        putExtra(R.string.param_lon.toString(), headquarters.lon)
                        putExtra(R.string.param_lat.toString(), headquarters.lat)

                        binding.root.context.startActivity(this)
                    }
                }

                binding.btnShowOrganizationActivities.setOnClickListener { _ ->
                    Intent(
                        binding.root.context,
                        OrganizationVolunteeringsActivity::class.java
                    ).apply {
                        putExtra(R.string.param_organization_id.toString(), id)
                        binding.root.context.startActivity(this)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return organizations.size
    }

    inner class OrganizationViewHolder(val binding: OrganizationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}