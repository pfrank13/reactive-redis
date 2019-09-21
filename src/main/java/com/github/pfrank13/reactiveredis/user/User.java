package com.github.pfrank13.reactiveredis.user;

/**
 * @author pfrank
 */
public class User {
  private Integer id;
  private String name;

  public User(){
    //For lame Deserializers
  }

  public User(final String name){
    this(null, name);
  }

  public User(final Integer id, final String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
