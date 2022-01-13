package com.example.moco_project_fibuflat.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.Views.LoginViewModel
import com.example.moco_project_fibuflat.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var password: String
    private lateinit var email: String
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.existingAccount.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }

        binding.registerButton.setOnClickListener { onRegister() }
    }

    private fun onRegister() {
        var anyFieldEmpty = false
        password = binding.password.text.toString()
        email = binding.email.text.toString()
        username = binding.username.text.toString()

        //check if anything´s empty
        if (viewModel.isPasswordEmpty(password)) {
            setErrorTextFieldPassword(true)
            anyFieldEmpty = true
        } else {
            setErrorTextFieldPassword(false)
        }
        if (viewModel.isMailEmpty(email)) {
            setErrorTextFieldEmail(true)
            anyFieldEmpty = true
        } else {
            setErrorTextFieldEmail(false)
        }
        if (viewModel.isUsernameEmpty(username)) {
            setErrorTextFieldUsername(true)
            anyFieldEmpty = true
        } else {
            setErrorTextFieldUsername(false)
        }

        if (!anyFieldEmpty && viewModel.isEmailValidRegister(email)) {
            registerSuccessful()
        }
    }

    private fun registerSuccessful() {
        //change fragment and set data
        val model = ViewModelProviders.of(activity!!)[LoginViewModel::class.java]
        val duration = Toast.LENGTH_SHORT
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        val toast = Toast.makeText(context, R.string.register_successful, duration)

        model.setData(email, password, username)

        findNavController().navigate(action)
        toast.show()
    }

    private fun setErrorTextFieldEmail(error: Boolean) {
        if (error) {
            binding.emailLabel.isErrorEnabled = true
            binding.emailLabel.error = getString(R.string.emptyMail)
        } else {
            binding.emailLabel.isErrorEnabled = false
        }
    }

    private fun setErrorTextFieldPassword(error: Boolean) {

        if (error) {
            binding.passwordLabel.isErrorEnabled = true
            binding.passwordLabel.error = getString(R.string.emptyPassword)
        } else {
            binding.passwordLabel.isErrorEnabled = false
        }
    }

    private fun setErrorTextFieldUsername(error: Boolean) {
        if (error) {
            binding.usernameLabel.isErrorEnabled = true
            binding.usernameLabel.error = getString(R.string.empty_username)
        } else {
            binding.usernameLabel.isErrorEnabled = false
        }
    }
}