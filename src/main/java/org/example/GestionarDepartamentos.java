package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * Clase GestionarDepartamentos que se va a encargar de realizar todas las operaciones de lectura y escritura
 * en el fichero AleatorioDepart.dat.
 */
public class GestionarDepartamentos {

    private static final int LONGITUD_DEPARTAMENTO = 64;
    private RandomAccessFile raf;

    // Constructor de la clase GestionarDepartamento encargado de crear el fichero de acceso aleatorio.
    public GestionarDepartamentos(File fichero) {
        try {
            this.raf = new RandomAccessFile(fichero, "rw");
        } catch (FileNotFoundException ex) {
            System.out.println("Error al abrir el fichero: " + ex.getMessage());
        }

    }
    // Calcula el byte exacto donde empieza un registro según su número de departamento.
    private long calcularPosicion(short numDepartamento) {
        return (numDepartamento - 1) * (long) LONGITUD_DEPARTAMENTO;
    }

    // Método que devuelve si existe o no un departamento en base al número del departamento.
    public boolean existeDepartamento(short numDepartamento) {
        long posicion = calcularPosicion(numDepartamento);
        try {
            if (posicion >= raf.length()) {
                return false;
            }else {
                raf.seek(posicion);
            }

            short numDepartamentoLeido = raf.readShort();

            if(numDepartamentoLeido == numDepartamento){
                return true;
            }else{
                return false;
            }
        } catch (IOException ex) {
            System.out.println("Error en la E/S del fichero.");
            return false;
        }

    }

    /**
     * Método que se encarga de acceder directamente con el puntero a la posición indicada
     * y muestra todos los datos del registro.
     */
    public  void consultaDepartamentos(short numDepartamento){
        try{
            if(!existeDepartamento(numDepartamento)){
                System.out.println("El departamento no está activa o no existe.");
            }else{
                raf.seek(calcularPosicion(numDepartamento));
                short numDepartamentoFinal = raf.readShort();

                char[] letrasNombre = new char[15];
                for(int i = 0; i < 15; i++){
                    letrasNombre[i] = raf.readChar();
                }
                String nuevoNombre = new String(letrasNombre);

                char[] letrasLocalidad = new char[15];
                for(int i = 0; i < 15; i++){
                    letrasLocalidad[i]= raf.readChar();
                }
                String nuevaLocalidad = new String(letrasLocalidad);
                short empleados = raf.readShort();

                Departamento dep = new Departamento(numDepartamentoFinal, nuevoNombre, nuevaLocalidad);
                dep.setNumEmpleados(empleados);
                System.out.println("Departamentos: "+dep);

            }
        }catch (IOException ex){
            System.out.println("Error de lectura al consultar el departamento: "+ex.getMessage());
        }
    }

    // Método que se encarga de eliminar un departamento, marcando el número de departamento con -1 y llenando de asteriscos.
    public void eliminaDepartamento(short numDepartamento){
        try{
            if(!existeDepartamento(numDepartamento)){
                System.out.println("No se pudo eliminar el departamento porque no existe o ya fue eliminado.");
            }else{
                raf.seek(calcularPosicion(numDepartamento));
                raf.writeShort(-1);

                for(int i=0 ; i < 15; i++){
                    raf.writeChar('*');
                }

                for(int i=0 ; i < 15; i++){
                   raf.writeChar('*');
                }
                raf.writeShort(-1);
                System.out.println("Departamento eliminado correctamente.");
            }
        }catch (IOException ex) {
            System.out.println("Error de escritura al intentar eliminar el departamento: "+ex.getMessage());
        }
    }

    /*
    * Método que se encarga de actualizar el número de empleados de un departamento en concreto
    * en base a su número de departamento
    */

    public void actualizaNumeroEmpleados(short numDepartamento, short nuevoEmpleados){

        try{
            if(!existeDepartamento(numDepartamento)){
                System.out.println("No se pudo actualizar el departamento porque  no existe o fue eliminado.");
            }else{
            raf.seek(calcularPosicion(numDepartamento)+62);
            raf.writeShort(nuevoEmpleados);
            System.out.println("Número de empleados actualizado correctamente.");
            }
        }catch (IOException ex){
            System.out.println("Error de escritura al intentar actualizar el departamento: "+ex.getMessage());
        }
    }

    // Método que se va a encargar de actualizar el nombre y la localidad del departamento,validando ambos previamente.
    public void actualizaNombreLocalidad(short numDepartamento, String nuevoNombre, String nuevaLocalidad){
    String nombreFinal =  Departamento.gestionarRellenado(nuevoNombre);
    String localidadFinal = Departamento.gestionarRellenado(nuevaLocalidad);
        if (nombreFinal == null || localidadFinal == null) {
            System.out.println("Operación cancelada: El nombre o la localidad no son válidos.");
            return;
        }
    try{
    if(!existeDepartamento(numDepartamento)){
        System.out.println("No se pudo actualizar la localidad del empleado porque no existe o fue eliminadada.");
    }else{
        raf.seek(calcularPosicion(numDepartamento)+2);
        raf.writeChars(nombreFinal);
        raf.writeChars(localidadFinal);
    }

    }catch (IOException ex){
        System.out.println("Error de escritura al intentar actualizar la localidad del empleado: "+ex.getMessage());
    }

    }

    /**
     * Método para insertar un departamento siempre y cuando cumpla con todos los requisitos
     * o se autorrellenará con 0 para los números o - para las cadenas de texto.
     */
    public void insertaDepartamento(short numDepartamento, String nombre, String localidad, short numEmpleados){
        String nombreFinal = Departamento.gestionarRellenado(nombre);
        String localidadFinal = Departamento.gestionarRellenado(localidad);

        if(nombreFinal == null || localidadFinal == null){
            System.out.println("Operación cancelada: El nombre o la localidad no son válidos");
            return;
        }
        try{
        if(existeDepartamento(numDepartamento)){
            System.out.println("No se pudo insertar un nuevo departamento porque ya existe uno activo.");
            return;
        }
        long posicion = calcularPosicion(numDepartamento);
        raf.seek(posicion);
        if(posicion > raf.length()){
            raf.seek(raf.length());
            while (raf.getFilePointer() < posicion){
                raf.writeShort(0);
                raf.writeChars("---------------");
                raf.writeChars("---------------");
                raf.writeShort(0);
            }
        }
            raf.seek(posicion);
            raf.writeShort(numDepartamento);
            raf.writeChars(nombreFinal);
            raf.writeChars(localidadFinal);
            raf.writeShort(numEmpleados);

            System.out.println("Departamento insertado correctamente.");

        }catch(IOException ex){
            System.out.println("Error de escritura al intenta insertar un nuevo departamento: "+ex.getMessage());
        }
    }

    // Método que se encargará de recorrer todo el fichero y mostrar los registros activos.
    public void listadoDepartamentos(){
        try{
            int contador = 0;
            raf.seek(0);

            while(raf.getFilePointer() < raf.length()){
                short numDepartamentoFinal = raf.readShort();

                if(numDepartamentoFinal > 0){

                    char[] leerNombre = new char[15];
                    for (int i = 0;i < 15; i++){
                        leerNombre[i] = raf.readChar();
                    }
                    String nuevoNombre = new String(leerNombre);

                    char[] leerLocalidad = new char[15];
                    for (int i = 0;i < 15; i++){
                        leerLocalidad[i] = raf.readChar();
                    }
                    String nuevaLocalidad = new String(leerLocalidad);
                    short empleados = raf.readShort();

                    Departamento dep = new Departamento(numDepartamentoFinal,nuevoNombre,nuevaLocalidad);
                    dep.setNumEmpleados(empleados);
                    System.out.println("Listado de Departamentos: "+dep);
                    contador++;
                }else {
                raf.skipBytes(62);
            }

            }
            if(contador == 0){
                System.out.println("No hay ningún departamento activo para mostrar.");
            }else{
                System.out.println("Total de departamentos mostrados: "+contador);
            }
        }catch (IOException ex) {
            System.out.println("Error al listar los departamentos: " + ex.getMessage());
        }
    }

    // Método que imprime el estado físico de todos los bloques de 64 bytes (Activa, Eliminada, Nunca Utilizada o Anómala).
    public void inspeccionFisica(){
        int numPosicion = 1;
        try{

        raf.seek(0);
        while (raf.getFilePointer() < raf.length()){
        long desplazamiento = raf.getFilePointer();
        short numDepartamentoAlmacenado =raf.readShort();

            char[] leerNombre = new char[15];
            for (int i = 0;i < 15; i++){
                leerNombre[i] = raf.readChar();
            }
            String nuevoNombre = new String(leerNombre);


            char[] leerLocalidad = new char[15];
            for (int i = 0;i < 15; i++){
                leerLocalidad[i] = raf.readChar();
            }
            String nuevaLocalidad = new String(leerLocalidad);

            short empleados = raf.readShort();

            short numDepartamentoEsperado = (short) numPosicion;

            String estado = "";
                if(numDepartamentoAlmacenado == 0){
                    estado = "Nunca utilizada";
                }else if(numDepartamentoAlmacenado == -1){
                    estado = "Eliminada";
                }else if(numDepartamentoAlmacenado == numDepartamentoEsperado && empleados >= 0){
                    estado = "Activa";
                }else{
                    estado = "Anómala";
                }
            System.out.printf("Posición: %d, Desplazamiento: %d bytes , Número Departamento Esperado: %d ," +
                            " Número Departamento Almacenado; %d , Estado: %s\n",
                    numPosicion,desplazamiento,numDepartamentoEsperado,numDepartamentoAlmacenado,estado);
            numPosicion++;
        }
    }catch (IOException ex){
            System.out.println("Error durante la inspección física: " + ex.getMessage());
    }
}


    // Método que calcula y muestra la ocupación total y la suma de empleados.
    public void estadisticas(){
        try{
            int activos = 0;
            int eliminados = 0;
            int nuncaUtilizados = 0;
            int sumaEmpleados = 0;
            long longitudFichero = raf.length();

            raf.seek(0);
            while(raf.getFilePointer() < longitudFichero){
                short numDepartamentoAlmacenado = raf.readShort();
                raf.skipBytes(60);
                short empleados = raf.readShort();

                if(numDepartamentoAlmacenado == 0){
                    nuncaUtilizados++;
                }else if(numDepartamentoAlmacenado == -1){
                    eliminados++;
                }else if(numDepartamentoAlmacenado > 0){
                    activos++;
                    sumaEmpleados += empleados;
                }
            }
            double porcentajeOcupacion;
            if(eliminados + nuncaUtilizados > 0){
                 porcentajeOcupacion = (activos * 100.0) / (activos + eliminados + nuncaUtilizados);
            }else{
                porcentajeOcupacion = 0.0;
            }
            System.out.printf("Longitud del Fichero:%d bytes\n Activos:%d , Eliminados:%d ," +
                            " Nunca Utilizados:%d\n Suma Empleados:%d , Porcentaje de Ocupación:%.2f%%\n",
                    longitudFichero,activos,eliminados,nuncaUtilizados,sumaEmpleados,porcentajeOcupacion);
        }catch (IOException ex){
            System.out.println("Error en alguna de las estadísticas: "+ex.getMessage());
        }
}
    // Método que se encarga de notificar si ha habido algún problema con el fichero.
    public boolean integridad(){
        boolean integro = true;
        try {
            if (raf.length() % 64 != 0) {
                System.out.println("Anomalía general: El tamaño del fichero no es múltiplo de 64 bytes.");
                integro = false;
            }
            int numPosicion = 1;
            raf.seek(0);

            long limiteLectura = raf.length() - (raf.length() % 64);

            while (raf.getFilePointer() < limiteLectura) {
                long desplazamiento = raf.getFilePointer();
                short numDepartamentoAlmacenado = raf.readShort();

                char[] leerNombre = new char[15];
                for (int i = 0; i < 15; i++) leerNombre[i] = raf.readChar();
                String nuevoNombre = new String(leerNombre);

                char[] leerLocalidad = new char[15];
                for (int i = 0; i < 15; i++) leerLocalidad[i] = raf.readChar();
                String nuevaLocalidad = new String(leerLocalidad);

                short empleados = raf.readShort();
                short numDepartamentoEsperado = (short) numPosicion;

                if (numDepartamentoAlmacenado < 0 && numDepartamentoAlmacenado != -1) {
                    System.out.printf("Anomalía (Byte %d): ID negativo no válido (%d).\n", desplazamiento, numDepartamentoAlmacenado);
                    integro = false;
                } else if (numDepartamentoAlmacenado == -1) {
                    if (empleados != -1 || !nuevoNombre.equals("***************") || !nuevaLocalidad.equals("***************")) {
                        System.out.printf("Anomalía (Byte %d): Registro eliminado sin las marcas de borrado correctas.\n",
                                desplazamiento);
                        integro = false;
                    }
                } else if (numDepartamentoAlmacenado == 0) {
                    if (empleados != 0 || !nuevoNombre.equals("---------------") || !nuevaLocalidad.equals("---------------")) {
                        System.out.printf("Anomalía (Byte %d): Posición nunca utilizada con formato incorrecto.\n",
                                desplazamiento);
                        integro = false;
                    }
                } else if (numDepartamentoAlmacenado > 0) {
                    if (numDepartamentoAlmacenado != numDepartamentoEsperado) {
                        System.out.printf("Anomalía (Byte %d): Número Departamento (%d) no corresponde a su posición (%d).\n",
                                desplazamiento,
                                numDepartamentoAlmacenado, numDepartamentoEsperado);
                        integro = false;
                    }
                    if (empleados < 0) {
                        System.out.printf("Anomalía (Byte %d): Número de empleados negativo en registro activo.\n",
                                desplazamiento);
                        integro = false;
                    }
                    if (nuevoNombre.trim().isEmpty() || nuevaLocalidad.trim().isEmpty()) {
                        System.out.printf("Anomalía (Byte %d): Nombre o localidad vacíos en un registro activo.\n",
                                desplazamiento);
                        integro = false;
                    }
                }
                numPosicion++;
            }
            if (integro) {
                System.out.println("Comprobación de integridad finalizada: El fichero es íntegro.");
            }
        } catch (IOException ex) {
            System.out.println("Error al comprobar la integridad: " + ex.getMessage());
            return false;
        }
        return integro;
}
}

