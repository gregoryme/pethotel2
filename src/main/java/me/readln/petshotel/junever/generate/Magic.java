package me.readln.petshotel.junever.generate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Magic {

    private ArrayList petNames;
    private ArrayList ownerNames;
    private ArrayList ownerContacts;
    private ArrayList eventActions;
    private ArrayList visitNotes;
    private ArrayList boxes;

    public Magic() {

        {
            petNames      = new ArrayList<String>();
            ownerNames    = new ArrayList<String>();
            ownerContacts = new ArrayList<String>();
            eventActions  = new ArrayList<String>();
            visitNotes    = new ArrayList<String>();
            boxes         = new ArrayList<String>();

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("magic.json"))
            {
                Object obj = jsonParser.parse(reader);
                JSONArray mainList = (JSONArray) obj; // "cover" array

                JSONArray internalList;

                // pet names
                internalList= (JSONArray) mainList.get(0);
                for (int j = 0; j < internalList.size(); j++) {
                   petNames.add(internalList.get(j));
                }

                // owner names
                internalList= (JSONArray) mainList.get(1);
                for (int j = 0; j < internalList.size(); j++) {
                    ownerNames.add(internalList.get(j));
                }

                // owner contacts
                internalList= (JSONArray) mainList.get(2);
                for (int j = 0; j < internalList.size(); j++) {
                    ownerContacts.add(internalList.get(j));
                }

                // event actions
                internalList= (JSONArray) mainList.get(3);
                for (int j = 0; j < internalList.size(); j++) {
                    eventActions.add(internalList.get(j));
                }

                // visit notes
                internalList= (JSONArray) mainList.get(4);
                for (int j = 0; j < internalList.size(); j++) {
                    visitNotes.add(internalList.get(j));
                }

                // boxes
                internalList= (JSONArray) mainList.get(5);
                for (int j = 0; j < internalList.size(); j++) {
                    boxes.add(internalList.get(j));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private String getRandomStringFromList (ArrayList<String> list) {
        String value;
        RandomIndex randomIndexBaker = new RandomIndex();
        value = list.get(randomIndexBaker.getIndex(list.size() - 1));
        return value;
    }

    public String getRandomPetName() {
        String name = getRandomStringFromList(petNames);
        return (name == null) ? "Pushok" : name;
    }

    public String getRandomOwnerName() {
        String name = getRandomStringFromList(ownerNames);
        return (name == null) ? "Sam Po Sebe Kot" : name;
    }

    public String getRandomOwnerContact() {
        String contact = getRandomStringFromList(ownerContacts);
        return (contact == null) ? "555 123 456 789" : contact;
    }

    public String getRandomNote() {
        String note = getRandomStringFromList(visitNotes);
        return (note == null) ? "YouYou is a Great Cat!" : note;
    }

    public String getRandomBoxNumber() {
        String box = getRandomStringFromList(boxes);
        return (box == null) ? "Room" : box;
    }

    public String getRandomEventDescription() {
        String eventDescription = getRandomStringFromList(eventActions);
        return (eventDescription == null) ? "Check water" : eventDescription;
    }

    public Date getRandomCheckInDate() {
        Date date = new Date(Calendar.getInstance().getTime().getTime());
        return date;
    }

    public Date getRandomCheckOutDate() {
        final long time_k = 100_000_000_00l;
        long random = (long)(Math.random() * time_k);
        Date date = new Date(Calendar.getInstance().getTime().getTime() + random);
        return date;
    }
}
