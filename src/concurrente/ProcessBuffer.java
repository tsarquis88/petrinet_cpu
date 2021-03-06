package concurrente;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessBuffer {

    private final ConcurrentLinkedQueue<Process> buffer;

    /**
     * Constructor de clase
     */
    public ProcessBuffer() {
        this.buffer = new ConcurrentLinkedQueue<>();
    }

    /**
     * Agregar nuevo Process al buffer
     * @param Process objeto Process a añadir
     */
    public void addProceso(Process Process) {
        this.buffer.add(Process);
    }

    /**
     * Procesar el ultimo elemento de la lista
     */
    public void procesar() {
        this.buffer.poll();
    }

    /**
     * Getter del tamaño del buffer
     * @return int relativo al size del buffer
     */
    public int getSize() {
        return this.buffer.size();
    }
}