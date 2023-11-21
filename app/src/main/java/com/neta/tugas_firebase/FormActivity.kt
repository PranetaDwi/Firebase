package com.neta.tugas_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.neta.tugas_firebase.databinding.ActivityFormBinding
import com.neta.tugas_firebase.databinding.ActivityMainBinding

class FormActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityFormBinding
    private val complaintCollectionRef = firestore.collection("complains")
    private val complaintListLiveData: MutableLiveData<List<Complaint>> by lazy {
        MutableLiveData<List<Complaint>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFormBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){

            val intentToMainActivity = Intent(this@FormActivity, MainActivity::class.java)

            button.setOnClickListener{
                val name = nameInput.text.toString()
                val title = titleInput.text.toString()
                val detail = detailInput.text.toString()
                val newComplaint = Complaint(name = name, title = title, detail=detail)
                addComplaint(newComplaint)
                startActivity(intentToMainActivity)
            }
        }
    }

    private fun addComplaint(complaint: Complaint){
        complaintCollectionRef.add(complaint)
            .addOnSuccessListener { docRef ->
                val createdComplaintId = docRef.id
                complaint.id = createdComplaintId
                docRef.set(complaint)
                    .addOnFailureListener{
                        Log.d("FormActivity", "Error updating budget ID: ", it)
                    }
                resetForm()
            }
            . addOnFailureListener{
                Log.d("FormActivity", "Error adding budget ID: ", it)
            }
    }

    private fun resetForm(){
        with(binding){
            nameInput.setText("")
            titleInput.setText("")
            detailInput.setText("")
        }
    }

}