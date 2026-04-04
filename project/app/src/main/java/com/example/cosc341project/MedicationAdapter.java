package com.example.cosc341project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private List<Medication> medicationList;

    public MedicationAdapter(List<Medication> medicationList) {
        this.medicationList = medicationList;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medicationList.get(position);
        holder.tvMedName.setText(medication.name);
        holder.tvMedNickname.setText(medication.nickname);
        holder.tvMedDosage.setText(medication.dosage + " " + medication.dosageUnit);
        holder.tvRemaining.setText("Remaining: " + medication.dosesRemaining);
        holder.tvRefills.setText("Refills: " + medication.refillsRemaining);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(v, medication);
            }
        });
    }

    private void showDeleteDialog(View v, Medication medication) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Delete Medication")
                .setMessage("Do you wish to permanently delete this medication? This cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance("https://cosc341project-66346-default-rtdb.firebaseio.com/")
                                .getReference().child("medications").child(medication.id).removeValue()
                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(v.getContext(), "Error deleting", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvMedNickname, tvMedDosage, tvRemaining, tvRefills;
        ImageView ivDelete;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tvMedName);
            tvMedNickname = itemView.findViewById(R.id.tvMedNickname);
            tvMedDosage = itemView.findViewById(R.id.tvMedDosage);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            tvRefills = itemView.findViewById(R.id.tvRefills);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}