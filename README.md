# ARSW-LAB02

## Integrantes

- Andrea Valentina Torres Tobar
- Andres Serrato Camero

## Control de hilos con wait/notify.

Este proyecto implementa un programa en Java que ejecuta hilos para encontrar números primos en un rango definido. 
Se han incorporado mecanismos de sincronización utilizando `wait()` y `notifyAll()` para pausar y reanudar los hilos cada cierto tiempo.

## Características

- Se ejecutan varios hilos en paralelo para encontrar números primos.
- Cada `5000` milisegundos, todos los hilos se detienen y se muestra la cantidad de primos encontrados hasta el momento.
- La ejecución se reanuda solo cuando el usuario presiona **ENTER**.
- Uso de `synchronized`, `wait()`, `notifyAll()` para la sincronización entre hilos.

---
## Instalación y Ejecución

### **Requisitos**
- Tener instalado **Java 8 o superior**

## Explicación del Código

### - Flujo de Ejecución

1. Los hilos comienzan a buscar primos.
2. Cada `TMILISECONDS`, los hilos se pausan** y se muestra el total de primos encontrados.
3. El usuario debe presionar ENTER para continuar.
4. Los hilos reanudan la ejecución** hasta la siguiente pausa.
5. El ciclo se repite hasta que el programa termine.

### - **Uso de `Scanner` para Esperar la Entrada del Usuario**

En el método `run()`, se usa `Scanner` para que pueda recibir cuando el usuario presione **ENTER**:

```java
Scanner scanner = new Scanner(System.in);

while(true){
    try {
        Thread.sleep(TMILISECONDS);
        pauseThreads();

        int totalPrimes = 0;
        for(PrimeFinderThread thread : pft) {
        totalPrimes += thread.getPrimes().size();
        }

        System.out.println("Total primes found: " + totalPrimes);
        System.out.println("Press enter to continue...");

        scanner.nextLine();
        resumeThread();

    } catch (InterruptedException e) {

        throw new RuntimeException(e);
    }
}
```

### - **Uso de `synchronized`, `wait()`, y `notifyAll()` para la Sincronización**

#### **En la ejecución de los hilos:**

```java
synchronized (control) {
    while (control.isPaused()) {
        try {
            control.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
- `synchronized (control) {}` asegura que solo un hilo a la vez pueda usar `control`.
- `while (control.isPaused())` verifica si los hilos deben estar en pausa.
- `control.wait();` **suspende el hilo** hasta que otro hilo llame a `notifyAll()`.

#### **En la reanudación de los hilos:**

```java
public synchronized void resumeThreads() {
    paused = false;  // Cambia el estado a "no pausado"
    notifyAll();  // Despierta a todos los hilos en espera
}
```