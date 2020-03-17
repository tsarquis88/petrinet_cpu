import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock mutex;
    private Cola[] colas;
    private RedDePetri redDePetri;
    private Politica politica;

    /**
     * Constructor de clase
     * Si o si se debe llamar al metodo setRedDePetri antes de usar
     */
    public Monitor(Politica politica) {
        this.politica = politica;
        this.mutex = new ReentrantLock(true);
    }

    public void setRedDePetri(RedDePetri redDePetri) {
        this.redDePetri = redDePetri;
        this.colas = new Cola[redDePetri.getTransiciones().length];
        for(int i = 0; i < colas.length; i++)
            this.colas[i] = new Cola();
    }

    /**
     * Intenta disparar la transicion dentro de la seccion critica.
     * @param transicion la transicion de la Red de Petri a disparar
     */
    public void disparar(int transicion){
        mutex.lock();

        boolean k = true;
        while(k) {
            if(redDePetri.disparoTemporal(transicion)) {
                despertar();
                k = false;
            }
            else {
                mutex.unlock();
                this.colas[transicion].acquire();
                mutex.lock();
                k = true;
            }
        }
        mutex.unlock();
    }

    /**
     * Despierta una transicion que esté durmiendo y sensibilizada
     */
    private void despertar() {
        int[] sensibilizadas = this.redDePetri.getTransiciones();
        int[] listas = new int[sensibilizadas.length];

        for(int i = 0; i < sensibilizadas.length; i++)
            listas[i] = sensibilizadas[i] * this.colas[i].getWaiting();

        int aDespertar = this.politica.getTransicionADespertar(listas);

        if(aDespertar >= 0)
            this.colas[aDespertar].release();
    }

    /**
     * Retorna el lock del monitor
     * @return ReentrantLock que funciona como mutex
     */
    public ReentrantLock getMutex() {
        return this.mutex;
    }
}