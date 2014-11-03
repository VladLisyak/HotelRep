import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Влад on 15.10.2014.
 */
public class Hotel {

   private Object standard;
   private Object econ;
   private Object lux;

    public Hotel() {
        for (int i = 0; i < 2; i++) {
            luxR[i]= new Room(1);
            standardR[i]=new Room(1);
            econR[i]=new Room(1);
        }

        for (int i = 2; i <4 ; i++) {
            luxR[i]= new Room(2);
            standardR[i]=new Room(2);
            econR[i]=new Room(2);
        }

        for (int i = 4; i < luxR.length; i++) {
            luxR[i]= new Room(3);
            standardR[i]=new Room(3);
            econR[i]=new Room(3);
        }

        standard =new Object();
        econ =new Object();
        lux =new Object();
    }



    private class Room{
        private int cap;

        private Appliance leaver;

        public Appliance getLeaver() {
            return leaver;
        }

        public int getCap() {
            return cap;
        }

        private Room(int cap) {
            this.cap = cap;
        }

        public void setLeaver(Appliance leaver) {
            this.leaver = leaver;
        }
    }


     Room[] luxR = new Room[6];
     Room[] standardR = new Room[6];
     Room[] econR = new Room[6];


    public Iterable<Room> getFreeRooms(Object room){
        Room[] curr;
        ArrayList<Room> rm = new ArrayList<Room>();
        if (room.toString().equals("Lux"))
            curr = luxR;
        else{
            if (room.toString().equals("Standard"))
                curr= standardR;
            else curr= econR;
        }
        for (Room room1 :curr) {
            if (room1.getLeaver()==null){
               rm.add(room1);
            }
        }


        Collections.sort(rm,new Comparator<Room>() {
            @Override
            public int compare(Room room, Room room2) {
                return room2.cap-room.cap;
            }
        });

        return rm;
    }

    public Iterable<Room> getOccupiedRooms(Object room){
        Room[] curr;
        ArrayList<Room> rm = new ArrayList<Room>();
        if (room.toString().equals("Lux"))
            curr = luxR;
        else{
            if (room.toString().equals("Standard"))
                curr= standardR;
            else curr= econR;
        }
        for (Room room1 :curr) {
            if (room1.getLeaver()!=null){
               rm.add(room1);
            }
        }


        Collections.sort(rm,new Comparator<Room>() {
            @Override
            public int compare(Room room, Room room2) {
                return room2.cap-room.cap;
            }
        });

        return rm;
    }

    public int getAvailableCap(Iterable<Room> rm){
       int i=0;
        for (Room room :rm) {
            i+=room.getCap();
        }
      return i;
    }

    public synchronized boolean settlement(Appliance app){
        List<Room> roomlist = (List<Room>) getFreeRooms(app.getDesiredRoom());
        Collections.reverse(roomlist);
             for (Room room :roomlist) {
                 if (room.cap>=app.getPeoples()){
                         room.leaver=app;
                         app.setAppName(app.getAppName()+": в комнате на "+room.cap+" ч.");
                         return true;
                 }
             }
             Iterable<Room> arr = getFreeRooms(app.getDesiredRoom());

             if (getAvailableCap(arr)>=app.getPeoples()){
                 for (Room room :arr) {
                     int ppl;
                   if (app.getPeoples()==0){
                       break;
                   }
                   if (app.getPeoples()>=room.cap){
                       ppl=room.cap;

                   } else{
                       ppl=app.getPeoples();
                   }
                     new Appliance("подпоток "+app.getAppName()+" "+ppl+" ч.",app.getMillis(),app.getDesiredRoom(),ppl,app.getHotel());
                     app.setPeoples(app.getPeoples()-ppl);
                     app.setDivided(true);
                 }
                 return true;
             }

           return false;
    }

    public Object getStandard() {
        return standard;
    }

    public Object getEcon() {
        return econ;
    }

    public Object getLux() {
        return lux;
    }

    public  synchronized void leave(Appliance app) throws IOException {
        if (!app.isDivided()) System.out.println('\n'+app.getFirstName()+" выселился "+(new SimpleDateFormat("HH:mm:ss:ms").format(new Date().getTime()))+'\n');

        Iterable<Room> curr = getOccupiedRooms(app.getDesiredRoom());
        for (Room room :curr) {
            if (room.leaver!=null){
                if (room.leaver.equals(app)){
                    room.leaver=null;
                    Object roomType;
                    if (app.getDesiredRoom().equals("Lux")){
                        roomType=getLux();
                    } else{
                        if(app.getDesiredRoom().equals("Standard")){
                            roomType=getStandard();
                        } else{
                            roomType=getEcon();
                        }
                    }
                    synchronized (roomType){
                    roomType.notify();}
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {

        Hotel hotel = new Hotel();

        Appliance a = new Appliance("a",15,"Standard",2,hotel);
        TimeUnit.MILLISECONDS.sleep(20);
        Appliance b = new Appliance("b",20,"Standard",2,hotel);
        Appliance c = new Appliance("c",10,"Standard",2,hotel);
        TimeUnit.MILLISECONDS.sleep(30);
        Appliance d = new Appliance("d",10,"Lux",3,hotel);
        Appliance e = new Appliance("e",30,"Econ",2,hotel);
        Appliance f = new Appliance("f",60,"Lux",3,hotel);
        Appliance g = new Appliance("g",40,"Standard",1,hotel);
        Appliance h = new Appliance("h",45,"Lux",3,hotel);
        TimeUnit.MILLISECONDS.sleep(10);
        Appliance j = new Appliance("j",40,"Econ",2,hotel);
        Appliance k = new Appliance("k",50,"Standard",1,hotel);
        Appliance l = new Appliance("l",20,"Econ",2,hotel);
        Appliance m = new Appliance("m",38,"Standard",3,hotel);
        TimeUnit.MILLISECONDS.sleep(20);
        Appliance n = new Appliance("n",90,"Lux",2,hotel);
        Appliance o = new Appliance("o",60,"Standard",1,hotel);
        Appliance p = new Appliance("p",30,"Lux",2,hotel);
        Appliance q = new Appliance("q",29,"Lux",2,hotel);
        Appliance a1 = new Appliance("a1",15,"Econ",3,hotel);
        TimeUnit.MILLISECONDS.sleep(20);
        Appliance b1 = new Appliance("b1",20,"Econ",1,hotel);
        Appliance c1 = new Appliance("c1",60,"Lux",1,hotel);
        TimeUnit.MILLISECONDS.sleep(30);
        Appliance d1 = new Appliance("d1",10,"Standard",3,hotel);
        Appliance e1 = new Appliance("e1",30,"Standard",3,hotel);
        Appliance f1 = new Appliance("f1",10,"Lux",1,hotel);
        Appliance g1 = new Appliance("g1",40,"Lux",3,hotel);
        Appliance h1 = new Appliance("h1",45,"Lux",1,hotel);


    }
}
