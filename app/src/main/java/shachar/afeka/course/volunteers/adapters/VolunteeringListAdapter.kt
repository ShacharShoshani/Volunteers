package shachar.afeka.course.volunteers.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import shachar.afeka.course.volunteers.MapActivity
import shachar.afeka.course.volunteers.R
import shachar.afeka.course.volunteers.databinding.VolunteeringListItemBinding
import shachar.afeka.course.volunteers.models.Volunteering

class VolunteeringListAdapter(private val volunteering: List<Volunteering>) :
    RecyclerView.Adapter<VolunteeringListAdapter.VolunteeringViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VolunteeringViewHolder {
        val binding =
            VolunteeringListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return VolunteeringViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: VolunteeringViewHolder,
        position: Int
    ) {
        with(holder) {
            with(volunteering[position]) {
                binding.volunteeringName.text = name
                binding.volunteeringCategory.text = category
                binding.volunteeringAbout.text = about
                binding.volunteeringCreatedAt.text =
                    buildString {
                        append("Created at: ")
                        append(createdAt)
                    }
                binding.volunteeringUpdatedAt.text =
                    buildString {
                        append("Updated at: ")
                        append(updatedAt)
                    }

                binding.btnShowVolunteeringOnMap.setOnClickListener { _ ->
                    Intent(binding.root.context, MapActivity::class.java).apply {
                        putExtra(R.string.param_title.toString(), name)
                        putExtra(R.string.param_lon.toString(), place.lon)
                        putExtra(R.string.param_lat.toString(), place.lat)

                        binding.root.context.startActivity(this)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return volunteering.size
    }

    inner class VolunteeringViewHolder(val binding: VolunteeringListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}