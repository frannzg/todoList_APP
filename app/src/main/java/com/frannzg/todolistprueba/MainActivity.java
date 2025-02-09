package com.frannzg.todolistprueba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText _task;
    private Button _btnAdd;
    private ListView _listViewTask;
    private AdapterItem _adapterItem;
    private List<Item> _ListItems;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializa las variables
        _task = findViewById(R.id.textTask);
        _btnAdd = findViewById(R.id.btnAdd);
        _listViewTask = findViewById(R.id.listViewTask);

        // Inicializa la base de datos
        databaseHelper = new DatabaseHelper(this);
        _ListItems = databaseHelper.getAllTasks(); // Carga las tareas existentes
        _adapterItem = new AdapterItem(this, _ListItems, databaseHelper);
        _listViewTask.setAdapter(_adapterItem);

        _btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = _task.getText().toString();
                String hora = getCurrentTime(); // Obtiene la hora actual
                _adapterItem.addItem(task, hora); // Agrega la tarea usando el adaptador
                _task.setText(""); // Limpia el campo de texto
            }
        });
    }

    // Método para obtener la hora actual en formato HH:mm
    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
