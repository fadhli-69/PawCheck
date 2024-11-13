import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pawcheck.R
import com.capstone.pawcheck.adapter.Drug

class DrugsAdapter(private val drugList: List<Drug>) : RecyclerView.Adapter<DrugsAdapter.DrugViewHolder>() {

    // ViewHolder untuk item `Drug`
    class DrugViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.drugImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvDrugName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDrugDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_drug, parent, false)
        return DrugViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val drug = drugList[position]

        // Set nama, deskripsi, dan gambar pada item
        holder.nameTextView.text = drug.name
        holder.descriptionTextView.text = drug.description
        holder.imageView.setImageResource(drug.imageResId)
    }

    override fun getItemCount(): Int {
        return drugList.size
    }
}
