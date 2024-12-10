import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pawcheck.R
import com.capstone.pawcheck.adapter.Drug
import com.capstone.pawcheck.views.drugpage.DrugDetailActivity

class DrugsAdapter(private val drugList: MutableList<Drug>) : RecyclerView.Adapter<DrugsAdapter.DrugViewHolder>() {

    class DrugViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.drugImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvDrugName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDrugDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drug, parent, false)
        return DrugViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val drug = drugList[position]

        holder.nameTextView.text = drug.name
        holder.descriptionTextView.text = drug.description
        holder.imageView.setImageResource(drug.imageResId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DrugDetailActivity::class.java).apply {
                putExtra("DRUG_NAME", drug.name)
                putExtra("DRUG_DESCRIPTION", drug.description)
                putExtra("DRUG_IMAGE", drug.imageResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return drugList.size
    }

    fun updateList(newDrugs: List<Drug>) {
        drugList.clear() // Menghapus data lama
        drugList.addAll(newDrugs) // Menambahkan data baru
        notifyDataSetChanged() // Memberitahu adapter bahwa data telah berubah
    }
}
