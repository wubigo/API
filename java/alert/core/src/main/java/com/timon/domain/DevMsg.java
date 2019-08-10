package com.timon.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract  class DevMsg {
   String nbiot_sno;
   String nbiot_company;
   String nbiot_kind;
   String nbiot_type;
   long nbiot_create_time;
   List<Location> locations;
   List<Kind>  kinds;
   public void toFact(){

   }
}
