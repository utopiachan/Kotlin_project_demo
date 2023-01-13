package au.edu.utas.kit305.tutorial05


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import au.edu.utas.kit305.tutorial05.databinding.ActivityAddStudentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddStudent : AppCompatActivity() {
    private lateinit var ui: ActivityAddStudentBinding

    @RequiresApi(Build.VERSION_CODES.M)
    val db = Firebase.firestore

    @SuppressLint("WrongThread")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(ui.root)
        var editName: String?=null
        var editStudentId:Int?=null
        var imgurl:String?=null

        ui.btnPhoto1.setOnClickListener {
            requestToTakeAPicture()
        }
        ui.button4.setOnClickListener(){
            finish()
        }

        ui.buttonAdd.setOnClickListener {
            //get the user input

            editName = ui.SName.text.toString()
            editStudentId = ui.SID.text.toString().toInt() //good code would check this is really an int

            val lotr = Student(
                title = editName,
                sID = editStudentId,
                url=imgurl
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
                }



// Get the data from an ImageView as bytes


            val storage = Firebase.storage
            var storageRef = storage.reference

            val Ref = editStudentId?.let { it1 -> storageRef.child(it1.toString()) }


// Create a reference to 'images/mountains.jpg'
            val ImagesRef = storageRef.child("images" + editStudentId)

// While the file names are the same, the references point to different files
            if (Ref != null) {
                if (ImagesRef !== null) {
                    Ref.name == ImagesRef.name
                }
            } // true
            if (Ref != null) {
                if (ImagesRef != null) {
                    Ref.path == ImagesRef.path
                }
            } // false
            if (ui.imageView.drawable !== null){
                ui.imageView.isDrawingCacheEnabled = true
            ui.imageView.buildDrawingCache()
            val bitmap = (ui.imageView.drawable as BitmapDrawable).bitmap
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
                    lotr.url = downloadUri.toString()

                    Log.d("test", "the lotr.url is " + imgurl)
                    val db = Firebase.firestore
                    var studentsCollection = db.collection("students")
                    studentsCollection.document(lotr.id!!)
                        .set(lotr)
                        .addOnSuccessListener {
                            Log.d(FIREBASE_TAG, "Successfully updated student ${lotr?.sID}")
                            //return to the list

                        }
                    finish()

                } else {
                    // Handle failures
                    Log.d("test", "error")

                }
            }
            }else {

                finish()
            }
            }




            //finish()
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
                setPic(ui.imageView)
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


