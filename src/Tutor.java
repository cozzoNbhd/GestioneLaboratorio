public class Tutor {
    public int n_computer;
    public int n_utenti; // il numero di utenti (studenti + tesisti) attualmente connessi al laboratorio
    public Computer[] laboratorio; // struttura dati effettiva
    public int n_professori; // il numero di professori attualmente connessi al laboratorio

    private static final Object lockVariabili = new Object();
    public Tutor (int n_computer) {
        this.n_computer = n_computer;
        this.n_utenti = 0;
        this.n_professori = 0;
        laboratorio = new Computer[n_computer];
        for (int i = 0; i < this.laboratorio.length; i++)
            this.laboratorio[i] = new Tutor.Computer(i);
    }

    public void accediStudente(int userID) {

        int n_postazione = 0;

        synchronized (lockVariabili) {
            while (this.n_professori > 0 || this.n_utenti == this.laboratorio.length) {
                try {
                    lockVariabili.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.n_utenti++;
            for (int i = 0; i < this.laboratorio.length; i++) {
                if (this.laboratorio[i].isFree()) {
                    this.laboratorio[i].aggiungiProprietario(userID);
                    n_postazione = this.laboratorio[i].id;
                    break;
                }
            }
            lockVariabili.notifyAll();
        }

        System.out.println("lo studente " + userID + " ha preso il pc " + this.laboratorio[n_postazione].id + ".");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("lo studente " + userID + " ha lasciato libero il pc " + this.laboratorio[n_postazione].id);


        synchronized (lockVariabili) {
            this.laboratorio[n_postazione].rimuoviProprietario();
            this.n_utenti--;
            lockVariabili.notifyAll();
        }

    }

    public void accediTesista(int userID) {

        int n_postazione = 0;

        synchronized (lockVariabili) {
            while (this.n_professori > 0 || !this.laboratorio[userID % this.n_computer].isFree()) {
                try {
                    lockVariabili.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.n_utenti++;
            this.laboratorio[userID % this.n_computer].aggiungiProprietario(userID);
            n_postazione = userID % this.n_computer;
            lockVariabili.notifyAll();
        }

        System.out.println("Il tesista " + userID + " ha preso il pc " + n_postazione + ".");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Il tesista " + userID + " ha lasciato libero il pc " + n_postazione + ".");

        synchronized (lockVariabili) {
            this.laboratorio[userID % this.n_computer].rimuoviProprietario();
            this.n_utenti--;
            lockVariabili.notifyAll();
        }

    }

    public void accediProfessore(int userID) {

        synchronized (lockVariabili) {
            while (this.n_utenti > 0) {
                try {
                    lockVariabili.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.n_professori++;
            lockVariabili.notifyAll();
        }


        System.out.println("Il professore " + userID + " e' entrato nel laboratorio.");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Il professore "+userID+" e' uscito dal laboratorio.");

        synchronized (lockVariabili) {
            this.n_professori--;
            lockVariabili.notifyAll();
        }

    }

    public static class Computer {
        public boolean libero;
        public int utente;
        public int id;

        public Computer(int id){
            this.id = id;
            this.libero = true;
            this.utente = -1;
        }

        public boolean isFree() {
            if (this.libero) return true;
            return false;
        }

        public void aggiungiProprietario(int newPossessor){
            assert (this.libero);
            this.libero = false;
            this.utente = newPossessor;
        }

        public void rimuoviProprietario(){
            this.libero = true;
            this.utente = -1;
        }
    }
}