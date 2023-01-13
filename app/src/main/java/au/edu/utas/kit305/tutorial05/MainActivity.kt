package au.edu.utas.kit305.tutorial05

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit305.tutorial05.databinding.ActivityMainBinding
import au.edu.utas.kit305.tutorial05.databinding.MyListItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()
{
    private lateinit var ui : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        ui.button.setOnClickListener {
            var i = Intent(ui.root.context, StudentSummary::class.java)
            startActivity(i)
        }
        ui.button3.setOnClickListener {
            var i = Intent(ui.root.context, WeekSummary::class.java)
            startActivity(i)
        }
        ui.button2.setOnClickListener {
            var i = Intent(ui.root.context, MarkTutorial::class.java)
            startActivity(i)
        }
    }


}

