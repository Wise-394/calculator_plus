package com.wise.calculatorplus.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wise.calculatorplus.R
import com.wise.calculatorplus.SettingsActivity
import com.wise.calculatorplus.database.Note

class Notes_MainActivity: AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {
    lateinit var viewModal: NoteViewModal
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var fabSettings: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_main)
        notesRV = findViewById(R.id.notesRV)
        addFAB = findViewById(R.id.idFAB)
        fabSettings = findViewById(R.id.fabSettings)
        notesRV.layoutManager = LinearLayoutManager(this)

        val noteRVAdapter = NoteRVAdapter(this, this, this)

        notesRV.adapter = noteRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener {
            val intent = Intent(this@Notes_MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
        fabSettings.setOnClickListener{
            onClickIconSettings()
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@Notes_MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
    }

    override fun onDeleteIconClick(note: Note) {
        viewModal.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }
    private fun onClickIconSettings(){
        val intent = Intent(this@Notes_MainActivity, SettingsActivity::class.java)
        startActivity(intent)
    }
}