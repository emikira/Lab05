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
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

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

        EditText editTextDescripcion = (EditText) findViewById(R.id.editTextDespcripcion);
        EditText editTextHoras  = (EditText) findViewById(R.id.editTextHorasEstimadas);
        SeekBar seekBarPrioridad = (SeekBar) findViewById(R.id.seekBarPrioridad);
        Spinner spinnerResponsable = (Spinner) findViewById(R.id.spinnerResponsable);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getInt("ID_TAREA")!=0){
            int idTarea = bundle.getInt("ID_TAREA");


        }

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

                Tarea tarea = new Tarea(
//String descripcion, Integer horasEstimadas, Integer minutosTrabajados, Boolean finalizada, Proyecto proyecto, Prioridad prioridad, Usuario responsable
                        descripcionTarea.getText().toString(),
                        Integer.getInteger(horasEstimadas.getText().toString()),
                        0,
                        false,
                        new Proyecto(), //Ver como obtener el proyecto
                        new Prioridad(String.valueOf(prioridad.getProgress())), // tiene que ser tipo prioridad
                        new Usuario()
                );

                proyectoDAO.nuevaTarea(tarea);

                Toast.makeText(this,"La tarea fue guardada exitosamente",Toast.LENGTH_SHORT).show();


                break;
            case R.id.btnCanelar:

                this.finish();

                break;
        }
    }

}
