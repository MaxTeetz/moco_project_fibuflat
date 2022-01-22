package com.example.moco_project_fibuflat.ActivityLogin.UI.Register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.ActivityLogin.MainActivity
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.FragmentRegisterBinding

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

        binding.registerButton.setOnClickListener { onRegister() }
    }

    private fun onRegister() {
        var anyFieldEmpty = false

        this.confirmPassword = binding.confirmPassword.text.toString()
        this.email = binding.email.text.toString()
        this.username = binding.username.text.toString()
        this.password = binding.password.text.toString()

        //check if anything´s empty
        if (!checkPassword()) { //ToDo call setError from ViewModel?
            anyFieldEmpty = true
        }
        if (viewModel.isTextInputEmpty(email)) {
            setErrorTextFieldEmail(true)
            anyFieldEmpty = true
        } else {
            setErrorTextFieldEmail(false)
        }
        if (viewModel.isTextInputEmpty(username)) {
            setErrorTextFieldUsername(true)
            anyFieldEmpty = true
        } else {
            setErrorTextFieldUsername(false)
        }

        if (!anyFieldEmpty) {
            (activity as MainActivity?)!!.registerUserInFirebase(email, password)
            registerSuccessful()
        }
    }

    //returns false if anything´s wrong
    private fun checkPassword(): Boolean {
        var check = true

        if (viewModel.isTextInputEmpty(password)) {
            setErrorTextFieldPassword(true)
            check = false
        } else {
            setErrorTextFieldPassword(false)
        }

        if (viewModel.isTextInputEmpty(confirmPassword)) {
            setErrorTextFieldConfirmPassword(true, getString(R.string.empty_confirm_password))
            check = false
        } else {
            setErrorTextFieldConfirmPassword(false, "")
        }

        if (confirmPassword != password && check) {
            setErrorTextFieldConfirmPassword(
                true,
                getString(R.string.password_doesnt_match_confirm_password)
            )
            check = false
        }

        return check
    }

    private fun registerSuccessful() {
        //change fragment and set data
        val model = ViewModelProviders.of(activity!!)[RegisterViewModel::class.java] //ToDo redundant code?

        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()

        model.setData(email, password, username)

        findNavController().navigate(action)
        //
    // toast.show()
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

    private fun setErrorTextFieldConfirmPassword(error: Boolean, errorMessage: String) {
        if (error) {
            binding.confirmPasswordLabel.isErrorEnabled = true
            binding.confirmPasswordLabel.error = errorMessage
        } else {
            binding.confirmPasswordLabel.isErrorEnabled = false
        }
    }

}