package com.example.hora_certa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.ViewHolder> {

    private List<Medicamento> listaMedicamentos;

    public MedicamentoAdapter(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicamento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicamento medicamento = listaMedicamentos.get(position);
        holder.tvNome.setText(medicamento.getNome());
        holder.tvHorario.setText(medicamento.getHorario());
        holder.tvStatus.setText(medicamento.getStatus());

        // Ajuste de cor baseado no status (exemplo simples)
        if ("Tomado".equalsIgnoreCase(medicamento.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.fundo_status_tomado);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.fundo_status_pendente);
        }
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvHorario, tvStatus;
        View viewStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome_medicamento);
            tvHorario = itemView.findViewById(R.id.tv_horario_medicamento);
            tvStatus = itemView.findViewById(R.id.tv_status_medicamento);
            viewStatus = itemView.findViewById(R.id.view_status_indicator);
        }
    }
}