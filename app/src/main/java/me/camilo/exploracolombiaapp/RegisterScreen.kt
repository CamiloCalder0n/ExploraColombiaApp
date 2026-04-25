package me.camilo.exploracolombiaapp

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import me.camilo.exploracolombiaapp.ui.theme.ExploraColombiaAppTheme

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var registerError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val auth = Firebase.auth
    val activity = LocalView.current.context as Activity

    val primaryOrange = Color(0xFFE45D25)
    val lightGrayBg = Color(0xFFF8F9FE)
    val inputBg = Color(0xFFE5E5EA)

    Surface(modifier = modifier.fillMaxSize(), color = lightGrayBg) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start).offset(x = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = primaryOrange
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Explorando Colombia",
                color = primaryOrange,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Crea tu cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Empieza tu aventura por el realismo mágico",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Campos ──
            Column(modifier = Modifier.fillMaxWidth()) {
                RegisterField(
                    label = "NOMBRE COMPLETO",
                    value = name,
                    onValueChange = {
                        name = it
                        if (registerError.isNotEmpty()) registerError = ""
                    },
                    placeholder = "Tu nombre",
                    leadingIcon = Icons.Default.Person,
                    inputBg = inputBg,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "CORREO ELECTRÓNICO",
                    value = email,
                    onValueChange = {
                        email = it
                        if (registerError.isNotEmpty()) registerError = ""
                    },
                    placeholder = "hola@ejemplo.com",
                    leadingIcon = Icons.Default.Email,
                    inputBg = inputBg,
                    keyboardType = KeyboardType.Email,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "CONTRASEÑA",
                    value = password,
                    onValueChange = {
                        password = it
                        if (registerError.isNotEmpty()) registerError = ""
                    },
                    placeholder = "Mínimo 6 caracteres",
                    leadingIcon = Icons.Default.Lock,
                    inputBg = inputBg,
                    isPassword = true,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(20.dp))

                RegisterField(
                    label = "CONFIRMAR CONTRASEÑA",
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (registerError.isNotEmpty()) registerError = ""
                    },
                    placeholder = "Repite tu contraseña",
                    leadingIcon = Icons.Default.Refresh,
                    inputBg = inputBg,
                    isPassword = true,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Términos ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = acceptedTerms,
                    onCheckedChange = {
                        acceptedTerms = it
                        if (registerError.isNotEmpty()) registerError = ""
                    },
                    colors = CheckboxDefaults.colors(checkedColor = primaryOrange),
                    enabled = !isLoading
                )
                Text(
                    text = buildAnnotatedString {
                        append("Acepto los ")
                        withStyle(style = SpanStyle(color = primaryOrange, fontWeight = FontWeight.Bold)) {
                            append("términos y condiciones")
                        }
                        append(" así como el tratamiento de datos personales.")
                    },
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Error ──
            if (registerError.isNotEmpty()) {
                Text(
                    text = registerError,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Botón Registrarse ──
            Button(
                onClick = {
                    // Validaciones locales
                    when {
                        name.isBlank() -> {
                            registerError = "Ingresa tu nombre completo"
                            return@Button
                        }
                        email.isBlank() -> {
                            registerError = "Ingresa tu correo electrónico"
                            return@Button
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            registerError = "El formato del correo no es válido"
                            return@Button
                        }
                        password.isBlank() -> {
                            registerError = "Ingresa una contraseña"
                            return@Button
                        }
                        password.length < 6 -> {
                            registerError = "La contraseña debe tener mínimo 6 caracteres"
                            return@Button
                        }
                        password != confirmPassword -> {
                            registerError = "Las contraseñas no coinciden"
                            return@Button
                        }
                        !acceptedTerms -> {
                            registerError = "Debes aceptar los términos y condiciones"
                            return@Button
                        }
                    }

                    // Llamada a Firebase
                    registerError = ""
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                onRegisterSuccess()
                            } else {
                                registerError = when (task.exception) {
                                    is FirebaseAuthUserCollisionException ->
                                        "Ya existe una cuenta con este correo"
                                    is FirebaseAuthWeakPasswordException ->
                                        "La contraseña debe tener mínimo 6 caracteres"
                                    is FirebaseAuthInvalidCredentialsException ->
                                        "El formato del correo no es válido"
                                    is FirebaseNetworkException ->
                                        "Sin conexión a internet. Verifica tu red"
                                    else ->
                                        "Error al registrarse. Intenta de nuevo"
                                }
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                enabled = !isLoading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = if (isLoading)
                                    listOf(Color.Gray, Color.Gray)
                                else
                                    listOf(primaryOrange, Color(0xFFFF8A65))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
                Text(
                    text = " O REGÍSTRATE CON ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), thickness = 0.5.dp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialButton(text = "Google", modifier = Modifier.weight(1f), icon = Icons.Default.Email)
                SocialButton(text = "Apple", modifier = Modifier.weight(1f), icon = Icons.Default.Lock)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Inicia sesión",
                    color = primaryOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    inputBg: Color,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(28.dp)),
            placeholder = { Text(placeholder, color = Color.Gray) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation()
            else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputBg,
                unfocusedContainerColor = inputBg,
                disabledContainerColor = inputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            enabled = enabled
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ExploraColombiaAppTheme {
        RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
    }
}