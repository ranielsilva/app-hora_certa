package com.example.hora_certa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("TITULO");
        String mensagem = intent.getStringExtra("MENSAGEM");
        int id = intent.getIntExtra("ID", 0);

        if (titulo == null) titulo = "Lembrete Hora Certa";
        if (mensagem == null) mensagem = "Você tem um compromisso de saúde agora.";

        NotificationHelper.showNotification(
                context,
                titulo,
                mensagem,
                id
        );
    }
}