package au.edu.utas.kit305.tutorial05

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit305.tutorial05.databinding.ActivityStudentSummaryBinding
import au.edu.utas.kit305.tutorial05.databinding.MyListItemBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.round

val items = mutableListOf<Student>()
const val FIREBASE_TAG = "FirebaseLogging"
const val STUDENT_INDEX = "Student_Index"

class StudentSummary : AppCompatActivity()
{
    private lateinit var ui : ActivityStudentSummaryBinding
    override fun onResume() {
        super.onResume()
        val db = Firebase.firestore

        var studentsCollection = db.collection("students")
        ui.lblStudentCount.text = "Loading..."
        studentsCollection
            .get()
            .addOnSuccessListener { result ->
                Log.d(FIREBASE_TAG, "--- all movies ---")
                items.clear()
                (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                for (document in result)
                {
                    //Log.d(FIREBASE_TAG, document.toString())
                    val student = document.toObject<Student>()
                    student.id = document.id
                    Log.d(FIREBASE_TAG, student.toString())

                    items.add(student)
                    (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                }
                ui.lblStudentCount.text = "Finish"
            }





    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityStudentSummaryBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.lblStudentCount.text = "${items.size} Students"
        ui.myList.adapter = StudentAdapter(students = items)
        ui.button5.setOnClickListener(){
            var i = Intent(ui.root.context, MainActivity::class.java)
            startActivity(i)
        }

        //vertical list
        ui.myList.layoutManager = LinearLayoutManager(this)
        //get db connection
        val db = Firebase.firestore
        //add some data (comment this out after running the program once and confirming your data is there)
       /* val lotr = Student(
            title = "Lord of the Rings: Fellowship of the Ring",
            year = 2001,
            duration = 9001F
        )
        var studentsCollection = db.collection("students")
        studentsCollection
            .add(lotr)
            .addOnSuccessListener {
                Log.d(FIREBASE_TAG, "Document created with id ${it.id}")
                lotr.id = it.id
            }
            .addOnFailureListener {
                Log.e(FIREBASE_TAG, "Error writing document", it)
            }*/
        //get all movies
        ui.lblStudentCount.text = "Loading..."
        var studentsCollection = db.collection("students")
        studentsCollection
            .get()
            .addOnSuccessListener { result ->
                items.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                Log.d(FIREBASE_TAG, "--- all Students ---")
                for (document in result)
                {
                    //Log.d(FIREBASE_TAG, document.toString())
                    val student = document.toObject<Student>()
                    student.id = document.id
                    Log.d(FIREBASE_TAG, student.toString())

                    items.add(student)
                }
                (ui.myList.adapter as StudentAdapter).notifyDataSetChanged()
                ui.lblStudentCount.text = "finished"

            }
        ui.AddButton.setOnClickListener {
            var i = Intent(ui.root.context, AddStudent::class.java)
            startActivity(i)
        }
    }

    inner class StudentHolder(var ui: MyListItemBinding) : RecyclerView.ViewHolder(ui.root) {}

    inner class StudentAdapter(private val students: MutableList<Student>) : RecyclerView.Adapter<StudentHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentSummary.StudentHolder {
            val ui = MyListItemBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return StudentHolder(ui)

        }

        override fun getItemCount(): Int {
            return students.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: StudentSummary.StudentHolder, position: Int) {
            val student = students[position]   //get the data at the requested position
            holder.ui.root.setOnClickListener {
                var i = Intent(holder.ui.root.context, StudentDetails::class.java)
                i.putExtra(STUDENT_INDEX, position)
                startActivity(i)
            }
            holder.ui.imageView2.setImageDrawable(null)
            holder.ui.txtName.text = student.title.toString()
            holder.ui.txtID.text=student.sID.toString()
            var totalMark:Double=0.0

            totalMark=(student.week1!!.toDouble()+student.week2!!.toDouble()+student.week3!!.toDouble()+
                    student.week4!!.toDouble()+student.week5!!.toDouble()+student.week6!!.toDouble()+
                    student.week7!!.toDouble()+student.week8!!.toDouble()+student.week9!!.toDouble()+
                    student.week10!!.toDouble()+student.week11!!.toDouble()+student.week12!!.toDouble()+student.week13!!.toDouble())
            if (totalMark>=0){
            holder.ui.txtAvg.text="Avg: "+(round(totalMark/13*100)/100).toString()+"%"}

                else{ holder.ui.txtAvg.text="Avg:0%"}

            if (student.url!=null) {
                Glide.with(this@StudentSummary)
                    .asBitmap()
                    .load(student.url)
                    .into(holder.ui.imageView2);
            }
            ui.lblStudentCount.text=(itemCount.toString()+" Students")
            
        }
    }
}

