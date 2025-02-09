package com.frannzg.todolistprueba;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterItem extends BaseAdapter {
    private Context context;
    private List<Item> tasks;
    private DatabaseHelper databaseHelper;

    public AdapterItem(Context _context, List<Item> _task, DatabaseHelper dbHelper) {
        this.context = _context;
        this.tasks = _task;
        this.databaseHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return this.tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.listtasks, null);

        TextView task = view.findViewById(R.id.tTask);
        TextView hora = view.findViewById(R.id.tHora);
        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnDelete = view.findViewById(R.id.btnDelete);

        task.setText(tasks.get(position).getName());
        hora.setText(tasks.get(position).getHora());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(position);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        return view;
    }

    public void addItem(String task, String hora) {
        Item newItem = new Item(0, task, hora); // ID se generará automáticamente
        databaseHelper.addTask(newItem); // Guardar en la base de datos
        tasks.add(newItem); // Agregar a la lista
        notifyDataSetChanged();
    }

    public void editItem(int pos) {
        Item item = (Item) getItem(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.edit));


        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        final EditText _task = new EditText(context);
        final EditText _hora = new EditText(context);

        _task.setText(item.getName());
        _hora.setText(item.getHora());

        layout.addView(_task);
        layout.addView(_hora);

        builder.setView(layout);

        builder.setPositiveButton(context.getString(R.string.save), (dialog, which) -> {
            item.setName(_task.getText().toString());
            item.setHora(_hora.getText().toString());
            databaseHelper.updateTask(item); // Actualizar en la base de datos
            notifyDataSetChanged();
        });

        builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void removeItem(int position) {
        Item item = (Item) getItem(position);
        databaseHelper.deleteTask(item.getId()); // Eliminar de la base de datos
        tasks.remove(position); // Eliminar de la lista
        notifyDataSetChanged();
    }
}
