	
## Escuela Colombiana de Ingeniería
### Arquitecturas de Software – ARSW


#### Ejercicio – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.

##### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

1. Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?

![imagen](https://github.com/user-attachments/assets/e012626c-35f7-48f7-a4cb-19a9129eb89f)

![imagen](https://github.com/user-attachments/assets/26ebec04-8311-48b3-8671-6cecdb357e42)


   **RTA**:// _Como se puede ver en la imagen, el consumo de CPU aumenta casi linealmente. Esto se debe a que la clase Consumer, no tiene un momento de pausa atendido por los métodos wait/notify, lo que produce un ciclo 	infinito. Se genera bbusy-wait, al generar un consumo innecesario en esta clase._
   
3. Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.

![imagen](https://github.com/user-attachments/assets/f629eef7-66cf-4dae-ada7-61dcf5efc177)

![imagen](https://github.com/user-attachments/assets/038a5f5f-84f0-4f82-b04b-627a8797b407)

_Se eliminaron las demoras por .sleep en consumidor/productor. Se cambiaron las "Queues" por "BlockingQueue", ya que tienen propiedades de mejora en entornos concurrentes. Ahora, se producen más de los consumidores._
   
4. Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.

![imagen](https://github.com/user-attachments/assets/c5c7b769-7ef6-4a12-8755-512572360cb2)

![imagen](https://github.com/user-attachments/assets/2f4b83a8-47a6-426a-92d0-bf1bdd862dc1)

_La mejora realizada incluye manejo de sleep por 3 segundos en el consumidor, y en productor por medio segundo. Un manejo de Errores es más limpio con "Thread.currentThread().interrupt()"._



##### Parte II. – Antes de terminar la clase.

Teniendo en cuenta los conceptos vistos de condición de carrera y sincronización, haga una nueva versión -más eficiente- del ejercicio anterior (el buscador de listas negras). En la versión actual, cada hilo se encarga de revisar el host en la totalidad del subconjunto de servidores que le corresponde, de manera que en conjunto se están explorando la totalidad de servidores. Teniendo esto en cuenta, haga que:

- La búsqueda distribuida se detenga (deje de buscar en las listas negras restantes) y retorne la respuesta apenas, en su conjunto, los hilos hayan detectado el número de ocurrencias requerido que determina si un host es confiable o no (_BLACK_LIST_ALARM_COUNT_).
- Lo anterior, garantizando que no se den condiciones de carrera.

  _Se crea la clase "BlackListSearch" usando AtomicInteger  para contar de forma segura las ocurrencias, ExcecutorService para gestionar hilos y poder cancelarlos y, Mecanismos de sincronización que aseguran la correcta lectura y actualización de la variable compartida._

##### Parte III. – Avance para el martes, antes de clase.

Sincronización y Dead-Locks.

![](http://files.explosm.net/comics/Matt/Bummed-forever.png)

1. Revise el programa “highlander-simulator”, dispuesto en el paquete edu.eci.arsw.highlandersim. Este es un juego en el que:

	* Se tienen N jugadores inmortales.
	* Cada jugador conoce a los N-1 jugador restantes.
	* Cada jugador, permanentemente, ataca a algún otro inmortal. El que primero ataca le resta M puntos de vida a su contrincante, y aumenta en esta misma cantidad sus propios puntos de vida.
	* El juego podría nunca tener un único ganador. Lo más probable es que al final sólo queden dos, peleando indefinidamente quitando y sumando puntos de vida.

2. Revise el código e identifique cómo se implemento la funcionalidad antes indicada. Dada la intención del juego, un invariante debería ser que la sumatoria de los puntos de vida de todos los jugadores siempre sea el mismo(claro está, en un instante de tiempo en el que no esté en proceso una operación de incremento/reducción de tiempo). Para este caso, para N jugadores, cual debería ser este valor?.

   **RTA**:// _El valor debería ser de tal modo que:_

   						**Total de Vida** = **N** x **L**

   _**N** es el número de jugadores y, **L** es la vida inicial._

4. Ejecute la aplicación y verifique cómo funcionan las opción ‘pause and check’. Se cumple el invariante?.

   _El invariante no se cumple, a pesar de que los puntos de im1, im2 e im3 conforman la totalidad de Health sum, la sumatoria de salud cambia en cada ciclo (de un mismo juego), que se utilice el botón de 'pause and 	check', como se puede ver en las imágenes:_

   ![imagen](https://github.com/user-attachments/assets/e93c8892-ac2a-4924-8637-5ad2a2bbbb51)

   ![imagen](https://github.com/user-attachments/assets/0912af2b-fd56-41b0-b15a-a8f7f54fbbb5)

   ![imagen](https://github.com/user-attachments/assets/596be981-69c8-466f-b570-324db85af543)

	


6. Una primera hipótesis para que se presente la condición de carrera para dicha función (pause and check), es que el programa consulta la lista cuyos valores va a imprimir, a la vez que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que efectivamente, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Adicionalmente, implemente la opción ‘resume’.

7. Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). Se cumple o no el invariante?.

8. Identifique posibles regiones críticas en lo que respecta a la pelea de los inmortales. Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:

	```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
	}
	```

 	_Se realiza un bloqueo en ambos jugadores, en el método fight() de la clase "Immortal"._

   ```public void fight(Immortal i2)
    {
        synchronized (this)
        {
            synchronized (i2)
            {
   		...
            }
        }
	```

	_Lo anterior, bloque ambos jugadores y, evita modificaciones en "i2" y en "health", simultáneos a otros hilos._

10. Tras implementar su estrategia, ponga a correr su programa, y ponga atención a si éste se llega a detener. Si es así, use los programas jps y jstack para identificar por qué el programa se detuvo.

    ![imagen](https://github.com/user-attachments/assets/7855fd32-1e42-4cdb-86a5-9fde9e65d575)

    _Se produce **deadlock**, debido al ajuste anidado anterior. Al ejecutar ```jps -l``` se produce el siguiente resultado:_

	![imagen](https://github.com/user-attachments/assets/889eed87-d771-443a-ac01-099848ebebeb)

	_Por su parte, el comando jstack muestra el estado de todos los hilos en una impresión exaustiva:_

	![imagen](https://github.com/user-attachments/assets/c5df26da-59c9-452f-b6e3-a938934d9eee)

	```
           Invoker.232.Thread.ReadAction=YES: Project(name=ConcurrentProgramming_Syncrhronization_DeadLocks_ThsSuspension-master, containerState=COMPONENT_CREATED, componentStore=C:			    		 \                Users\usuario\Downloads\ConcurrentProgramming_Syncrhronization_DeadLocks_ThsSuspension-master(1)\ConcurrentProgramming_Syncrhronization_DeadLocks_ThsSuspension-master)":supervisor:ChildScope{Active}
           [ComponentManager(ApplicationImpl@1521833736)]
 	```

12. Plantee una estrategia para corregir el problema antes identificado (puede revisar de nuevo las páginas 206 y 207 de _Java Concurrency in Practice_).

13. Una vez corregido el problema, rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante, debe analizar lo realizado en el paso 4.

    _Se implementa un condicional para lograr consitencia en el orden de sincronización:_

    ```
	if (System.identityHashCode(this) > System.identityHashCode(i2))
        {
            first = i2;
            second = this;
        }	
    ```

15. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'inmortales' vivos realizando peleas fallidas con 'inmortales' ya muertos. Es necesario ir suprimiendo los inmortales muertos de la simulación a medida que van muriendo. Para esto:
	* Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'inmortales' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
	* Corrija el problema anterior __SIN hacer uso de sincronización__, pues volver secuencial el acceso a la lista compartida de inmortales haría extremadamente lenta la simulación.

16. Para finalizar, implemente la opción STOP.

### Daniel Alejandro Acero
### Juan Felipe A. Martinez
<!--
### Criterios de evaluación

1. Parte I.
	* Funcional: La simulación de producción/consumidor se ejecuta eficientemente (sin esperas activas).

2. Parte II. (Retomando el laboratorio 1)
	* Se modificó el ejercicio anterior para que los hilos llevaran conjuntamente (compartido) el número de ocurrencias encontradas, y se finalizaran y retornaran el valor en cuanto dicho número de ocurrencias fuera el esperado.
	* Se garantiza que no se den condiciones de carrera modificando el acceso concurrente al valor compartido (número de ocurrencias).

2. Parte III.
	* Diseño:
		- Coordinación de hilos:
			* Para pausar la pelea, se debe lograr que el hilo principal induzca a los otros a que se suspendan a sí mismos. Se debe también tener en cuenta que sólo se debe mostrar la sumatoria de los puntos de vida cuando se asegure que todos los hilos han sido suspendidos.
			* Si para lo anterior se recorre a todo el conjunto de hilos para ver su estado, se evalúa como R, por ser muy ineficiente.
			* Si para lo anterior los hilos manipulan un contador concurrentemente, pero lo hacen sin tener en cuenta que el incremento de un contador no es una operación atómica -es decir, que puede causar una condición de carrera- , se evalúa como R. En este caso se debería sincronizar el acceso, o usar tipos atómicos como AtomicInteger).

		- Consistencia ante la concurrencia
			* Para garantizar la consistencia en la pelea entre dos inmortales, se debe sincronizar el acceso a cualquier otra pelea que involucre a uno, al otro, o a los dos simultáneamente:
			* En los bloques anidados de sincronización requeridos para lo anterior, se debe garantizar que si los mismos locks son usados en dos peleas simultánemante, éstos será usados en el mismo orden para evitar deadlocks.
			* En caso de sincronizar el acceso a la pelea con un LOCK común, se evaluará como M, pues esto hace secuencial todas las peleas.
			* La lista de inmortales debe reducirse en la medida que éstos mueran, pero esta operación debe realizarse SIN sincronización, sino haciendo uso de una colección concurrente (no bloqueante).

	

	* Funcionalidad:
		* Se cumple con el invariante al usar la aplicación con 10, 100 o 1000 hilos.
		* La aplicación puede reanudar y finalizar(stop) su ejecución.

  
		-->

<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br />Este contenido hace parte del curso Arquitecturas de Software del programa de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería, y está licenciado como <a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">Creative Commons Attribution-NonCommercial 4.0 International License</a>.
