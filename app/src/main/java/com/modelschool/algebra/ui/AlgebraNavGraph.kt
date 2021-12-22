package com.modelschool.algebra.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.modelschool.algebra.viewmodel.LessonViewModel
import com.modelschool.algebra.viewmodel.LoginViewModel
import com.modelschool.algebra.viewmodel.RegistrationViewModel
import com.modelschool.algebra.viewmodel.TopicViewModel

object MainDestinations {
    const val SPLASH_ROUTE = "splash"
    const val LOGIN_ROUTE = "login"
    const val REGISTRATION_ROUTE = "registration"
    const val TOPIC_ROUTE = "topics"
    const val LESSON_ROUTE = "lessons"
}

@Composable
fun AlgebraNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.SPLASH_ROUTE
) {
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.SPLASH_ROUTE) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            SplashScreen(
                loginViewModel = loginViewModel,
                navToLogin = { navController.navigate(MainDestinations.LOGIN_ROUTE) },
                navToTopic = { navController.navigate(MainDestinations.TOPIC_ROUTE) }
            )
        }
        composable(MainDestinations.LOGIN_ROUTE) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(
                loginViewModel = loginViewModel,
                navToRegister = { navController.navigate(MainDestinations.REGISTRATION_ROUTE) },
                navToTopic = { navController.navigate(MainDestinations.TOPIC_ROUTE) }
            )
        }
        composable(MainDestinations.REGISTRATION_ROUTE) {
            val registrationViewModel = hiltViewModel<RegistrationViewModel>()
            RegistrationScreen(
                registrationViewModel = registrationViewModel,
                onBack = actions.upPress
            )
        }
        composable(MainDestinations.TOPIC_ROUTE) {
            val topicViewModel = hiltViewModel<TopicViewModel>()
            TopicScreen(
                topicViewModel = topicViewModel,
                navToLesson = actions.navigateToLesson
            )
        }
        composable(
            route = "${MainDestinations.TOPIC_ROUTE}/{topic_id}/${MainDestinations.LESSON_ROUTE}",
            arguments = listOf(navArgument("topic_id") { type = NavType.StringType })
        ) {
            val lessonViewModel = hiltViewModel<LessonViewModel>()
            LessonScreen(
                lessonViewModel = lessonViewModel
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToLesson: (String) -> Unit = { topicId: String ->
        navController.navigate("${MainDestinations.TOPIC_ROUTE}/$topicId/${MainDestinations.LESSON_ROUTE}")
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}