package com.timon.domain.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.timon.common.JsonUtil;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = City.CityBuilder.class)
public class City {
    String name;
    String locationID;
    AlertCount count;
    List<District> districts;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CityBuilder {

    }

    public static void main(String[] args) {


        List<MeetingRoom> meetingRoomList = new ArrayList<MeetingRoom>();
        for ( int i=0; i<2; i++) {
            AlertCount c = AlertCount.builder().critical(3+i).build();


            MeetingRoom m = MeetingRoom.builder().name("m"+i).build();
            m.count = c;
            m.locationID = m.name;

            meetingRoomList.add(m);
        }

        List<Company> companies = new ArrayList<Company>();
        for ( int i=0; i<2; i++) {


            Company c = Company.builder().name("com"+i).build();
            c.meetingRooms = meetingRoomList;
            c.locationID = c.name;
            companies.add(c);
        }


        List<District> districtList = new ArrayList<District>();

        for ( int i=0; i<2; i++) {
            District d = District.builder().name("district"+i).build();
            d.locationID = d.name;
            d.companies =companies;
            districtList.add(d);
        }

        City c = City.builder().name("bj").build();
        c.locationID = c.name;

        c.districts = districtList;

        JsonUtil.write("city",  c);
    }
}
