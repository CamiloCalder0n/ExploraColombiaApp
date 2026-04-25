package me.camilo.exploracolombiaapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import me.camilo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

@Composable
fun HomeScreen(onLogout: () -> Unit = {}) {
    val primaryOrange = Color(0xFFE45D25)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = primaryOrange
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Muestra el correo del usuario logueado
            val userEmail = Firebase.auth.currentUser?.email ?: ""
            if (userEmail.isNotEmpty()) {
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ── Cerrar sesión ──
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLogout()
                },
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                modifier = Modifier.height(50.dp).widthIn(min = 180.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ExploraColombiaAppTheme {
        HomeScreen()
    }
}