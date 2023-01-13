package au.edu.utas.kit305.tutorial05


import android.Manifest
import android.R
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import au.edu.utas.kit305.tutorial05.databinding.ActivityStudentDetailBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round


const val REQUEST_IMAGE_CAPTURE = 1

// Create a storage reference from our app


class StudentDetails : AppCompatActivity() {
    private lateinit var ui: ActivityStudentDetailBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = FirebaseStorage.getInstance()


        ui = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(ui.root)

        val studentID = intent.getIntExtra(STUDENT_INDEX, -1)
        var studentObject = items[studentID]
        ui.txtDuration.text = Editable.Factory.getInstance().newEditable(studentObject.title)
        ui.txtID.text=Editable.Factory.getInstance().newEditable(studentObject.sID.toString())
        if(studentObject.week1.toString()== "100"){ui.checkBoxW1.isChecked=true}
        if(studentObject.week2.toString()== "100"){ui.checkBoxW2.isChecked=true}
        if(studentObject.week3.toString()== "100"){ui.checkBoxW3.isChecked=true}
        if(studentObject.week4.toString()== "100"){ui.checkBoxW4.isChecked=true}
        if(studentObject.week5.toString()=="100"){ui.week5M.text="2"}
        if(studentObject.week5.toString()=="50"){ui.week5M.text="1"}
        ui.week6M.text=studentObject.week6.toString()
        ui.week7M.text="Not marked"
        ui.week8M.text="Not marked"
        if(studentObject.week7.toString()=="100"){ui.week7M.text="HD+"}
        if(studentObject.week7.toString()=="80"){ui.week7M.text="HD"}
        if(studentObject.week7.toString()=="70"){ui.week7M.text="DN"}
        if(studentObject.week7.toString()=="60"){ui.week7M.text="CR"}
        if(studentObject.week7.toString()=="50"){ui.week7M.text="PP"}
        if(studentObject.week7.toString()=="0"){ui.week7M.text="NN"}
        if(studentObject.week8.toString()=="100"){ui.week8M.text="A"}
        if(studentObject.week8.toString()=="80"){ui.week8M.text="B"}
        if(studentObject.week8.toString()=="70"){ui.week8M.text="C"}
        if(studentObject.week8.toString()=="60"){ui.week8M.text="D"}
        if(studentObject.week8.toString()=="0"){ui.week8M.text="F"}
        ui.week9M.text=studentObject.week9.toString()
        ui.week10M.text=studentObject.week10.toString()
        ui.week11M.text=studentObject.week11.toString()
        ui.week12M.text=studentObject.week12.toString()
        ui.week13M.text=studentObject.week13.toString()

        var totalMark:Double=0.0

        totalMark=(studentObject.week1!!.toDouble()+studentObject.week2!!.toDouble()+studentObject.week3!!.toDouble()+
                studentObject.week4!!.toDouble()+studentObject.week5!!.toDouble()+studentObject.week6!!.toDouble()+
                studentObject.week7!!.toDouble()+studentObject.week8!!.toDouble()+studentObject.week9!!.toDouble()+
                studentObject.week10!!.toDouble()+studentObject.week11!!.toDouble()+studentObject.week12!!.toDouble()+studentObject.week13!!.toDouble())
        ui.textViewTotal.text=("Total Mark: "+totalMark.toString())
        if (totalMark>=0){
            ui.textViewAvg.text="Average: "+(round(totalMark/13*100)/100).toString()+"%"}
        else{ ui.textViewAvg.text="Avg:0%"}
        if (studentObject.url!==null) {
            Glide.with(this)
                    .asBitmap()
                    .load(studentObject.url)
                    .into(ui.myImageView);
        }

// Create a reference to a file from a Google Cloud Storage URI

        ui.buttonDel.setOnClickListener {
            //dialog function from  https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
            AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Delete Student "+studentObject.title.toString()+" ?") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.yes) { dialog, which ->
                        //update the database
                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                                .delete()
                                .addOnSuccessListener {
                                    Log.d(FIREBASE_TAG, "Successfully updated student ${studentObject?.id}")
                                    //return to the list
                                    finish()
                                }
                        // Continue with delete operation
                    } // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.no, null)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .show()


        }
        ui.buttonCancel.setOnClickListener {

                        finish()

        }
        ui.btnPhoto.setOnClickListener {
            requestToTakeAPicture()
        }
        ui.btnSave.setOnClickListener {
            //get the user input
            studentObject.title = ui.txtDuration.text.toString()
            /*  studentObject.year =
                ui.txtYear.text.toString().toInt() //good code would check this is really an int
            studentObject.duration = ui.txtDuration.text.toString()
                .toFloat() //good code would check this is really a float*/

            //update the database

            val storage = Firebase.storage
            var storageRef = storage.reference
            val Ref = studentObject.id?.let { it1 -> storageRef.child(it1) }

// Create a reference to 'images/mountains.jpg'
            val ImagesRef = storageRef.child("images" + studentObject.id)

// While the file names are the same, the references point to different files
            if (Ref != null) {

                    Ref.name == ImagesRef.name

            } // true
            if (Ref != null) {

                    Ref.path == ImagesRef.path

            } // false
// Get the data from an ImageView as bytes
            if (ui.myImageView.drawable !== null) {
                ui.myImageView.isDrawingCacheEnabled = true
                ui.myImageView.buildDrawingCache()
                val bitmap = (ui.myImageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                var uploadTask = Ref?.putBytes(data)
                val urlTask = uploadTask?.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    Ref?.downloadUrl
                }?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("test", "worked, this is the url link " + downloadUri)
                        studentObject.url = downloadUri.toString()
                        Log.d("test", "the studentobject.url is " + studentObject.url)

                        val db = Firebase.firestore
                        var studentsCollection = db.collection("students")
                        studentsCollection.document(studentObject.id!!)
                            .set(studentObject)
                            .addOnSuccessListener {
                                Log.d(
                                    FIREBASE_TAG,
                                    "Successfully updated student ${studentObject?.id}"

                                )
                                //return to the list

                            }
                        finish()


                    } else {
                        // Handle failures
                        Log.d("test", "error")

                    }
                }
            }else {

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





            //finish()
        }
    }


    //step 4
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestToTakeAPicture() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_IMAGE_CAPTURE
        )
    }

    //step 5
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted.
                    takeAPicture()
                } else {
                    Toast.makeText(
                        this,
                        "Cannot access camera, permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    //step 6
    private fun takeAPicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //try {
        val photoFile: File = createImageFile()!!
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "au.edu.utas.kit305.tutorial05",
            photoFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        //} catch (e: Exception) {}

    }

    //step 6 part 2
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //step 7
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic(ui.myImageView)
        }
    }

    //step 7 pt2
    private fun setPic(imageView: ImageView) {
        // Get the dimensions of the View
        val targetW: Int = imageView.measuredWidth
        val targetH: Int = imageView.measuredHeight

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(currentPhotoPath, this)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }


}
