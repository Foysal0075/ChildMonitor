package com.finalproject.childmonitor.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.childmonitor.EditChildActivity;
import com.finalproject.childmonitor.ObjectClass.Child;
import com.finalproject.childmonitor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.ChildDeviceViewHolder> {

    private List<Child> childList = new ArrayList<>();
    private Context context;
    private ProgressDialog progressDialog;

    public ChildListAdapter(List<Child> childList, Context context) {
        this.childList = childList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_device_list,parent,false);

        return  new ChildDeviceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChildDeviceViewHolder holder, int position) {

        final Child child = childList.get(position);

        holder.childName.setText(child.getChildName());
        holder.childDeviceName.setText(child.getDeviceName());
        holder.childAgeView.setText(child.getAge());

        final PopupMenu popupMenu = new PopupMenu(context,holder.itemView);
        popupMenu.inflate(R.menu.edit_del_menu);

        holder.deviceOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.edit_menu:
                                Intent intent = new Intent(context, EditChildActivity.class);
                                context.startActivity(intent);
                            case R.id.delete_menu:

                                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setView(R.layout.delet_alert_dialog);
                                alert.setTitle("Remove Child");
                                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Removing Child Device");
                                        progressDialog.show();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        reference.child("Parents").child(user.getUid()).child(child.getChildkey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Child Device Successfully Removed", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "Failed, Please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alert.show();


                        }

                        return false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChildDeviceViewHolder extends RecyclerView.ViewHolder {


        TextView childDeviceName, childName, childAgeView, deviceOptionMenu;

        public ChildDeviceViewHolder(View itemView) {
            super(itemView);
            childDeviceName = itemView.findViewById(R.id.childDeviceView);
            childName = itemView.findViewById(R.id.childNameView);
            childAgeView = itemView.findViewById(R.id.childAgeView);
            deviceOptionMenu = itemView.findViewById(R.id.deviceOptionView);

        }
    }

}
