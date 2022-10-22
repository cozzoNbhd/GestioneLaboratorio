public class Gestione_Laboratorio {

    public static class Studente implements Runnable {

        public Tutor tutor;
        public int id;
        public int k;

        public Studente(Tutor tutor, int id, int k){
            this.tutor = tutor;
            this.id = id;
            this.k = k;
        }

        public void run() {

            for (int i = 0; i < this.k; i++) {

                this.tutor.accediStudente(this.id);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

    }

    public static class Tesista implements Runnable{
        public Tutor tutor;
        public int id;
        public int k;
        public Tesista(Tutor tutor, int id, int k){
            this.tutor = tutor;
            this.id = id;
            this.k = k;
        }

        public void run() {

            for (int i = 0; i < this.k; i++) {

                this.tutor.accediTesista(this.id);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static class Professore implements Runnable{
        public Tutor tutor;
        public int id;
        public int k;

        public Professore(Tutor tutor, int id, int k){
            this.tutor = tutor;
            this.id = id;
            this.k = k;
        }

        public void run() {

            for (int i = 0; i < this.k; i++) {

                this.tutor.accediProfessore(this.id);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 3) {
            System.out.println("Errore nel formato di input");
            System.exit(1);
        }

        if ( (!isNumber(args[0])) || (!isNumber(args[1])) || (!isNumber(args[2])) ) {
            System.out.println("Devi inserire valori interi come parametri!");
            System.exit(1);
        }

        int n_studenti = Integer.parseInt(args[0]);
        int n_tesisti = Integer.parseInt(args[1]);
        int n_professori = Integer.parseInt(args[2]);

        // genero un numero casuale
        int k = (int) (Math.random() * (10 - 1)) + 1;

        // creo il tutor
        Tutor tutor = new Tutor(20);

        int n_utenti = n_studenti + n_tesisti + n_professori;

        Thread[] studenti = new Thread[n_studenti];

        // waiting -> ready -> running

        for (int i = 0; i < n_studenti; i++) {
            studenti[i] = new Thread(new Studente(tutor, i, k));
            studenti[i].setPriority(Thread.MIN_PRIORITY);
            studenti[i].start();
        }

        Thread[] tesisti = new Thread[n_tesisti];

        for (int i = 0; i < n_tesisti; i++) {
            tesisti[i] = new Thread(new Tesista(tutor, n_studenti + i, k));
            tesisti[i].setPriority(Thread.NORM_PRIORITY);
            tesisti[i].start();
        }

        Thread[] professori = new Thread[n_professori];

        for (int i = 0; i < n_professori; i++) {
            professori[i] = new Thread(new Professore(tutor, n_studenti + n_tesisti + i, k));
            professori[i].setPriority(Thread.MAX_PRIORITY);
            professori[i].start();
        }

        // Attendo la terminazione di tutti i thread

        for (int i = 0; i < n_studenti; i++)
            studenti[i].join();

        for (int i = 0; i < n_tesisti; i++)
            tesisti[i].join();

        for (int i = 0; i < n_professori; i++)
            professori[i].join();

        System.out.println("Tutti gli utenti hanno eseguito i " + k + " accessi!");

        System.exit(0);

    }

}