package com.thinkinginjavaexamples.typeinfo;//: typeinfo/NullRobot.java
// Using a dynamic proxy to create a Null Object.
import java.lang.reflect.*;
import java.util.*;

import com.thinkinginjavaexamples.concurrency.Robot;
import net.mindview.util.*;

class NullRobotProxyHandler implements InvocationHandler {
  private String nullName;
  private com.thinkinginjavaexamples.concurrency.Robot proxied = new NRobot();
  NullRobotProxyHandler(Class<? extends com.thinkinginjavaexamples.concurrency.Robot> type) {
    nullName = type.getSimpleName() + " NullRobot";
  }
  private class NRobot implements Null, com.thinkinginjavaexamples.concurrency.Robot {
    public String name() { return nullName; }
    public String model() { return nullName; }
    public List<Operation> operations() {
      return Collections.emptyList();
    }
  }	
  public Object
  invoke(Object proxy, Method method, Object[] args)
  throws Throwable {
    return method.invoke(proxied, args);
  }
}

public class NullRobot {
  public static com.thinkinginjavaexamples.concurrency.Robot
  newNullRobot(Class<? extends com.thinkinginjavaexamples.concurrency.Robot> type) {
    return (com.thinkinginjavaexamples.concurrency.Robot)Proxy.newProxyInstance(
      NullRobot.class.getClassLoader(),
      new Class[]{ Null.class, com.thinkinginjavaexamples.concurrency.Robot.class },
      new NullRobotProxyHandler(type));
  }	
  public static void main(String[] args) {
    com.thinkinginjavaexamples.concurrency.Robot[] bots = {
      new SnowRemovalRobot("SnowBee"),
      newNullRobot(SnowRemovalRobot.class)
    };
    for(com.thinkinginjavaexamples.concurrency.Robot bot : bots)
      com.thinkinginjavaexamples.concurrency.Robot.Test.test(bot);
  }
} /* Output:
Robot name: SnowBee
Robot model: SnowBot Series 11
SnowBee can shovel snow
SnowBee shoveling snow
SnowBee can chip ice
SnowBee chipping ice
SnowBee can clear the roof
SnowBee clearing roof
[Null Robot]
Robot name: SnowRemovalRobot NullRobot
Robot model: SnowRemovalRobot NullRobot
*///:~
