package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project.ui.screen.container.ScreenContainer
import com.example.project.ui.screen.forgot_password.ForgotPasswordScreen
import com.example.project.ui.screen.login.LoginScreen
import com.example.project.ui.screen.pages.NotificationPage
import com.example.project.ui.screen.register.RegisterScreen
import com.example.project.ui.theme.ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                // LoginScreen(onOpenDrawer = {},onNavigateToRegister = {}, onNavigateToForgotPassword = {})
                /* RegisterScreen(
                onOpenDrawer = {},
                onNavigateToLogin= {},
                onNavigateToTerms= {},
                onNavigateToPrivacy= {})
            }*/
              /*  ForgotPasswordScreen(
                onOpenDrawer= {},
                onNavigateBack = {}
                )
                */

               /* NotificationPage(
                    onOpenDrawer = {}
                )*/


            ScreenContainer()

            }
        }
    }
}