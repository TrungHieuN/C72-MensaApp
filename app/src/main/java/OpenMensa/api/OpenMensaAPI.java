package OpenMensa.api;

import java.io.IOException;
import java.time.LocalDate;

import OpenMensa.api.dataprovider.OpenMensaDataProvider;
import OpenMensa.api.dataprovider.OpenMensaOrg;
import OpenMensa.api.model.Canteen;


import OpenMensa.api.model.DayStatus;
import OpenMensa.api.model.Meal;

import OpenMensa.api.model.DayStatus;
import OpenMensa.api.model.Meal;

import OpenMensa.api.model.Menu;
import OpenMensa.api.modelbuilders.CanteenJSON;
import OpenMensa.api.modelbuilders.DayStatusJSON;
import OpenMensa.api.modelbuilders.MealJSON;

public class OpenMensaAPI {
    private OpenMensaDataProvider dataProvider;

    //constructor
    public OpenMensaAPI() {
        this.dataProvider = new OpenMensaOrg();
    }

    /**
     * Gets a list of all canteens.
     *
     * @return The list of canteens.
     * @throws IOException On failing to retrieve data from the API.
     */
    public String getCanteens() throws IOException {
        String json = dataProvider.queryAPI("/canteens");
        return json;
    }


    // temp
    public String getSomeMealsFromHTW() throws IOException{
        String json = dataProvider.queryAPI("/canteens/24/days/2018-05-18/meals");
        return json;
    }

    /**
     * Returns a canteen objects by the given canteenID in openMensaAPI
     * htw Wilhelminenhof : id = 24
     * Treskowallee : id = 30
     * @param CanteenId
     * @return
     * @throws IOException
     */
    public Canteen getCanteenById(int CanteenId) throws IOException{
        String json = dataProvider.queryAPI("/canteens/"+CanteenId);
        return CanteenJSON.translate(json);
    }

    /**
     * Returns a Menu object from a given canteen object and a date
     * @param canteen
     * @param date
     * @return
     */
    public Menu getMenuFromCanteenByDate(Canteen canteen, String date) throws IOException{ // String date statt Localdate lokaldate
        Menu menu = new Menu();
        menu.setCanteen(canteen);
        menu.setDate(date);

       // menu.setStatus(getDayStatusFromCanteenByDate(canteen, date));

        //if (!menu.getStatus().isOpen()) return menu;

        String command = "/canteens/"+canteen.getId()+"/days/"+date.toString()+"/meals";
        String json = dataProvider.queryAPI(command);

        menu.setMeals(MealJSON.parse(json));
        return menu;
    }

    public Menu temp_getMenuFromCanteenByDate(Canteen canteen, String date) throws IOException{
        Menu menu = new Menu();
        menu.setCanteen(canteen);
        //menu.setDate(date);

        // menu.setStatus(getDayStatusFromCanteenByDate(canteen, date));

        //if (!menu.getStatus().isOpen()) return menu;

        String command = "/canteens/"+canteen.getId()+"/days/"+date+"/meals";
        String json = dataProvider.queryAPI(command);

        menu.setMeals(MealJSON.parse(json));
        return menu;
    }

    public Menu getMenuFromCanteenByDate(int id, String date) throws IOException{
        return getMenuFromCanteenByDate(getCanteenById(id),date);
    }


    /**
     * Returns a Menu object from a given canteen object and a date
     * @param canteen
     * @param date
     * @return
     */
    public DayStatus getDayStatusFromCanteenByDate(Canteen canteen, String date) throws IOException{ // String date statt Localdate lokaldate
        String command = "/canteens/"+canteen.getId()+"/days/"+date.toString()+"/";
        String json = dataProvider.queryAPI(command);

        DayStatus status = DayStatusJSON.translate(json);
        return status;
    }

    public DayStatus getDayStatusFromCanteenByDate(int id, String date) throws IOException{ // String date statt Localdate lokaldate
        return getDayStatusFromCanteenByDate(getCanteenById(id),date);
    }

}
