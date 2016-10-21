package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;

    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public Cursor obtenerProyecto (Integer proyectoId){
        Cursor cursorPry = null;
        cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + "WHERE " + ProyectoDBMetadata.TablaProyectoMetadata._ID + " = ?",new String[]{proyectoId.toString()});

        if(cursorPry.moveToFirst()){
            return cursorPry;
        }

        return cursorPry;
    }

    public Integer nuevaTarea(Tarea t){

        //Obtener el proyecto
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }

        //Obtener el usuario
        Integer idUser = 0;
        Cursor cursorUser = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaUsuariosMetadata._ID +
                " FROM " + ProyectoDBMetadata.TABLA_USUARIOS +
                "WHERE " + ProyectoDBMetadata.TABLA_USUARIOS +" = ?", new String[]{t.getResponsable().getId().toString()});
        if(cursorUser.moveToFirst()){
            idUser=cursorUser.getInt(0);
        }

        String insertarTarea = "INSERT INTO" + ProyectoDBMetadata.TABLA_TAREAS +
        "(descripcion, horasEstimadas, minutosTrabajados, finalizada, proyecto, prioridad, responsable)" +
                "VALUES (" + t.getDescripcion() + ", " + t.getHorasEstimadas() + ", " + t.getMinutosTrabajados() + ", " + t.getFinalizada() + ", " + t.getProyecto().getId() + ", " + t.getPrioridad().getPrioridad() + ", " + idUser + ")";

        cursorPry = db.rawQuery(insertarTarea,null);
        Integer insertedRowId = 0;

        insertedRowId = obtenerUltimoId();

        return insertedRowId;
    }

    public int obtenerUltimoId() {
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }
    public Tarea obtenerTarea(int id){
        Tarea nueva = new Tarea();
        Cursor cursorTarea = null;
        cursorTarea = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaTareasMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_TAREAS + "WHERE " + ProyectoDBMetadata.TablaTareasMetadata._ID+ " = ?",new String[]{String.valueOf(id)});

        if(cursorTarea.moveToFirst()){
            nueva.setDescripcion(cursorTarea.getString(cursorTarea.getColumnIndex("DESCRIPCION")));
            nueva.setHorasEstimadas(cursorTarea.getInt(cursorTarea.getColumnIndex("HORAS_PLANIFICADAS")));
            nueva.setPrioridad(cursorTarea.getInt(cursorTarea.getColumnIndex("ID_PRIORIDAD")));



        }
    }

    public void actualizarTarea(Tarea t){

    }

    public void borrarTarea(Tarea t){

    }

    public List<Prioridad> listarPrioridades(){
        return null;
    }

    public List<Usuario> listarUsuarios(){
        return null;
    }

    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});
        mydb.close();
    }

    public List<Tarea> listarDesviosPlanificacion(Boolean soloTerminadas,Integer desvioMaximoMinutos){
        // retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
        // que el tiempo planificado.
        // si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.
        return null;
    }


}
