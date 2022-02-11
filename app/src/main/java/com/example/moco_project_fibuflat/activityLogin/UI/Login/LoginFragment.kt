package com.example.moco_project_fibuflat.activityLogin.UI.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.activityLogin.MainActivity
import com.example.moco_project_fibuflat.data.ErrorMessageType
import com.example.moco_project_fibuflat.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String
    private lateinit var password: String

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Login"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.createAccountText.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }

        binding.loginButton.setOnClickListener { onLogin() }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            when (it.errorMessageType) {
                ErrorMessageType.EMAIL -> setErrorTextField(it.error!!, binding.emailLabel, it.message)
                ErrorMessageType.PASSWORD -> setErrorTextField(it.error!!, binding.passwordLabel, it.message)
                else -> (activity as MainActivity).firebaseLogin(email, password)
            }
        }
    }

    private fun onLogin() {
        //binding.email.setText("max-julien@hotmail.de")
        //binding.password.setText("Baum123!")
        this.password = binding.password.text.toString()
        this.email = binding.email.text.toString()

        viewModel.onLogin(email, password, requireContext())
    }

    private fun setErrorTextField(error: Boolean, textField: TextInputLayout?, message: String?) {
        if (error) {
            textField!!.isErrorEnabled = true
            textField.error = message
        } else {
            textField!!.isErrorEnabled = false
        }
    }
}
