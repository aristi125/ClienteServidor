package c_userBanco;

public class Cliente {

	/**
	 * EN ESTA CLASE CLIENTE SE LE PASARA LAS VARIABLES
	 * LAS CUALES DEBEN CUMPLIR CADA USUARIO PARA PERTENECER
	 * Y OBTENR UNA CUENTA DEL BANCO
	 */
	private int id; 
	private float saldo;
	private String cuentaBancaria;
	private String nombre;
	private int pin;

	/**
	 * SE GENERA UN COSNTRUCTOR CON SOLO EL PARAMETRO DE NOMBRE
	 * @param nombre
	 */
	public Cliente(String nombre) {
		this.nombre = nombre;
	}

	//===================== GETTERS AND SETTERS===============================
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public float getSaldo() {
		return saldo;
	}


	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}


	public String getCuentaBancaria() {
		return cuentaBancaria;
	}


	public void setCuentaBancaria(String cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public int getPin() {
		return pin;
	}


	public void setPin(int pin) {
		this.pin = pin;
	}

	
}
