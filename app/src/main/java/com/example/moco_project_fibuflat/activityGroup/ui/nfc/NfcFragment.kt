package com.example.moco_project_fibuflat.activityGroup.ui.nfc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moco_project_fibuflat.databinding.FragmentNfcBinding

class NfcFragment : Fragment() {

    private lateinit var nfcViewModel: NfcViewModel
    private var _binding: FragmentNfcBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        nfcViewModel =
            ViewModelProvider(this)[NfcViewModel::class.java]

        _binding = FragmentNfcBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        nfcViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("nfcFragment", "onDestroyView()")
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        Log.d("nfcFragment", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("nfcFragment", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("nfcFragment", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("nfcFragment", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("nfcFragment", "onDestroy()")
    }

}