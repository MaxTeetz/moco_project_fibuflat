package com.example.moco_project_fibuflat.activityLogin.UI.Register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moco_project_fibuflat.activityLogin.MainActivity
import com.example.moco_project_fibuflat.data.ErrorMessageType
import com.example.moco_project_fibuflat.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputLayout

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var password: String
    private lateinit var email: String
    private lateinit var username: String
    private lateinit var confirmPassword: String

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Register"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerButton.setOnClickListener { onRegister() }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            when(it.errorMessageType){
                ErrorMessageType.EMAIL -> setErrorTextField(it.error!!, binding.emailLabel, it.message)
                ErrorMessageType.USERNAME -> setErrorTextField(it.error!!, binding.usernameLabel, it.message)
                ErrorMessageType.PASSWORD -> setErrorTextField(it.error!!, binding.passwordLabel, it.message)
                ErrorMessageType.CONFIRMPASSWORD -> setErrorTextField(it.error!!, binding.confirmPasswordLabel, it.message)
                ErrorMessageType.PASSWORDCONFIRMPASSWORD -> setErrorTextField(it.error!!, binding.confirmPasswordLabel, it.message)
                else -> registerSuccessful()
            }
        }
    }

    private fun onRegister() {

        this.confirmPassword = binding.confirmPassword.text.toString()
        this.email = binding.email.text.toString()
        this.username = binding.username.text.toString()
        this.password = binding.password.text.toString()

        viewModel.onRegister(email, username, password, confirmPassword, requireContext())
    }

    private fun registerSuccessful() { //ToDoEntry ask prof if this need to get into the viewModel
        //get rid of empty spaces
        val email: String = email.trim { it <= ' ' }
        val password: String = password.trim { it <= ' ' }
        (activity as MainActivity?)!!.firebaseRegister(email, password, username)
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