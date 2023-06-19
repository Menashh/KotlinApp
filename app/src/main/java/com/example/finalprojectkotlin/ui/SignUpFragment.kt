package com.example.finalprojectkotlin.ui
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var go_to_login_btn: Button
    private lateinit var sign_up_button: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText1: EditText
    private lateinit var passwordEditText2: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        mAuth = FirebaseAuth.getInstance()
        go_to_login_btn = view.findViewById(R.id.go_to_login_btn)
        sign_up_button = view.findViewById(R.id.signup_button)
        emailEditText = view.findViewById(R.id.signup_email)
        passwordEditText1 = view.findViewById(R.id.signup_password)
        passwordEditText2 = view.findViewById(R.id.signup_password2)

        sign_up_button.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password1 = passwordEditText1.text.toString().trim()
            val password2 = passwordEditText2.text.toString().trim()

            if (validateInfo(email, password1, password2)) {
                mAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            val user = task.result?.user?.uid?.let { it1 -> User(email, it1) }
                            if (user != null) {
                                insertUserRealTime(user)
                            }

                            val bundle = Bundle()
                            bundle.putStringArray("credentials", arrayOf(email, password1))

                            Toast.makeText(
                                context,
                                "SignUp succeed",
                                Toast.LENGTH_LONG
                            ).show()
                           // Navigation.findNavController(view)
                            //    .navigate(R.id.action_signUpFragment_to_typesFragment, bundle)//////////////////////////////////////////////////
                        } else {
                            Toast.makeText(
                                context,
                                "The email address is not valid / Short password",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        go_to_login_btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        return view
    }

    private fun validateInfo(email: String, password1: String, password2: String): Boolean {
        if (email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(activity, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password1 != password2) {
            Toast.makeText(activity, "Passwords do not match, Please try again.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun insertUserRealTime(user: User) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(user.uid ?: "")
        myRef.setValue(user)
    }
}