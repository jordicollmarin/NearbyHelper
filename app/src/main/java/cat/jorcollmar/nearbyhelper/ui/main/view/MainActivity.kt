package cat.jorcollmar.nearbyhelper.ui.main.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cat.jorcollmar.nearbyhelper.R
import cat.jorcollmar.nearbyhelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Replace them Splash -> NoActionBar
        setTheme(R.style.AppTheme_NoActionBar)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> onFilterClicked()
            R.id.action_order_by -> onOrderByClicked()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFilterClicked(): Boolean {
        // TODO: Open filter for (Cafes, Restaurant, Bars)
        return true
    }

    private fun onOrderByClicked(): Boolean {
        // TODO: Open Order by (Name, Open/Closed, etc.)
        return true
    }
}