package com.hcato.hakai.core.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hcato.hakai.R

class FireMessagingService : FirebaseMessagingService() {

    // Esta función se dispara cuando llega una notificación de Firebase
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("HAKAI_FCM", "¡Mensaje recibido desde Firebase!")

        // Extraemos el título y el mensaje
        val title = remoteMessage.notification?.title ?: "¡Nuevo Estreno!"
        val body = remoteMessage.notification?.body ?: "Entra ahora a la app."

        showNotification(title, body)
    }

    // Si Firebase cambia el token de este teléfono, lo registramos
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("HAKAI_FCM", "Nuevo token FCM: $token")
        // Aquí podrías enviar el token a tu FastAPI si quisieras notificaciones individuales
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "hakai_live_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Transmisiones en Vivo",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Asegúrate de que este ícono exista
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}