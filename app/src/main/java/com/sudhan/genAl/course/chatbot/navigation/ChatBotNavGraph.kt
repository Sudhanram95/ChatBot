package com.sudhan.genAl.course.chatbot.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sudhan.genAl.course.chatbot.authentication.login.LoginScreen
import com.sudhan.genAl.course.chatbot.authentication.login.LoginViewModel
import com.sudhan.genAl.course.chatbot.authentication.register.SignUpScreen
import com.sudhan.genAl.course.chatbot.authentication.forgotpassword.ForgotPasswordScreen
import com.sudhan.genAl.course.chatbot.chatroom.ChatRoomScreen
import com.sudhan.genAl.course.chatbot.chatroom.ChatRoomViewModel
import com.sudhan.genAl.course.chatbot.chatroom.EMPTY_TITLE
import com.sudhan.genAl.course.chatbot.message.MessageScreen
import com.sudhan.genAl.course.chatbot.message.MessageViewModel
import com.sudhan.genAl.course.chatbot.message.MessageViewModelFactory
import com.sudhan.genAl.course.chatbot.photoreasoning.PhotoReasoningScreen

@Composable
fun ChatBotNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navAction: ChatBotNavigationActions,
    loginViewModel: LoginViewModel,
    chatRoomViewModel: ChatRoomViewModel,
    startDestination: String,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        authGraph(
            navAction, navController, loginViewModel, modifier
        )
        homeGraph(navAction, navController, chatRoomViewModel, modifier)

    }

}

fun NavGraphBuilder.authGraph(
    navAction: ChatBotNavigationActions,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    modifier: Modifier,
) {
    navigation(
        startDestination = Route.LoginScreen().routeWithArgs,
        route = Route.NESTED_AUTH_ROUTE,
    ) {
        composable(
            route = Route.LoginScreen().routeWithArgs,
            arguments = listOf(
                navArgument(name = Route.isEmailSentArg) {}
            )
        ) { entry ->
            LoginScreen(
                onSignUpClick = {
                    navAction.navigateToSignUpScreen()
                },
                isVerificationEmailSent = entry.arguments?.getString(Route.isEmailSentArg)
                    .toBoolean(),
                onForgotPasswordClick = {
                    navAction.navigateToForgotPasswordScreen()
                },
                navigateToHomeScreen = {
                    navAction.navigateToHomeGraph()
                },
                modifier = modifier,
                viewModel = loginViewModel
            )
        }
        composable(route = Route.SignupScreen().route) {
            SignUpScreen(
                onLoginClick = {
                    navAction.navigateToLoginScreenWithArgs(false)
                },
                onNavigateToLoginScreen = {
                    navAction.navigateToLoginScreenWithArgs(it)
                },
                onBackButtonClicked = {
                    navAction.navigateToLoginScreenWithArgs(false)
                },
                modifier = modifier
            )
        }
        composable(route = Route.ForgotPasswordScreen().route) {
            ForgotPasswordScreen {
                navController.navigateUp()
            }
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navAction: ChatBotNavigationActions,
    navController: NavHostController,
    chatRoomViewModel: ChatRoomViewModel,
    modifier: Modifier = Modifier,
) {
    val messageRoute = "${Route.MessageScreen().route}/{chatId}/{chatTitle}"

    navigation(startDestination = Tabs.Chats.title, route = Route.NESTED_HOME_ROUTE) {
        composable(route = Tabs.Chats.title) {
            ChatRoomScreen(
                modifier,
                chatRoomViewModel
            ) { id, chatTitle ->
                navController.navigate("${Route.MessageScreen().route}/$id/$chatTitle") {
                    launchSingleTop = true
                    popUpTo(Route.MessageScreen().route) { saveState = true }
                    restoreState = true
                }
            }
        }
        composable(
            route = messageRoute,
            arguments = listOf(navArgument("chatId") {}, navArgument("chatTitle") {})
        ) { backStack ->
            val id = backStack.arguments?.getString("chatId") ?: ""
            val chatTitle = backStack.arguments?.getString("chatTitle") ?: EMPTY_TITLE
            val viewModel: MessageViewModel = viewModel(factory = MessageViewModelFactory(id, chatTitle))
            MessageScreen(
                modifier = modifier,
                messageViewModel = viewModel
            )
        }

        composable(
            route = Tabs.VisualIq.title,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            PhotoReasoningScreen(modifier = modifier)
        }

    }
}