package com.timon.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public abstract  class Dev {
   String nbiot_sno;
   String nbiot_company;
   String nbiot_kind;
   String nbiot_type;
}
