package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;

public class AltaTareaActivity extends AppCompatActivity implements View.OnClickListener {


    ProyectoDAO proyectoDAO = new ProyectoDAO(AltaTareaActivity.this);
    EditText descripcionTarea = (EditText)findViewById(R.id.editTextDespcripcion);
    EditText horasEstimadas = (EditText) findViewById(R.id.editTextHorasEstimadas);
    Spinner responsable = (Spinner) findViewById(R.id.spinnerResponsable);
    SeekBar prioridad = (SeekBar) findViewById(R.id.seekBarPrioridad);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);

        Button botonGuardar = (Button) findViewById(R.id.btnGuardar);
        Button botonCancelar = (Button) findViewById(R.id.btnCanelar);

        botonGuardar.setOnClickListener(this);
        botonCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnGuardar:

                Cursor cursor = proyectoDAO.obtenerProyecto(1);
                //Ver como obtener el proyecto
/*
                Tarea tarea = new Tarea(
                        descripcionTarea.getText().toString(),
                        Integer.getInteger(horasEstimadas.getText().toString()),
                        0,
                        false,
                        ,
                        new Proyecto(),
                        prioridad.getProgress(), // tiene que ser tipo prioridad
                        responsable.getSelectedItem()
                );

                proyectoDAO.nuevaTarea(tarea);
*/
                Toast.makeText(this,"La tarea fue guardada exitosamente",Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnCanelar:

                this.finish();

                break;
        }
    }

}
