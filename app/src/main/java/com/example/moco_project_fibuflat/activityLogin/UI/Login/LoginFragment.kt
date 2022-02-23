package com.example.moco_project_fibuflat.activityLogin.UI.Login

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
import com.example.moco_project_fibuflat.data.database.DatabaseUser
import com.example.moco_project_fibuflat.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
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
        viewModelDatabase.retrieveUserInfo(1).observe(this.viewLifecycleOwner) {
            if (it != null) {
                Log.d("loginFragment", "$it")
                user = it
                binding.email.setText(user!!.email)
                binding.password.setText(user!!.password)
            }
        }
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

        viewModel.groupAccess.observe(this.viewLifecycleOwner) {
            (activity as MainActivity).changeActivity(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            when (it.errorMessageType) {
                ErrorMessageType.EMAIL -> setErrorTextField(it.error!!,
                    binding.emailLabel,
                    it.message)
                ErrorMessageType.PASSWORD -> setErrorTextField(it.error!!,
                    binding.passwordLabel,
                    it.message)
                else -> {
                    (firebaseLogin(email, password, binding.switchSavePassword.isChecked))
                }
            }
        }
    }

    private fun firebaseLogin(
        email: String,
        password: String,
        activated: Boolean,
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password) //ToDo back in MainActivity. Doesn't sign in all the time, even with email and password
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.Default).launch {
                    if (!activated && user != null) {
                        Log.d("loginFragment", "${user?.id} + ${user?.email} + ${user?.password}")
                        viewModelDatabase.deleteItem(user!!)
                    }
                    if (activated && user == null) {
                        Log.d("loginFragment", "$email / $password")
                        saveUserInfo(email, password)
                    }
                    viewModel.getDBGroupEntry()
                }
            }.addOnFailureListener { task ->
                (activity as MainActivity).taskFailed(task)
                Log.d("loginFragmentTaskFailed", "$task")
                Log.d("loginFragmentTaskFailed", "$email + $password")
            }
    }

    private fun saveUserInfo(email: String, password: String) {
        Log.d("mainActivity", "User Info saved")
        viewModelDatabase.addNewUserInfo(email, password)
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
