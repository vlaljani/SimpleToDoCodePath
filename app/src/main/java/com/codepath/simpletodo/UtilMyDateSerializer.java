package com.codepath.simpletodo;

import com.activeandroid.serializer.TypeSerializer;

/**
 * Created by vibhalaljani on 12/29/14.
 */
public class UtilMyDateSerializer extends TypeSerializer {

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August",
                       "September", "October", "November", "December"};
    @Override
    public Class<?> getDeserializedType() {
        return MyCustomDate.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (data == null) {
            return null;
        }

        MyCustomDate currDate = (MyCustomDate) data;
        return (currDate.getDayOfMonth() + " " + months[currDate.getMonthOfYear()]
                + " " + currDate.getYear());
    }

    @Override
    public MyCustomDate deserialize(Object data) {
        if (data == null) {
            return null;
        }

        String dataString = (String) data;
        String[] dateSplit = dataString.split(" ");
        int monthOfYear = 0;
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(dateSplit[1])) {
                monthOfYear = i;
            }
        }
        return new MyCustomDate(Integer.parseInt(dateSplit[0]),
                                monthOfYear,
                                Integer.parseInt(dateSplit[2]));
    }
}
