package com.example.finalprojectkotlin.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.finalprojectkotlin.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var emailEditText1: EditText
    private lateinit var passwordEditText1: EditText
    private lateinit var login: Button
    private lateinit var register: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText1 = view.findViewById(R.id.login_email)
        passwordEditText1 = view.findViewById(R.id.login_password)
        login = view.findViewById(R.id.login_button)
        register = view.findViewById(R.id.go_to_sign_up_button)

        mAuth = FirebaseAuth.getInstance()

        arguments?.let { arguments ->
            val credentials = arguments.getStringArray("credentials")
            credentials?.let {
                emailEditText1.setText(it[0])
                passwordEditText1.setText(it[1])
            }
        }

        login.setOnClickListener {
            val email = emailEditText1.text.toString().trim()
            val password = passwordEditText1.text.toString().trim()

            if (validateInfo(email, password)) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            val currentUID = task.result?.user?.uid
                            storage.setCurrentUID(currentUID)

                            if (task.result.user!!.email.equals("admin@admin.com")) {
                                Navigation.findNavController(view)
                                    .navigate(R.id.action_loginFragment_to_showMoviesFragment)
                            } else {
                               // Navigation.findNavController(view)
                                   //admin.com .navigate(R.id.action_loginFragment_to_typesFragment)
                            }


                        } else {
                            Toast.makeText(
                                activity,
                                "Email and password do not match.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        register.setOnClickListener { view ->
            Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        return view
    }

    private fun validateInfo(email: String, password: String): Boolean {
        return when {
            email.isEmpty() && password.isEmpty() -> {
                Toast.makeText(activity, "Please enter your email and password.", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() -> {
                Toast.makeText(activity, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(activity, "Please enter your password.", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}