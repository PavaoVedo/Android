package hr.algebra.nasa.adapter

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.nasa.R
import hr.algebra.nasa.ANIMAL_PROVIDER_CONTENT_URI
import hr.algebra.nasa.LocationWebActivity
import hr.algebra.nasa.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(
    private val context: Context,
    private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvBname = itemView.findViewById<TextView>(R.id.tvBname)
        internal val tvLocation = itemView.findViewById<TextView>(R.id.tvLocation)
        private val tvLastRecord = itemView.findViewById<TextView>(R.id.tvLastRecord)
        private val tvExplanation = itemView.findViewById<TextView>(R.id.tvExplanation)
        fun bind(item: Item) {
            tvName.text = item.binomialName
            tvBname.text = item.commonName
            tvLocation.text = item.location
            tvLastRecord.text = item.lastRecord
            tvExplanation.text = item.shortDesc
            ivRead.setImageResource(
                if(item.read) R.drawable.like_fill else R.drawable.like_empty
            )
            Picasso.get()
                .load(File(item.imageSrc))
                .error(R.drawable.dino_splash)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivRead.setOnClickListener {
            updateItem(position)
        }
        holder.bind(items[position])
        holder.tvLocation.setOnClickListener { view ->
            val location = items[position].location

            val intent = Intent(view.context, LocationWebActivity::class.java).apply {
                putExtra(LocationWebActivity.EXTRA_LOCATION, location)
            }
            view.context.startActivity(intent)
        }
    }

    private fun updateItem(position: Int) {
        val item = items[position]
        item.read = !item.read
        context.contentResolver.update(
            ContentUris.withAppendedId(ANIMAL_PROVIDER_CONTENT_URI, item._id!!),
            ContentValues().apply {
                put(Item::read.name, item.read)
            },
            null,
            null
        )
        notifyItemChanged(position)
        sendReadNotification(item)
    }

    @SuppressLint("MissingPermission")
    private fun sendReadNotification(item: Item) {

        val notificationManager = NotificationManagerCompat.from(context)


        val channelId = "read_toggle_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "ReadToggleChannel"
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }


        val message = if (item.read) {
            "You liked ${item.binomialName}"
        } else {
            "You unliked ${item.binomialName}"
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Read Toggle")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        notificationManager.notify(item._id!!.toInt(), builder.build())
    }

}