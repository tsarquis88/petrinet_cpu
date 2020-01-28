public class CPUPower extends Thread {

    private Monitor monitor;
    private CPUBuffer cpuBuffer;
    private int standByDelay;
    private boolean isActive;
    private boolean isOn;
    private int[] secuencia = {99, 99, 99};
    private String cpuId;
    private long inicioSleep;
    private long tiempoSleep;

    private static int sleep = 50;

    /**
     * Constructor de clase
     * @param monitor monitor del CPUProcessing
     */
    public CPUPower(Monitor monitor, CPUBuffer cpuBuffer, int standbyDelay, String cpuID) {
        setName("CPUPower " + cpuID);
        this.monitor = monitor;
        this.cpuBuffer = cpuBuffer;
        this.standByDelay = standbyDelay;
        this.isActive = false;
        this.isOn = false;
        this.cpuId = cpuID;
        this.tiempoSleep = 0;

        if(cpuID.equalsIgnoreCase("A")) {
            this.secuencia[0] = 2;
            this.secuencia[1] = 3;
            this.secuencia[2] = 4;
        }
        else if(cpuID.equalsIgnoreCase("B")) {
            this.secuencia[0] = 9;
            this.secuencia[1] = 10;
            this.secuencia[2] = 11;
        }
    }

    /**
     * Accion del hilo
     * Encender y apagar el CPU
     */
    @Override
    public void run() {
        System.out.println(Colors.RED_BOLD + "INICIO CPUPower " + this.cpuId + Colors.RESET);
        boolean flag = false;

        while(!currentThread().isInterrupted()) {

            if(this.cpuBuffer.getSize() > 0 && !this.isOn) {
                try {
                    monitor.entrar(secuencia[0]);    // pasar de stand by a encendido
                    monitor.salir();
                    Thread.sleep(this.standByDelay);
                } catch (InterruptedException e) {
                    interruptedReaccion();
                }

                try {
                    monitor.entrar(secuencia[1]);    // encender CPU
                    this.isOn = true;

                    if(flag)
                        this.tiempoSleep = this.tiempoSleep + (System.currentTimeMillis() - this.inicioSleep);
                    else
                        flag = true;

                    monitor.salir();
                    System.out.println(Colors.RED_BOLD + "ENCENDIDO:                         CPU " + this.cpuId + Colors.RESET);
                } catch (InterruptedException e) {
                    interruptedReaccion();
                }
            }
            else if(this.cpuBuffer.getSize() <= 0 && this.isOn && !this.isActive) {
                try {
                    monitor.entrar(secuencia[2]);   // apagado
                    this.isOn = false;
                    this.inicioSleep = System.currentTimeMillis();
                    monitor.salir();
                    System.out.println(Colors.RED_BOLD + "APAGADO:                           CPU " + this.cpuId + Colors.RESET);
                } catch (InterruptedException e) {
                    interruptedReaccion();
                }
            }

            try {
                sleep(sleep);
            }
            catch (InterruptedException e) {
                interruptedReaccion();
            }
        }
    }

    /**
     * Getter del monitor del CPU
     * @return objeto Monitos correspondiente al CPU
     */
    public Monitor getMonitor() {
        return this.monitor;
    }

    /**
     * Getter del CPUBuffer
     * @return objeto CPUBuffer relativo al buffer de instancia
     */
    public CPUBuffer getCpuBuffer() {
        return this.cpuBuffer;
    }

    /**
     * Seteo el estado de actividad del cpu
     * @param isActive true para activo, false para inactivo
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Reaccion a interrupcion
     * VACIO
     */
    private void interruptedReaccion() {}

    /**
     * Getter del tiempo total en estado Off
     * @return tiempo de double
     */
    public double getTiempoSleep() {
        return (this.tiempoSleep / 1000.00);
    }
}
