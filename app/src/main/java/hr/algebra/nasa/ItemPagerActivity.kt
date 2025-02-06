package hr.algebra.nasa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.nasa.databinding.ActivityItemPagerBinding
import hr.algebra.nasa.adapter.ItemPagerAdapter
import hr.algebra.nasa.framework.fetchItems
import hr.algebra.nasa.model.Item

const val ITEM_POSITION = "hr.algebra.nasa.item_position"
class ItemPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemPagerBinding
    private lateinit var items: MutableList<Item>

    private var itemPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initPager() {
        items = fetchItems()
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        binding.viewPager.adapter = ItemPagerAdapter(this, items)
        binding.viewPager.currentItem = itemPosition
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}