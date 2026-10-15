package org.example;

/**
 * Clase POJO que representa un departamento y agrupa sus datos y validaciones lógicas.
 */
public class Departamento {
    private short numDepartamento = 0;
    private String nombre = "";
    private String localidad = "";
    private short numEmpleados = 0;

    //Constructor de la clase Departamento
    public Departamento(short numDepartamento, String nombre, String localidad) {
    this.numDepartamento = numDepartamento;
    this.nombre = nombre;
    this.localidad = localidad;
    this.numEmpleados = 1;
    }
    // Getter y Setter
    public short getNumDepartamento() {
        return numDepartamento;
    }

    public void setNumDepartamento(short numDepartamento) {
        this.numDepartamento = numDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public short getNumEmpleados() {
        return numEmpleados;
    }

    public void setNumEmpleados(short numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    /*
     * Método que se encarga de validar un texto,es decir,que no sea nulo, que no esté vacio
     * y que NO contenga más de 15 carácteres.
     */

    public static boolean validarTexto(String texto){
        if(texto == null){
            return false;
        }
        String textoTrim = texto.trim();
        if(!textoTrim.isEmpty() && textoTrim.length()<=15){
            return true;
        }
        return false;
    }
    /*
     * Método que se encarga de ajustar el texto añadiendo espacios a la derecha hasta llegar a los 15 caracteres.
     */
    public static String gestionarRellenado(String texto){
    if(!validarTexto(texto)){
        System.out.println("La rellenada no cumple con los requisitos.");
        return null;
    }else{
        String textoFormateado = String.format("%-15s",texto.trim());
        return textoFormateado;
    }

    }
    /*
     * Método encargado de validar que el número de departamento y los empleados estén dentro de los rangos permitidos.
     */
    public static  boolean validarDepartamento(short numDepartamento, short numEmpleados){
        if(numDepartamento < 1 || numDepartamento > 32767){
            System.out.println("El número de departamento no está en el rango permitido (1-32767).");
            return false;
        }if(numEmpleados < 0){
            System.out.println("El número de empleados no puede ser negativo (0-32767).");
            return false;
        }
        return true;
        }

    /**
     * Método toString encargado de mostrar por pantalla los departamentos.
     */
    @Override
    public String toString(){
        return String.format("Número Departamento:%d, Nombre:%s, Localidad:%s, Número Empleados:%d",
                numDepartamento,nombre.trim(),localidad.trim(), numEmpleados);
        }

    }
