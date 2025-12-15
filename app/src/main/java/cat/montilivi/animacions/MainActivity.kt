package cat.montilivi.animacions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.montilivi.animacions.ui.theme.AnimacionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimacionsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Animacions (innerPadding)
                }
            }
        }
    }
}


@Composable
fun Animacions(innerPadding: PaddingValues) {
    // 1. Estat Mestre: Controla l'inici i el final de la "cursa"
    var mogut by remember { mutableStateOf(false) }

    // Distància que recorreran (prou gran per veure l'efecte)
    val distanciaFinal = 280.dp
    val duradaAnimacio = 6000 // 2 segons per veure-ho a càmera lenta

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Animació en Compose",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

//        // --- PISTA 1: LINEAR (El Robot) ---
//        // Velocitat constant. No accelera ni frena.
//        val deplasamentLinial by animateDpAsState(
//            targetValue = if (mogut) distanciaFinal else 0.dp,
//            animationSpec = tween(durationMillis = duradaAnimacio, easing = LinearEasing),
//            label = "Linial"
//        )
//        val color by animateColorAsState(
//            targetValue = if (mogut) Color(0xFFEF5350) else Color(0xFFFAFA27),
//            animationSpec = tween(durationMillis = duradaAnimacio),
//            label = "ColorLinial"
//        )
//        ObjecteAnimat(nom = "Linial (Robot)", desplasament = deplasamentLinial, color = color)
//
//        // --- PISTA 2: FAST OUT SLOW IN (L'Estàndard) ---
//        // Comença ràpid, frena suau. És el més natural per a UI (Material Design).
//        val desplasamentEstandar by animateDpAsState(
//            targetValue = if (mogut) distanciaFinal else 0.dp,
//            animationSpec = tween(durationMillis = duradaAnimacio, easing = FastOutSlowInEasing),
//            label = "Estàndar",
//        )
//        ObjecteAnimat(nom = "Estàndar (Natural)", desplasament = desplasamentEstandar, color = Color(0xFF42A5F5))
//
        // --- PISTA 3: FAST OUT LINEAR IN (El cohet) ---
        // Comença parat i accelera fins al màxim al final. Com un coet enlairant-se.
        val desplasamentCohet by animateDpAsState(
            targetValue = if (mogut) distanciaFinal else 0.dp,
            animationSpec = tween(durationMillis = duradaAnimacio, easing = FastOutLinearInEasing),
            label = "Cohet",
        )
        ObjecteAnimat(nom = "Cohet", desplasament = desplasamentCohet, color = Color(0xFFC0A100))

        // --- PISTA 4: LINEAR OUT SLOW IN (Frenada) ---
        // Comença a màxima velocitat i va frenant fins aturar-se.
        val desplasamentFrenada by animateDpAsState(
            targetValue = if (mogut) distanciaFinal else 0.dp,
            animationSpec = tween(durationMillis = duradaAnimacio, easing = LinearOutSlowInEasing),
            label = "Frenada",
        )
        ObjecteAnimat(nom = "Frenada", desplasament = desplasamentFrenada, color = Color(0xFF272772))

        // --- PISTA 3: SPRING (La Física) ---
        // No té durada fixa, té "rebot". Si la canvies bruscament a meitat camí, s'adapta.
        val desplasamentRebotant by animateDpAsState(
            targetValue = if (mogut) distanciaFinal else 0.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy, // Molt rebot
                stiffness = Spring.StiffnessVeryLow // Poc rígid (lent i xiclet)
            ),
            label = "Rebot"
        )
        ObjecteAnimat(nom = "Spring (Rebot)", desplasament = desplasamentRebotant, color = Color(0xFF66BB6A))

        // --- PISTA 4: KEYFRAMES (La Muntanya Russa) ---
        // Control total punt per punt.
        val offsetKeyframes by animateDpAsState(
            targetValue = if (mogut) distanciaFinal else 0.dp,
            animationSpec = keyframes {
                durationMillis = duradaAnimacio
                // Al 10% del temps, retrocedeix a -20dp (agafa impuls)
                -20.dp at (duradaAnimacio / 10) using FastOutSlowInEasing
                // Al 50% del temps, va molt ràpid al 80% del camí
                (distanciaFinal * 0.8f) at (duradaAnimacio / 2)
                // Al 90% del temps, gairebé ha arribat al final
                (distanciaFinal * 0.95f) at (duradaAnimacio * 9 / 10) using LinearEasing
            },
            label = "keyframes"
        )
        ObjecteAnimat(nom = "Keyframes (Personalitzat)", desplasament = offsetKeyframes, color = Color(0xFFFFA726))


        // Botó gran per controlar l'acció
        val infiniteTransition = rememberInfiniteTransition(label = "bucle")

        // Animem una mida de text de 12sp a 24sp i viceversa
        val mida by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000), // 1 segon per expandir
            repeatMode = RepeatMode.Reverse // I torna a fer-se petit
        ),
            label = "mida"
        )
        Button(
            onClick = { mogut = !mogut },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = if (mogut) "Reinicia" else "Inicia l'animació", fontSize = mida.sp)
        }
        Spacer(modifier = Modifier.weight(1f))

        var comptador by remember { mutableIntStateOf(0) }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(
                targetState = comptador,
                transitionSpec = {
                    // El nou entra per sota (slideInVertically) + apareix (fadeIn)
                    ( slideInVertically
                        //(animationSpec = tween (durationMillis = duradaAnimacio, easing = LinearEasing ))
                    { height -> height } + fadeIn())
                        .togetherWith(
                            // El vell marxa cap a dalt (slideOutVertically) + desapareix (fadeOut)
                            slideOutVertically
                                //(animationSpec = tween (durationMillis = duradaAnimacio, easing = LinearEasing))
                            { height -> -height } + fadeOut()
                        )
                },
                label = "transicioNumeros"
            ) { targetCount ->
                // Important: Aquí fem servir el valor 'targetCount', no la variable 'comptador' directament
                Text(text = "$targetCount", fontSize = 80.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { comptador++ }) { Text("Incrementa") }
        }

    }


}

// Component auxiliar per dibuixar cada "carril" de la cursa
@Composable
fun ObjecteAnimat(nom: String, desplasament: androidx.compose.ui.unit.Dp, color: Color) {
    Column {
        Text(text = nom, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Alçada del carril
                .background(Color.White, RoundedCornerShape(25.dp))
                .padding(4.dp), // Padding intern pel rail
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = desplasament) // AQUÍ PASSA LA MÀGIA
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview    (showBackground = true)
@Composable
fun VistaPReviaAnimacions(){
    AnimacionsTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Animacions (innerPadding)
        }
    }
}