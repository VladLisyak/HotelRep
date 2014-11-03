import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Влад on 15.10.2014.
 */
public class Appliance extends Thread {

    private final String firstName;
    private final Hotel hotel;
    private String name;
    private int millis;
    private String desiredRoom;
    private int peoples;
    private boolean written=false;
    private boolean Divided = false;

    public boolean isDivided() {
        return Divided;
    }

    public void setDivided(boolean isDivided) {

        this.Divided = isDivided;
    }

    public String getFirstName() {
        return firstName;
    }




    public Appliance(String name, int millis, String desiredRoom, int peoples,Hotel hotel) {
        this.name = name;
        this.millis = millis;
        this.desiredRoom = desiredRoom;
        this.peoples = peoples;
        this.firstName = name;
        this.hotel = hotel;
        this.start();
    }

    public String getAppName() {
        return name;
    }

    public int getMillis() {
        return millis;
    }


    public String getDesiredRoom() {
        return desiredRoom;
    }

    public void setAppName(String name) {
        this.name = name;
    }

    public void setPeoples(int peoples) {
        this.peoples = peoples;

    }

    public int getPeoples() {
        return peoples;
    }

    public Hotel getHotel() {
        return hotel;
    }

    @Override
    public void run() {

        while (!hotel.settlement(this)){
           try {
               Object roomType;
               if (desiredRoom.equals("Lux")){
                   roomType=hotel.getLux();
               } else{
                   if(desiredRoom.equals("Standard")){
                       roomType=hotel.getStandard();
                   } else{
                       roomType=hotel.getEcon();
                   }
               }
               synchronized (roomType){
                   if (!written){
                       System.out.println('\n' + getAppName() + " ждет\n");
                       written=true;
                   }
                   roomType.wait();
                   roomType.notifyAll(); //думаю здесь
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
        if (!Divided){
        System.out.println('\n'+getAppName()+" класса "+getDesiredRoom()+" поселилось "+getPeoples()+" ч. "+(new SimpleDateFormat("HH:mm:ss:SSS").format(new Date().getTime())));}

        for (int i = 0; i < millis; i++) {

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        try {
            hotel.leave(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.interrupt();
   }


}
