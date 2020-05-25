package kelvinclark;

/**
 * Utility class that allows the creation of custom events that helps user to manipulate the flow of code written inside draw().
 * This is useful to prevent draw() from making multiple calls to a block of code that is within an active condition.
 * 
 * @author Kelvin Clark Spátola
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import processing.core.PApplet;

public class Trigger {
    private final PApplet parent;
    private static Method triggerEventMethod;
    
    private boolean withinConditions;
    private boolean taskDone;
    private int counter;
    
    // CONSTRUCTOR
    /**
     * Initializes the class and registers the "triggerEvent(Trigger t)" method for use in the sketch
     * 
     * @param parent
     */
    
    public Trigger(PApplet parent) {
        this.parent = parent;
        triggerEventMethod = registerEventMethod("triggerEvent", new Class[] { Trigger.class });
    }
    
    /**
     * Checks whether the condition sent as the parameter is true and calls the triggerEvent()
     * once and execute the code in it. Then, it prevents calling triggerEvent() again multiple 
     * times while the condition is true, as frames are being updated within draw().
     * <br> It resets when <code>trigger</code> turns false
     * 
     * @param trigger the specified condition to call the triggerEvent()
     */
    
    public void callTrigger(boolean trigger) {
        if (trigger) {
            withinConditions = true; //yes, you are within the conditions to run the event
        } else {// you are not within the conditions, so reset the booleans, making it possible to reactive them later
            withinConditions = false;
            taskDone = false;
        }
        if (withinConditions && !taskDone) { //you are within and actions has not been done yet
            invokeMethod(triggerEventMethod, new Object[] { this });
            taskDone = true;
            counter++;
        }
    }
    
    /**
     * Checks whether the condition sent to callTrigger() is true.
     * 
     * @return true if conditions sent to callTrigger() are true.
     */
    
    public boolean isActive() {
        return withinConditions;
    }
    
    /**
     * Returns the number of times the callTrigger() method was called and completed it's task.
     * 
     * @return the number of executions.
     */
    
    public int getNumExecutions() {
        return counter;
    }
    
    
    private Method registerEventMethod(String methodName, Class... args) {
        try {
            return parent.getClass().getMethod(methodName, args);
        } catch(NoSuchMethodException | SecurityException e){
            System.err.println("Your sketch needs to implement \"void "+methodName+"(Trigger t)\"");
        }
        return null;
    }
    
    
    private void invokeMethod(Method method, Object... args) {
        if(method != null) {
            try {
                method.invoke(parent, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                System.err.println("Failed to call method " + method.toString() + " inside your sketch");
            }
        }
    }
    

    @Override
    public String toString() {
        return "Trigger{" + "isActive = " + withinConditions + ", taskDone = " + taskDone + ", number of executions: " + counter + '}';
    }
}