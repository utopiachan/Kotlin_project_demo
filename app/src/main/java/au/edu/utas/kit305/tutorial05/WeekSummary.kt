package au.edu.utas.kit305.tutorial05

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit305.tutorial05.databinding.ActivityWeekSummaryBinding
import au.edu.utas.kit305.tutorial05.databinding.MyListItem1Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.round

var week:String = ""
var mark:String? = null

class WeekSummary : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var ui: ActivityWeekSummaryBinding
    override fun onResume() {
        super.onResume()
        val db = Firebase.firestore

        var studentsCollection = db.collection("students")

        studentsCollection
                .get()
                .addOnSuccessListener { result ->
                    Log.d(FIREBASE_TAG, "--- all movies ---")
                    items.clear()
                    (ui.weekList.adapter as StudentAdapter).notifyDataSetChanged()
                    for (document in result) {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val student = document.toObject<Student>()
                        student.id = document.id
                        Log.d(FIREBASE_TAG, student.toString())

                        items.add(student)
                        (ui.weekList.adapter as StudentAdapter).notifyDataSetChanged()
                    }

                }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityWeekSummaryBinding.inflate(layoutInflater)
        setContentView(ui.root)
        ui.weekList.adapter = StudentAdapter(students = items)
        ui.weekList.layoutManager = LinearLayoutManager(this)
        //get db connection
        val db = Firebase.firestore
        ArrayAdapter.createFromResource(
                this,
                R.array.weekArray,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            ui.spinner.adapter = adapter
        }

        ui.spinner.onItemSelectedListener = this
        ui.button6.setOnClickListener() {
            finish()
        }
    }


    inner class StudentHolder(var ui: MyListItem1Binding) : RecyclerView.ViewHolder(ui.root) {}

    inner class StudentAdapter(private val students: MutableList<Student>) : RecyclerView.Adapter<StudentHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekSummary.StudentHolder {
            val ui = MyListItem1Binding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return StudentHolder(ui)

        }

        override fun getItemCount(): Int {
            return students.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: WeekSummary.StudentHolder, position: Int) {
            val student = students[position]   //get the data at the requested position
            holder.ui.root.setOnClickListener {
                var i = Intent(holder.ui.root.context, StudentDetails::class.java)
                i.putExtra(STUDENT_INDEX, position)
                startActivity(i)
            }
            mark=""
            holder.ui.txtName1.text = "Name: "+student.title.toString()
            holder.ui.txtID1.text="ID: "+student.sID.toString()
           when (week) {
               "week1"->if (student.week1.toString() == "100") { mark = "Attended" }else
                   if(student.week1.toString() == "0") { mark = "NOT Attended" }
               "week2"->if(student.week2.toString() == "100"){ mark = "Attended" }else
                   if(student.week2.toString() == "0"){ mark = "NOT Attended" }
               "week3"->if (student.week3.toString() == "100") { mark = "Attended" }else
                   if (student.week3.toString() == "0") { mark = "NOT Attended" }
               "week4"->if(student.week4.toString() == "100"){ mark = "Attended" }else
                   if(student.week4.toString() == "0"){ mark = "NOT Attended" }

               "week5"->if (student.week5.toString() == "100") { mark = "2 Point" }
               else if(student.week5.toString() == "50"){ mark = "1 Point" }
               else if (student.week5.toString() == "0") { mark = "0 Point" }
               "week7"->if(student.week7.toString() == "100"){ mark = "HD+" }
               else if (student.week7.toString() == "80") { mark = "HD" }
               else if(student.week7.toString() == "70"){ mark = "DN" }
               else if (student.week7.toString() == "60") { mark = "CR" }
               else if(student.week7.toString() == "50"){ mark = "PP" }
               else if (student.week7.toString() == "0") { mark = "NN" }
               "week8"->if(student.week8.toString() == "100"){ mark = "A" }
               else if (student.week8.toString() == "80") { mark = "B" }
               else if(student.week8.toString() == "70"){ mark = "C" }
               else if (student.week8.toString() == "60") { mark = "D" }
               else if(student.week8.toString() == "0"){ mark = "F" }
           }

            when (week){
                "week1"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week2"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week3"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week4"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week5"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week6"-> holder.ui.txtAvg1.text="Mark: "+student.week6.toString()
                "week7"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week8"-> holder.ui.txtAvg1.text= "Mark: $mark"
                "week9"-> holder.ui.txtAvg1.text="Mark: "+student.week9.toString()
                "week10"-> holder.ui.txtAvg1.text="Mark: "+student.week10.toString()
                "week11"-> holder.ui.txtAvg1.text="Mark: "+student.week11.toString()
                "week12"-> holder.ui.txtAvg1.text="Mark: "+student.week12.toString()
                "week13"-> holder.ui.txtAvg1.text="Mark: "+student.week13.toString()



            }


        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val numbers = listOf("week1", "week2", "week3", "week4", "week5", "week6", "week7", "week8", "week9", "week10", "week11", "week12", "week13")
        week = numbers[position]
        val db = Firebase.firestore
        var studentsCollection = db.collection("students")
        var countStudent = 0
        var WeekTotalMark = 0.0
        studentsCollection
                .get()
                .addOnSuccessListener { result ->
                    Log.d(FIREBASE_TAG, "--- all movies ---")
                    items.clear()
                    (ui.weekList.adapter as WeekSummary.StudentAdapter).notifyDataSetChanged()
                    for (document in result) {
                        //Log.d(FIREBASE_TAG, document.toString())
                        val student = document.toObject<Student>()
                        student.id = document.id
                        countStudent += 1
                        when (week){
                            "week1"-> WeekTotalMark += student.week1.toString().toDouble()
                            "week2"-> WeekTotalMark += student.week2.toString().toDouble()
                            "week3"-> WeekTotalMark += student.week3.toString().toDouble()
                            "week4"-> WeekTotalMark += student.week4.toString().toDouble()
                            "week5"-> WeekTotalMark += student.week5.toString().toDouble()
                            "week6"-> WeekTotalMark += student.week6.toString().toDouble()
                            "week7"-> WeekTotalMark += student.week7.toString().toDouble()
                            "week8"-> WeekTotalMark += student.week8.toString().toDouble()
                            "week9"-> WeekTotalMark += student.week9.toString().toDouble()
                            "week10"-> WeekTotalMark += student.week10.toString().toDouble()
                            "week11"-> WeekTotalMark += student.week11.toString().toDouble()
                            "week12"-> WeekTotalMark += student.week12.toString().toDouble()
                            "week13"-> WeekTotalMark += student.week13.toString().toDouble()
                        }

                        Log.d(FIREBASE_TAG, student.toString())
                        items.add(student)
                        (ui.weekList.adapter as WeekSummary.StudentAdapter).notifyDataSetChanged()
                    }
                    ui.textView7.text="Week Average: "+(WeekTotalMark/countStudent).toString()+"%"
                }


    }
}
