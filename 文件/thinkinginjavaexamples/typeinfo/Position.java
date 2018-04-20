package com.thinkinginjavaexamples.typeinfo;//: typeinfo/Position.java

import com.thinkinginjavaexamples.initialization.Person;

class Position {
  private String title;
  private com.thinkinginjavaexamples.initialization.Person person;
  public Position(String jobTitle, com.thinkinginjavaexamples.initialization.Person employee) {
    title = jobTitle;
    person = employee;
    if(person == null)
      person = com.thinkinginjavaexamples.initialization.Person.NULL;
  }
  public Position(String jobTitle) {
    title = jobTitle;
    person = com.thinkinginjavaexamples.initialization.Person.NULL;
  }	
  public String getTitle() { return title; }
  public void setTitle(String newTitle) {
    title = newTitle;
  }
  public com.thinkinginjavaexamples.initialization.Person getPerson() { return person; }
  public void setPerson(com.thinkinginjavaexamples.initialization.Person newPerson) {
    person = newPerson;
    if(person == null)
      person = com.thinkinginjavaexamples.initialization.Person.NULL;
  }
  public String toString() {
    return "Position: " + title + " " + person;
  }
} ///:~
