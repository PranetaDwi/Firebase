package com.neta.tugas_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.neta.tugas_firebase.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complains")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            val intentToMainActivity = Intent(this@UpdateActivity, MainActivity::class.java)

            val id_complaint = intent.getStringExtra(DetailActivity.ID_EXTRA).toString()

            val name_detail = intent.getStringExtra(DetailActivity.NAME_EXTRA)
            nameInput.setText(name_detail)
            val title_detail = intent.getStringExtra(DetailActivity.TITLE_EXTRA)
            titleInput.setText(title_detail)
            val detail_detail = intent.getStringExtra(DetailActivity.DETAIL_EXTRA)
            detailInput.setText(detail_detail)

            button.setOnClickListener{
                val name = nameInput.text.toString()
                val title = titleInput.text.toString()
                val detail = detailInput.text.toString()

                val complaintToUpdate = Complaint(id = id_complaint, name = name, title=title, detail=detail)
                updateComplaint(id_complaint, complaintToUpdate)
                setEmptyField()
                startActivity(intentToMainActivity)

            }
        }
    }

    private fun updateComplaint(complaintId : String, complaint: Complaint){
        complaintCollectionRef.document(complaintId).set(complaint)
            .addOnFailureListener {
                Log.d("UpdateActivity", "Error updating complaint: ", it)
            }
    }

    private fun setEmptyField() {
        with(binding) {
            nameInput.setText("")
            titleInput.setText("")
            detailInput.setText("")
        }
    }
}