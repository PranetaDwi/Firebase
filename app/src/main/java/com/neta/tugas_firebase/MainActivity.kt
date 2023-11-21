package com.neta.tugas_firebase

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.neta.tugas_firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val complaintCollectionRef = firestore.collection("complains")
    private lateinit var binding:ActivityMainBinding
    private var updateId = ""
    private val complaintListLiveData: MutableLiveData<List<Complaint>> by lazy {
        MutableLiveData<List<Complaint>>()
    }

    companion object{
        const val ID_EXTRA = "id"
        const val NAME_EXTRA = "name"
        const val TITLE_EXTRA = "titles"
        const val DETAIL_EXTRA = "detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intentToFormActivity = Intent(this@MainActivity, FormActivity::class.java)

        with(binding){
            addButton.setOnClickListener{
                startActivity(intentToFormActivity)
            }
            listView.setOnItemClickListener { adapterView, _, i, _->
                val item = adapterView.adapter.getItem(i) as Complaint
                updateId = item.id
                val intentToDetailActivity = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetailActivity.putExtra(ID_EXTRA, item.id)
                intentToDetailActivity.putExtra(NAME_EXTRA, item.name)
                intentToDetailActivity.putExtra(TITLE_EXTRA, item.title)
                intentToDetailActivity.putExtra(DETAIL_EXTRA, item.detail)
                startActivity(intentToDetailActivity)
            }
        }

        observeComplaints()
        getAllComplaint()
    }

    private fun getAllComplaint(){
        observeComplaintChanges()
    }


    private fun observeComplaints() {
        complaintListLiveData.observe(this) { budgets ->
            val adapter = ArrayAdapter(
                this,
                R.layout.simple_list_item_1,
                budgets.toMutableList()
            )
            binding.listView.adapter = adapter
        }
    }

    private fun observeComplaintChanges(){
        complaintCollectionRef.addSnapshotListener{ snapshots, error->
            if (error != null) {
                Log.d("MainActivity", "Error listening for complaint changes: ", error)
                return@addSnapshotListener
            }
            val complaints = snapshots?.toObjects(Complaint::class.java)
            if (complaints != null) {
                complaintListLiveData.postValue(complaints)
            }

        }
    }

}