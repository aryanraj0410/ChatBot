package com.aryanraj.chatbot.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource  // Import this to access drawable resources
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.aryanraj.chatbot.R
import com.aryanraj.chatbot.ui.theme.limegreen
import com.aryanraj.chatbot.ui.theme.lumicolor
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController


@Composable
fun GetStarted(navController: NavController) {
    Column( modifier = Modifier
        .background(limegreen)
        .fillMaxSize()
        .padding(40.dp)) {

        Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.lumi_bg),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .size(450.dp)
                    .padding(top = 50.dp)
            )

        }
    }
        Spacer(modifier = Modifier.height(25.dp))
        Column {
            Text(
                text = "LUMI is a MRI Image classification tool, that can be used for medical purposes.",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center

            )
//            Text(
//                text = "LUMI",
//                color = lumicolor,
//                fontSize = 35.sp,
//                fontWeight = FontWeight.ExtraBold,
//                fontStyle = FontStyle.Italic
//            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        CustomButton(
            buttonText = "Ok, I'll take this now →",
            textColor = limegreen,
            textSize = 20,
            onClick = { navController.navigate("MainScreen") }
        )


    }
}
@Composable
fun CustomButton(
    buttonText: String,
    textColor: Color,
    textSize: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = buttonText,
            color = textColor,
            fontSize = textSize.sp
        )
    }
}


