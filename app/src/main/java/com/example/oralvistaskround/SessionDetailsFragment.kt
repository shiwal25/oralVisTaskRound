package com.example.oralvistaskround

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SessionDetailsFragment : Fragment() {

    companion object {
        const val ARG_SESSION = "session"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_session_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = arguments?.getSerializable(ARG_SESSION) as? Session ?: return

        val tvId: TextView = view.findViewById(R.id.tvSessionId)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAge: TextView = view.findViewById(R.id.tvAge)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvPhotos)

        tvId.text = "Session ID: ${session.sessionId}"
        tvName.text = "Name: ${session.name}"
        tvAge.text = "Age: ${session.age}"

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PhotosAdapter(session.photoPaths)
    }
}
