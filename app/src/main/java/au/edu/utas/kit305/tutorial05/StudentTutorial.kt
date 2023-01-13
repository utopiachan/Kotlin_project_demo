package au.edu.utas.kit305.tutorial05

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import au.edu.utas.kit305.tutorial05.databinding.ActivityStudentTutorialBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class StudentTutorial : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var ui: ActivityStudentTutorialBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = FirebaseStorage.getInstance()


        ui = ActivityStudentTutorialBinding.inflate(layoutInflater)
        setContentView(ui.root)


        val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
        var studentObject = items[studentID]
        ArrayAdapter.createFromResource(
                this,
                R.array.Normal,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            ui.spinner3.adapter = adapter}
        ui.spinner3.onItemSelectedListener = this
        when (week) {
            "week1" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.Attend,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week2" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.Attend,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week3" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.Attend,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week4" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.Attend,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week5" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.checkpoint,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week7" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.HDGrade,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
            "week8" -> ArrayAdapter.createFromResource(
                    this,
                    R.array.ABCGrade,
                    android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                ui.spinner3.adapter = adapter
            }
        }
        ui.buttonsave.setOnClickListener() {
            when (week) {
                "week1" -> if (ui.spinner3.selectedItem.toString() == "Attended") {
                    studentObject.week1 = 100
                } else {
                    studentObject.week1 = 0
                }
                "week2" -> if (ui.spinner3.selectedItem.toString() == "Attended") {
                    studentObject.week2 = 100
                } else {
                    studentObject.week2 = 0
                }
                "week3" -> if (ui.spinner3.selectedItem.toString() == "Attended") {
                    studentObject.week3 = 100
                } else {
                    studentObject.week3 = 0
                }
                "week4" -> if (ui.spinner3.selectedItem.toString() == "Attended") {
                    studentObject.week4 = 100
                } else {
                    studentObject.week4 = 0
                }
                "week5" -> if (ui.spinner3.selectedItem.toString() == "2") {
                    studentObject.week5 = 100
                } else
                    if (ui.spinner3.selectedItem.toString() == "1") {
                        studentObject.week5 = 50
                    } else {
                        studentObject.week5 = 0
                    }
                "week6"->studentObject.week6=ui.spinner3.selectedItem.toString().toInt()
                "week7" -> if (ui.spinner3.selectedItem.toString() == "HD+") {
                    studentObject.week7 = 100
                } else
                    if (ui.spinner3.selectedItem.toString() == "HD") {
                        studentObject.week7 = 80
                    } else
                        if (ui.spinner3.selectedItem.toString() == "DN") {
                            studentObject.week7 = 70
                        } else
                            if (ui.spinner3.selectedItem.toString() == "CR") {
                                studentObject.week7 = 60
                            } else
                                if (ui.spinner3.selectedItem.toString() == "PP") {
                                    studentObject.week7 = 50
                                } else
                                    if (ui.spinner3.selectedItem.toString() == "NN") {
                                        studentObject.week7 = 0
                                    }
                "week8" -> if (ui.spinner3.selectedItem.toString() == "A") {
                    studentObject.week7 = 100
                } else
                    if (ui.spinner3.selectedItem.toString() == "B") {
                        studentObject.week8 = 80
                    } else
                        if (ui.spinner3.selectedItem.toString() == "C") {
                            studentObject.week7 = 70
                        } else
                            if (ui.spinner3.selectedItem.toString() == "D") {
                                studentObject.week7 = 60
                            } else
                                if (ui.spinner3.selectedItem.toString() == "F") {
                                    studentObject.week7 = 0
                                }

                "week9"->studentObject.week9=ui.spinner3.selectedItem.toString().toInt()

                "week10"->studentObject.week10=ui.spinner3.selectedItem.toString().toInt()

                "week11"->studentObject.week11=ui.spinner3.selectedItem.toString().toInt()

                "week12"->studentObject.week12=ui.spinner3.selectedItem.toString().toInt()

                "week13"->studentObject.week13=ui.spinner3.selectedItem.toString().toInt()

            }


            val db = Firebase.firestore
            var studentsCollection = db.collection("students")
            studentsCollection.document(studentObject.id!!)
                    .set(studentObject)
                    .addOnSuccessListener {
                        Log.d(FIREBASE_TAG, "Successfully updated student ${studentObject?.id}")
                        //return to the list

                        finish()
                    }

        }
        ui.buttoncan.setOnClickListener(){
            finish()
        }
        if (studentObject.url!==null) {
            Glide.with(this)
                    .asBitmap()
                    .load(studentObject.url)
                    .into(ui.imageView3);
        }
        ui.textName.text="Student Name:"+studentObject.title.toString()
        ui.textID.text="Student ID:"+studentObject.sID.toString()
        ui.TextWeek.text="Current Week:"+week

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val test=ui.spinner3.selectedItem.toString()

    }

}