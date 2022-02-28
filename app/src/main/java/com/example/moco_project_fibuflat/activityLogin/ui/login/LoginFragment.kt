package com.example.moco_project_fibuflat.activityLogin.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.UserApplication
import com.example.moco_project_fibuflat.UserViewModel
import com.example.moco_project_fibuflat.UserViewModelFactory
import com.example.moco_project_fibuflat.activityLogin.MainActivity
import com.example.moco_project_fibuflat.data.ErrorMessageType
import com.example.moco_project_fibuflat.data.UserDatabaseCase
import com.example.moco_project_fibuflat.data.database.DatabaseUser
import com.example.moco_project_fibuflat.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var email: String
    private lateinit var password: String
    private var user: DatabaseUser? = null

    private val viewModelDatabase: UserViewModel by activityViewModels {
        UserViewModelFactory((activity?.application as UserApplication).database.userInfoDao())
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = "Login"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setObserverDatabase()
        setObserverGroupAccess()
        setObserverErrorMessage()

        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener { onLogin() }

        binding.createAccountText.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun setObserverDatabase() {
        viewModelDatabase.allUsers.observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Log.d("loginFragment", "$it")
                user = it[0]
                binding.email.setText(user!!.email)
                binding.password.setText(user!!.password)
                binding.switchSavePassword.isChecked = true
            }
        }

        viewModel.userDatabaseCase.observe(this.viewLifecycleOwner) {
            Log.d("loginFragment", "userDataBase")
            CoroutineScope(Dispatchers.Default).launch {
                when (it) {
                    UserDatabaseCase.ADD -> {
                        viewModelDatabase.addNewUserInfo(email, password)
                        Log.d("loginFragment", "Add")
                    }
                    UserDatabaseCase.DELETE -> {
                        viewModelDatabase.deleteUser(user!!)
                        Log.d("loginFragment", "Delete")
                    }
                    UserDatabaseCase.CHANGE -> {
                        viewModelDatabase.updateUserInfo(user!!.id, email, password)
                        Log.d("loginFragment", "Change")
                    }
                    else -> {
                        Log.d("loginFragment", "Error")
                    }
                }
            }
        }
    }

    private fun setObserverGroupAccess() {
        viewModel.groupAccess.observe(this.viewLifecycleOwner) {
            (activity as MainActivity).changeActivity(it)
        }
    }

    private fun setObserverErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            when (it.errorMessageType) {
                ErrorMessageType.EMAIL -> setErrorTextField(it.error!!,
                    binding.emailLabel,
                    it.message)
                ErrorMessageType.PASSWORD -> setErrorTextField(it.error!!,
                    binding.passwordLabel,
                    it.message)
                else -> viewModel.firebaseLogin(email,
                    password,
                    binding.switchSavePassword.isChecked,
                    user)
            }
        }
    }

    private fun onLogin() {
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
