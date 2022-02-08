package com.example.moco_project_fibuflat.activityLogin.UI.Register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityLogin.MainActivity
import com.example.moco_project_fibuflat.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputLayout

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerPassword: String
    private lateinit var registerEmail: String
    private lateinit var registerUsername: String
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
    }

    private fun onRegister() { //ToDO make clean
        var anyFieldEmpty = false

        this.confirmPassword = binding.confirmPassword.text.toString()
        this.registerEmail = binding.email.text.toString()
        this.registerUsername = binding.username.text.toString()
        this.registerPassword = binding.password.text.toString()


        //check if anything´s empty
        if (viewModel.isTextInputEmpty(registerEmail)) {
            setErrorTextField(true, binding.emailLabel, R.string.emptyMail)
            anyFieldEmpty = true
        } else {
            setErrorTextField(false, binding.emailLabel, null)
        }
        if (viewModel.isTextInputEmpty(registerUsername)) {
            setErrorTextField(true, binding.usernameLabel, R.string.empty_username)
            anyFieldEmpty = true
        } else {
            setErrorTextField(false, binding.usernameLabel, null)
        }

        if (checkPassword() && !anyFieldEmpty) {
            registerSuccessful()
        }
    }

    //returns false if anything´s wrong
    private fun checkPassword(): Boolean {
        var check = true

        if (viewModel.isTextInputEmpty(registerPassword)) {
            setErrorTextField(true, binding.passwordLabel, R.string.emptyPassword)
            check = false
        } else
            setErrorTextField(false, binding.passwordLabel, null)

        if (viewModel.isTextInputEmpty(confirmPassword)) {
            setErrorTextField(true, binding.confirmPasswordLabel, R.string.empty_confirm_password)
            check = false
        } else {
            setErrorTextField(false, binding.confirmPasswordLabel, null)
        }

        if (confirmPassword != registerPassword && check) {
            setErrorTextField(true, binding.confirmPasswordLabel, R.string.password_confirm_password_unequal)
            check = false
        }
        return check
    }

    private fun registerSuccessful() {
        //get rid of empty spaces
        val email: String = registerEmail.trim { it <= ' ' }
        val password: String = registerPassword.trim { it <= ' ' }

        (activity as MainActivity?)!!.firebaseRegister(email, password, registerUsername)

        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        this.findNavController().navigate(action)
    }

    private fun setErrorTextField(error: Boolean, textField: TextInputLayout?, int: Int?) {
        if (error) {
            textField!!.isErrorEnabled = true
            textField.error = getString(int!!)
        } else {
            textField!!.isErrorEnabled = false
        }
    }
}