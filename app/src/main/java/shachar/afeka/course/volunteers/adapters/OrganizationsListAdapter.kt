package shachar.afeka.course.volunteers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            }
        }
    }

    override fun getItemCount(): Int {
        return organizations.size
    }

    inner class OrganizationViewHolder(val binding: OrganizationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}