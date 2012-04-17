package ice.audio;

interface IAudio {

      void playMusic(String res, boolean loop);

      void playSound(String res);

      void resume(String res);

      void pause(String res);

      void stop(String res);

      void release();

}
