package com.neta.tugas_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.neta.tugas_firebase.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object{
        const val ID_EXTRA = "id"
        const val NAME_EXTRA = "name"
        const val TITLE_EXTRA = "titles"
        const val DETAIL_EXTRA = "detail"
    }


    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complains")
    private lateinit var binding : ActivityDetailBinding
    private var updateId = ""
    private val complaintListLiveData: MutableLiveData<List<Complaint>> by lazy {
        MutableLiveData<List<Complaint>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){

            val intentToMainActivity = Intent(this@DetailActivity, MainActivity::class.java)
            val id_complaint = intent.getStringExtra(MainActivity.ID_EXTRA).toString()
            val name_detail = intent.getStringExtra(MainActivity.NAME_EXTRA)
            nameDet.text = name_detail
            val title_detail = intent.getStringExtra(MainActivity.TITLE_EXTRA)
            titleDet.text = title_detail
            val detail_detail = intent.getStringExtra(MainActivity.DETAIL_EXTRA)
            detailDet.text = detail_detail

            val intentToUpdateActivity = Intent(this@DetailActivity, UpdateActivity::class.java)
            deleteButton.setOnClickListener{
                deleteComplaint(id_complaint)
                startActivity(intentToMainActivity)
            }
            updateButton.setOnClickListener{
                intentToUpdateActivity.putExtra(ID_EXTRA, id_complaint)
                intentToUpdateActivity.putExtra(NAME_EXTRA, name_detail)
                intentToUpdateActivity.putExtra(TITLE_EXTRA, title_detail)
                intentToUpdateActivity.putExtra(DETAIL_EXTRA, detail_detail)
                startActivity(intentToUpdateActivity)
            }
        }

    }

    private fun deleteComplaint(complaintId : String){
        if (complaintId.isEmpty()){
            Log.d("DetailActivity", "Error deleting: budget ID is empty!")
            return
        }

        complaintCollectionRef.document(complaintId).delete()
            .addOnFailureListener{
                Log.d("DetailActivity", "Error deleting budget: ", it)
            }
    }

}