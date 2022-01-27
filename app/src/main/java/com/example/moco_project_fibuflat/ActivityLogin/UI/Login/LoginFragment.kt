package com.example.moco_project_fibuflat.ActivityLogin.UI.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.ActivityLogin.MainActivity
import com.example.moco_project_fibuflat.ActivityLogin.UI.Register.RegisterViewModel
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val registerView: RegisterViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = ViewModelProviders.of(activity!!).get(RegisterViewModel::class.java)

        super.onViewCreated(view, savedInstanceState)

        //set email and password for login
        registerView.password.observe(
            this,
            {   //ToDo observe data Package LoggedInUser or Repository
                binding.email.setText(model.email.value)
                binding.password.setText(model.password.value) //works now I guess. Before setData.toString()
            })

        binding.createAccountText.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }

        binding.loginButton.setOnClickListener { onLogin() }
    }

    private fun onLogin() {
        binding.email.setText("max-julien@hotmail.de")
        binding.password.setText("Baum123!")
        val password = binding.password.text.toString()
        val email = binding.email.text.toString()
        var check = true

        if (viewModel.isTextInputEmpty(email)) {
            setErrorTextFieldEmail(true)
            check = false
        } else
            setErrorTextFieldEmail(false)
        if (viewModel.isTextInputEmpty(password)) {
            check = false
            setErrorTextFieldPassword(true)
        } else {
            setErrorTextFieldPassword(false)
        }

        if (check) {
            (activity as MainActivity)!!.firebaseLogin(email, password)
        }
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
}